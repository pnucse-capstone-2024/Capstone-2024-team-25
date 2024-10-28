import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re

def generate_reading_1_problem_type_4(verbose=False, default_model="gpt-4o-mini"):
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
    problem_type = 'READING_1_PROBLEM_TYPE_4'
    random_question = find_random_question(keyword, problem_type, detail_type=1, count=2)

    example_dict = []
    for question in random_question:
        sentence = question['example']
        problem_generate_model.request_models_responses([
        SystemMessage(content="문장의 주제 단어 1개를 제시하라."),
        HumanMessage(content="문장: "+ sentence + "주제 단어: "),
            ]
        )
        total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', problem_generate_model.get_model_responses()[0][2]).group(1))
        title_responses = problem_generate_model.get_model_responses()[0][1]
        temp = {"주제어":title_responses, "문장": sentence, "선택지":question['selector']}
        example_dict.append(temp)

    # example_dict = [
    #     {
    #         "주제어": "제빵",
    #         "문장": "제 동생은 빵을 잘 만듭니다. 동생이 만든 빵은 아주 맛있습니다. 저도 빵 만드는 방법을 배우고 싶습니다.",
    #     },
    #     {
    #         "주제어": "수영",
    #         "문장": "저는 바다에서 수영하는 것을 좋아합니다. 여름에는 수영을 하러 바다에 자주 갑니다. 빨리 여름이 오면 좋겠습니다.",
    #     },
    # ]
    example_dict = random.sample(example_dict, 2)
    
    if verbose:
        print(example_dict)

    example_str = ""
    for example in example_dict:
        example_str += (
            "주제어: " + example["주제어"] + " 결과: " + example["문장"] + " "
        )

    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content="#예시처럼 주어지는 주제어에 대한 문장을 제시하라. #답변 예시 "
                + example_str
            ),
            HumanMessage(content="주제어: " + word_responses[0][1] + " 문장: "),
        ]
    )

    example_responses = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', example_responses[0][2]).group(1))
    
    example_responses = example_responses[0][1]
        
    example_str = ""
    for example in example_dict:
        example_selector_str = ""
        idx = 1
        for answers in example["선택지"]:
            example_selector_str += (
                str(idx) + ". " + answers + " \n"
            )
            idx+=1
        example_str += (
            "문장: " + example["문장"] + " 선택지: " + example_selector_str
        )
    if verbose:
        print(example_str)
        print(example_responses)
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content="너는 문장을 읽고 올바른 중심 내용을 선택하는 문제 출제자이다. 틀린 선택지 3개, 정답 1개의 예시같은 선다형 문제를 만들어라." + "예시: " + example_str
            ),
            HumanMessage(content="문장: " + example_responses),
        ]
    )
    selector_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', selector_responses[0][2]).group(1))
    
    selector_responses = selector_responses[0][1]
    if verbose:
        print(selector_responses)

    eval_models = ["gpt-4o"]
    problem_evaluate_model = Problem_Generate_Model(
        eval_models, use_cache=True, temperature=0, verbose=verbose
    )

    problem_evaluate_model.request_models_responses(
        [
            SystemMessage(
                content="example을 읽고 중심 내용으로 올바른 답을 골라 제시하라. 출력은 "
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

    selector_responses = re.findall(r"\d+\.\s*(.*)", selector_responses)

    selector = []
    for sentence in selector_responses:
        selector.append(sentence)

    if len(selector) > 4:
        selector = selector[:4]
    random.shuffle(selector)

    answer_pattern = r"answer: (.*)\n"
    explain_pattern = r"explain: (.*)"

    answer_match = re.search(answer_pattern, eval_responses[0][1])
    explain_match = re.search(explain_pattern, eval_responses[0][1])

    if answer_match and explain_match:
        answer_text = answer_match.group(1)
        explain_text = explain_match.group(1)

    answer_text = re.findall(r"\d+\.\s*(.*)", answer_text)
    result = {
        "example": example_responses,
        "selector": selector,
        "answer": answer_text[0],
        "eval_answer": answer_text[0],
        "explain": explain_text,
        "total_cost": total_cost,
    }
    
    return result

if __name__ == "__main__":
    result = generate_reading_1_problem_type_4(default_model="gpt-3.5-turbo",verbose=True)
    print(result)