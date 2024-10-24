import glob
import os
import json
from datetime import datetime
import requests
import time

def post_exam():
    folder_path = '/home/shinbg/topik-korea-data/data/test/exam_data' 
    url = "http://waterboom.iptime.org:8080/exam"
    headers = {'Content-Type': 'application/json', 'Authorization': 'Bearer ASDLFNAFO!#!ENADSLFSADFSANVSAL!ADSLKFJSNAVLSANVSNNVASDJVASLJSAFLQNA>S:SA:WNCW:ASAFAWE'}

    # 'generate_exam_'로 시작하고 '.json'으로 끝나는 모든 파일을 검색합니다.
    file_pattern = os.path.join(folder_path, "pnt_*.json")
    files = glob.glob(file_pattern)

    # print(files)
    if not files:
        print("JSON 파일을 찾을 수 없습니다.")
    else:
        for file in files:
            with open(file, "r", encoding='utf-8-sig') as json_file:
                json_data = json.load(json_file)
                print(json_data['title'])
                response = requests.post(url, data=json.dumps(json_data), headers=headers)
                print(response)
                if response.status_code == 201:
                    print('Success')
                else:
                    print('Failed')
                    raise ValueError('Failed')
                time.sleep(1)
    return True

def post_image():        
    folder_path = '/home/shinbg/topik-korea-data/data/test/exam_data' 
    url = "http://waterboom.iptime.org:8080/file/image"
    # headers = {'Content-Type': 'multipart/form-data', 'Authorization': 'Bearer ASDLFNAFO!#!ENADSLFSADFSANVSAL!ADSLKFJSNAVLSANVSNNVASDJVASLJSAFLQNA>S:SA:WNCW:ASAFAWE'}

    # 'generate_exam_'로 시작하고 '.json'으로 끝나는 모든 파일을 검색합니다.
    file_pattern = os.path.join(folder_path, "*.png")
    files = glob.glob(file_pattern)
    print(len(files))
    for file in files:
        file_name = file.split('/')[-1]
        file_type = file_name.split('_')[0]
        file_id = file_name.split('_')[1].split('.')[0]
        form_data = {}
        form_data['objectId'] = file_id
        form_data['objectType'] = file_type
        image = {'image': open(file, 'rb')}
        print(form_data)
        response = requests.post(url, data=form_data, files=image)
        print(response)
        if response.status_code == 200:
                print('Success')
        else:
            print('Failed')
            raise ValueError('Failed')
        time.sleep(5)

def post_mp3():
    folder_path = '/home/shinbg/topik-korea-data/data/test/exam_data' 
    url = "http://waterboom.iptime.org:8080/file/listen"

    file_pattern = os.path.join(folder_path, "*.mp3")
    files = glob.glob(file_pattern)
    for file in files:
        file_type = 'listen'
        file_id = file.split('/')[-1].split('.')[0]
        form_data = {}
        form_data['objectId'] = file_id
        form_data['objectType'] = file_type
        print(form_data)
        mp3_file = {'listen': open(file, 'rb')}
        response = requests.post(url, data=form_data, files=mp3_file)
        if response.status_code == 200 or response.status_code == 201:
                    print('Success')
        else:
            print('Failed')
            raise ValueError('Failed')
        time.sleep(10)
if __name__ == "__main__":
    #post_exam()
    post_image()
    #post_mp3()