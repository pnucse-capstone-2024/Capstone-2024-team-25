import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re
import ast

### Paragraph
sample_paragraph_0 = {"title": "초대합니다.", "content": "한 달 전에 이사를 했습니다. 그동안 집안 정리 때문에 정신이 없었는데 이제 좀 정리가 됐습니다." \
        "그래서 저희 집에서 집들이를 할 생각입니다. 5월 17일 17시에 와 주시겠습니까? 그 시간이 괜찮으신지 연락 주시면 감사하겠습니다."}
sample_paragraph_1 = {"title": "축제 관련 문의", "content": "지난 주말 '인주시 별빛 축제'에 갔던 외국인입니다. 지금까지 살면서 이렇게 많은 별을 본 적이 한 번도 없었습니다. 이번 축제에서 별도 보고 공연도 볼 수 있어서 정말 좋았습니다. 혹시 축제가 언제 또 있습니까? 있다면 이런 멋진 경험을 다시 하고 싶습니다."}

### Blank Data
sample_blank_0 = {"article": "한 달 전에 이사를 했습니다. \
    그동안 집안 정리 때문에 정신이 없었는데 이제 좀 정리가 됐습니다. <blank1>. <blank2>? \
    그 시간이 괜찮으신지 연락 주시면 감사하겠습니다.", 
    "blank1": "그래서 저희 집에서 집들이를 할 생각입니다", 
    "blank2": "5월 17일 17시에 와 주시겠습니까"
    }
sample_blank_1 = {"article": "지난 주말 '인주시 별빛 축제'에 갔던 외국인입니다. 지금까지 살면서 이렇게 많은 별을 <blank1> 한 번도 없었습니다. 이번 축제에서 별도 보고 공연도 볼 수 있어서 정말 좋았습니다. 혹시 축제가 언제 또 있습니까? 있다면 이런 멋진 경험을 다시 <blank2>", 
    "blank1": "본 적이", 
    "blank2": "하고 싶습니다"
    }

def generate_writing_problem_type_1(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
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

    article_topics = ['광고문', '안내문']
    article_topic = random.choice(article_topics)
    sample_paragraph = sample_paragraph_1
    human_prompt = f"""주제어: {main_topic_generated}, 답변 예시: {sample_paragraph}"""
    paragraph_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"너는 주제어와 관련된 {article_topic}을 쓰는 봇이다."
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    paragraph_response = paragraph_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', paragraph_response[0][2]).group(1))

    paragraph_response = paragraph_response[0][1]
    paragraph_response = ast.literal_eval(paragraph_response)
    # print(f"paragraph_response: {paragraph_response}")

    blank_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=1.0, verbose=verbose
    )

    sample_blank = sample_blank_1
    human_prompt = f"""Target article: {paragraph_response['content']}"""
    blank_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"너는 문장 일부를 대체하는 빈칸을 만드는 봇이야. 문맥 상 쉽게 유추할 수 있는 부분을 빈칸을 2개 만들어. 답변 예시: {sample_blank}"
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    blank_paragraph_response = blank_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', blank_paragraph_response[0][2]).group(1))

    blank_paragraph_response = blank_paragraph_response[0][1]
    blank_paragraph_response = ast.literal_eval(blank_paragraph_response)

    result = {
        "question": "다음을 읽고 ㉠과 ㉡에 들어갈 말을 각각 한 문장으로 쓰십시오.",
        "article": blank_paragraph_response['article'],
        "total_cost": total_cost
    }
    return result

if __name__ == "__main__":
    result = generate_writing_problem_type_1(default_model="gpt-4o-mini",verbose=True)
    print(result)