from google.cloud import vision
from google.oauth2 import service_account
import json

import requests.structures


class Image_Loader:
    def __init__(self, image_path):
        self.image_path = image_path
        self.image = self.load_image(image_path)
        self.__credentials = service_account.Credentials.from_service_account_file(
            "API_KEY/google_vision_api_key.json"
        )
        self.image_text = self.load_image_text()

    @staticmethod
    def load_image(image_path):
        if image_path.startswith("http"):
            return vision.Image(source=vision.ImageSource(image_uri=image_path))
        elif image_path.startswith("/s3-bucket/"):
            image_path = image_path.replace(
                "/s3-bucket/", "https://topikkorea.s3.amazonaws.com/"
            )
            return vision.Image(source=vision.ImageSource(image_uri=image_path))
        with open(image_path, "rb") as image_file:
            content = image_file.read()
        return vision.Image(content=content)

    def request_ocr(self, image):
        client = vision.ImageAnnotatorClient(credentials=self.__credentials)
        response = client.text_detection(image=image)
        if response.error.message:
            raise Exception(
                "{}\nFor more info on error messages, check: "
                "https://cloud.google.com/apis/design/errors".format(
                    response.error.message
                )
            )
        return response

    @staticmethod
    def ocr_reponse_to_json(response):
        response_json = {"text_all": "", "bounding_poly_all": [], "response": []}
        texts = response.text_annotations
        idx = 0
        for text in texts:
            if idx == 0:
                response_json["text_all"] = text.description
                response_json["bounding_poly_all"] = [
                    {"x": vertex.x, "y": vertex.y}
                    for vertex in text.bounding_poly.vertices
                ]
                idx += 1
            else:
                vertices = [
                    {"x": vertex.x, "y": vertex.y}
                    for vertex in text.bounding_poly.vertices
                ]
                response_json["response"].append(
                    {"text": text.description, "bounding_poly": vertices}
                )
                idx += 1
        return response_json

    def save_response_json(self, response_json, output_path):
        file_name = self.image_path.split("/")[-1].split(".")[0] + ".json"
        with open(output_path + file_name, "w", encoding="utf-8") as f:
            json.dump(response_json, f, ensure_ascii=False, indent="\t")

    def load_image_text(self):
        try:
            with open(
                "data/image_ocr_data/"
                + self.image_path.split("/")[-1].split(".")[0]
                + ".json",
                "r",
                encoding="utf-8",
            ) as f:
                response_json = json.load(f)
        except FileNotFoundError:
            response = self.request_ocr(self.image)
            response_json = self.ocr_reponse_to_json(response)
            self.save_response_json(response_json, "data/image_ocr_data/")
        return response_json["text_all"]

    def get_image_text(self):
        return self.image_text

    def __str__(self):
        return f"image_path: {self.image_path}, image_text: {self.image_text}"


import random
import json
import requests
import ast
import re


def anthropic_callback(response):
    model = response.response_metadata["model"]
    token_len = response.response_metadata["usage"]
    tokens_used = token_len["input_tokens"] + token_len["output_tokens"]
    prompt_tokens = token_len["input_tokens"]
    completion_tokens = token_len["output_tokens"]
    successful_requests = 1
    if model == "claude-3-opus-20240229":
        input_cost = 15
        output_cost = 75
        total_cost = (input_cost * tokens_used / 1000000) + (
            output_cost * tokens_used / 1000000
        )
    elif model == "claude-3-sonnet-20240229":
        input_cost = 3
        output_cost = 15
        total_cost = (input_cost * tokens_used / 1000000) + (
            output_cost * tokens_used / 1000000
        )
    else:
        input_cost = 0.25
        output_cost = 1.25
        total_cost = (input_cost * tokens_used / 1000000) + (
            output_cost * tokens_used / 1000000
        )
    total_cost = round(total_cost, 5)
    krw_cost = round(total_cost * 1350, 5)
    return (
        "Anthropic Tokens Used: "
        + str(tokens_used)
        + "\n    Prompt Tokens: "
        + str(prompt_tokens)
        + "\n    Completion Tokens: "
        + str(completion_tokens)
        + "\nSuccessful Requests: "
        + str(successful_requests)
        + "\nTotal Cost (USD): $"
        + str(total_cost)
        + "\nTotal Cost (KRW): ₩"
        + str(krw_cost)
    )


