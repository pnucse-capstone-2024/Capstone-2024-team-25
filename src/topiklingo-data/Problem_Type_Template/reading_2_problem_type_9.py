import sys, os

# For .py
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
# For .ipynb
# sys.path.append(os.path.dirname(os.getcwd()))

from util import random_word
from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
import re
import ast
import random

def generate_reading_2_problem_type_9(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    keyword = 'TOPIK_2_READING'
    problem_type = 'READING_2_PROBLEM_TYPE_2'
    # random_question = find_random_question(keyword, problem_type, detail_type=1, count=2)
    # random_question

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
            SystemMessage(content="주제어와 관련된 일상적인 명사 1개를 제시하라. no intro. no outro."),
            HumanMessage(content="주제어: " + main_topic + " 명사: "),
        ]
    )
    word_responses = problem_generate_model.get_model_responses()
    main_topic_generated = word_responses[0][1]
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))

    example_question = {'제목': '대출 금리 하락세, 부동산 시장 기지개',
                        '정답': ['대출 금리가 떨어지면서 부동산 시장이 살아나기 시작했다.'],
                        '오답': ['부동산 시장에 대한 규제가 대출 금리 하락에 영향을 미쳤다.', '대출 금리가 하락했지만 부동산 시장의 거래는 줄어들고 있다.', '부동산 시장을 활성화하려고 대출 금리 안정화 대책이 논의되고 있다.']
        }
    human_prompt = f"""주제어: {main_topic_generated}."""
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"너는 신문 기사의 제목을 만드는 봇이다. 기사 제목을 가장 잘 설명하는 문장 1개, 아닌 문장 3개를 만들어라. 예시: {example_question}"
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    response = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', response[0][2]).group(1))

    response = response[0][1]
    response = ast.literal_eval(response)

    # target_phrase = None
    # similar_phrase = None
    # for phrase in phrase_list:
    #     if phrase in sentence_response:
    #         target_phrase = phrase
    #     else:
    #         similar_phrase = phrase
    # assert target_phrase is not None, "Target phrase not found in the sentence"
    # assert similar_phrase is not None, "Similar phrase not found in the sentence"
    # print(f'Target Phrase: {target_phrase}\nSimilar Phrase: {similar_phrase}')

    selector = response['정답'] + response['오답']
    random.shuffle(selector)
    
    result = {
        "example": response['제목'],
        "selector": selector,
        "answer": response['정답'][0],
        "eval_answer": response['정답'][0],
        "total_cost": total_cost,
    }
    return result
    
if __name__ == "__main__":
    result = generate_reading_2_problem_type_9(default_model="gpt-4o-mini",verbose=True)
    print(result)