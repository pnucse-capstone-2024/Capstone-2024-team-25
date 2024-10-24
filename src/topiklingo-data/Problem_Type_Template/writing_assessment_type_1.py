import sys, os
# For .py
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
import ast
from openai import OpenAI
import numpy as np
import json
import nltk
from nltk.tokenize import word_tokenize
from nltk import pos_tag
from difflib import SequenceMatcher
from openai import OpenAI
import numpy as np
import json
import re

# nltk.download('punkt')
# nltk.download('averaged_perceptron_tagger')

def create_openai_client():
    api_key_file = "API_KEY/llm_api_key.json"
    if os.path.exists(api_key_file):
        with open(api_key_file) as f:
            api_key = json.load(f)
    else:
        api_key = {
            "OPENAI_API_KEY": "your_openai_api_key_here",
        }
        with open(api_key_file, "w") as f:
            json.dump(api_key, f)

    if (
        api_key["OPENAI_API_KEY"] == "your_openai_api_key_here"
    ):
        print("Please update your API keys in the API_KEY/api_key.json file")
        sys.exit()
    else:
        # openai.api_key = api_key["OPENAI_API_KEY"]
        client = OpenAI(
        api_key=api_key["OPENAI_API_KEY"]
        )
    return client

# Function to get the embedding
def get_embedding(text, model="text-embedding-3-small"):
    client = create_openai_client()
    response = client.embeddings.create(input=text, model=model)
    return response.data[0].embedding

# Function to calculate cosine similarity
def cosine_similarity(embedding1, embedding2):
    # Cosine similarity: dot_product(A, B) / (norm(A) * norm(B))
    embedding1 = np.array(embedding1)
    embedding2 = np.array(embedding2)
    
    dot_product = np.dot(embedding1, embedding2)
    norm_a = np.linalg.norm(embedding1)
    norm_b = np.linalg.norm(embedding2)
    
    return dot_product / (norm_a * norm_b)

def cosine_similarity_using_openai_embeddings(text1, text2):
    embedding1 = get_embedding(text1)
    embedding2 = get_embedding(text2)
    similarity = cosine_similarity(embedding1, embedding2)
    return similarity

def sentence_to_tokens(sentence):
    return word_tokenize(sentence)

def sequenceMatcher_similarity(a, b):
    return SequenceMatcher(None, a, b).ratio()

def filter_nouns(tokens):
    tagged_tokens = pos_tag(tokens)
    nouns = [word for word, pos in tagged_tokens if pos.startswith('NN')]
    return nouns

def similarity_between_sentences(sentence1, sentence2):
    tokens1 = sentence_to_tokens(sentence1)
    tokens2 = sentence_to_tokens(sentence2)
    nouns1 = filter_nouns(tokens1)
    nouns2 = filter_nouns(tokens2)
    similarity = 0.0
    for noun1 in nouns1:
        for noun2 in nouns2:
            similarity += sequenceMatcher_similarity(noun1, noun2)
    return similarity

def bonus_point(similarity):
    print(f"similarity: {similarity}")
    bonus_point = 0
    if similarity > 0.4 and similarity < 1.0:
        bonus_point = 2
    elif similarity >= 1.0:
        bonus_point = 4
    return bonus_point

