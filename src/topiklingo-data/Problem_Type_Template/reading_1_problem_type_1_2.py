import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re
from konlpy.tag import Okt

def extract_masking_data(text, masking_target):
    okt = Okt()
    pos_data = okt.pos(text)
    nouns = [x[0] for x in pos_data if x[1] == 'Noun']
    verbs = [x[0] for x in pos_data if x[1] == 'Verb']
    adjectives = [x[0] for x in pos_data if x[1] == 'Adjective']
    josas = [x[0] for x in pos_data if x[1] == 'Josa']

    masking_data = []
    if masking_target == "명사":
        for noun in nouns:
            if len(noun) > 1:
                masking_data.append(noun)
    elif masking_target == "동사":
        for verb in verbs:
            masking_data.append(verb)
    elif masking_target == "형용사":
        for adjective in adjectives:
            masking_data.append(adjective)
    elif masking_target == "조사":
        for josa in josas:
            masking_data.append(josa)
    random.shuffle(masking_data)
    try:
        masking_data = random.choice(masking_data)
    except IndexError:
        #check length verbs, adjectives, josas, nouns
        # choice random list not 0 length 
        if len(verbs) > 0:
            masking_data = random.choice(verbs)
        elif len(adjectives) > 0:
            masking_data = random.choice(adjectives)
        elif len(josas) > 0:
            masking_data = random.choice(josas)
        elif len(nouns) > 0:
            for noun in nouns:
                if len(noun) > 1:
                    masking_data.append(noun)
            masking_data = random.choice(nouns)
        else:
            for noun in nouns:
                if len(noun) > 1:
                    masking_data.append(noun)
            masking_data = random.choice(nouns)
    return masking_data

def generate_reading_1_problem_type_1_2(target="명사",verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.1, verbose=verbose
    )

    main_title = random_word(difficulty="Easy")
    if verbose:
        print(main_title)

    problem_generate_model.request_models_responses([
        SystemMessage(content="주제어와 관련된 쉬운 단어 1개를 제시하라."),
        HumanMessage(content="주제어: "+main_title+ " 단어: "),
    ]
    )
    word_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))

    keyword = 'TOPIK_1_READING'
    problem_type = 'READING_1_PROBLEM_TYPE_1'
    random_question = find_random_question(keyword, problem_type, detail_type=1, count=2)

    example_dict = []
    for question in random_question:
        temp = {"문장": question['example'], "주제어": question["answer"]}
        example_dict.append(temp)

    # example_dict = [{"주제어":"계절", "문장":"여름에는 덥습니다. 겨울에는 추웁니다." }, {"주제어":"과일", "문장":"사과가 맛있습니다. 바나나도 맛있습니다."}]
    example_dict = random.sample(example_dict, 2)

    example_str=""
    for example in example_dict:
        example_str += "주제어: "+example["주제어"]+" 문장: "+example["문장"]+" "

    problem_generate_model.request_models_responses([
        SystemMessage(content="#명사,조사,동사,형용사로 이루어진 쉬운 문장 2개를 제시하라. #답변 예시 "+example_str+ "단, 결과는 형식없이 문장만 대답하라."),
        HumanMessage(content="주제어: " +word_responses[0][1]+ " 문장: "),
    ]
    )
    example_responses = problem_generate_model.get_model_responses()
    
    def clean_text(text):
        pattern = r':\s*(.*)$'
        match = re.search(pattern, text)
        if match:
            return match.group(1).strip()
        text = re.sub(r'\d+\.', '', text)  # 숫자 다음에 오는 점 (예: "1.", "2.") 제거
        text = re.sub(r'\n', ' ', text)  # 개행 문자를 공백으로 대체
        return text.strip()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', example_responses[0][2]).group(1))

    text = clean_text(example_responses[0][1])
    target = target

    masking_data = extract_masking_data(text, target)
    masking_example = text.replace(masking_data, "(    )", 1)
    
    if target == "명사":   
        problem_generate_model.request_models_responses([
            SystemMessage(content="주어지는 단어와 같은 수준의 상의어지만, 다른 주제의 쉬운 두 글자 단어 3개를 제시하라. #답변 형식: 1. "+target+"\n 2. "+target+"\n 3. "+target),
            HumanMessage(content="0. " + masking_data + "\n"),
        ]
        )
        selector_responses = problem_generate_model.get_model_responses()
    elif target == "동사":
        problem_generate_model.request_models_responses([
            SystemMessage(content="주어지는 동사의 끝과 일치하는 다른 동사 3개를 제시하라. 단, 각 동사의 의미는 서로 완전 달라야 한다. #답변 형식: 1. "+target+"\n 2. "+target+"\n 3. "+target),
            HumanMessage(content="0. " + masking_data + "\n"),
        ]
        )
        selector_responses = problem_generate_model.get_model_responses()
    elif target == "형용사":
        problem_generate_model.request_models_responses([
            SystemMessage(content="주어지는 형용사의 끝과 일치하는 다른 형용사 3개를 제시하라. 단, 각 형용사의 의미는 서로 완전 달라야 한다. #답변 형식: 1. "+target+"\n 2. "+target+"\n 3. "+target),
            HumanMessage(content="0. " + masking_data + "\n"),
        ]
        )
        selector_responses = problem_generate_model.get_model_responses()
    elif target == "조사":
        problem_generate_model.request_models_responses([
            SystemMessage(content="주어지는 조사와 다른 종류의 조사 3개를 제시하라. 단, 각 조사는 서로 완전 달라야 한다. #답변 형식: 1. "+target+"\n 2. "+target+"\n 3. "+target),
            HumanMessage(content="0. " + masking_data + "\n"),
        ]
        )
        selector_responses = problem_generate_model.get_model_responses()
    else:
        problem_generate_model.request_models_responses([
            SystemMessage(content="각각 다르지만 비슷한 한국어 "+target+" 3개를 제시하라. #답변 형식: 1. "+target+"\n 2. "+target+"\n 3. "+target),
            HumanMessage(content="0. " + masking_data + "\n"),
        ]
        )
        selector_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', selector_responses[0][2]).group(1))
    
    if verbose:
        print(selector_responses[0][1])
    answer = masking_data
    example_responses = clean_text(example_responses[0][1])
    selector_responses = selector_responses[0][1]
    selector_responses = re.sub(r"\d+\.\s+", "", selector_responses)
    selector_responses = selector_responses.split("\n")
    if len(selector_responses) > 3:
        selector_responses = selector_responses[:3]
    selector_responses.append(answer)
    random.shuffle(selector_responses)
    
    if len(selector_responses) != 4:
        raise ValueError("selector_responses length is not 4")
    for selector in selector_responses:
        if selector == "":
            raise ValueError("selector_responses has empty string")

    for i in range(len(selector_responses)):
        selector_responses[i] = selector_responses[i].strip()

    result = {
            "example": masking_example,
            "selector": selector_responses,
            "answer": answer,
            "eval_answer": answer,
            "eval_explain": text,
            "total_cost": total_cost,
        }

    return result