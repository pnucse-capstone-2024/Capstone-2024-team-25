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

def generate_reading_2_problem_type_8(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    
    ### Create a model for generating a problem
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.5, verbose=verbose
    )

    ### get random word
    main_topic = random_word(difficulty="normal")
    if verbose:
        print(main_topic)

    ## Generate a new word based on the random word
    problem_generate_model.request_models_responses(
        [
            SystemMessage(content="주제어와 관련된 명사 1개를 제시하라. 해당 명사는 이야기의 소재가 될 것이다. no intro. no outro."),
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

    example_question = {'이야기': 
        '꽃집을 지나다가 꽃말에 이끌려 금잔화 꽃씨를 샀다. 화분에 심어 사무실의 내 책상 위에 두었더니 어느 날 싹이 텄다. 때맞춰 물도 주며 나는 수시로 들여다보았다. 신기했다. 작고 여린 싹은 눈에 띄게 쑥쑥 자랐다. 그런데 내가 상상한 모습이 아니었다. 도대체 여기서 어떻게 꽃이 핀다는 건지. 무순처럼 길쭉하게 위로만 자라는 것이었다.',
        '심정을 드러내는 문장': '도대체 여기서 어떻게 꽃이 핀다는 건지'
        }
    human_prompt = f"""주어: '나', 주제어: {main_topic_generated}.
    """
    paragraph_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"""너는 교훈을 주는 이야기를 만드는 봇이다. 화자의 심정을 드러내는 문장이 이야기에 포함되어야 한다. 하지만 해당 문장에 심정을 직접적으로 나타내는 단어는 없어야 한다. 총 10개의 문장으로 구성하라.
                #답변 예시: {example_question}"""
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    paragraph_response = paragraph_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', paragraph_response[0][2]).group(1))

    paragraph_response = paragraph_response[0][1]
    paragraph_response = ast.literal_eval(paragraph_response)
    
    example_question = {
        '정답': ['의심스러운'],
        '오답': ['고통스러운', '조심스러운', '부담스러운']
        }
    human_prompt = f"""문장: {paragraph_response['심정을 드러내는 문장']}"""
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"주어진 문장에 담긴 심정을 유추하는 봇이다. 정답 심정 하나와 오답 심정 3개를 만들어라. 예시: {example_question}"
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    feeling_response = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', feeling_response[0][2]).group(1))

    feeling_response = feeling_response[0][1]
    feeling_response = ast.literal_eval(feeling_response)
    
    example_question = {
        '정답': ['내가 금잔화 화분을 옮긴 곳은 햇볕이 잘 들었다.'],
        '오답': ['내 책상 위에 둔 금잔화는 금방 말라 죽었다.', '나는 금잔화 화분에 물을 제대로 주지 못했다.', '나는 꽃집에서 금잔화가 피어 있는 화분을 샀다.']
        }
    human_prompt = f"""
    글: {paragraph_response['이야기']}"""
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"글의 내용과 같은 문장 1개와 다른 문장 3개를 만들어라. 예시: {example_question}"
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    matching_content_response = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', matching_content_response[0][2]).group(1))

    matching_content_response = matching_content_response[0][1]
    matching_content_response = ast.literal_eval(matching_content_response)
    
    final_example = paragraph_response['이야기'].replace(paragraph_response['심정을 드러내는 문장'], f"<ins>{paragraph_response['심정을 드러내는 문장']}</ins>")

    question_1_selector = feeling_response['정답'] + feeling_response['오답']
    random.shuffle(question_1_selector)
    question_2_selector = matching_content_response['정답'] + matching_content_response['오답']
    random.shuffle(question_2_selector)

    result = {
        "example": final_example,
        "questions": [{
            "question": "밑줄 친 부분에 나타난 ‘나’의 심정으로 가장 알맞은 것을 고르십시오.",
            "selector": question_1_selector,
            "answer": feeling_response['정답'][0],
            "eval_answer": feeling_response['정답'][0],
            "eval_explain": ""
            }, 
                    {
            "question": "윗글의 내용과 같은 것을 고르십시오.",
            "selector": question_2_selector,
            "answer": matching_content_response['정답'][0],
            "eval_answer": matching_content_response['정답'][0],
            "eval_explain": ""
            }],
        "total_cost": total_cost,
    }
    return result

if __name__ == "__main__":
    result = generate_reading_2_problem_type_8(default_model="gpt-4o-mini",verbose=True)
    print(result)