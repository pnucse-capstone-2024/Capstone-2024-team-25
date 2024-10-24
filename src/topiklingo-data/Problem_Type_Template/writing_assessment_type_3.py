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

def assess_writing_type_3(default_model="gpt-4o-mini",verbose=False, topic=None, user_answer=None):
    assessment_criteria_3_1_1 = "주어진 과제를 충실히 수행하였는가?"
    assessment_criteria_3_1_2 = "주제와 관련된 내용으로 구성하였는가?"
    assessment_criteria_3_1_3 = "내용을 풍부하고 다양하게 표현하였는가?"
    assessment_criteria_3_2_1 = "글의 구성이 명확하고 논리적인가?"
    assessment_criteria_3_2_2 = "중심 생각이 잘 구성되어 있는가?"
    assessment_criteria_3_2_3 = "논리 전개에 도움이 되는 담화 표지를 적절하게 사용하여 조직적으로 연결하였는가?"
    assessment_criteria_3_3_1 = "문법과 어휘를 다양하고 풍부하게 사용하며 적절한 문법과 어휘를 선택하여 사용하였는가?"
    assessment_criteria_3_3_2 = "문법, 어휘, 맞춤법 등의 사용이 정확한가?"
    assessment_criteria_3_3_3 = "글의 목적과 기능에 따라 격식에 맞게 글을 썼는가?"

    level_to_points_criteria_for_3_1 = {"상": 12/3, "중": 8/3, "하": 4/3}
    level_to_points_criteria_for_3_2 = {"상": 12/3, "중": 8/3, "하": 4/3}
    level_to_points_criteria_for_3_3 = {"상": 26/3, "중": 18/3, "하": 10/3}
    
    models = [default_model]
    assessment_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.5, verbose=verbose
    )

    total_points = 0
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_3_1_1, level_to_points_criteria_for_3_1)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_3_1_2, level_to_points_criteria_for_3_1)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_3_1_3, level_to_points_criteria_for_3_1)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_3_2_1, level_to_points_criteria_for_3_2)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_3_2_2, level_to_points_criteria_for_3_2)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_3_2_3, level_to_points_criteria_for_3_2)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_3_3_1, level_to_points_criteria_for_3_3)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_3_3_2, level_to_points_criteria_for_3_3)
    total_points += assess_by_criteria(assessment_model, topic, user_answer, assessment_criteria_3_3_3, level_to_points_criteria_for_3_3)

    total_points = round(total_points)

    penalty_weight = 1
    if len(user_answer) < 400:
        penalty_weight = 0.0
    elif len(user_answer) >= 400 and len(user_answer) < 433:
        penalty_weight = 0.5
    elif len(user_answer) >= 433 and len(user_answer) < 466:
        penalty_weight = 0.666
    elif len(user_answer) >= 466 and len(user_answer) < 500:
        penalty_weight = 0.833
    elif len(user_answer) >= 500:
        penalty_weight = 1.0

    total_points = total_points * penalty_weight

    if total_points > 50:
        total_points = 50
    print(f"Article length: {len(user_answer)}")
    print(f"total_points: {total_points}")
    return total_points

if __name__ == "__main__":
    user_answer = "환경오염을 줄이는 것은 우리 세대의 가장 중요한 과제 중 하나입니다. 지구 온난화, 대기오염, 해양 플라스틱 문제 등 다양한 환경 문제가 심각해지고 있는 현 시점에서, 우리 모두가 적극적으로 행동에 나서야 할 때입니다. 먼저, 개인적 차원에서 할 수 있는 일부터 시작해야 합니다. 일회용품 사용을 줄이고 재사용 가능한 제품을 선택하는 것이 중요합니다. 예를 들어, 플라스틱 빨대 대신 금속 빨대를, 비닐봉지 대신 에코백을 사용하는 등의 작은 실천이 모여 큰 변화를 만들 수 있습니다. 또한, 에너지 절약을 위해 불필요한 전등을 끄고, 대중교통이나 자전거 이용을 늘리는 것도 효과적인 방법입니다. 기업들의 역할도 중요합니다. 생산 과정에서 발생하는 오염물질을 줄이고, 친환경 기술 개발에 투자해야 합니다. 재생 에너지 사용을 확대하고, 제품 포장을 최소화하는 등의 노력이 필요합니다. 또한, 기업의 사회적 책임을 다하기 위해 환경보호 활동에 적극적으로 참여하고, 소비자들에게 환경 친화적인 선택을 할 수 있도록 정보를 제공해야 합니다. 정부 차원에서는 강력한 환경 정책과 법규를 마련하고 시행해야 합니다. 오염 물질 배출 기준을 강화하고, 위반 시 엄중한 처벌을 해야 합니다. 또한, 친환경 기술 개발을 위한 연구비 지원, 재활용 인프라 구축, 친환경 제품 사용 촉진을 위한 세제 혜택 등 다양한 정책을 통해 환경보호를 장려해야 합니다. 교육의 중요성도 간과할 수 없습니다. 어릴 때부터 환경 보호의 중요성을 인식하고 실천할 수 있도록 학교와 가정에서 체계적인 환경 교육이 이루어져야 합니다. 이를 통해 미래 세대가 환경에 대한 책임감을 가지고 지속 가능한 발전을 이끌어갈 수 있을 것입니다. 결론적으로, 환경오염을 줄이기 위해서는 개인, 기업, 정부, 교육 기관 등 사회 전반의 협력과 노력이 필요합니다. 우리 모두가 환경 보호의 주체라는 인식을 가지고, 각자의 위치에서 할 수 있는 일들을 실천해 나간다면, 보다 깨끗하고 건강한 지구를 후대에 물려줄 수 있을 것입니다. 지금 당장 시작하는 작은 변화가 미래를 바꿀 수 있다는 믿음으로, 우리 모두 환경 보호에 동참합시다."
    user_answer = "환경오염은 우리 일상에 깊숙이 자리 잡은 필수적인 도구가 되었습니다. 정보의 바다라 불리는 환경오염은 전 세계의 지식을 손쉽게 접할 수 있게 해주며, 시공간의 제약 없이 소통을 가능케 합니다. 교육, 비즈니스, 엔터테인먼트 등 다양한 분야에서 혁신을 이끌어내고 있죠. 그러나 환경오염에는 부작용도 있습니다. 개인정보 유출, 사이버 범죄, 허위정보 확산 등의 문제가 끊임없이 제기되고 있습니다. 또한 과도한 사용으로 인한 중독과 현실과의 괴리 현상도 우려됩니다. 따라서 환경오염의 장점을 최대한 활용하면서도 그 부작용을 최소화하기 위한 노력이 필요합니다. 디지털 리터러시 교육과 적절한 규제, 그리고 개인의 자제력이 요구되는 시대입니다."
    topic = '최근 세계적으로 환경오염을 줄이기 위해 많은 노력을 기울이고 있습니다. 환경오염을 줄일 수 있는 효과적인 방법에 대해 아래의 내용을 중심으로 주장하는 글을 쓰십시오.'
    
    result = assess_writing_type_3(default_model="gpt-4o-mini",verbose=False, topic=topic, user_answer=user_answer)
    print(result)