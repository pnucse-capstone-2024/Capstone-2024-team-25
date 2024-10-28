import sys, os

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import find_random_question
from util import random_word
import re
import random

def generate_reading_2_problem_type_12(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.7, verbose=verbose)

    main_title = random_word(difficulty="normal")
    if verbose:
        print(main_title)

    problem_generate_model.request_models_responses(
        [
            SystemMessage(content="주제어와 관련된 일상적인 단어 1개를 제시하라."),
            HumanMessage(content="주제어: " + main_title + " 단어: "),
        ]
    )
    word_responses = problem_generate_model.get_model_responses()
    word = word_responses[0][1]
    def clean_text(text):
        text = text.replace("'", '＇')
        text = text.replace('"', '＇')
        
        pattern = r':\s*(.*)$'
        match = re.search(pattern, text)
        if match:
            return match.group(1).strip()
        text = re.sub(r'\n', ' ', text)  # 개행 문자를 공백으로 대체
        return text.strip()
    word = clean_text(word)
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))

    keyword = 'TOPIK_2_READING'
    problem_type = 'READING_2_PROBLEM_TYPE_12'
    random_question = find_random_question(keyword, problem_type, detail_type=1, count=2)
    if verbose:
        print(random_question)


    def split_text(input_text):
        # <보기>로 문자열을 분리하여 리스트로 만듭니다.
        parts = re.split(r'(<보기>)', input_text)
        
        # 중복된 <보기>를 제거하고 문장과 <보기>를 분리하여 저장
        result = []
        temp_text = ''
        
        for part in parts:
            if part == '<보기>':
                if temp_text.strip():
                    result.append(temp_text.strip())
                    temp_text = ''
                if '<보기>' not in result:
                    result.append(part)
            else:
                temp_text += part
        
        # 마지막으로 남은 텍스트를 추가
        if temp_text.strip():
            result.append(temp_text.strip())
        
        example_text = result[0]
        answer_text = result.pop()
        return example_text, answer_text

    def remove_parentheses_only(text):
        # 괄호만 제거하는 정규식
        text = re.sub(r'\s*\((.*?)\)\s*', r' \1 ', text)
        # 연속된 공백을 하나로 정리
        text = re.sub(r'\s+', ' ', text)
        return text.strip()

    def fill_in_blank(text, answer, position):
        # 정답을 빈칸에 채워넣기
        text = text.replace(position, answer)
        # 나머지 빈칸을 정규식으로 제거하기
        text = re.sub(r'\( *㉠ *\)', '', text)
        text = re.sub(r'\( *㉡ *\)', '', text)
        text = re.sub(r'\( *㉢ *\)', '', text)
        text = re.sub(r'\( *㉣ *\)', '', text)
        
        return remove_parentheses_only(text)
    example_dict = []
    for question in random_question:
        example_text, answer_text =  split_text(question['example'])
        sentence = fill_in_blank(example_text, answer_text, question['answer'])
        if verbose:
            print(example_text)
            print(answer_text)
            print("origin_text")
            print(sentence)
        problem_generate_model.request_models_responses([
            SystemMessage(content="문장의 주제 단어 1개를 제시하라."),
            HumanMessage(content="문장: "+ sentence + "주제 단어: "),
                ]
            )
        total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', problem_generate_model.get_model_responses()[0][2]).group(1))
        title_responses = problem_generate_model.get_model_responses()[0][1]
        title = clean_text(title_responses)
        temp = {"주제어":title, "문장": sentence, "선택지":question['selector']}
        example_dict.append(temp)
            
        if verbose:
            print("example_dict")
            print(example_dict)

        example_str = ""
        for example in example_dict:
            example_str += (
                "주제어: " + example["주제어"] + " 결과: " + example["문장"] + " "
            )
        if verbose:
            print("example_str")
            print(example_str)
            
    problem_generate_model.request_models_responses(
        [
            SystemMessage(
                content="#예시처럼 주어지는 주제에 대한 글을 제시하라. 문장은 6개로 이루어지며 이어지는 순서가 명확해야 한다. #답변 예시 "
                + example_str
            ),
            HumanMessage(content="주제: " + word + " 문장: "),
        ]
    )
    example_responses = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', example_responses[0][2]).group(1))

    example_responses = example_responses[0][1]
    text = example_responses
    text = clean_text(text)
    if verbose:
        print(text)
    origin_text = text
    text = [s + "." for s in text.split(".") if s]

    selectors = ["㉠", "㉡", "㉢", "㉣"]
    selector_answer = random.randint(0, 3)
    answer = text[1:][selector_answer]

    example = ""
    i=0
    text.remove(answer)
    for item in text:
        if i > 3:
            example += item
        else:
            example += f"{item} ({selectors[i]})"
        i += 1

    result = {
                "example": str([[answer.strip()],[example.strip()]]),
                "selector": selectors,
                "answer": selectors[selector_answer],
                "eval_answer": selectors[selector_answer],
                "eval_explain":origin_text,
                "total_cost": total_cost,
            }
    return result