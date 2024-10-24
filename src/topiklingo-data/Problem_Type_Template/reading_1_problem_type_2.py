import sys, os

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

import json
import random
from util import Image_Loader
from util import find_random_question
from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
import re

def generate_reading_1_problem_type_2(default_model="gpt-4o-mini", verbose=False):
    total_cost = 0
    keyword = 'TOPIK_1_READING'
    problem_type = 'READING_1_PROBLEM_TYPE_2'
    result = find_random_question(keyword, problem_type, detail_type=1, count=1)[0]

    original_data = {'selector':result['selector'],'answer':result['answer']}
    
    transformed_data = {
    "selector": {},
    "answer": {}
    }

    # selector 변환
    for idx, sentence in enumerate(original_data['selector'], 1):
        transformed_data['selector'][str(idx)] = sentence

    # answer 변환
    for key, value in transformed_data['selector'].items():
        if value == original_data['answer']:
            transformed_data['answer'][key] = value
            break

    # JSON으로 출력
    transformed_data['example'] = result['example']
    content_text = [transformed_data]
    if verbose:
        print(content_text)
    def extract_non_answer_selectors(selector, answer):
        answer_keys = set(answer.keys())
        non_answer_selectors = {key: value for key, value in selector.items() if key not in answer_keys}
        
        return non_answer_selectors

    collect_example_prompt = ""
    wrong_example_prompt = ""
    for item in content_text:
        image_text = Image_Loader(item["example"]).get_image_text()
        example = "이미지 내용: " + image_text
        selector = extract_non_answer_selectors(item["selector"], item["answer"])
        selector = list(selector.values())
        collect_example_prompt += example+"\n"
        idx =1
        for s in selector:
            collect_example_prompt += str(idx)+". " + s + "\n"
            idx+=1
        wrong_example_prompt += example
        wrong_example_prompt += "\n"
        wrong_example_prompt += "틀린 내용: " + list(item["answer"].values())[0] + "\n"

    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=1, verbose=verbose
    )
        
    image_text = Image_Loader(item["example"]).get_image_text()
    
    problem_generate_model.request_models_responses([
            SystemMessage(content="OCR로 인식된 이미지 내용과 문장을 참고하여 이미지를 올바르게 서술한 문장 3개를 생성하라. 단, 각 문장은 '~니다.' 로 끝나는 15글자 이내로 서로 다른 내용을 서술해야 한다. #결과 예시 "+ collect_example_prompt),
            HumanMessage(content="이미지 내용 : "+ image_text + "\n"),
        ]
        )
    selector_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', selector_responses[0][2]).group(1))

    problem_generate_model.request_models_responses([
            SystemMessage(content="이미지 내용을 토대로 틀린 내용 1개를 생성하라. #결과 예시 "+ wrong_example_prompt + "단, 결과는 문장만 제시한다."),
            HumanMessage(content="이미지 내용 : "+ image_text + "\n" + "틀린 내용: "),
        ]
        )
    answer_response = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', answer_response[0][2]).group(1))
    
    if verbose:
        print(image_text)
        print(selector_responses[0][1])
        print(answer_response[0][1])

    selector_responses = selector_responses[0][1]
    answer_response = answer_response[0][1]
    selector_responses = re.sub(r"\d+\.\s+", "", selector_responses)
    selector_responses = selector_responses.split("\n")
    if len(selector_responses) > 3:
        selector_responses = selector_responses[:3]
    selector_responses
    selector_responses.append(answer_response)
    random.shuffle(selector_responses)

    # selector_responses 길이가 4가 아닌 경우 ValueError 발생
    if len(selector_responses) != 4:
        raise ValueError("selector_responses length is not 4")
    
    # selector_responses 중 빈 문자열이 있는 경우 ValueError 발생
    for selector in selector_responses:
        if selector == "":
            raise ValueError("selector_responses has empty string")
    
    for i in range(len(selector_responses)):
        selector_responses[i] = selector_responses[i].strip()
    
    # selector_responses 안의 문자열 길이 순 정렬
    selector_responses = sorted(selector_responses, key=lambda x: len(x))
    
    result = {
        "example": item["example"],
        "selector": selector_responses,
        "answer" : answer_response,
        "eval_answer": answer_response,
        "explain":"",
        "total_cost": total_cost,
    }
    return result

if __name__ == "__main__":
    result = generate_reading_1_problem_type_2(default_model="gpt-3.5-turbo",verbose=True)
    print(result)