def assess_writing_type_1(default_model="gpt-4o-mini",verbose=False, paragraph=None, user_answer1=None, user_answer2=None):
    assert paragraph is not None, "paragraph is None"
    assert user_answer1 is not None, "user_answer1 is None"
    assert user_answer2 is not None, "user_answer2 is None"
    
    criteria = "상: 의미가 완전히 일치함, 중: 의미가 일부 일치함, 하: 의미가 일치하지 않음"
    level_to_points = {"상": 5, "중": 3, "하": 1}

    total_cost = 0
    models = [default_model]
    answer_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.5, verbose=verbose
    )

    ### Generate answers
    answer_generate_model.request_models_responses(
        [
            SystemMessage(content="너는 한국어 전문가야. 빈칸에 들어가야 할 말을 문맥에 맞게, 구체적으로 생성해줘. 예시: {'answer1': '', 'answer2': ''}"),
            HumanMessage(content=f"paragraph: {paragraph}"),
        ]
    )
    answer_responses = answer_generate_model.get_model_responses()
    answers = answer_responses[0][1]
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', answer_responses[0][2]).group(1))
    answers = ast.literal_eval(answers)
    print(answers)

    ### Assess answers based on the criteria, which is 상/중/하
    answer_assessment_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.8, verbose=verbose
    )

    userMsg = {"answer1:": answers["answer1"], "answer1_guess": user_answer1, 
            "answer2:": answers["answer2"], "answer2_guess": user_answer2}
    userMsg = str(userMsg)
    response_example = "예시: {'point1': '상', 'point2': '중'}"
    print(f"userMsg: {userMsg}")
    answer_assessment_model.request_models_responses(
        [
            SystemMessage(content=f"정답과 비교하여 입력된 답안을 채점해줘. 채점 기준: {criteria}. 예시: {response_example}"),
            HumanMessage(content=userMsg),
        ]
    )
    scoring_responses = answer_assessment_model.get_model_responses()
    scoring_result = scoring_responses[0][1]
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', scoring_responses[0][2]).group(1))
    scoring_result = ast.literal_eval(scoring_result)
    print(scoring_result)
    point_pair = [level_to_points[scoring_result["answer1"]], level_to_points[scoring_result["answer2"]]]

    ### post-processing
    ## Translate the answers to English
    translate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.0, verbose=verbose
    )
    translate_model.request_models_responses(
        [
            SystemMessage(content="너는 번역가야. 한국어를 영어로 번역해줘. 예시: {'answer1': '', 'answer1_guess': '', 'answer2': '', 'answer2_guess': ''}"),
            HumanMessage(content=f"input: {userMsg}"),
        ]
    )
    english_responses = translate_model.get_model_responses()
    english_answers = english_responses[0][1]
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', english_responses[0][2]).group(1))
    english_answers = ast.literal_eval(english_answers)
    print(english_answers)

    similarity1 = cosine_similarity_using_openai_embeddings(english_answers["answer1"], english_answers["answer1_guess"])
    similarity2 = cosine_similarity_using_openai_embeddings(english_answers["answer2"], english_answers["answer2_guess"])

    point_pair[0] += bonus_point(similarity1)
    point_pair[1] += bonus_point(similarity2)
    if point_pair[0] > 5:
        point_pair[0] = 5
    if point_pair[1] > 5:
        point_pair[1] = 5
    print(f"point_pair: {point_pair}")

    ### Grammar checking model
    grammar_check_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.3, verbose=verbose
    )
    grammar_check_model.request_models_responses(
        [
            SystemMessage(content="당신은 오타(typo)를 찾아내는 도우미입니다. 주어진 문장에서 문맥, 문장부호, 조사 유무는 고려하지 말고, 오직 단어의 철자가 정확한지 판단해주세요. 예시: {'typo_in_sentence1': 'none', 'typo_in_sentence2': '공뷰'}"),
            HumanMessage(content=f"sentence1: {user_answer1}, sentence2: {user_answer2}"),
        ]
    )
    grammar_check_responses = grammar_check_model.get_model_responses()
    grammar_answers = grammar_check_responses[0][1]
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', grammar_check_responses[0][2]).group(1))
    grammar_answers = ast.literal_eval(grammar_answers)
    grammar_answers
    print(f"Grammar answers: {grammar_answers}")
    if grammar_answers["typo_in_sentence1"] != "none":
        point_pair[0] -= 1
    if grammar_answers["typo_in_sentence2"] != "none":
        point_pair[1] -= 1
    total_points = sum(point_pair)
    print(f"point_pair: {point_pair}")
    print(f"total_points: {total_points}")
    result = {"total_points": total_points, "total_cost": total_cost}
    return result

if __name__ == "__main__":
    paragraph = "초대합니다. 한 달 전에 이사를 했습니다. 그동안 집안 정리 때문에 정신이 없었는데 이제 좀 정리가 됐습니다." \
        "그래서 저희 집에서 (㉠). (㉡)? 그 시간이 괜찮으신지 연락 주시면 감사하겠습니다."
    user_answer1 = "파티를 열려고 합니다."
    # user_answer2 = "5월 17일에 오후 1시에 시간되실까요"
    user_answer2 = "5월 17일에 오후 1시에 시강되실까요"
    # user_answer2 = "음식은 양식 어떠세요"
    # user_answer2 = "옴식은 양식 어떠세요"
    result = assess_writing_type_1(default_model="gpt-4o-mini",verbose=True, 
                                   paragraph=paragraph, 
                                   user_answer1=user_answer1, user_answer2=user_answer2)
    print(result)