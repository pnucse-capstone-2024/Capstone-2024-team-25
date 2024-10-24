import json
import random
from langchain.schema import HumanMessage, SystemMessage
from util import Image_Loader


class Problem_Template:
    def __init__(self, exam_type, problem_type):
        self.exam_type = exam_type
        self.problem_type = problem_type
        self.problem_type_template_name = exam_type + "_" + problem_type
        problem_type_template_path = (
            "Problem_Type_Template/" + self.problem_type_template_name + ".json"
        )
        self.config_json = json.load(
            open(problem_type_template_path, "r", encoding="utf-8")
        )
        self.instruction = ""
        self.use_example_num = 2

    def set_instruction(self, instruction):
        self.instruction = instruction

    def set_example_num(self, use_example_num):
        self.use_example_num = use_example_num

    def get_problem_type(self):
        return self.problem_type

    def get_exam_type(self):
        return self.exam_type

    def get_problem_type_template_name(self):
        return self.problem_type_template_name

    def get_problem_config(self):
        return self.config_json

    def get_model_message(self):
        return Prompt_Generator(self.config_json).get_model_prompt(
            self.instruction, self.use_example_num
        )

    def get_eval_message(self, response):
        return Prompt_Generator(self.config_json).get_eval_prompt(response)

    def get_output_parser_message(self, response):
        return Prompt_Generator.get_output_parser(response)

    def __str__(self):
        return f"problem_type: {self.problem_type_template_name}, config: {self.config_json}"


class Prompt_Generator:
    def __init__(self, config_json):
        self.system_message = config_json["system_message"]
        self.content_text = config_json["content_text"]
        try:
            self.eval_message = config_json["eval_message"]
        except KeyError:
            self.eval_message = {}

    def get_context_prompt(self, example_num):
        if len(self.content_text) < example_num:
            pass
        else:
            self.content_text = random.sample(self.content_text, example_num)
            random.shuffle(self.content_text)

        human_messages = []
        idx = 1
        for item in self.content_text:
            index = "출제 예시" + str(idx) + "번"

            try:
                if item["image_true"]:
                    image_text = Image_Loader(item["example"]).get_image_text()
                    example = "<보기> " + image_text
            except KeyError:
                example = "<보기> " + item["example"]

            try:
                if item["questions"]:
                    question_idx = 1
                    for question in item["questions"]:
                        question_text = question["question"]
                        selectors = question["selector"]
                        options = []
                        for key, value in selectors.items():
                            temp = f"{key}.{value}"
                            options.append(temp)
                        options_str = ", ".join(options)
                        answer_key = next(iter(question["answer"]))
                        answer_value = question["answer"][answer_key]
                        answer = f"<정답> {answer_key}.{answer_value}"
                        if question_idx == 1:
                            prompt = f"[{index} : {example} <질문 {question_idx}> {question_text} <선택지> {options_str} {answer}"
                            if len(item["questions"]) == 1:
                                prompt += "]"
                        else:
                            prompt = f"<질문 {question_idx}> {question_text} <선택지> {options_str} {answer}]"
                        human_messages.append(prompt)
                        idx += 1
                        question_idx += 1
            except KeyError:
                selectors = item["selector"]
                options = []
                for key, value in selectors.items():
                    temp = f"{key}.{value}"
                    options.append(temp)
                options_str = ", ".join(options)
                answer_key = next(iter(item["answer"]))
                answer_value = item["answer"][answer_key]
                answer = f"<정답> {answer_key}.{answer_value}"
                prompt = f"[{index} : {example} <선택지> {options_str} {answer}]"
                human_messages.append(prompt)
                idx += 1
        context_prompt = " \n ".join(human_messages)
        return context_prompt

    def get_system_prompt(self, example_num):
        main_role = "### " + self.system_message["main_role"]
        sub_role = self.system_message["sub_role"]
        task = "출제할 " + self.system_message["task"]
        condition = self.system_message["condition"]
        conditions = []
        for key, value in condition.items():
            temp = f"{key}. {value}"
            conditions.append(temp)
        conditions_str = " ".join(conditions)
        context = self.get_context_prompt(example_num)
        system_prompt = f"{main_role} {sub_role} \n {task} \n {context} \n 다음 규칙을 준수하라: {conditions_str}"
        return system_prompt

    def get_eval_prompt(self, response):
        try:
            main_role = "### " + self.eval_message["main_role"]
            sub_role = self.eval_message["sub_role"]
            task = "- " + self.eval_message["task"]
            problem = "- " + self.system_message["task"]
            eval_prompt = f"{main_role} {sub_role} {task}"
            response = f"# {problem} \n 주어진 문제 : {response}"
        except KeyError:
            raise KeyError("eval_message가 없습니다, 검증기를 실행하지 않습니다.")
        return [
            SystemMessage(content=eval_prompt),
            HumanMessage(content=response),
        ]

    @staticmethod
    def get_output_parser(response):
        parser_prompt = "Extract <보기> {내용} <선택지> {1. xxx, 2. xxx, 3. xxx, 4. xxx} <정답> {정답 번호} format"
        response = f"#Response : {response}"
        return [
            SystemMessage(content=parser_prompt),
            HumanMessage(content=response),
        ]

    def get_model_prompt(self, instruction, example_num):
        if instruction == "":
            instruction = "변형 문제를 1개 출제하라."

        self.system_message = self.get_system_prompt(example_num)
        # self.content_text = self.get_context_prompt(example_num)
        return [
            SystemMessage(content=self.system_message),
            HumanMessage(content=instruction),
        ]


if __name__ == "__main__":
    problem_template = Problem_Template("reading_1", "problem_type_1_1")
    print(problem_template.get_model_message())
    problem_template.set_instruction("변형 문제를 2개 출제하라.")
    problem_template.set_example_num(1)
    print(problem_template.get_model_message())
    print(problem_template.get_eval_message("1.1"))
    print(problem_template.get_output_parser_message("1.1"))
