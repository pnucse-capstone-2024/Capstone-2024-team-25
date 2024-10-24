import sys, os

# For .py
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
# For .ipynb
# sys.path.append(os.path.dirname(os.getcwd()))

from util import find_random_question
from util import random_word
from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
import re
import ast
import random
from konlpy.tag import Okt

def remove_keyword_in_phrase(phrase, keyword):
    word_list = phrase.split(" ")
    preprocessed_phrase = " ".join([word for word in word_list if keyword not in word])
    return preprocessed_phrase

def remove_duplicate_word_from_phrases(phrase_list):
    token_list = []
    for phrase in phrase_list:
        token_list += [token.strip() for token in phrase.split(" ")]
    token_list = list(set(token_list))

    duplicated_tokens = []
    for token in token_list:
        if token in phrase_list[0] and token in phrase_list[1]:
            duplicated_tokens.append(token)
    preprocessed_phrase_list = []
    for duplicated_token in duplicated_tokens:
        for phrase in phrase_list:
            preprocessed_phrase_list.append(phrase.replace(duplicated_token, "").strip())
    return preprocessed_phrase_list

def generate_reading_2_problem_type_2(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    keyword = 'TOPIK_2_READING'
    problem_type = 'READING_2_PROBLEM_TYPE_2'
    random_questions = find_random_question(keyword, problem_type, detail_type=1, count=1)
    
    # Extract text between <ins> and </ins>
    matches = re.findall(r'<ins>(.*?)</ins>', random_questions[0]['example'])
    assert len(matches) == 1
    underlined_text = matches[0].strip()
    ### Prepare a pair of sample phrases
    a_pair_of_similar_phrases = [underlined_text, random_questions[0]['answer']]
    if verbose:
        print(f'Example phrases: {a_pair_of_similar_phrases}')
    
    ### Create a model for generating a problem
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.5, verbose=verbose
    )

    ### get random word
    main_topic = random_word(difficulty="normal")
    if verbose:
        print(main_topic)

    ## Generate a new word based on the random word
    problem_generate_model.request_models_responses(
        [
            SystemMessage(content="주제어와 관련된 일상적인 명사 1개를 제시하라. no intro. no outro."),
            HumanMessage(content="주제어: " + main_topic + " 명사: "),
        ]
    )
    word_responses = problem_generate_model.get_model_responses()
    main_topic_generated = word_responses[0][1]
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))
    
    human_prompt = f"두 구문은 서로 대체될 수 있어야 한다. 두 구문은 자연스러워야한다. 주제어: {main_topic_generated}"
    
    ### Create a model for generating a phrase
    models = ['gpt-4o']
    phrase_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.1, verbose=verbose
    )
    phrase_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"너는 의미가 유사한 한국어 구문을 생성하는 봇이다. 예시 구문: {a_pair_of_similar_phrases}. No intro. No conclusion."
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    phrase_response = phrase_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', phrase_response[0][2]).group(1))

    phrase_response = phrase_response[0][1]
    phrase_response

    # Step 1: Remove the unwanted characters
    cleaned_str = phrase_response.replace("[", "").replace("]", "").replace("'", "").strip()

    # Step 2: Split the string by the comma
    elements = cleaned_str.split(",")

    # Step 3: Strip any extra spaces
    phrase_list = [element.strip() for element in elements]
    phrase_list

    # # Remove any word that contains keyword
    # preprocessed_phrase_list = [remove_keyword_in_phrase(phrase, main_topic_generated) for phrase in phrase_list]
    # preprocessed_phrase_list
    
    human_prompt = f"""Make a sentence where either of the given phrases can be contained, but contain only one of them. Never change any letter in the phrase.
Phrases: [{phrase_list[0]}, {phrase_list[1]}]"""
    if verbose:
        print(f'Human prompt: {human_prompt}')
    models = ['gpt-4o']
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.5, verbose=verbose
    )
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"#너는 자연스러운 한국어 문장을 생성하는 봇이다. 주제어: {main_topic_generated}"
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    sentence_response = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', sentence_response[0][2]).group(1))

    sentence_response = sentence_response[0][1]
    
    target_phrase = None
    similar_phrase = None
    for phrase in phrase_list:
        if phrase in sentence_response:
            target_phrase = phrase
        else:
            similar_phrase = phrase
    assert target_phrase is not None, "Target phrase not found in the sentence"
    assert similar_phrase is not None, "Similar phrase not found in the sentence"
    if verbose:
        print(f'Target Phrase: {target_phrase}\nSimilar Phrase: {similar_phrase}')

    ### Make an example question for system message to refer to
    random_question = random_questions[0]
    example_sentence = random_question['example'].replace('<ins>', '').replace('</ins>', '')
    example_question = {
        '문장': example_sentence,
        '밑줄' : underlined_text,
        '정답': random_question['answer'],
        '오답': [selector for selector in random_question['selector'] if selector != random_question['answer']]
    }
    ### prompt
    human_prompt = f"""문장: {sentence_response}
    밑줄: "{target_phrase}"
    모든 선택지의 어간은 동일해야하며, 밑줄에 자연스럽게 들어갈 수 있어야한다."""
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"""너는 문장의 밑줄 친 부분과 의미가 가장 비슷한 선택지를 만드는 봇이다. 오답 3개, 정답 1개의 선택지를 만들어라.
                #답변 예시: {example_question}"""
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    response = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', response[0][2]).group(1))

    selectors = response[0][1]
    selectors = ast.literal_eval(selectors)
    answer = selectors['정답']
    selectors = selectors['오답'] + [answer]
    # selectors = selectors['오답']
    # selectors.append(similar_phrase)
    random.shuffle(selectors)
    
    first_word_in_target = target_phrase.split()[0]
    okt = Okt()
    pos_data = okt.pos(first_word_in_target)
    josas = [x[0] for x in pos_data if x[1] == 'Josa']
    if len(josas) > 0:
        i=0
        while i < len(selectors):
            if first_word_in_target not in selectors[i]:
                break
            i += 1
        ### If the first word is also in every selector, remove the word from the selectors
        if i == len(selectors):
            selectors = [selector.replace(first_word_in_target, '').strip() for selector in selectors]
            target_phrase_without_first_word = target_phrase.replace(first_word_in_target, '').strip()
            final_sentence = sentence_response.replace(target_phrase, f"{first_word_in_target} <ins>{target_phrase_without_first_word}</ins>")
            answer = answer.replace(first_word_in_target, '').strip()
        else:
            final_sentence = sentence_response.replace(target_phrase, f"<ins>{target_phrase}</ins>")

        result = {
            "example": final_sentence,
            "selector": selectors,
            "answer": answer,
            "eval_answer": answer,
            "eval_explain": "",
            "total_cost": total_cost,
        }
    return result


if __name__ == "__main__":
    result = generate_reading_2_problem_type_2(default_model="gpt-4o-mini",verbose=True)
    print(result)