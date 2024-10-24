import sys, os

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import find_random_question
from util import random_word
import re
import random

def generate_reading_2_problem_type_11(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.7, verbose=verbose)

    main_title = random_word()
    if verbose:
        print(main_title)

    problem_generate_model.request_models_responses(
        [
            SystemMessage(content="주제어와 관련된 단어 1개를 제시하라."),
            HumanMessage(content="주제어: " + main_title + " 단어: "),
        ]
    )
    word_responses = problem_generate_model.get_model_responses()
    word = word_responses[0][1]
    def clean_text(text):
                pattern = r':\s*(.*)$'
                match = re.search(pattern, text)
                if match:
                    return match.group(1).strip()
                text = re.sub(r'\n', ' ', text)  # 개행 문자를 공백으로 대체
                return text.strip()
    word = clean_text(word)
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))

    keyword = 'TOPIK_2_READING'
    problem_type = 'READING_2_PROBLEM_TYPE_11'
    random_question = find_random_question(keyword, problem_type, detail_type=1, count=2)
    if verbose:
        print(random_question)
        
    example_dict = []
    for item in random_question:
        example_text = item['example']
        problem_generate_model.request_models_responses([
        SystemMessage(content="문장의 주제 단어 1개를 제시하라."),
        HumanMessage(content="문장: "+ example_text + "주제 단어: "),
            ]
        )
        total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', problem_generate_model.get_model_responses()[0][2]).group(1))
        title_responses = problem_generate_model.get_model_responses()[0][1]
        temp = {"주제": title_responses, "예문": example_text, "선택지": ', '.join(item['selector']), "정답": item['answer']}
        example_dict.append(temp)
    if verbose:
        print(example_dict)
    example_prompt = ""
    for item in example_dict:
        example_str = f"주제: {item['주제']}, 예문: {item['예문']}\n 선택지: {item['선택지']}\n 정답: {item['정답']}"
        example_prompt += example_str + "\n"
    if verbose:
        print(example_prompt)
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content="#너는 문제를 출제하는 선생님이야. 예시처럼 주어지는 주제에 대한 올바른 글의 목적 찾기 문제 1개를 생성해줘. 단, 각 선택지는 서로 다른 주제를 다루면 좋을 것 같아. #답변 예시 "
                + example_prompt
            ),
            HumanMessage(content="주제: " + word),
        ]
    )
    problem_responses = problem_generate_model.get_model_responses()
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', problem_responses[0][2]).group(1))
    if verbose:
        print(problem_responses[0][1])
        
    def extract_parts(text):
        # 예문 추출
        example_pattern = r'예문:\s*(.*)'
        example_match = re.search(example_pattern, text)
        example = example_match.group(1).strip() if example_match else None

        # 선택지 추출
        options_pattern = r'선택지:\s*(.*)'
        options_match = re.search(options_pattern, text)
        if options_match:
            options = options_match.group(1).strip().split(', ')
            for option in options:
                option = clean_text(option)
        else:
            options = []

        # 정답 추출
        answer_pattern = r'정답:\s*(.*)'
        answer_match = re.search(answer_pattern, text)
        answer = answer_match.group(1).strip() if answer_match else None

        return example, options, answer 

    example, options, answer = extract_parts(problem_responses[0][1])
    random.shuffle(options)
    
    # check option length == 4, answer in options
    if len(options) != 4:
        raise ValueError("Option length is not 4")
    if answer not in options:
        raise ValueError("Answer is not in options")
    
    result = {
        "example": example,
        "selector": options,
        "answer": answer,
        "eval_answer": answer,
        "eval_explain": "",
        "total_cost": total_cost
    }
    return result