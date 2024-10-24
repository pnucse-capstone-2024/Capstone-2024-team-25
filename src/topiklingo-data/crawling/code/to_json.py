### Raise Error if Python version is below 3.4
import sys
if sys.version_info < (3, 4):
    raise Exception("Please use Python version 3.4 or above to use the library 'pathlib'.")
import pandas as pd
import numpy as np
import ast
import os
import json
import uuid
import time
from tqdm import tqdm
import shutil
import math
import re
from pathlib import Path

BASE_PATH = Path(__file__).parent
PREVIOUS_FOLDER = (BASE_PATH / "../data/img").resolve()
NEW_FOLDER      = (BASE_PATH / "../data/img_uuid").resolve()
id_type_path = {'id': [], 'type': [], 'path': []}

def string_to_list(string):
    ### Convert a string to a list
    if pd.isna(string):
        return None
    if string == '[]':
        return None
    return ast.literal_eval(string)

def copy_image(past_name, new_name):
    ### Copy the image from a previous folder to a new folder with a new name
    source_path = f'{PREVIOUS_FOLDER}/{past_name}'
    target_path = f'{NEW_FOLDER}/{new_name}.png'
    # source_path = os.path.join(PREVIOUS_FOLDER, past_name)
    # target_path = os.path.join(NEW_FOLDER, new_name)
    shutil.copyfile(source_path, target_path)

def generate_uuid():
    time.sleep(0.01)
    return str(uuid.uuid1())

def convert_title(title):
    title = title.upper().split('-')
    title = '_'.join(title)
    return title

def get_title(tag, type):
    title = []
    title += tag.split('-')
    title += type.upper().split('-')
    title = '_'.join(title)
    title
    return title

def remove_question_number_from_question_text(text):
    pattern = r'^\d+\.'
    # found_pattern = re.findall(pattern, mystr)
    refined_text = re.sub(pattern, '', text)
    return refined_text

def update_json_data_with_unit_problem(prob_type, json_data, parent, childs):
    if 'uuid' not in json_data:
        json_data['uuid'] = generate_uuid()
    if 'title' not in json_data:
        json_data['title'] = get_title(parent['tag'][1], parent['title'])
    if 'type' not in json_data:
        json_data['type'] = convert_title(parent['title'])
    if 'year' not in json_data:
        json_data['year'] = int(parent['tag'][0])
    if 'problems' not in json_data:
        json_data['problems'] = []
    
    problem = {}
    problem['uuid'] = generate_uuid()
    problem['problem'] = parent['question']
    problem['PType'] = prob_type.upper()
    problem['EType'] = 'TEXT' if pd.isna(parent['img_name']) else 'IMAGE'
    example = {'conversation': parent['text_in_box'], 
               'answers': parent['choices'], 
               'selected': parent['selected_choice_number']
               }
    problem['example'] = str(example)
    # problem['example'] = json.dumps(example, ensure_ascii=False)

    if problem['EType'] == 'IMAGE':
        copy_image(parent['img_name'][0], problem['uuid'])
        img_id = problem['uuid']
        img_type = 'problem'
        img_path = f'{NEW_FOLDER}/{problem["uuid"]}.png'
        id_type_path['id'].append(img_id)
        id_type_path['type'].append(img_type)
        id_type_path['path'].append(img_path)

    
    questions = []
    for i, child in childs.iterrows():
        ### Convert the string to a list
        # child['choices'] = string_to_list(child['choices'])
        # child['img_name'] = string_to_list(child['img_name'])
        question = {}
        question['uuid'] = generate_uuid()
        # print(f'child[img_name]: {child["img_name"]}')
        ## if pd.isna(child['img_name']):
        # if child['img_name'] is None:
        #     question['QType'] = 'TEXT'
        # else:
        #     if len(child['img_name']) == 1:
        #         question['QType'] = 'IMAGE'
        #         copy_image(child['img_name'][0], question['uuid'])
        #     elif len(child['img_name']) > 1:
        #         question['QType'] = 'TEXT'
        #     else:
        #         raise ValueError('Invalid image name')
        if child['img_name'] is not None and len(child['img_name']) == 1:
                question['QEType'] = 'IMAGE'
                copy_image(child['img_name'][0], question['uuid'])
                img_id = question['uuid']
                img_type = 'question'
                img_path = f'{NEW_FOLDER}/{question["uuid"]}.png'
                id_type_path['id'].append(img_id)
                id_type_path['type'].append(img_type)
                id_type_path['path'].append(img_path)
        else:
            question['QEType'] = 'TEXT'
        question['questionProblem'] = "" if pd.isna(child['question']) else remove_question_number_from_question_text(child['question'])
        question['score'] = int(child['point'])
        question['rightAnswer'] = child['answer_num']
        question['example'] = child['text_in_box'] if pd.notna(child['text_in_box']) else ""
        # question['example'] = ""
        answers = []
        ### Choices
        # if pd.isna(child['img_name']):
        if child['choices'] is not None and len(child['choices']) > 1:
            for j in range(len(child['choices'])):
                answer = {}
                answer['uuid'] = generate_uuid()
                answer['AType'] = 'TEXT'
                answer['answer'] = child['choices'][j]
                answers.append(answer)
        ### Images
        elif child['img_name'] is not None and len(child['img_name']) > 1:
            for j in range(len(child['img_name'])):
                answer = {}
                answer['uuid'] = generate_uuid()
                answer['AType'] = 'IMAGE'
                answer['answer'] = None
                answers.append(answer)
                copy_image(child['img_name'][j], answer['uuid'])
                img_id = answer['uuid']
                img_type = 'answer'
                img_path = f'{NEW_FOLDER}/{answer["uuid"]}.png'
                id_type_path['id'].append(img_id)
                id_type_path['type'].append(img_type)
                id_type_path['path'].append(img_path)
        else:
            # print(child)
            raise ValueError('Both choices and images are None')
        question['answers'] = answers
        questions.append(question)
    problem['questions'] = questions
    ### Append the problem to problems
    json_data['problems'].append(problem)
    return json_data

