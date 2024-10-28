import asyncio
import sys, os
import random
import re
from konlpy.tag import Okt

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question


def clean_text(text):
    text = text.replace("'", "＇")
    text = text.replace('"', "＇")

    pattern = r":\s*(.*)$"
    match = re.search(pattern, text)
    if match:
        return match.group(1).strip()
    text = re.sub(r"\n", " ", text)  # 개행 문자를 공백으로 대체
    return text.strip()


def replace_placeholder(text, replacement):
    # 정규 표현식으로 ( ) 사이의 공백을 찾아서 replacement로 대체
    result = re.sub(r"\(\s*\)", f"{replacement}", text)
    return result


def masking_data_extract(origin_text):
    # 문장을 랜덤으로 선택
    sentences = origin_text.split(".")[:-1]
    selected_sentence = random.choice(sentences).strip()

    # 형태소 분석
    okt = Okt()
    pos_data = okt.pos(selected_sentence)

    # 마스킹 대상 형태소 선택
    adverbs = [x[0] for x in pos_data if x[1] == "Adverb"]
    conjunctions = [x[0] for x in pos_data if x[1] == "Conjunction"]
    adjectives = [x[0] for x in pos_data if x[1] in ["Verb", "Adjective"]]

    masking_targets = adverbs + conjunctions + adjectives
    if not masking_targets:
        return selected_sentence  # 마스킹할 형태소가 없으면 원래 문장 반환

    # 랜덤으로 형태소 하나 선택
    target = random.choice(masking_targets)

    # 선택된 형태소가 포함된 어절 찾기
    words = selected_sentence.split()
    target_word_index = next(
        (i for i, word in enumerate(words) if target in word), None
    )
    if target_word_index is None:
        return (
            selected_sentence  # 형태소가 포함된 어절을 찾지 못하면 원래 문장 반환
        )

    # 앞뒤 어절 포함하여 추출
    start_index = max(0, target_word_index - 1)
    end_index = min(len(words), target_word_index + 2)

    masked_phrase = " ".join(words[start_index:end_index])

    return masked_phrase


async def generate_problem_0(problem_generate_model, example_dict, origin_example_text):
    example_str_0 = ""
    for example in example_dict:
        example_str_0 += (
            "논설문: " + replace_placeholder(example["문장"], example["정답"]) + " "
        )
        break

    question_number = 0
    for example in example_dict:
        example_selector_str = ""
        idx = 1
        for answers in example["선택지"]:
            example_selector_str += str(idx) + ". " + answers + " \n"
            idx += 1
        if question_number == 0:
            example_str_0 += (
                "\n 논설문을 쓴 목적으로 올바른 것은?"
                + " 선택지: \n"
                + example_selector_str
            )
        question_number += 1

    selector_generate_model = Problem_Generate_Model(
        ["gpt-4o-mini"], use_cache=True, temperature=0, verbose=verbose
    )
    selector_generate_model.request_models_responses(
        [
            SystemMessage(
                content="너는 한국어 문제를 만드는 선생님이야. 논설문을 쓴 가장 알맞는 이유 맞추기 선다형 문제를 출제해줘. 단, 정답 이외의 선택지는 논설문과 어울리지 않는 선택지여야 해"
                + "예시: "
                + example_str_0
            ),
            HumanMessage(content="논설문: " + origin_example_text),
        ]
    )
    selector_responses = selector_generate_model.get_model_responses()
    selector_responses = selector_responses[0][1]
    selector_responses_filter = re.findall(r"\d+\.\s*(.*)", selector_responses)

    return selector_responses_filter


