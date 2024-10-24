import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re
from konlpy.tag import Okt

def generate_reading_1_problem_type_8_2(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.1, verbose=verbose
    )

    main_title = random_word(difficulty="easy")
    if verbose:
        print(main_title)

    problem_generate_model.request_models_responses(
        [
            SystemMessage(content="주제어와 관련된 일상적인 단어 1개를 제시하라."),
            HumanMessage(content="주제어: " + main_title + " 단어: "),
        ]
    )
    word_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))

    keyword = 'TOPIK_1_READING'
    problem_type = 'READING_1_PROBLEM_TYPE_8'
    random_question = find_random_question(keyword, problem_type, detail_type=1, count=1)

    sentence = random_question[0]['example'][0]
    problem_generate_model.request_models_responses([
    SystemMessage(content="문장의 주제 단어 1개를 제시하라."),
    HumanMessage(content="문장: "+ sentence + "주제 단어: "),
        ]
    )
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', problem_generate_model.get_model_responses()[0][2]).group(1))
    title_responses = problem_generate_model.get_model_responses()[0][1]

    example_dict = []
    for question in random_question:
        temp = {"주제어":title_responses, "문장": sentence, "선택지":question['selector'], "정답":question['answer']}
        example_dict.append(temp)
            
    if verbose:
        print("example_dict")
        print(example_dict)

    example_str = ""
    for example in example_dict:
        example_str += (
            "주제어: " + example["주제어"] + " 결과: " + example["문장"].replace("( ㉠ )",example["정답"]) + " "
        )
        break

    if verbose:
        print("example_str")
        print(example_str)

    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content="#예시처럼 주어지는 주제어에 대한 이어지는 5개의 문장형태로 제시하라. 단, 외국인도 이해할 수 있는 쉬운 어휘를 사용해야 된다. #답변 예시 "
                + example_str
            ),
            HumanMessage(content="주제어: " + word_responses[0][1] + " 문장: "),
        ]
    )

    example_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', example_responses[0][2]).group(1))

    example_responses = example_responses[0][1]
        
    example_str = "문장: "+ example_dict[0]["문장"].replace("( ㉠ )", "(  )") + "\n"
    question_number = 1
    for example in example_dict:
        example_selector_str = ""
        idx = 1
        for answers in example["선택지"]:
            example_selector_str += (
                str(idx) + ". " + answers + " \n"
            )
            idx+=1
        if question_number==1:
            example_str += (
                "빈칸에 들어갈 알맞은 말 고르기 문제" +" 선택지: " + example_selector_str
            )
        else:
            example_str += (
                "문장을 올바르게 서술한 문제" +" 선택지: " + example_selector_str
            )
        
        question_number += 1

    def masking_data_extract(origin_text):
        # 문장을 랜덤으로 선택
        sentences = origin_text.split(".")[:-2]
        selected_sentence = random.choice(sentences).strip()
        
        # 형태소 분석
        okt = Okt()
        pos_data = okt.pos(selected_sentence)
        
        # 마스킹 대상 형태소 선택
        adverbs = [x[0] for x in pos_data if x[1] == 'Adverb']
        conjunctions = [x[0] for x in pos_data if x[1] == 'Conjunction']
        adjectives = [x[0] for x in pos_data if x[1] in ['Verb', 'Adjective']]
        
        masking_targets = adverbs + conjunctions + adjectives
        if not masking_targets:
            return selected_sentence  # 마스킹할 형태소가 없으면 원래 문장 반환

        # 랜덤으로 형태소 하나 선택
        target = random.choice(masking_targets)
        
        # 선택된 형태소가 포함된 어절 찾기
        words = selected_sentence.split()
        target_word_index = next((i for i, word in enumerate(words) if target in word), None)
        if target_word_index is None:
            return selected_sentence  # 형태소가 포함된 어절을 찾지 못하면 원래 문장 반환
        
        # 앞뒤 어절 포함하여 추출
        start_index = max(0, target_word_index - 1)
        end_index = min(len(words), target_word_index + 2)
        
        masked_phrase = ' '.join(words[start_index:end_index])
        
        return masked_phrase

    masking_data = masking_data_extract(example_responses)
    masking_example = example_responses.replace(masking_data,"(  )",1)
    if verbose:
        print(masking_example)

    selector_generate_model = Problem_Generate_Model(
        ["gpt-4o"], use_cache=True, temperature=0.5, verbose=verbose
    )
    selector_generate_model.request_models_responses(
            [
                SystemMessage(
                    content="너는 변형 문제 출제자이다. 주어지는 문장을 이용해 예시같은 선다형 문제를 만들어줘. 각 선택지는 25글자 이하로 구성해줘. 단, 정답 이외의 선택지는 문장과 어울리지 않는 선택지여야 해." + "예시: " + example_str
                ),
                HumanMessage(content="문장: " + masking_example),
            ]
        )
    selector_responses = selector_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', selector_responses[0][2]).group(1))
    
    if verbose:
        print(selector_responses)
    selector_responses = selector_responses[0][1]
    selector_responses
    selector_responses_filter = re.findall(r"\d+\.\s*(.*)", selector_responses)

    eval_models = ["gpt-4o"]
    problem_evaluate_model = Problem_Generate_Model(
        eval_models, use_cache=True, temperature=0, verbose=verbose
    )
    problem_evaluate_model.request_models_responses(
            [
                SystemMessage(
                    content="example을 읽고 두 문제의 올바른 답을 골라 제시하라. 각 문제의 출력형태는 "
                    "answer"
                    ":"
                    "답"
                    " "
                    " explain"
                    ":"
                    "해설"
                    " 형식으로 제시한다. 답은 번호와 답안을 함께 제시한다."
                ),
                HumanMessage(
                    content="example: " + masking_example + "문제: " + selector_responses
                ),
            ]
        )
    eval_responses = problem_evaluate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', eval_responses[0][2]).group(1))

    answer_pattern = r"answer: (.*)\n"
    explain_pattern = r"explain: (.*)"

    answer_match = re.findall(answer_pattern, eval_responses[0][1])
    explain_match = re.findall(explain_pattern, eval_responses[0][1])
    # answer_1 = masking_data
    answer_1 = re.findall(r"\d+\.\s*(.*)", answer_match[0])[0]
    answer_2 = re.findall(r"\d+\.\s*(.*)", answer_match[1])[0]

    # if masking_data != answer_1:
    #     for item in selector_responses_filter[0:4]:
    #         if masking_data == item:
    #             answer_1 = masking_data
    #             flag = 1
    #             break
    #         flag = -1
    #     if flag == -1:
    #         for i in range(4):
    #             if answer_1 == selector_responses_filter[i]:
    #                 selector_responses_filter[i] = masking_data
    #                 explain_match[0]= explain_match[0].replace(answer_1,masking_data)
    #                 answer_1 = masking_data
    
    selector_1 = selector_responses_filter[0:4]
    selector_2 = selector_responses_filter[4:8]
    random.shuffle(selector_1)
    random.shuffle(selector_2)
    
    questions = [
        {
            "question": "㉠ 에 들어갈 알맞은 말을 고르십시오.",
            "selector": selector_1,
            "answer": answer_1,
            "eval_answer": answer_1,
            "eval_explain": explain_match[0],
        },
        {
            "question": "이 글의 내용과 같은 것을 고르십시오.",
            "selector": selector_2,
            "answer": answer_2,
            "eval_answer": answer_2,
            "eval_explain": explain_match[1],
        }
    ]
    example_responses = masking_example.replace("(  )", "( ㉠ )", 1)
    result = {
        "example": example_responses,
        "questions": questions,
        "total_cost": total_cost,
    }
    return result

if __name__ == "__main__":
    result = generate_reading_1_problem_type_8_2(default_model="gpt-3.5-turbo",verbose=True)
    print(result)