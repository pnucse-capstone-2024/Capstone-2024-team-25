import sys, os

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question
import random
import re


def generate_reading_1_problem_type_3(verbose=False, default_model="gpt-4o-mini", default_type="일치"):
    total_cost = 0
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.1, verbose=verbose
    )

    main_title = random_word(difficulty="easy")
    if verbose:
        print(main_title)
    
    problem_generate_model.request_models_responses([
        SystemMessage(content="주제어와 관련된 일상적인 단어 1개를 제시하라."),
        HumanMessage(content="주제어: "+main_title+ " 단어: "),
    ]
    )
    word_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))

    keyword = 'TOPIK_1_READING'
    problem_type = 'READING_1_PROBLEM_TYPE_3'
    random_question = find_random_question(keyword, problem_type, detail_type=1, count=2)

    example_dict = []
    for question in random_question:
        sentence = question['example']
        problem_generate_model.request_models_responses([
        SystemMessage(content="문장의 주제 단어 1개를 제시하라."),
        HumanMessage(content="문장: "+ sentence + "주제 단어: "),
            ]
        )
        total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', problem_generate_model.get_model_responses()[0][2]).group(1))
        title_responses = problem_generate_model.get_model_responses()[0][1]
        
        temp = {"주제어":title_responses, "문장": sentence}
        example_dict.append(temp)
    
    if verbose:
        print(example_dict)
        
    # example_dict = [{"주제어":"음식", "문장":"저는 요리를 잘 못합니다. 그래서 음식을 보통 사 먹습니다. 오늘 저녁은 집 근처 식당에서 불고기를 먹을 겁니다." }, {"주제어":"방문", "문장":"어제 친구가 한국에 왔습니다. 오늘 우리 집에 놀러 올 겁니다. 저는 집을 깨끗하게 청소했습니다."}]
    example_dict = random.sample(example_dict, 2)

    example_str=""
    for example in example_dict:
        example_str += "주제어: "+example["주제어"]+" 결과: "+example["문장"]+" "

    problem_generate_model.request_models_responses([
        SystemMessage(content="#예시처럼 주어지는 주제어에 대한 문장을 제시하라. 단, 문장은 3개로 구성되며 쉬운 어휘를 사용해야 한다. #답변 예시 "+example_str),
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

    example_responses = clean_text(example_responses[0][1])
    if verbose:
        print(example_responses)

    problem_generate_model.request_models_responses([
        SystemMessage(content="너는 문장을 읽고 일치하지 않는 것을 선택하는 문제를 내는 출제자이다. 주어지는 문장에 대해 선다형 25글자 이하의 선택지 4개와 정답을 제시하라. 단, 각 문장은 '~니다.' 로 끝나고 결과 형식은 '선택지: 1. 2. 3. 4. 정답: 정답 선택지 문장' 형태로 제시하라."),
        HumanMessage(content="문장: " +example_responses),
    ]
    )
    collect_selector = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', collect_selector[0][2]).group(1))
    
    if verbose:
        print(collect_selector)
    
    collect_sentences = re.findall(r'\d+\.\s*(.*?)(?=\.\s*\d+|\.\s*정답:|$)', collect_selector[0][1])
    
    collect_selector = []
    
    for sentence in collect_sentences:
        if not sentence.endswith("."):
            sentence += "."
        collect_selector.append(sentence)
    
    if default_type == "불일치":

        selector = []
        # 추출된 문장들 출력
        for sentence in collect_selector:
            selector.append(sentence)

        answer = selector.pop()
        random.shuffle(selector)
        result ={
            "example": example_responses,
            "selector": selector,
            "answer": answer,
            "eval_answer": answer,
            "explain": ""
        }

        return result
    
    else:
        problem_generate_model.request_models_responses([
            SystemMessage(content="너는 문제 출제자이다. 문장을 올바르게 서술하지 않는 선다형 25글자 이하의 선택지를 4개 제시하라. 단, 각 문장은 '~니다.' 로 끝난다."),
            HumanMessage(content="문장: " +example_responses),
        ]
        )
        wrong_responses = problem_generate_model.get_model_responses()
        
        total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', wrong_responses[0][2]).group(1))
        
        if verbose:
            print(wrong_responses)
        
        wrong_sentences = re.findall(r'\d+\.\s*(.*)', wrong_responses[0][1])
        
        wrong_selector = []
        for sentence in wrong_sentences:
            wrong_selector.append(sentence)
        
        selector = []
        # 추출된 문장들 출력
        wrong_selector = random.sample(wrong_selector, 3)
        for sentence in wrong_selector:
            selector.append(sentence)
        if verbose:
            print(collect_selector)

        temp =  collect_selector.pop()
        #collect_selector에서 temp와 같은 문장을 제거
        collect_selector = [x for x in collect_selector if x != temp]
        if verbose:
            print(collect_selector)
        answer = random.choice(collect_selector)
        selector.append(answer)

        #result strip
        for i in range(len(selector)):
            selector[i] = selector[i].strip()
        answer = answer.strip()
        
        random.shuffle(selector)
        
        result ={
            "example": example_responses,
            "selector": selector,
            "answer": answer,
            "eval_answer": answer,
            "explain": "",
            "total_cost": total_cost,
        }

        return result
    
if __name__ == "__main__":
    result = generate_reading_1_problem_type_3(default_model="gpt-3.5-turbo",verbose=True, default_type="일치")
    print(result)