def random_word(difficulty="None"):
    word = ""
    if difficulty == "Easy":
        with open("Voca/easy_words_list.txt", "r", encoding="utf-8") as f:
            words = f.readlines()
            word = random.choice(words).strip()
    elif difficulty == "Normal":
        with open("Voca/normal_words_list.txt", "r", encoding="utf-8") as f:
            words = f.readlines()
            word = random.choice(words).strip()
    else:
        with open("Voca/words_list.txt", "r", encoding="utf-8") as f:
            words = f.readlines()
            word = random.choice(words).strip()
    return word


def openai_callback(response):
    model = response.response_metadata["model_name"]
    token_len = response.response_metadata["token_usage"]
    tokens_used = token_len["prompt_tokens"] + token_len["completion_tokens"]
    prompt_tokens = token_len["prompt_tokens"]
    completion_tokens = token_len["completion_tokens"]
    successful_requests = 1
    if model == "gpt-4-turbo":
        input_cost = 10
        output_cost = 30
        total_cost = (input_cost * tokens_used / 1000000) + (
            output_cost * tokens_used / 1000000
        )
    elif model == "gpt-4o":
        input_cost = 5
        output_cost = 15
        total_cost = (input_cost * tokens_used / 1000000) + (
            output_cost * tokens_used / 1000000
        )
    elif model == "gpt-4o-mini":
        input_cost = 0.15
        output_cost = 0.6
        total_cost = (input_cost * tokens_used / 1000000) + (
            output_cost * tokens_used / 1000000
        )
    else:
        input_cost = 0.5
        output_cost = 1.5
        total_cost = (input_cost * tokens_used / 1000000) + (
            output_cost * tokens_used / 1000000
        )
    total_cost = round(total_cost, 5)
    krw_cost = round(total_cost * 1350, 5)
    return (
        "OpenAI Tokens Used: "
        + str(tokens_used)
        + "\n    Prompt Tokens: "
        + str(prompt_tokens)
        + "\n    Completion Tokens: "
        + str(completion_tokens)
        + "\nSuccessful Requests: "
        + str(successful_requests)
        + "\nTotal Cost (USD): $"
        + str(total_cost)
        + "\nTotal Cost (KRW): ₩"
        + str(krw_cost)
    )


def find_random_problem(exam_type, use_generated_data=True, header=None):
    if exam_type in "TOPIK_1_READING":
        requestData = requests.get(
            "http://waterboom.iptime.org:8080/exam/topik1-reading"
        )
        requestData = requestData.json()
        if use_generated_data:
            requestList = []
            for item in requestData:
                requestList.append(item)
        else:
            requestList = []
            for item in requestData:
                if "Generated" not in item["title"]:
                    requestList.append(item)
    elif exam_type in "TOPIK_2_READING":
        requestData = requests.get(
            "http://waterboom.iptime.org:8080/exam/topik2-reading"
        )
        requestData = requestData.json()
        if use_generated_data:
            requestList = []
            for item in requestData:
                requestList.append(item)
        else:
            requestList = []
            for item in requestData:
                if "Generated" not in item["title"]:
                    requestList.append(item)
    else:
        requestData = requests.get("http://waterboom.iptime.org:8080/exam")
        requestData = requestData.json()
        requestList = []
        for item in requestData:
            if exam_type in item["title"]:
                requestList.append(item)
    choose_data = random.choice(requestList)
    json_data = requests.get(
        "http://waterboom.iptime.org:8080/exam/" + choose_data["id"], headers=header
    )
    # json_data = requests.get(
    #     "http://waterboom.iptime.org:8080/exam/"
    #     + "779f2662-23ef-11ef-a243-e5a43cd400fd",
    #     headers=header,
    # )
    json_data = json_data.json()

    answer_data = requests.get(
        "http://waterboom.iptime.org:8080/exam-answer/" + choose_data["id"],
        headers=header,
    )
    # answer_data = requests.get(
    #     "http://waterboom.iptime.org:8080/exam-answer/"
    #     + "779f2662-23ef-11ef-a243-e5a43cd400fd",
    #     headers=header,
    # )

    answer_data = answer_data.json()
    return json_data, answer_data


