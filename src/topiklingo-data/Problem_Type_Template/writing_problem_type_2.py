import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re
import ast


def generate_writing_problem_type_2(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=1.0, verbose=verbose
    )

    ### get random word
    main_topic = random_word()
    if verbose:
        print(main_topic)

    debatable_issue = "관광버스의 활용은 지역 경제 활성화에 기여하는 한편, 환경 오염과 교통 혼잡을 유발하는 문제를 동반한다."
    ## Generate a new word based on the random word
    problem_generate_model.request_models_responses(
        [
            SystemMessage(content="주제어와 관련된 양면성이 있는 사회적인 내용이 담긴 주제를 1문장으로 제시하라. no intro. no outro."),
            HumanMessage(content="주제어: " + main_topic + ", 예시: " + debatable_issue),
        ]
    )

    word_responses = problem_generate_model.get_model_responses()
    social_issue = word_responses[0][1]
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))

    problem_generate_model.request_models_responses(
        [
            SystemMessage(content="글 내용을 바탕으로 장단점을 논할 수 있는 명확한 주제어를 제시하라. 장점이 존재해야해. no intro. no outro."),
            HumanMessage(content="글: " + social_issue + ", 답변 예시: 인터넷의 장단점"),
        ]
    )

    word_responses = problem_generate_model.get_model_responses()
    refined_social_issue = word_responses[0][1]
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))

    refined_social_issue = refined_social_issue.replace('의 장단점', '')
    refined_social_issue = refined_social_issue.replace(' 장단점', '')
    # print(f"refined_social_issue: {refined_social_issue}")

    pros_and_cons_examples = Problem_Generate_Model(
        models, use_cache=True, temperature=1.0, verbose=verbose
    )

    sample_pros_and_cons = {"pro1": "", "pro2": "", "con1": "", "con2": ""}
    human_prompt = f"""주제어: {refined_social_issue}, 답변 형식: {sample_pros_and_cons}"""
    pros_and_cons_examples.request_models_responses(
        [
            SystemMessage(
                content=f"너는 장단점을 쓰는 봇이다. 주어진 주제에 대한 장단점을 2개씩 작성하라."
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    pros_and_cons_response = pros_and_cons_examples.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', pros_and_cons_response[0][2]).group(1))

    pros_and_cons_response = pros_and_cons_response[0][1]
    pros_and_cons_response = ast.literal_eval(pros_and_cons_response)
    # print(f"pros_and_cons_response: {pros_and_cons_response}")

    result = {
        "question": f"다음을 참고하여 ‘{refined_social_issue}의 장단점’에 대한 글을 200~300자로 쓰십시오. 단, 글의 제목을 쓰지 마십시오.",
        "keyword": refined_social_issue,
        "pro1": pros_and_cons_response['pro1'],
        "pro2": pros_and_cons_response['pro2'],
        "con1": pros_and_cons_response['con1'],
        "con2": pros_and_cons_response['con2'],
        "total_cost": total_cost
    }
    return result

if __name__ == "__main__":
    result = generate_writing_problem_type_2(default_model="gpt-4o-mini",verbose=True)
    print(result)