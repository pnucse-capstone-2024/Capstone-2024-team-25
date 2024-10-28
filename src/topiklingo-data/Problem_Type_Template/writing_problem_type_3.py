import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re
import ast


def generate_writing_problem_type_3(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    models = [default_model]
    editorial_topic_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=1.0, verbose=verbose
    )

    ### get random word
    main_topic = random_word()
    if verbose:
        print(main_topic)

    answer_format = {"topic": ""}
    ## Generate a new word based on the random word
    editorial_topic_generate_model.request_models_responses(
        [
            SystemMessage(content="주제어와 관련된 사회적인 내용이 담긴 주제를 한 단어로 생성하라. no intro. no outro."),
            HumanMessage(content=f"주제어: {main_topic}, 답변 형식: {answer_format}"),
        ]
    )
    editorial_topic_responses = editorial_topic_generate_model.get_model_responses()
    editorial_topic = editorial_topic_responses[0][1]
    editorial_topic = ast.literal_eval(editorial_topic)['topic']
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', editorial_topic_responses[0][2]).group(1))

    problem_intro_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.0, verbose=verbose
    )

    problem_intro_example = "최근 세계적으로 환경오염을 줄이기 위해 많은 노력을 기울이고 있습니다. 환경오염을 줄일 수 있는 효과적인 방법에 대해 아래의 내용을 중심으로 주장하는 글을 쓰십시오."
    problem_intro_generate_model.request_models_responses(
        [
            SystemMessage(content="논설문 작성을 안내하는 글의 도입부를 두 문장으로 작성해. 예시 문장 구조를 따라야해. '~에 대해 아래의 내용을 중심으로 주장하는 글을 쓰십시오.'로 끝나야해"),
            HumanMessage(content=f"논설문 주제어: {editorial_topic}, 예시: {problem_intro_example}"),
        ]
    )

    problem_intro_responses = problem_intro_generate_model.get_model_responses()
    problem_intro = problem_intro_responses[0][1]
    problem_intro = problem_intro.strip()
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', problem_intro_responses[0][2]).group(1))

    editorial_guide_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.5, verbose=verbose
    )

    last_sentence_of_problem_intro = problem_intro.split('. ')[-1]
    editorial_guide_example = {"target": "환경오염을 줄일 수 있는 효과적인 방법에 대해 아래의 내용을 중심으로 주장하는 글을 쓰십시오.",
                            "key1": "환경오염으로 인해 어떤 문제가 생기고 있습니까?",
                            "key2": "환경오염을 줄이기 위해 어떤 노력이 필요합니까?",
                            "key3": "사람들이 실천할 수 있는 효과적인 방법은 무엇입니까?"
                            }
    editorial_guide_generate_model.request_models_responses(
        [
            SystemMessage(content="다음 문장을 완성시켜줄 중심 내용을 3가지 작성해줘. 각 문장은 예시의 의문문 어투를 따라야해. no intro. no outro."),
            HumanMessage(content=f"Target: {last_sentence_of_problem_intro}, 답변 형식: {editorial_guide_example}"),
        ]
    )
    
    editorial_guide_responses = editorial_guide_generate_model.get_model_responses()
    editorial_guide = editorial_guide_responses[0][1]
    editorial_guide = ast.literal_eval(editorial_guide)
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', editorial_guide_responses[0][2]).group(1))

    result = {
        "question": f"다음을 주제로 하여 자신의 생각을 600~700자로 글을 쓰십시오. 단, 문제를 그대로 옮겨 쓰지 마십시오.",
        "paragraph": problem_intro,
        "guide1": editorial_guide['key1'],
        "guide2": editorial_guide['key2'],
        "guide3": editorial_guide['key3'],
        "total_cost": total_cost
    }
    return result

if __name__ == "__main__":
    result = generate_writing_problem_type_3(default_model="gpt-4o-mini",verbose=True)
    print(result)