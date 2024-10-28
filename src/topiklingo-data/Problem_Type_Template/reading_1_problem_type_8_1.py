import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re


def generate_reading_1_problem_type_8_1(verbose=False, default_model="gpt-4o-mini"):
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

    origin_example = ""
    example_str = ""
    for example in example_dict:
        origin_example = example["문장"].replace("( ㉠ )",example["정답"])
        example_str += (
            "주제어: " + example["주제어"] + " 결과: " + origin_example + " "
        )
        break

    if verbose:
        print("example_str")
        print(example_str)

    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content="#예시처럼 주어지는 주제어에 대한 이어지는 6개의 문장형태로 제시하라. #답변 예시 "
                + example_str
            ),
            HumanMessage(content="주제어: " + word_responses[0][1] + " 문장: "),
        ]
    )

    example_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', example_responses[0][2]).group(1))

    example_responses = example_responses[0][1]

    example_str = "문장: "+ origin_example + "\n"
    example_dict.pop(0)
    for example in example_dict:
        example_selector_str = ""
        idx = 1
        for answers in example["선택지"]:
            example_selector_str += (
                str(idx) + ". " + answers + " \n"
            )
            idx+=1
        example_str += (
            "문장을 올바르게 서술한 문제" +" 선택지: " + example_selector_str
        )
    selector_generate_model = Problem_Generate_Model(
            ["gpt-4o"], use_cache=True, temperature=0, verbose=verbose
        )
    selector_generate_model.request_models_responses(
            [
                SystemMessage(
                    content="너는 변형 문제 출제자이다. 주어지는 문장을 이용해 예시같은 선다형 문제를 만들어줘. 각 선택지는 25글자 이하로 구성해줘. 단, 정답 이외의 선택지는 문장과 어울리지 않는 선택지여야 해." + "예시: " + example_str
                ),
                HumanMessage(content="문장: " + example_responses),
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
                    content="example을 읽고 문제의 올바른 답을 골라 제시하라. 문제의 출력형태는 "
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
                    content="example: " + example_responses + "문제: " + selector_responses
                ),
            ]
        )
    eval_responses = problem_evaluate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', eval_responses[0][2]).group(1))

    answer_pattern = r"answer: (.*)\n"
    explain_pattern = r"explain: (.*)"

    answer_match = re.findall(answer_pattern, eval_responses[0][1])
    explain_match = re.findall(explain_pattern, eval_responses[0][1])
    answer_2 = re.findall(r"\d+\.\s*(.*)", answer_match[0])[0]

    text = example_responses
    text = [s + "." for s in text.split(".") if s]
    
    selectors = ["㉠", "㉡", "㉢", "㉣"]
    selector_answer = random.randint(0, 3)
    answer = text[1:-1][selector_answer]
    
    example = ""
    if verbose:
        print(text)
        print(selector_answer)
        print(answer)
        
    i=0
    text.remove(answer)
    for item in text:
        if i > 3:
            example += item
        else:
            example += f"{item} ({selectors[i]})"
        i += 1

    random.shuffle(selector_responses_filter)

    questions = [
        {
            "question": "다음 문장이 들어갈 곳으로 가장 알맞은 것을 고르십시오.",
            "example" : answer,
            "selector": ["㉠", "㉡", "㉢", "㉣"],
            "answer" : selectors[selector_answer],
            "eval_answer": selectors[selector_answer],
            "eval_explain": ""
        },
        {
            "question": "윗글의 내용과 같은 것을 고르십시오.",
            "selector": selector_responses_filter,
            "answer" : answer_2,
            "eval_answer": answer_2,
            "eval_explain": explain_match[0]
        }
    ]
    result = {
            "example": example,
            "questions": questions,
            "total_cost": total_cost,
        }
    return result

if __name__ == "__main__":
    result = generate_reading_1_problem_type_8_1(default_model="gpt-3.5-turbo",verbose=True)
    print(result)