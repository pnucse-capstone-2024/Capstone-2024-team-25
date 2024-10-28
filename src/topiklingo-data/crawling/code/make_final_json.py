import os
import json
import math

def replace_nan_with_empty_string(data):
    if isinstance(data, dict):
        return {key: replace_nan_with_empty_string(value) for key, value in data.items()}
    elif isinstance(data, list):
        return [replace_nan_with_empty_string(item) for item in data]
    elif isinstance(data, float) and math.isnan(data):
        return ''
    else:
        return data

# Specify the folder path where the JSON files are located
folder_path = 'C:/Users/User/Desktop/temp/uni/final_project/topik-korea-data/crawling/data/json'

### Create an Empty Final JSON file
final_json = {}

# Initialize an empty list to store the combined data
combined_data = []

# Iterate over each file in the folder
for filename in os.listdir(folder_path):
    if filename.endswith('.json'):
        file_path = os.path.join(folder_path, filename)
        with open(file_path, 'r', encoding='utf-8-sig') as file:
            # Load the JSON data from each file
            data = json.load(file)
            # Append the data to the combined_data list
            combined_data.append(data)


final_json['Tests'] = combined_data

# Replace NaN with empty string
final_json = replace_nan_with_empty_string(final_json)

# Write the combined data to a new JSON file
output_file = 'C:/Users/User/Desktop/temp/uni/final_project/topik-korea-data/crawling/data/json/final_json.json'
with open(output_file, 'w', encoding='utf-8-sig') as file:
    json.dump(final_json, file, ensure_ascii=False, indent=4)