def update_json_data_topik_1(json_data, prob_num, parent, childs):
    ### Sort into several problem types with the problem number
    if prob_num >= 1 and prob_num <= 4:
        return update_json_data_with_unit_problem(prob_type='listen_1_problem_type_1', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 5 and prob_num <= 6:
        return update_json_data_with_unit_problem(prob_type='listen_1_problem_type_2', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 7 and prob_num <= 14:
        return update_json_data_with_unit_problem(prob_type='listen_1_problem_type_3', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 15 and prob_num <= 16:
        return update_json_data_with_unit_problem(prob_type='listen_1_problem_type_4', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 17 and prob_num <= 24:
        return update_json_data_with_unit_problem(prob_type='listen_1_problem_type_5', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 25 and prob_num <= 28:
        return update_json_data_with_unit_problem(prob_type='listen_1_problem_type_6', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 29 and prob_num <= 30:
        return update_json_data_with_unit_problem(prob_type='listen_1_problem_type_7', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 31 and prob_num <= 39:
        return update_json_data_with_unit_problem(prob_type='reading_1_problem_type_1', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 40 and prob_num <= 42:
        return update_json_data_with_unit_problem(prob_type='reading_1_problem_type_2', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 43 and prob_num <= 45:
        return update_json_data_with_unit_problem(prob_type='reading_1_problem_type_3', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 46 and prob_num <= 48:
        return update_json_data_with_unit_problem(prob_type='reading_1_problem_type_4', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 49 and prob_num <= 56:
        return update_json_data_with_unit_problem(prob_type='reading_1_problem_type_5', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 57 and prob_num <= 58:
        return update_json_data_with_unit_problem(prob_type='reading_1_problem_type_6', json_data=json_data, parent=parent, childs=childs)
    elif (prob_num >= 59 and prob_num <= 62) or (prob_num >= 65 and prob_num <= 70):
        return update_json_data_with_unit_problem(prob_type='reading_1_problem_type_8', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 63 and prob_num <= 64:
        return update_json_data_with_unit_problem(prob_type='reading_1_problem_type_7', json_data=json_data, parent=parent, childs=childs)
    else :
        raise ValueError('Invalid problem number')

def update_json_data_topik_2_listening(json_data, prob_num, parent, childs):
    ### Sort into several problem types with the problem number
    if prob_num >= 1 and prob_num <= 3:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_1', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 4 and prob_num <= 8:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_2', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 9 and prob_num <= 12:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_3', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 13 and prob_num <= 16:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_4', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 17 and prob_num <= 20:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_5', json_data=json_data, parent=parent, childs=childs)
    elif (prob_num >= 21 and prob_num <= 22) or (prob_num >= 25 and prob_num <= 26) or (prob_num >= 33 and prob_num <= 34):
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_6', json_data=json_data, parent=parent, childs=childs)
    elif (prob_num >= 23 and prob_num <= 24) or (prob_num >= 35 and prob_num <= 36):
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_7', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 27 and prob_num <= 28:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_8', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 29 and prob_num <= 30:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_9', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 31 and prob_num <= 32:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_10', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 37 and prob_num <= 38:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_11', json_data=json_data, parent=parent, childs=childs)
    elif (prob_num >= 39 and prob_num <= 40) or (prob_num >= 47 and prob_num <= 48):
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_12', json_data=json_data, parent=parent, childs=childs)
    elif (prob_num >= 41 and prob_num <= 42) or (prob_num >= 45 and prob_num <= 46) or (prob_num >= 49 and prob_num <= 50):
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_13', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 43 and prob_num <= 44:
        return update_json_data_with_unit_problem(prob_type='listen_2_problem_type_14', json_data=json_data, parent=parent, childs=childs)
    else :
        raise ValueError('Invalid problem number')
    
def update_json_data_topik_2_reading(json_data, prob_num, parent, childs):
    ### Sort into several problem types with the problem number
    if prob_num >= 1 and prob_num <= 2:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_1', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 3 and prob_num <= 4:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_2', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 5 and prob_num <= 8:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_3', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 9 and prob_num <= 12:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_4', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 13 and prob_num <= 15:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_5', json_data=json_data, parent=parent, childs=childs)
    elif (prob_num >= 16 and prob_num <= 18) or (prob_num >= 28 and prob_num <= 31):
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_6', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 19 and prob_num <= 22:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_7', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 23 and prob_num <= 24:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_8', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 25 and prob_num <= 27:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_9', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 32 and prob_num <= 34:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_10', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 35 and prob_num <= 38:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_11', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 39 and prob_num <= 41:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_12', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 42 and prob_num <= 43:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_13', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 44 and prob_num <= 45:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_14', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 46 and prob_num <= 47:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_15', json_data=json_data, parent=parent, childs=childs)
    elif prob_num >= 48 and prob_num <= 50:
        return update_json_data_with_unit_problem(prob_type='reading_2_problem_type_16', json_data=json_data, parent=parent, childs=childs)
    else :
        raise ValueError('Invalid problem number')

def update_json_data(json_data, prob_num, parent, childs):
    if parent['topik_level'] == 1:
        return update_json_data_topik_1(json_data, prob_num, parent, childs)
    elif parent['topik_level'] == 2:
        if parent['listen_or_read'] == '듣기':
            return update_json_data_topik_2_listening(json_data, prob_num, parent, childs)
        elif parent['listen_or_read'] == '읽기':
            return update_json_data_topik_2_reading(json_data, prob_num, parent, childs)
        else:
            raise ValueError('Invalid topik level')
    else:
        raise ValueError('Invalid topik level')

def create_json_file(file_name, data):
    ### Get the file path
    file_path = (BASE_PATH / f"../data/json/{file_name}.json").resolve()
    if os.path.exists(file_path):
        # If the file exists, load the existing data
        with open(file_path, 'r', encoding='utf-8-sig') as file:
            existing_data = json.load(file)
        
        # Update the existing data with the new data
        existing_data.update(data)
        data = existing_data
    
    # Write the data to the JSON file
    with open(file_path, 'w', encoding='utf-8-sig') as file:
        json.dump(data, file, ensure_ascii=False, indent=4)

def replace_nan_with_empty_string(data):
    if isinstance(data, dict):
        return {key: replace_nan_with_empty_string(value) for key, value in data.items()}
    elif isinstance(data, list):
        return [replace_nan_with_empty_string(item) for item in data]
    elif isinstance(data, float) and math.isnan(data):
        return ''
    else:
        return data

### Define the main function
def main():
    ### Get the csv file path
    file_path = (BASE_PATH / "../data/csv/test_data.csv").resolve()
    ### Load the data
    df = pd.read_csv(file_path)

    ### Change null values to ''
    # df = df.fillna('')

    ### Convert the string to a list
    df['tag'] = df['tag'].apply(string_to_list)
    df['choices'] = df['choices'].apply(string_to_list)
    df['img_name'] = df['img_name'].apply(string_to_list)
    ### Get a list of the second element of each list
    exam_codes = df['tag'].apply(lambda x: x[1] if x else None)
    df['exam_code'] = exam_codes
    unique_exam_codes = np.unique(exam_codes)
    unique_tities = df['title'].unique()
    examname_to_uuid = {}
    i=0
    for exam_code in unique_exam_codes:
        for title in unique_tities:
            ### Save each exam data to a json file
            print(f'Iteration: {i}')
            i+=1
            print(f'Exam_code: {exam_code}, Title: {title}')
            df_each_exam = df[(df['exam_code'] == exam_code) & (df['title'] == title)]
            df_parent = df_each_exam[df_each_exam['parent_or_child'] == 'Parent']
            # print(f'Number of parent rows: {len(df_parent)}')
            json_data = {}
            for row in tqdm(df_parent.itertuples()):
                prob_num = None
                
                # print('Parent')
                child_ids = row._asdict()["child_ids"]
                ### convert to a list of integers from str
                child_ids = ast.literal_eval(child_ids)
                # print(f'child_ids: {child_ids}')
                child_id = int(child_ids[0])
                # print(f'child_idx: {child_id}')
                child_index = df[df['unique_id'] == child_id].index
                child_indices = df[df['unique_id'].isin(child_ids)].index
                # print(f'child_idx: {child_index}')
                prob_num = df.iloc[child_index]['prob_num'].values[0]
                ### prepare data(parent, childs)
                parent, childs = row._asdict(), df.iloc[child_indices]
                ### Convert the string to a list
                # tag = string_to_list(parent['tag'])
                ### Update the json data
                json_data = update_json_data(json_data, prob_num, parent, childs)
            # Replace NaN with empty string
            json_data = replace_nan_with_empty_string(json_data)
            file_name = f'{exam_code}-{title}'
            create_json_file(file_name, json_data)
            examname = file_name.upper().replace('-', '_')
            examname_to_uuid[examname] = json_data['uuid']
    
    ### Save id_type_path to a json file
    create_json_file('id_type_path', id_type_path)
    create_json_file('examname_to_uuid', examname_to_uuid)

if __name__ == '__main__':
    main()
else:
    print('Imported to another module')