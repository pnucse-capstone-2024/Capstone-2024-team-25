import sys, os

# For .py
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
# For .ipynb
# sys.path.append(os.path.dirname(os.getcwd()))

import random
from util import random_word
from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
import re
import ast

def generate_reading_2_problem_type_15(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    
    ### Create a model for generating a problem
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.5, verbose=verbose
    )

    ### get random word
    main_topic = random_word()
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

    ### Create a model for generating a problem
    models = [default_model]
    paragraph_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=1.0, verbose=verbose
    )

    human_prompt = f"""주제어: {main_topic_generated}"""
    paragraph_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"너는 논설문을 쓰는 봇이다. 총 8개의 문장으로 구성하라."
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    paragraph_response = paragraph_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', paragraph_response[0][2]).group(1))

    paragraph_response = paragraph_response[0][1]
    
    example_question = {
        '정답': ['과학 정책에 대한 정부의 지나친 개입을 경계하고 있다.'],
        '오답': ['과학 기술 발전을 위해서는 연구가 중요함을 강조하고 있다.', '과학 기술 발전이 경제 성장에 미치는 영향력에 감탄하고 있다.', '과학 정책 수립 시 우주 과학이 소홀히 다루어질 것을 우려하고 있다.']
        }
    human_prompt = f"""논설문: {paragraph_response}"""
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"주어진 논설문에서 필자의 태도를 알아내라. 필자의 태도를 나타내는 선택지 1개, 필자의 태도와 반대되는 선택지 3개를 만들어라. 예시: {example_question}"
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    attitude_response = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', attitude_response[0][2]).group(1))

    attitude_response = attitude_response[0][1]
    attitude_response = ast.literal_eval(attitude_response)
    
    example_question = {
        '정답': ['많은 국가들이 신에너지 개발에 대한 투자를 줄이고 있다.'],
        '오답': ['과학 정책이 빠르게 변해서 과학 기술이 발전할 수 있었다.', '정부가 우주 산업에 대한 규제를 풀어 성장한 민간 기업이 있다.', '우주 개발에 참여 중인 민간 기업이 화성에 호텔을 건설하고 있다.']
        }
    human_prompt = f"""
    글: {paragraph_response}"""
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"글의 내용을 요약한 문장 1개와 틀린 문장 3개를 만들어라. 예시: {example_question}"
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    matching_content_response = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', matching_content_response[0][2]).group(1))

    matching_content_response = matching_content_response[0][1]
    matching_content_response = ast.literal_eval(matching_content_response)
    
    question_1_selector = attitude_response['정답'] + attitude_response['오답']
    question_2_selector = matching_content_response['정답'] + matching_content_response['오답']
    
    random.shuffle(question_1_selector)
    random.shuffle(question_2_selector)
    
    result = {
        "example": paragraph_response,
        "questions": [{
            "question": f"윗글에 나타난 필자의 태도로 가장 알맞은 것을 고르십시오.",
            "selector": question_1_selector,
            "answer": attitude_response['정답'][0],
            "eval_answer": attitude_response['정답'][0],
            }, 
                    {
            "question": "윗글의 내용과 같은 것을 고르십시오.",
            "selector": question_2_selector,
            "answer": matching_content_response['정답'][0],
            "eval_answer": matching_content_response['정답'][0],
            }],
        "total_cost": total_cost,
    }
    return result

if __name__ == "__main__":
    result = generate_reading_2_problem_type_15(default_model="gpt-4o-mini",verbose=True)
    print(result)