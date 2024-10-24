import sys, os
# For .py
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
import ast
from openai import OpenAI
import numpy as np
import json
import nltk
from nltk.tokenize import word_tokenize
from nltk import pos_tag
from difflib import SequenceMatcher
from openai import OpenAI
import numpy as np
import json

def assess_by_criteria(llm_model, topic, user_answer, criteria, level_to_points):
    llm_model.request_models_responses(
        [
            SystemMessage(content="너는 주어진 기준에 따라 글을 채점해줘. 채점은 상/중/하 중 하나로. 예시: {'level': '상'}"),
            HumanMessage(content=f"user_answer: {user_answer}, topic: {topic}, criteria: {criteria}"),
        ]
    )
    responses = llm_model.get_model_responses()
    result = responses[0][1]
    result = ast.literal_eval(result)
    point = level_to_points[result["level"]]
    print(f"point: {point}")
    return point

def assess_writing_type_2(default_model="gpt-4o-mini",verbose=False, topic=None, user_answer=None):
    assessment_criteria_2_1_1 = "주어진 과제를 충실히 수행하였는가?"
    assessment_criteria_2_1_2 = "주제와 관련된 내용으로 구성하였는가?"
    assessment_criteria_2_1_3 = "주어진 내용을 풍부하고 다양하게 표현하였는가?"
    assessment_criteria_2_2_1 = "글의 구성이 명확하고 논리적인가?"
    assessment_criteria_2_2_2 = "글의 내용에 따라 단락 구성이 잘 이루어졌는가?"
    assessment_criteria_2_2_3 = "논리 전개에 도움이 되는 담화 표지를 적절하게 사용하여 조직적으로 연결하였는가?"
    assessment_criteria_2_3_1 = "문법과 어휘를 다양하고 풍부하게 사용하며 적절한 문법과 어휘를 선택하여 사용하였는가?"
    assessment_criteria_2_3_2 = "문법, 어휘, 맞춤법 등의 사용이 정확한가?"
    assessment_criteria_2_3_3 = "글의 목적과 기능에 따라 격식에 맞게 글을 썼는가?"
    
    level_to_points_criteria_for_2_1 = {"상": 7/3, "중": 5/3, "하": 2/3}
    level_to_points_criteria_for_2_2 = {"상": 7/3, "중": 5/3, "하": 2/3}
    level_to_points_criteria_for_2_3 = {"상": 16/3, "중": 12/3, "하": 6/3}
    
    models = [default_model]
    assessment_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.5, verbose=verbose
    )

    total_points = 0
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_2_1_1, level_to_points_criteria_for_2_1)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_2_1_2, level_to_points_criteria_for_2_1)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_2_1_3, level_to_points_criteria_for_2_1)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_2_2_1, level_to_points_criteria_for_2_2)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_2_2_2, level_to_points_criteria_for_2_2)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_2_2_3, level_to_points_criteria_for_2_2)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_2_3_1, level_to_points_criteria_for_2_3)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_2_3_2, level_to_points_criteria_for_2_3)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_2_3_3, level_to_points_criteria_for_2_3)

    penalty_weight = 1
    if len(user_answer) < 200:
        penalty_weight = 2/3

    total_points = total_points * penalty_weight
    total_points = round(total_points)

    if total_points > 30:
        total_points = 30
    print(f"Article length: {len(user_answer)}")
    print(f"total_points: {total_points}")
    return total_points

if __name__ == "__main__":
    user_answer = "인터넷은 우리 일상에 깊숙이 자리 잡은 필수적인 도구가 되었습니다. 정보의 바다라 불리는 인터넷은 전 세계의 지식을 손쉽게 접할 수 있게 해주며, 시공간의 제약 없이 소통을 가능케 합니다. 교육, 비즈니스, 엔터테인먼트 등 다양한 분야에서 혁신을 이끌어내고 있죠. 그러나 인터넷에는 부작용도 있습니다. 개인정보 유출, 사이버 범죄, 허위정보 확산 등의 문제가 끊임없이 제기되고 있습니다. 또한 과도한 사용으로 인한 중독과 현실과의 괴리 현상도 우려됩니다. 따라서 인터넷의 장점을 최대한 활용하면서도 그 부작용을 최소화하기 위한 노력이 필요합니다. 디지털 리터러시 교육과 적절한 규제, 그리고 개인의 자제력이 요구되는 시대입니다."
    user_answer = "인터넷은 좋은 점도 있고 나쁜 점도 있습니다. 좋은 점은 정보를 쉽게 찾을 수 있고 멀리 있는 사람들과 대화할 수 있다는 거에요. 그리고 온라인 쇼핑도 할 수 있어서 편리합니다. 하지만 나쁜 점도 있어요. 가짜 뉴스가 많이 퍼지고 있고, 어린이들이 나쁜 것을 보게 될 수도 있습니다. 그리고 너무 많이 사용하면 중독될 수 있어요. 그래서 인터넷을 사용할 때는 조심해야 해요. 부모님들은 아이들이 인터넷을 사용할 때 옆에 있어주는 게 좋습니다. 그리고 모든 사람들이 인터넷에서 본 정보가 진짜인지 확인해봐야 해요."
    user_answer = "인터넷은 좋은 점도 있고 나쁜 점도 있습니다. 좋은 점은 정보를 쉽게 찾을 수 있고 멀리 있는 사람들과 대화할 수 있다는 거에요. 그리고 온라인 쇼핑도 할 수 있어서 편리합니다. "
    topic = '인터넷의 장단점'
    
    result = assess_writing_type_2(default_model="gpt-4o-mini",verbose=False, topic=topic, user_answer=user_answer)
    print(result)