async def generate_problem_1(problem_generate_model, example_dict, origin_example_text, example_responses):
    example_str_1 = "논설문: " + example_dict[0]["문장"].replace("( )", "(  )") + "\n"
    question_number = 0
    for example in example_dict:
        example_selector_str = ""
        idx = 1
        for answers in example["선택지"]:
            example_selector_str += str(idx) + ". " + answers + " \n"
            idx += 1
        if question_number == 1:
            example_str_1 += (
                "\n 빈칸에 들어갈 알맞은 말로 알맞은 것은?"
                + " 선택지: \n"
                + example_selector_str
            )
        question_number += 1

    masking_data = masking_data_extract(example_responses)
    masking_example = example_responses.replace(masking_data, "(  )", 1)

    selector_generate_model = Problem_Generate_Model(
        ["gpt-4o-mini"], use_cache=True, temperature=0, verbose=verbose
    )
    selector_generate_model.request_models_responses(
        [
            SystemMessage(
                content="너는 한국어 문제를 만드는 선생님이야. 논설문을 읽고 빈칸에 들어갈 말 맞추기 선다형 문제를 출제해줘. 단, 정답 이외의 선택지는 논설문과 어울리지 않는 선택지여야 해"
                + "예시: "
                + example_str_1
            ),
            HumanMessage(content="논설문: " + masking_example),
        ]
    )
    selector_responses = selector_generate_model.get_model_responses()
    selector_responses = selector_responses[0][1]
    selector_responses_filter = re.findall(r"\d+\.\s*(.*)", selector_responses)

    return selector_responses_filter, masking_example


async def generate_problem_2(problem_generate_model, example_dict, origin_example_text):
    example_str_2 = ""
    for example in example_dict:
        example_str_2 += (
            "논설문: " + replace_placeholder(example["문장"], example["정답"]) + " "
        )
        break

    question_number = 0
    for example in example_dict:
        example_selector_str = ""
        idx = 1
        for answers in example["선택지"]:
            example_selector_str += str(idx) + ". " + answers + " \n"
            idx += 1
        if question_number == 2:
            example_str_2 += (
                "\n 논설문을 올바르게 서술한 것은?"
                + " 선택지: \n"
                + example_selector_str
            )
        question_number += 1

    selector_generate_model = Problem_Generate_Model(
        ["gpt-4o-mini"], use_cache=True, temperature=0, verbose=verbose
    )
    selector_generate_model.request_models_responses(
        [
            SystemMessage(
                content="너는 한국어 문제를 만드는 선생님이야. 논설문을 읽고 문장을 올바르게 서술한 선다형 문제를 출제해줘. 단, 정답 이외의 선택지는 논설문과 어울리지 않는 선택지여야 해."
                + "예시: "
                + example_str_2
            ),
            HumanMessage(content="논설문: " + origin_example_text),
        ]
    )
    selector_responses = selector_generate_model.get_model_responses()
    selector_responses = selector_responses[0][1]
    selector_responses_filter = re.findall(r"\d+\.\s*(.*)", selector_responses)

    return selector_responses_filter


async def generate_reading_2_problem_type_16(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.1, verbose=verbose
    )

    main_title = random_word()
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
    word = clean_text(word)
    total_cost += float(
        re.search(r"Total Cost \(KRW\): ₩([\d.]+)", word_responses[0][2]).group(1)
    )

    keyword = "TOPIK_2_READING"
    problem_type = "READING_2_PROBLEM_TYPE_16"
    random_question = find_random_question(
        main_title, word, problem_type, keyword, default_model
    )

    origin_example_text = random_question["origin"]
    example_dict = random_question["examples"]

    problem_0, problem_1, problem_2 = await asyncio.gather(
        generate_problem_0(problem_generate_model, example_dict, origin_example_text),
        generate_problem_1(problem_generate_model, example_dict, origin_example_text),
        generate_problem_2(problem_generate_model, example_dict, origin_example_text),
    )

    return {
        "title": main_title,
        "word": word,
        "origin_text": origin_example_text,
        "problem_0": problem_0,
        "problem_1": problem_1,
        "problem_2": problem_2,
        "total_cost": total_cost,
    }


# 비동기 함수 실행
result = asyncio.run(generate_reading_2_problem_type_16(verbose=True))
print(result)
