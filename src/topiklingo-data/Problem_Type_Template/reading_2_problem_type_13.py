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

def remove_specific_pattern(text):
    # Define the regular expression pattern to match "(중략)" within double quotes
    pattern = r'(".*?\(중략\).*?")'
    
    # Function to remove the specific pattern from the matched group
    def replace_pattern(match):
        return match.group(0).replace("(중략) ", "")
    
    # Use re.sub with the custom replacement function
    cleaned_text = re.sub(pattern, replace_pattern, text)
    
    return cleaned_text

def generate_reading_2_problem_type_13(verbose=False, default_model="gpt-4o-mini"):
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
    novel_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=1.0, verbose=verbose
    )

    human_prompt = f"""주제어: {main_topic_generated}
    (중략)이라는 표현을 2개 포함하라."""
    novel_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"너는 전지적 작가 시점에서 소설을 쓰는 봇이다. 인물은 2~3명으로 구성하고, 인물의 이름도 소설에 포함하라. 인물의 대사는 큰 따옴표로 표시하라. 인물의 심정을 나타내는 대사도 1개 포함하라. 총 15개의 문장으로 구성하라."
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    novel_response = novel_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', novel_response[0][2]).group(1))

    novel_response = novel_response[0][1]
    cleaned_novel_response = remove_specific_pattern(novel_response)
    
    example_question = {
        '화자': '부모님',
        '심정을 드러내는 문장': '네가 그걸 어떻게 알아?',
        '정답': ['의심스러운'],
        '오답': ['후회스러운', '실망스러운', '짜증스러운']
        }
    human_prompt = f"""글: {cleaned_novel_response}. 정답은 문장을 보고 유추할 수 있어야한다."""
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content=f"주어진 글에서 화자의 심정을 드러내는 문장을 찾고, 정답 심정 하나와 오답 심정 3개를 만들어라. 예시: {example_question}"
            ),
            HumanMessage(content=human_prompt),
        ]
    )

    feeling_response = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', feeling_response[0][2]).group(1))

    feeling_response = feeling_response[0][1]
    feeling_response = ast.literal_eval(feeling_response)
    if "'" in feeling_response['심정을 드러내는 문장']:
        feeling_response['심정을 드러내는 문장'] = feeling_response['심정을 드러내는 문장'].strip("'")
    
    example_question = {
        '정답': ['준은 여행지에서 해 뜨는 방향을 한 번에 찾았다.'],
        '오답': ['준의 부모님은 아들과 같은 능력을 가지고 있었다.', '준은 공부를 잘해서 학교에서 모르는 사람이 없었다.', '준의 부모님은 아들의 재능을 발견한 후 걱정하기 시작했다.']
        }
    human_prompt = f"""
    글: {cleaned_novel_response}"""
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
    
    final_example = cleaned_novel_response.replace(feeling_response['심정을 드러내는 문장'], f"<ins>{feeling_response['심정을 드러내는 문장']}</ins>")
    
    question_1_selector = feeling_response['정답'] + feeling_response['오답']
    question_2_selector = matching_content_response['정답'] + matching_content_response['오답']
    
    random.shuffle(question_1_selector)
    random.shuffle(question_2_selector)
    
    result = {
            "example": final_example,
            "questions": [{
                "question": f"밑줄 친 부분에 나타난 ‘{feeling_response['화자']}’의 심정으로 가장 알맞은 것을 고르십시오.",
                "selector": question_1_selector,
                "answer": feeling_response['정답'][0],
                "eval_answer": feeling_response['정답'][0],
                }, 
                        {
                "question": "윗글의 내용으로 알 수 있는 것을 고르십시오.",
                "selector": question_2_selector,
                "answer": matching_content_response['정답'][0],
                "eval_answer": matching_content_response['정답'][0],
                }],
            "total_cost": total_cost,
        }
    return result

if __name__ == "__main__":
    result = generate_reading_2_problem_type_13(default_model="gpt-4o-mini",verbose=True)
    print(result)