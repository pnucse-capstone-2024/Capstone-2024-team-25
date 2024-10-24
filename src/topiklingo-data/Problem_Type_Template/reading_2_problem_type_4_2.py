import sys, os

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import find_random_question
import re
import random

def generate_reading_2_problem_type_4_2(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    
    keyword = 'TOPIK_2_READING'
    problem_type = 'READING_2_PROBLEM_TYPE_4'
    random_question = find_random_question(keyword, problem_type, detail_type=2, count=1)
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
                SystemMessage(content="주어지는 문장을 동일한 뜻을 지닌 다른 문장으로 바꿔줘. 대치법, 동의어, 유의어 등을 활용하면 좋아."),
                HumanMessage(content="바꿀 문장: " + select + " 바뀐 문장: "),
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

    result = {
        "example": random_question['example'],
        "selector": selector,
        "answer": random_question['answer'],
        "eval_answer": random_question['answer'],
        "eval_explain": "",
        "total_cost": total_cost,
    }

    return result