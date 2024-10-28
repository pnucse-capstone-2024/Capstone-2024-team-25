import re
import random
import itertools

def origin_example_parser(text, answer_text):
    #order = answer_text.split("-")
    order = re.findall(r'[가-힣]', answer_text)
    #order = [re.search(r"（(.*)）", x).group(1) for x in order]
    # 문장을 나누기 위한 딕셔너리
    sentences_dict = {}
    # 주어진 문자열을 분할하여 딕셔너리에 저장
    try:
        split_text = text.split('（')
        for part in split_text:
            if part:
                key = part[0]
                value = part[1:].strip()
                sentences_dict[key] = value

        # 새로운 순서에 맞게 문장 재배치
        reordered_sentences = [sentences_dict[key][1:] for key in order]
        # 결과를 하나의 문자열로 합침
        reordered_text = ' '.join(reordered_sentences)
        return reordered_text
    except KeyError:
        split_text = text.split('(')
        for part in split_text:
            if part:
                key = part[0]
                value = part[1:].strip()
                sentences_dict[key] = value

        # 새로운 순서에 맞게 문장 재배치
        reordered_sentences = [sentences_dict[key][1:] for key in order]
        # 결과를 하나의 문자열로 합침
        reordered_text = ' '.join(reordered_sentences)
        return reordered_text
    
def order_permutation():
    order = ['(가)','(나)','(다)','(라)']
    # Generate all permutations of the order list
    permutations = list(itertools.permutations(order))

    # Convert tuples to lists for better readability
    permutations = [list(permutation) for permutation in permutations]
    random.shuffle(permutations)
    selector = random.sample(permutations, 4)
    answer = random.choice(selector)
    return selector, answer

def split_text_into_sentences(text):
    # Use regular expression to split the text by sentence-ending punctuation
    sentences = re.split(r'(?<=[.!?])\s*', text)
    # Remove any empty strings that might result from splitting
    sentences = [sentence for sentence in sentences if sentence]
    return sentences

def randomly_group_sentences_preserve_order(sentences, num_groups=4):
    # Create empty groups
    groups = [[] for _ in range(num_groups)]
    
    # Calculate the number of sentences per group
    num_sentences = len(sentences)
    group_sizes = [num_sentences // num_groups] * num_groups
    
    # Distribute remaining sentences if the division is not even
    for i in range(num_sentences % num_groups):
        group_sizes[i] += 1

    # Randomize the order of the groups
    random.shuffle(group_sizes)

    # Fill the groups based on the calculated sizes
    current_idx = 0
    for size in group_sizes:
        for _ in range(size):
            groups[group_sizes.index(size)].append(sentences[current_idx])
            current_idx += 1
        group_sizes[group_sizes.index(size)] = -1  # Mark this group size as used

    # Join sentences in each group to form complete segments
    segments = [' '.join(group) for group in groups]
    
    return segments

import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from problem_generator import Problem_Generate_Model
from langchain.schema import HumanMessage, SystemMessage
from util import random_word
from util import find_random_question


def generate_reading_1_problem_type_6(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    models = [default_model]
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.1, verbose=verbose
    )

    main_title = random_word(difficulty="easy")
    if verbose:
        print(main_title)

    problem_generate_model.request_models_responses(
        [
            SystemMessage(content="주제어와 관련된 쉬운 단어 1개를 제시하라."),
            HumanMessage(content="주제어: " + main_title + " 단어: "),
        ]
    )
    word_responses = problem_generate_model.get_model_responses()
    
    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', word_responses[0][2]).group(1))
    
    keyword = 'TOPIK_1_READING'
    problem_type = 'READING_1_PROBLEM_TYPE_6'
    random_question = find_random_question(keyword, problem_type, detail_type=1, count=2)

    example_dict = []
    for question in random_question:
        sentence = origin_example_parser(question['example'], question['answer'])
        problem_generate_model.request_models_responses([
        SystemMessage(content="문장의 주제 단어 1개를 제시하라."),
        HumanMessage(content="문장: "+ sentence + "주제 단어: "),
            ]
        )
        total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', problem_generate_model.get_model_responses()[0][2]).group(1))
        title_responses = problem_generate_model.get_model_responses()[0][1]
        temp = {"주제어":title_responses, "문장": sentence, "선택지":question['selector']}
        example_dict.append(temp)

    example_dict = random.sample(example_dict, 2)
        
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
                content="#예시처럼 주어지는 주제어에 대한 문장을 제시하라. 문장은 4개로 이루어지며 이어지는 순서가 명확해야 한다. #답변 예시 "
                + example_str
            ),
            HumanMessage(content="주제어: " + word_responses[0][1] + " 문장: "),
        ]
    )

    example_responses = problem_generate_model.get_model_responses()

    total_cost += float(re.search(r'Total Cost \(KRW\): ₩([\d.]+)', example_responses[0][2]).group(1))

    example_responses = example_responses[0][1]
    text = example_responses

    # Split the text into sentences
    sentences = split_text_into_sentences(text)

    # Randomly group sentences while preserving their order
    segments = randomly_group_sentences_preserve_order(sentences)

    example_list = []
    selector, answer = order_permutation()
    for i in range(4):
        example_list.append(f"{answer[i]} {segments[i]} ")
    random.shuffle(example_list)
    example_responses = ''.join(example_list)
    
    def reorder_sentences(text):
        # Split the text by sentences
        sentences = text.split('. ')
        
        # Create a dictionary to hold sentences with their labels
        sentence_dict = {}
        
        for sentence in sentences:
            if sentence:
                label = sentence[:3]  # Extract the label (가, 나, 다, 라)
                sentence_dict[label] = sentence
                
        # Define the correct order
        order = ['(가)', '(나)', '(다)', '(라)']
        
        # Reorder sentences based on the defined order
        reordered_sentences = [sentence_dict[label] for label in order if label in sentence_dict]
        
        # Join the reordered sentences into a single string
        result = '. '.join(reordered_sentences) + '.'
        
        return result

    example_responses = reorder_sentences(example_responses)
    
    selector = [' - '.join(order) for order in selector]
    answer = ' - '.join(answer)
    result = {
        "example": example_responses,
        "selector": selector,
        "answer": answer,
        "eval_answer": answer,
        "eval_explain":text,
        "total_cost": total_cost,
    }
    return result

if __name__ == "__main__":
    result = generate_reading_1_problem_type_6(default_model="gpt-4o-mini",verbose=True)
    print(result)