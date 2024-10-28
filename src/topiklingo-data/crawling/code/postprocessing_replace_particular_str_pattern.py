### Read a json file named 'C:\Users\User\Desktop\temp\uni\final_project\topik-korea-data\crawling\data\json\2022-0-topik-1-reading.json'

import json
import re

def replace_particular_pattern(data, pattern, replacement):
    if isinstance(data, dict):
        return {key: replace_particular_pattern(value, pattern, replacement) for key, value in data.items()}
    elif isinstance(data, list):
        return [replace_particular_pattern(item, pattern, replacement) for item in data]
    elif isinstance(data, str):
        return re.sub(pattern, replacement, data)
    else:
        return data
    
json_folder = 'C:/Users/User/Desktop/temp/uni/final_project/topik-korea-data/crawling/data/json/'
### Apply the function to the every json file in the folder

import os
for json_file in os.listdir(json_folder):
    if json_file.endswith('.json'):
        json_file_path = json_folder + json_file
        with open(json_file_path, 'r', encoding='utf-8-sig') as f:
            data = json.load(f)
        if data:
            data = replace_particular_pattern(data, pattern=r"\\'", replacement="\\")
            # data = replace_particular_pattern(data, pattern=r"\\+'", replacement="<apos>")
            # data = replace_particular_pattern(data, pattern=r"\\+'", replacement="<apos>")
            # data = replace_particular_pattern(data, pattern=r"\\+'", replacement="<apos>")
            ### Save the modified json file
            with open(json_file_path, 'w', encoding='utf-8-sig') as f:
                json.dump(data, f, ensure_ascii=False, indent=4)

# with open(json_file, 'r', encoding='utf-8-sig') as f:
#     data = json.load(f)
# if data:
#     modified_json = replace_particular_pattern(data)
#     ### Save the modified json file
#     with open(json_file_modified, 'w', encoding='utf-8-sig') as f:
#         json.dump(modified_json, f, ensure_ascii=False, indent=4)

