import sys, os
import random
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from util import find_random_question

def generate_reading_1_problem_type_7(verbose=False, default_model="gpt-4o-mini"):
    total_cost = 0
    exam_type = 'TOPIK_1_READING'
    problem_type = 'READING_1_PROBLEM_TYPE_7'
    random_question = find_random_question(exam_type, problem_type, detail_type=1, count=1,use_generated_data=False)
    questions = []
    question_number = 0
    for question in random_question:
        selector = question["selector"]
        random.shuffle(selector)
        if question_number == 0:
            data = {
                "question": "이 글을 쓴 의도와 가장 가까운 것을 고르십시오.",
                "selector": selector,
                "answer": question["answer"],
                "eval_answer": question["answer"],
                "eval_explain": ""
            }
            questions.append(data)
            question_number += 1
        elif question_number == 1:
            data = {
                "question": "이 글의 내용과 같은 것을 고르십시오.",
                "selector": selector,
                "answer": question["answer"],
                "eval_answer": question["answer"],
                "eval_explain": ""
            }
            questions.append(data)
            question_number += 1

    if random_question[0]["example"].startswith("/s3-bucket/"):
        random_question[0]["example"] = random_question[0]["example"].replace("/s3-bucket/", "https://topikkorea.s3.amazonaws.com/")

    result = {
        "example": random_question[0]["example"],
        "questions": questions,
        "total_cost": total_cost,
    }
    return result

if __name__ == "__main__":
    result = generate_reading_1_problem_type_7(default_model="gpt-3.5-turbo",verbose=True)
    print(result)