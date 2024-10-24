import sys, os

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import find_random_question
import re
import random

def generate_reading_2_problem_type_3(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    keyword = 'TOPIK_2_READING'
    problem_type = 'READING_2_PROBLEM_TYPE_3'
    random_question = find_random_question(keyword, problem_type, detail_type=1, count=1)
    if verbose:
        print(random_question)
    random_question = random_question[0]
    random_question_selector = random_question['selector']
    if verbose:
        print(random_question_selector)

    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=1, verbose=verbose)
    selector = []
    for select in random_question_selector:
        if verbose:
            print(f"selector: {select}")
        problem_generate_model.request_models_responses(
            [
                SystemMessage(content="주어지는 단어의 동의어 혹은 외래어 1개를 제시해줘. 단, 주어지는 단어와 글자 길이가 같아야 하고 결과는 단어만 입력해. 예를 들어, 4글자 단어가 주어지면 4글자 단어를 입력하고 2글자 단어면 2글자 단어를 입력해."),
                HumanMessage(content="#주어지는 단어: " + select + " 동의어: "),
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
        selector.append(word)
        
        if select == random_question['answer']:
            random_question['answer'] = word
        
    if verbose:
        print(selector)

    random.shuffle(selector)
    result = {
        "example": random_question['example'],
        "selector": selector,
        "answer": random_question['answer'],
        "eval_answer": random_question['answer'],
        "eval_explain": "",
        "total_cost": total_cost,
    }
    return result
