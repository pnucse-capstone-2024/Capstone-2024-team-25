import glob
import os
import json
from datetime import datetime
import requests
import time

folder_path = "/home/shinbg/topik-korea-data/data/reading_2_v2/exam_data"
url = "http://waterboom.iptime.org:8080/exam"
headers = {
    "Content-Type": "application/json",
    "Authorization": "Bearer ASDLFNAFO!#!ENADSLFSADFSANVSAL!ADSLKFJSNAVLSANVSNNVASDJVASLJSAFLQNA>S:SA:WNCW:ASAFAWE",
}

# 'generate_exam_'로 시작하고 '.json'으로 끝나는 모든 파일을 검색합니다.
file_pattern = os.path.join(folder_path, "generate_exam_*.json")
# file_pattern = os.path.join(folder_path, "generate_exam_20240925_0958.json")
files = glob.glob(file_pattern)

# print(files)
if not files:
    print("JSON 파일을 찾을 수 없습니다.")
else:

    def extract_datetime_from_filename(filename):
        base_name = os.path.basename(filename)
        datetime_str = "_".join(base_name.split("_")[-2:]).split(".")[0]
        return datetime.strptime(datetime_str, "%Y%m%d_%H%M")

    files = sorted(files, key=extract_datetime_from_filename)
    i = 1
    for file in files:
        with open(file, "r", encoding="utf-8-sig") as json_file:
            json_data = json.load(json_file)
            #json_data["title"] = json_data["title"].replace("TEST", "V2TEST", 1)
            json_data["title"] = json_data["title"].replace("N", str(i), 1)
            print(json_data["title"])
            response = requests.post(url, data=json.dumps(json_data), headers=headers)
            print(response)
            if response.status_code == 201:
                print("Success")
            else:
                print("Failed")
                raise ValueError("Failed")
            i += 1
            time.sleep(1)