def answer_finder(question_id, answer_data):
    for item in answer_data:
        if item["questionId"] == question_id:
            return item["rightAnswer"]


def conversation_parser(data):
    match = re.search(r"'conversation': \"\['(.*?)\']\"", data)
    if match:
        conversation = match.group(1)
        return [conversation]
    else:
        match = re.search(r"\"conversation\":\s*\[\s*\"(.*?)\"\s*\]", data)
        if match:
            conversation = match.group(1)
            return [conversation]
        return "No match found."


def random_question_extractor(problem_type, detail_type, problem_data, count):
    if problem_type == "reading_1_problem_type_1":
        if detail_type == 1:
            problem_data = problem_data[0]
        if detail_type == 2:
            problem_data = problem_data[1]
        random_question = random.sample(problem_data["questions"], count)
        return random_question
    if problem_type == "reading_1_problem_type_2":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_1_problem_type_3":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_1_problem_type_4":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_1_problem_type_5":
        problem_data = random.sample(problem_data, 1)[0]
        example = conversation_parser(problem_data["example"])
        random_question = problem_data["questions"]
        for item in random_question:
            item["example"] = example
        return random_question
    if problem_type == "reading_1_problem_type_6":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_1_problem_type_7":
        problem_data = random.sample(problem_data, 1)[0]
        example = problem_data["example"]
        random_question = problem_data["questions"]
        for item in random_question:
            item["example"] = example
        return random_question
    if problem_type == "reading_1_problem_type_8":
        problem_data = random.sample(problem_data[1:], 1)[0]
        example = conversation_parser(problem_data["example"])
        random_question = problem_data["questions"]
        for item in random_question:
            item["example"] = example
        return random_question
    if problem_type == "reading_2_problem_type_1":
        random_question = random.sample(problem_data[0]["questions"], count)
        for item in random_question:
            item["example"] = item["question"]
        return random_question
    if problem_type == "reading_2_problem_type_2":
        random_question = random.sample(problem_data[0]["questions"], count)
        for item in random_question:
            item["example"] = item["question"]
        return random_question
    if problem_type == "reading_2_problem_type_3":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_2_problem_type_4":
        if problem_data[0]["problemId"] == "780bb2df-23ef-11ef-8b81-e5a43cd400fd":
            if detail_type == 1:
                problem_data = [
                    problem_data[0]["questions"][0],
                    problem_data[0]["questions"][3],
                ]
            elif detail_type == 2:
                problem_data = [
                    problem_data[0]["questions"][1],
                    problem_data[0]["questions"][2],
                ]
            random_question = random.sample(problem_data, count)
            return random_question
        try:
            temp = problem_data[1]
            if detail_type == 1:
                problem_data = problem_data[0]["questions"]
            elif detail_type == 2:
                problem_data = problem_data[1]["questions"]
        except IndexError:
            if detail_type == 1:
                problem_data = problem_data[0]["questions"][:-2]
            elif detail_type == 2:
                problem_data = problem_data[0]["questions"][-2:]
        random_question = random.sample(problem_data, count)
        return random_question
    if problem_type == "reading_2_problem_type_5":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_2_problem_type_6":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_2_problem_type_7":
        problem_data = random.sample(problem_data, 1)[0]
        example = conversation_parser(problem_data["example"])
        random_question = problem_data["questions"]
        for item in random_question:
            item["example"] = example
        return random_question
    if problem_type == "reading_2_problem_type_8":
        problem_data = random.sample(problem_data, 1)[0]
        example = conversation_parser(problem_data["example"])
        random_question = problem_data["questions"]
        for item in random_question:
            item["example"] = example
        return random_question
    if problem_type == "reading_2_problem_type_9":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_2_problem_type_10":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_2_problem_type_11":
        random_question = random.sample(problem_data[0]["questions"], count)
        return random_question
    if problem_type == "reading_2_problem_type_12":

        def concatenate_sentences(input_text):
            data = ast.literal_eval(input_text)
            text_list = [item for sublist in data for item in sublist]
            text = " ".join(text_list)
            if "<보기>" in text:
                return input_text
            # 배열 앞의 문장
            front_sentence = data[0][0]
            # 배열 뒤의 문장들
            back_sentences = [sentence[0] for sentence in data[1:]]
            # <보기>로 시작하여 배열 뒤의 문장들을 이어붙임.
            result = f"[['{' '.join(back_sentences)} <보기> {front_sentence}']]"
            return result

        random_question = random.sample(problem_data[0]["questions"], count)
        for item in random_question:
            item["example"] = concatenate_sentences(item["example"])
        return random_question
    if problem_type == "reading_2_problem_type_13":
        problem_data = random.sample(problem_data, 1)[0]
        example = conversation_parser(problem_data["example"])
        random_question = problem_data["questions"]
        for item in random_question:
            item["example"] = example
        return random_question
    if problem_type == "reading_2_problem_type_14":
        problem_data = random.sample(problem_data, 1)[0]
        example = conversation_parser(problem_data["example"])
        random_question = problem_data["questions"]
        for item in random_question:
            item["example"] = example
        return random_question
    if problem_type == "reading_2_problem_type_15":
        problem_data = random.sample(problem_data, 1)[0]
        example = conversation_parser(problem_data["example"])
        random_question = problem_data["questions"]
        for item in random_question:
            item["example"] = example
        return random_question
    if problem_type == "reading_2_problem_type_16":
        problem_data = random.sample(problem_data, 1)[0]
        example = conversation_parser(problem_data["example"])
        random_question = problem_data["questions"]
        for item in random_question:
            item["example"] = example
        return random_question


