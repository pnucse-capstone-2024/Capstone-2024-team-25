import sys, os

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re

def generate_reading_1_problem_type_1_1(difficulty="Normal", verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.1, verbose=verbose
    )

    main_title = random_word(difficulty="Easy")
    if verbose:
        print(main_title)

    problem_generate_model.request_models_responses(
        [
            SystemMessage(content="주제어와 관련된 쉬운 단어 1개를 제시하라. 답변은 제시된 형식대로 단어만 답하라."),
            HumanMessage(content="주제어: "+main_title+ " 단어: "),
        ]
    )
    word_responses = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))

    keyword = 'TOPIK_1_READING'
    problem_type = 'READING_1_PROBLEM_TYPE_1'
    random_question = find_random_question(keyword, problem_type, detail_type=1, count=2)

    example_dict = []
    for question in random_question:
        temp = {"문장": question['example'], "주제어": question["answer"]}
        example_dict.append(temp)
        
    # example_dict = [
    #     {"주제어": "계절", "문장": "여름에는 덥습니다. 겨울에는 추웁니다."},
    #     {"주제어": "과일", "문장": "사과가 맛있습니다. 바나나도 맛있습니다."},
    # ]
    example_dict = random.sample(example_dict, 2)
    if verbose:
        print(example_dict)
        
    example_str = ""
    for example in example_dict:
        example_str += (
            "주제어: " + example["주제어"] + " 문장: " + example["문장"] + " "
        )

    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content="#4개 단어로 이루어진 간단한 문장 2개를 제시하라. 답변은 문장만 반환하라. #답변 예시 "
                + example_str
            ),
            HumanMessage(content="주제어: " + word_responses[0][1] + " 문장: "),
        ]
    )
    example_responses = problem_generate_model.get_model_responses()
    
    def clean_text(text):
        pattern = r':\s*(.*)$'
        match = re.search(pattern, text)
        if match:
            return match.group(1).strip()
        text = re.sub(r'\d+\.', '', text)  # 숫자 다음에 오는 점 (예: "1.", "2.") 제거
        text = re.sub(r'\n', ' ', text)  # 개행 문자를 공백으로 대체
        return text.strip()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', example_responses[0][2]).group(1))

    example_text = clean_text(example_responses[0][1])
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content="주어지는 문장을 뜻하는 쉬운 두 글자 단어 1개를 제시하라. 단, 문장 안에 포함된 단어는 제외한다."
            ),
            HumanMessage(
                content="문장: " + example_text + " 상위 개념 두 글자 명사: "
            ),
        ]
    )
    answer_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', answer_responses[0][2]).group(1))

    if main_title in example_responses[0][1].strip():
        if word_responses[0][1].strip() in example_responses[0][1].strip():
            answer = answer_responses[0][1].strip()
        else:
            answer = word_responses[0][1].strip()
    elif word_responses[0][1].strip() in example_responses[0][1].strip():
        if answer_responses[0][1].strip() in example_responses[0][1].strip():
            answer = main_title
        else:
            answer = answer_responses[0][1].strip()
    else:
        if answer_responses[0][1].strip() in example_responses[0][1].strip():
            answer = word_responses[0][1].strip()
        else:
            answer = answer_responses[0][1].strip()

    if difficulty == "Easy":
        problem_generate_model.request_models_responses(
            [
                SystemMessage(
                    content="주어지는 단어와 관련 없는 주제의 두 글자 명사 3개를 제시하라."
                ),
                HumanMessage(content="0. " + answer + "\n"),
            ]
        )
    elif difficulty == "Normal":
        problem_generate_model.request_models_responses(
            [
                SystemMessage(
                    content="주어지는 단어와 같은 수준의 상의어지만, 명확히 다른 주제의 두 글자 단어 3개를 제시하라."
                ),
                HumanMessage(content="0. " + answer + "\n"),
            ]
        )
    elif difficulty == "Hard":
        problem_generate_model.request_models_responses(
            [
                SystemMessage(content="주어지는 단어와 다른 주제의 명사 3개를 제시하라."),
                HumanMessage(content="0. " + answer + "\n"),
            ]
        )

    selector_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', selector_responses[0][2]).group(1))

    example_responses = example_text
    selector_responses = selector_responses[0][1]
    selector_responses = re.sub(r"\d+\.\s+", "", selector_responses)
    selector_responses = selector_responses.split("\n")
    if len(selector_responses) > 3:
        selector_responses = selector_responses[:3]
    selector_responses.append(answer)
    random.shuffle(selector_responses)

    if len(selector_responses) != 4:
        raise ValueError("selector_responses length is not 4")
    for selector in selector_responses:
        if selector == "":
            raise ValueError("selector_responses has empty string")
    
    for i in range(len(selector_responses)):
        selector_responses[i] = selector_responses[i].strip()
    
    eval_models = ["gpt-4o"]
    problem_evaluate_model = Problem_Generate_Model(
        eval_models, use_cache=True, temperature=0, verbose=verbose
    )

    problem_evaluate_model.request_models_responses(
        [
            SystemMessage(
                content="example에 해당하는 알맞은 단어를 맞추는 문제이다. selector에서 올바른 답을 골라 제시하라. 출력은 "
                "answer"
                ":"
                "답"
                "\n"
                " explain"
                ":"
                "해설"
                " 형식으로 제시한다."
            ),
            HumanMessage(
                content="example: "
                + example_responses
                + "selector: "
                + str(selector_responses)
            ),
        ]
    )
    eval_responses = problem_evaluate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', eval_responses[0][2]).group(1))


    answer_pattern = r"answer:(.*)\n"
    explain_pattern = r"explain:(.*)"

    answer_match = re.search(answer_pattern, eval_responses[0][1])
    explain_match = re.search(explain_pattern, eval_responses[0][1])

    if answer_match and explain_match:
        answer_text = answer_match.group(1)
        explain_text = explain_match.group(1)

    example_responses.replace("\n","")
    answer_text = answer_text.strip()
    explain_text = explain_text.replace("\n","").strip()
    
    result = {
        "example": example_responses,
        "selector": selector_responses,
        "answer": answer,
        "eval_answer": answer_text,
        "eval_explain": explain_text,
        "total_cost": total_cost,
    }
    return result