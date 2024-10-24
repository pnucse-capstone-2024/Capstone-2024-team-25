import sys
if sys.version_info < (3, 4):
    raise Exception("Please use Python version 3.4 or above to use the library 'pathlib'.")
from pathlib import Path
import requests
import os
import json
import time

BASE_PATH = Path(__file__).parent

def post_exam():
    ### Post the exam data to the server
    url = "http://waterboom.iptime.org:8080/exam"

    ### Get all the json files in a particular directory
    path = (BASE_PATH / "../data/json").resolve()
    files = []
    for file in os.listdir(path):
        if file.endswith('.json'):
            file_path = f'{path}/{file}'
            with open(file_path, 'r', encoding='utf-8-sig') as f:
                data = json.load(f)
                # print(data)
                # raise ValueError('stop')
                files.append(data)

    headers = {
        'Content-Type': 'application/json'
        }
    # drop the last 2 file since they are not exam data
    files = files[:-2]
    for file in files:
        # data=json.dumps(file)
        data = file
        print(data)
        response = requests.post(url, data=json.dumps(data), headers=headers)
        print(response)
        if response.status_code == 201:
            print('Success')
        else:
            print('Failed')
            raise ValueError('Failed')
        time.sleep(1)
    
def post_image():
    ### Post the image data to the server
    json_path = (BASE_PATH / "../data/json/id_type_path.json").resolve()
    data = None
    with open(json_path, 'r', encoding='utf-8-sig') as f:
        data = json.load(f)

    # Specify the URL to which you want to send the request
    url = "http://waterboom.iptime.org:8080/file/image"

    headers = {
        'Content-Type': 'multipart/form-data'
    }

    if data is not None:
        keys = list(data.keys())
        for i in range(len(data[keys[0]])):
            form_data = {}
            form_data['objectId'] = data['id'][i]
            form_data['objectType'] = data['type'][i]
            img_path = data['path'][i]
            files = {
                'image': open(img_path, 'rb')
            }
            response = requests.post(url, data=form_data, files=files)
            ### Print the response
            print(response)
            if response.status_code == 200:
                print('Success')
            else:
                print('Failed')
                raise ValueError('Failed')
            time.sleep(10)
        
def post_audio():
    ### Post the audio data to the server
    mp3_filename_to_examname = {
        '35_1.mp3': '2014_2_TOPIK_1_LISTENING',
        '35_2.mp3': '2014_2_TOPIK_2_LISTENING',
        '36_1.mp3': '2014_1_TOPIK_1_LISTENING',
        '36_2.mp3': '2014_1_TOPIK_2_LISTENING',
        '37_1.mp3': '2014_0_TOPIK_1_LISTENING',
        '37_2.mp3': '2014_0_TOPIK_2_LISTENING',
        '41_1.mp3': '2015_0_TOPIK_1_LISTENING',
        '41_2.mp3': '2015_0_TOPIK_2_LISTENING',
        '47_1.mp3': '2016_0_TOPIK_1_LISTENING',
        '47_2.mp3': '2016_0_TOPIK_2_LISTENING',
        '52_1.mp3': '2017_0_TOPIK_1_LISTENING',
        '52_2.mp3': '2017_0_TOPIK_2_LISTENING',
        '60_1.mp3': '2018_0_TOPIK_1_LISTENING',
        '60_2.mp3': '2018_0_TOPIK_2_LISTENING',
        '64_1.mp3': '2019_0_TOPIK_1_LISTENING',
        '64_2.mp3': '2019_0_TOPIK_2_LISTENING',
        '83_1.mp3': '2022_0_TOPIK_1_LISTENING',
        '83_2.mp3': '2022_0_TOPIK_2_LISTENING',
        '91_1.mp3': '2023_0_TOPIK_1_LISTENING',
        '91_2.mp3': '2023_0_TOPIK_2_LISTENING'
        }

    json_path = (BASE_PATH / "../data/json/examname_to_uuid.json").resolve()

    examname_to_id = None
    with open(json_path, 'r', encoding='utf-8-sig') as f:
        examname_to_id = json.load(f)

    if examname_to_id is None:
        raise ValueError('examname_to_id is None')

    # Example usage
    url = "http://waterboom.iptime.org:8080/file/listen"

    headers = {
        'Content-Type': 'multipart/form-data'
    }

    ### Get all the json files in a particular directory
    path = (BASE_PATH / "../data/audio").resolve()
    files = []
    for file in os.listdir(path):
        if file.endswith('.mp3'):
            file_path = f'{path}/{file}'
            with open(file_path, 'rb') as f:
                form_data = {}
                form_data['objectId'] = examname_to_id[mp3_filename_to_examname[file]]
                form_data['objectType'] = 'listen'
                files = {'listen': f}
                response = requests.post(url, data=form_data, files=files)
                print(response)
                if response.status_code == 200 or response.status_code == 201:
                    print('Success')
                else:
                    print('Failed')
                    raise ValueError('Failed')
                time.sleep(10)

def main():
    post_exam()
    post_image()
    post_audio()
                
if __name__ == '__main__':
    main()
else:
    print('Imported to another module')