import json
import os
import sys


def find_random_question(
    exam_type,
    problem_type,
    detail_type,
    count,
    use_generated_data=True,
    api_key_path="API_KEY/topik_api_key.json",
):
    api_key_file = api_key_path
    if os.path.exists(api_key_file):
        with open(api_key_file) as f:
            api_key = json.load(f)
    else:
        api_key = {"TOPIK_API_KEY": "your_topik_backend_api_key_here"}
        with open(api_key_file, "w") as f:
            json.dump(api_key, f)
        print("Please create your API keys in the API_KEY/topik_api_key.json file")
        sys.exit()
    if api_key["TOPIK_API_KEY"] == "your_openai_api_key_here":
        print("Please update your API keys in the API_KEY/topik_api_key.json file")
        sys.exit()
    else:
        auth_header = api_key["TOPIK_API_KEY"]
        header = requests.structures.CaseInsensitiveDict()
        header["Authorization"] = "Bearer "
        header["Authorization"] += auth_header
        random_problem, random_answer = find_random_problem(
            exam_type, use_generated_data=use_generated_data, header=header
        )
        question = random_question_extractor(
            problem_type.lower(),
            detail_type,
            random_problem["config"][problem_type.lower()],
            count,
        )
        result = []
        for item in question:
            try:
                example = item["example"]
                example = ast.literal_eval(example)
                example = [item for sublist in example for item in sublist]
                example = " ".join(example)
            except:
                example = item["example"]
            selector = []
            for answer in item["answers"]:
                selector.append(answer["answer"])
            answer = selector[answer_finder(item["questionId"], random_answer) - 1]
            temp = {"example": example, "selector": selector, "answer": answer}
            result.append(temp)
        return result


if __name__ == "__main__":
    # print(Image_Loader("https://topikkorea.s3.amazonaws.com/topik_image/e474bcea-163f-11ef-aa13-e5a43cd400fd.png").get_image_text())

    exam_type = "TOPIK_2_READING"
    problem_type = "READING_2_PROBLEM_TYPE_4"
    result = find_random_question(
        exam_type, problem_type, detail_type=1, count=1, use_generated_data=False
    )
    print("result")
    print(result)
