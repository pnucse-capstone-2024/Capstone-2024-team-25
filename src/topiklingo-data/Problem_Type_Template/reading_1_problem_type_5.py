import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re
from konlpy.tag import Okt

def generate_reading_1_problem_type_5(verbose=False, default_model="gpt-4o-mini"):
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
    problem_type = 'READING_1_PROBLEM_TYPE_5'
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
                content="#예시처럼 주어지는 주제어에 대한 이어지는 4개의 문장형태로 제시하라. 단, 외국인도 이해할 수 있는 쉬운 단어를 사용해야 된다. #답변 예시 "
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
                "빈칸에 들어갈 알맞은 문제" +" 선택지: " + example_selector_str
            )
        else:
            example_str += (
                "문장을 올바르게 서술한 문제" +" 선택지: " + example_selector_str
            )
        
        question_number += 1
    
    def masking_data_extract(origin_text):
        text = random.choice(origin_text.split(".")[:-1])
        okt = Okt()
        pos_data = okt.pos(text)
        adverbs = [x[0] for x in pos_data if x[1] == 'Adverb']
        conjunctions = [x[0] for x in pos_data if x[1] == 'Conjunction']
        adjectives = [x[0] for x in pos_data if x[1] == 'Verb' or x[1] == 'Adjective']
        masking_targets = ["부사", "접속사", "동사 및 형용사"]
        masking_data = []
        for masking_target in masking_targets:
            if masking_target == "부사":
                for adverb in adverbs:
                    masking_data.append(adverb)
            elif masking_target == "접속사":
                for conjunction in conjunctions:
                    masking_data.append(conjunction)
            elif masking_target == "동사 및 형용사":
                for adjective in adjectives:
                    masking_data.append(adjective)
        masking_data = random.choice(masking_data)
        return masking_data
    masking_data = masking_data_extract(example_responses)
    masking_example = example_responses.replace(masking_data,"(  )",1)
    if verbose:
        print(masking_example)

    selector_generate_model = Problem_Generate_Model(
    ["gpt-4o"], use_cache=True, temperature=0.3, verbose=verbose
)
    selector_generate_model.request_models_responses(
            [
                SystemMessage(
                    content="너는 변형 문제 출제자이다. 주어지는 문장을 이용해 예시같은 선다형 문제를 만들어줘. 단, 정답 이외의 선택지는 헷갈리지 않게 틀린 선택지여야 한다." + "예시: " + example_str
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
                    "\n"
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

    answer_pattern = r"answer:(.*)\n"
    explain_pattern = r"explain:(.*)"

    answer_match = re.findall(answer_pattern, eval_responses[0][1])
    explain_match = re.findall(explain_pattern, eval_responses[0][1])
    # answer_1 = masking_data
    answer_1 = re.findall(r"\d+\.\s*(.*)", answer_match[0])[0]
    answer_2 = re.findall(r"\d+\.\s*(.*)", answer_match[1])[0]
    if verbose:
        print("answer_1 : " + answer_1)
        print("masking_data : " + masking_data)
    if masking_data != answer_1:
        for item in selector_responses_filter[0:4]:
            if masking_data == item:
                answer_1 = masking_data
                flag = 1
                break
            flag = -1
        if flag == -1:
            for i in range(4):
                if answer_1 == selector_responses_filter[i]:
                    selector_responses_filter[i] = masking_data
                    explain_match[0]= explain_match[0].replace(answer_1,masking_data)
                    answer_1 = masking_data
                
    selector_1 = selector_responses_filter[0:4]
    random.shuffle(selector_1)
    selector_2 = selector_responses_filter[4:8]
    random.shuffle(selector_2)
    
    #selctor strip
    selector_1 = [x.strip() for x in selector_1]
    selector_2 = [x.strip() for x in selector_2]
    answer_2 = answer_2.strip()
    
    #check answer in selector
    if answer_2 not in selector_2:
        raise ValueError("answer_2 not in selector_2")
    if masking_data not in selector_1:
        ran = random.randint(1,4)
        selector_1[ran] = masking_data
        explain_match[0] = "정답은 " + masking_data + "입니다."
    
    questions = [
        {
            "question": "( ㉠ )에 들어갈 말로 가장 알맞은 것을 고르십시오.",
            "selector": selector_1,
            "answer": masking_data,
            "eval_answer": masking_data,
            "eval_explain": explain_match[0],
        },
        {
            "question": "<보기>의 내용과 맞는 것을 고르십시오.",
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
    result = generate_reading_1_problem_type_5(default_model="gpt-3.5-turbo",verbose=True)
    print(result)