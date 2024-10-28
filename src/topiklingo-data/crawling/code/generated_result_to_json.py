import time
import uuid
import json
import os

problem_types_dependent_on_example = [5, 7, 8]

problem_type_to_question = {
    'READING_1_PROBLEM_TYPE_1_1': '※ 무엇에 대한 내용입니까? <보기>와 같이 알맞은 것을 고르십시오.',
    'READING_1_PROBLEM_TYPE_1_2': '※ <보기>와 같이 ( \t)에 들어갈 말로 가장 알맞은 것을 고르십시오.',
    'READING_1_PROBLEM_TYPE_2'  : '※ 다음을 읽고 맞지 않는 것을 고르십시오.',
    'READING_1_PROBLEM_TYPE_3'  : '※ 다음을 읽고 내용이 같은 것을 고르십시오.',
    'READING_1_PROBLEM_TYPE_4'  : '※ 다음을 읽고 중심 내용을 고르십시오.',
    'READING_1_PROBLEM_TYPE_5'  : '※ 다음을 읽고 물음에 답하십시오.',
    'READING_1_PROBLEM_TYPE_6'  : '※ 다음을 순서에 맞게 배열한 것을 고르십시오.',
    'READING_1_PROBLEM_TYPE_7'  : '※ 다음을 읽고 물음에 답하십시오.',
    'READING_1_PROBLEM_TYPE_8'  : '※ 다음을 읽고 물음에 답하십시오.'
}

problem_type_to_example = {
    'READING_1_PROBLEM_TYPE_1_1': {
            'example': ['오늘은 월요일입니다. 내일은 화요일입니다.'],
            'selector': ['공부', '얼굴', '요일', '계절'],
            'answer': '요일',
            },
    'READING_1_PROBLEM_TYPE_1_2': {
            'example': ['단어를 모릅니다. ( )을 찾습니다.'],
            'selector': ['안경', '옷장', '사전', '지갑'],
            'answer': '사전',
            },
     'READING_1_PROBLEM_TYPE_2': {
            'example': None,
            'selector': None,
            'answer': None,
            },
    'READING_1_PROBLEM_TYPE_3': {
            'example': None,
            'selector': None,
            'answer': None,
            },
    'READING_1_PROBLEM_TYPE_4': {
            'example': None,
            'selector': None,
            'answer': None,
            }, 
    'READING_1_PROBLEM_TYPE_6': {
            'example': None,
            'selector': None,
            'answer': None,
            }, 
}

problem_type_to_EType = {
    'READING_1_PROBLEM_TYPE_1' : 'TEXT',
    'READING_1_PROBLEM_TYPE_2' : 'TEXT',
    'READING_1_PROBLEM_TYPE_3'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_4'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_5'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_6'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_7'  : 'IMAGE',
    'READING_1_PROBLEM_TYPE_8'  : 'TEXT',
}

problem_type_to_QEType = {
    'READING_1_PROBLEM_TYPE_1' : 'TEXT',
    'READING_1_PROBLEM_TYPE_2' : 'IMAGE',
    'READING_1_PROBLEM_TYPE_3'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_4'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_5'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_6'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_7'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_8'  : 'TEXT',
}

problem_type_to_AType = {
    'READING_1_PROBLEM_TYPE_1' : 'TEXT',
    'READING_1_PROBLEM_TYPE_2' : 'TEXT',
    'READING_1_PROBLEM_TYPE_3'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_4'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_5'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_6'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_7'  : 'TEXT',
    'READING_1_PROBLEM_TYPE_8'  : 'TEXT',
}

def generate_uuid():
    time.sleep(0.01)
    return str(uuid.uuid1())

def get_answer_number(answer, selector):
    if answer is None or selector is None:
        return None
    for i, s in enumerate(selector):
        if s == answer:
            return i+1
    raise ValueError('Answer not found in selector')

def update_json(json_data, single_problems, topik_type, problem_type, detailed_problem_type):
    if 'uuid' not in json_data:
        json_data['uuid'] = generate_uuid()
    if 'title' not in json_data:
        json_data['title'] = 'Generated_n_TOPIK_1_READING'
    if 'type' not in json_data:
        json_data['type'] = topik_type
    if 'year' not in json_data:
        json_data['year'] = 2024
    if 'problems' not in json_data:
        json_data['problems'] = []
    
    ### get the integer from the problem type
    problem_type_num = int(problem_type.split('_')[-1])
    
    if problem_type_num not in problem_types_dependent_on_example:
        problem = {}
        problem['uuid'] = generate_uuid()
        problem['problem'] = problem_type_to_question[detailed_problem_type]
        problem['PType'] = problem_type
        problem['EType'] = problem_type_to_EType[problem_type]
        sample_example = problem_type_to_example[detailed_problem_type]
        example = {
            "conversation": str(sample_example['example']),
            "answers": sample_example['selector'],
            "selected": get_answer_number(sample_example['answer'], sample_example['selector'])
        }
        problem['example'] = json.dumps(example, ensure_ascii=False)
        # problem['example'] = str(example)

        questions = []
        for single_problem in single_problems:
            question = {}
            question['uuid'] = generate_uuid()
            question['QEType'] = problem_type_to_QEType[problem_type]
            question['questionProblem'] = ''
            question['score'] = single_problem['score']
            question['rightAnswer'] = get_answer_number(single_problem['answer'], single_problem['selector'])
            question['example'] = str([single_problem['example']])
            answers = []
            for i, s in enumerate(single_problem['selector']):
                answer = {}
                answer['uuid'] = generate_uuid()
                answer['AType'] = problem_type_to_AType[problem_type]
                answer['answer'] = s
                answers.append(answer)
            question['answers'] = answers
            questions.append(question)

        problem['questions'] = questions
        ### Append the problem to problems
        json_data['problems'].append(problem)

    ### Problem types dependent on example
    else:
        for single_problem in single_problems:
            problem = {}
            problem['uuid'] = generate_uuid()
            problem['problem'] = problem_type_to_question[detailed_problem_type]
            problem['PType'] = problem_type
            problem['EType'] = problem_type_to_EType[problem_type]
            example = {
                "conversation": str([single_problem['example']]),
                "answers": None,
                "selected": None
            }
            problem['example'] = json.dumps(example, ensure_ascii=False)

            questions = []
            for q in single_problem['questions']:
                question = {}
                question['uuid'] = generate_uuid()
                question['QEType'] = problem_type_to_QEType[problem_type]
                question['questionProblem'] = q['question']
                question['score'] = q['score']
                question['rightAnswer'] = get_answer_number(q['answer'], q['selector'])
                question['example'] = ""
                answers = []
                for _, s in enumerate(q['selector']):
                    answer = {}
                    answer['uuid'] = generate_uuid()
                    answer['AType'] = problem_type_to_AType[problem_type]
                    answer['answer'] = s
                    answers.append(answer)
                question['answers'] = answers
                questions.append(question)

            problem['questions'] = questions
            ### Append the problem to problems
            json_data['problems'].append(problem)

    return json_data

def create_json_file(base_path, file_name, data):
    file_path = f'{base_path}/{file_name}'
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
        print(f'{file_path} has been created')

dict1_1_q1 = {'example': '빨강색이 좋아요. 파랑색도 이쁘죠.',
 'selector': ['산악', '천장', '해안', '배경'],
 'answer': '배경',
 'eval_answer': '배경',
 'eval_explain': "이 문제에서는 색상에 대한 언급이 있으며, 색상은 일반적으로 어떤 배경이나 환경에서 관찰될 수 있습니다. '산악', '천장', '해안'은 모두 특정 위치나 자연 현상을 지칭하지만, '배경'은 빨강색과 파랑색 같은 색상이 사용될 수 있는 일반적인 맥락을 제공합니다. 따라서 '배경'이 가장 적절한 단어입니다.",
 'score': 2}
dict1_1_q2 = {'example': '의자는 편안합니다. 나무 의자가 예쁩니다.',
 'selector': ['가구', '과일', '동물', '식물'],
 'answer': '가구',
 'eval_answer': '가구',
 'eval_explain': "예시 문장에서 언급된 '의자'는 가구에 속하는 물건입니다. 따라서 '가구'가 올바른 선택입니다. '과일', '동물', '식물'은 의자와 관련이 없습니다.",
 'score': 2} 

dict1_2_q1 =  {'example': '고추장이 매워요. (   )도 매워요.',
 'selector': ['김치', '무', '고추장', '된장'],
 'answer': '김치',
 'eval_answer': '김치',
 'eval_explain': '고추장이 매워요. 김치도 매워요.',
 'score': 2}
dict1_2_q2 = {'example': '공부를 열심히 해야 (   ). 시험에 잘 보세요.',
 'selector': ['되다', '즐겁다', '하다', '합니다'],
 'answer': '합니다',
 'eval_answer': '합니다',
 'eval_explain': '공부를 열심히 해야 합니다. 시험에 잘 보세요.',
 'score': 2}

dict2_q1 = {'example': 'asset/sample_image/83_1_42.png',
 'selector': ['수미 씨가 제주도에 도착했어요.',
  '수미 씨가 제주도의 날씨를 즐기고 있어요.',
  '민희가 미국에 있습니다.',
  '수미 씨는 제주공항에 도착했습니다.'],
 'answer': '민희가 미국에 있습니다.',
 'eval_answer': '민희가 미국에 있습니다.',
 'explain': '',
 'score': 2}
dict2_q2 = {'example': 'asset/sample_image/83_1_41.png',
 'selector': ['방이 아닌 텐트가 있습니다.',
  '큰 부엌이 함께 제공됩니다.',
  '한국대학교에서 가깝게 위치하고 있습니다.',
  '깨끗한 화장실이 포함되어 있습니다.'],
 'answer': '방이 아닌 텐트가 있습니다.',
 'eval_answer': '방이 아닌 텐트가 있습니다.',
 'explain': '',
 'score': 2}

dict3_q1 = {'example': '도로를 건너기 전에는 항상 신호등을 확인해야 합니다. 신호등이 빨간 불일 때는 반드시 멈춰야 합니다. 신호등이 초록불로 바뀌면 건너도 안전합니다.',
 'selector': ['신호등이 보라색 불일 때는 뛰어서 건너는 것이 좋습니다.',
  '신호등이 초록불로 바뀌면 건너도 안전합니다',
  '신호등이 꺼져 있을 때는 자유롭게 건너면 됩니다.',
  '신호등이 노란 불일 때는 빨리 건너는 것이 좋습니다.'],
 'answer': '신호등이 초록불로 바뀌면 건너도 안전합니다',
 'eval_answer': '신호등이 초록불로 바뀌면 건너도 안전합니다',
 'explain': '',
 'score': 2}
dict3_q2 = {'example': '저는 매년 새해가 되면 새로운 달력을 사서 벽에 걸어놓습니다. 달력을 보면서 일정을 계획하고 중요한 날짜를 확인합니다. 달력은 제 삶을 조직화하는데 큰 도움이 됩니다.',
 'selector': ['달력은 제 삶을 조직화하는데 큰 도움이 됩니다.',
  '매년 새해가 되면 새로운 달력을 사서 벽에 걸어놓습니다.',
  '달력을 보면서 일정을 계획하고 중요한 날짜를 확인합니다.',
  '달력을 보면서 요일을 확인하고 계획을 세웁니다.'],
 'answer': '달력을 보면서 요일을 확인하고 계획을 세웁니다.',
 'eval_answer': '달력을 보면서 요일을 확인하고 계획을 세웁니다.',
 'explain': '',
 'score': 2}

dict4_q1 = {'example': '저는 예술에 많은 관심이 있습니다. 특히 그림 그리기를 좋아합니다. 시간이 날 때마다 미술관에 가는 것을 즐깁니다.',
 'selector': ['저는 음악에 많은 관심이 있습니다.',
  '저는 예술에 많은 관심이 있습니다.',
  '저는 운동을 좋아합니다.',
  '저는 요리를 즐깁니다.'],
 'answer': '저는 예술에 많은 관심이 있습니다.',
 'eval_answer': '저는 예술에 많은 관심이 있습니다.',
 'explain': '주어진 문장에서 "저는 예술에 많은 관심이 있습니다"라는 문장이 중심 내용입니다. 특히 그림 그리기를 좋아하고 미술관에 가는 것을 즐긴다는 추가 정보가 이를 뒷받침합니다. 나머지 선택지는 주어진 문장의 내용과 일치하지 않습니다.',
 'score': 2}

dict5_q1 = {
  "example": "저는 유치원 선생님입니다. 저는 아이들을 좋아해서 유치원 선생님이 되었습니다. 우리 유치원에는 아이들이 많아서 일이 조금 힘듭니다. 또 집에 늦게 가는 날도 많습니다. ( ) 아이들이 정말 귀엽고 예뻐서 저는 제 일을 좋아합니다.",
  "questions":[
    {
      "question" : "( ) 에 들어갈 말로 가장 알맞은 것을 고르십시오.",
      "selector": ["그러면","하지만","그래서","그리고"],
      "answer": "하지만",
      "eval_answer" : "하지만",
      "eval_explain" : "",
      'score': 2
    },
    {
      "question" : "<보기>의 내용과 같은 것을 고르십시오.",
      "selector": ["저는 아이들이 좋습니다.","저는 보통 일찍 집에 갑니다.","유치원 일은 힘들지 않습니다.","유치원에는 아이들이 많지 않습니다."],
      "answer": "저는 아이들이 좋습니다.",
      "eval_answer" : "저는 아이들이 좋습니다.",
      "eval_explain" : "",
      'score': 2
    }
  ]
}

dict6_q1 = {
      "example": "（가）채소가 싸고 좋아서 많이 샀습니다.（나）지난 주말에 채소를 사러 시장에 갔습니다.（다）아주머니가 주신 토마토 때문에 기분이 좋았습니다.（라）채소 가게 아주머니가 토마토를 하나 더 주셨습니다.",
      "selector": ["（가）-（나）-（라）-（다）","（가）-（다）-（나）-（라）","（나）-（가）-（라）-（다）","（나）-（다）-（라）-（가）"],
      "answer": "（나）-（가）-（라）-（다）",
      'score': 2
    }

dict7_q1 = {
      "example": "https://topikkorea.s3.amazonaws.com/topik_image/b805aea2-163f-11ef-ac46-e5a43cd400fd.png",
      "questions":[
        {
          "question" : "이 글을 쓴 의도와 가장 가까운 것을 고르십시오.",
          "selector": ["음악회를 계획하려고","음악회에 초대하려고","음악회 참석을 확인하려고","음악회 참석에 감사하려고"],
          "answer": "음악회에 초대하려고",
          'score': 2
        },
        {
          "question" : "이 글의 내용과 같은 것을 고르십시오.",
          "selector": ["초콜릿을 먹으면 기분이 좋아집니다.","목이 마를 때 초콜릿을 먹는 것이 좋습니다.","가수들은 보통 공연 전에 초콜릿을 먹습니다.","말을 많이 해야 할 때 초콜릿을 먹으면 좋습니다."],
          "answer": "초콜릿을 먹으면 기분이 좋아집니다.",
          'score': 2
        }
      ]
    }

dict8_q1 = {
      "example": "초콜릿은 달아서 사람의 기분을 좋게 합니다. 그래서 사람들이 초콜릿을 자주 먹습니다. 그런데 말을 많이 할 때나 발표를 해야 할 때는 초콜릿을 먹지 않는 것이 좋습니다. 초콜릿을 먹으면 목이 마르게 되어서 목소리가 잘 안 ( ㉠ ). 그래서 가수들도 공연 전에는 초콜릿을 먹지 않습니다.",
      "questions":[
        {
          "question" : "( ㉠ ) 에 들어갈 말로 가장 알맞은 것을 고르십시오. ",
          "selector": ["나와도 됩니다","나와야 합니다","나오기로 합니다","나오기 때문입니다"],
          "answer": "나오기 때문입니다",
          'score': 2
        },
        {
          "question" : "<보기>의 내용과 같은 것을 고르십시오.",
          "selector": ["초콜릿을 먹으면 기분이 좋아집니다.","목이 마를 때 초콜릿을 먹는 것이 좋습니다.","가수들은 보통 공연 전에 초콜릿을 먹습니다.","말을 많이 해야 할 때 초콜릿을 먹으면 좋습니다."],
          "answer": "초콜릿을 먹으면 기분이 좋아집니다.",
          'score': 2
        }
      ]
    }

list_of_dict_READING_1_PROBLEM_TYPE_1_1 = [dict1_1_q1, dict1_1_q2]
list_of_dict_READING_1_PROBLEM_TYPE_1_2 = [dict1_2_q1, dict1_2_q2]
list_of_dict_READING_1_PROBLEM_TYPE_2 = [dict2_q1, dict2_q2]
list_of_dict_READING_1_PROBLEM_TYPE_3 = [dict3_q1, dict3_q2]
list_of_dict_READING_1_PROBLEM_TYPE_4 = [dict4_q1]
list_of_dict_READING_1_PROBLEM_TYPE_5 = [dict5_q1]
list_of_dict_READING_1_PROBLEM_TYPE_6 = [dict6_q1]
list_of_dict_READING_1_PROBLEM_TYPE_7 = [dict7_q1]
list_of_dict_READING_1_PROBLEM_TYPE_8 = [dict8_q1]

my_json = {}
my_json = update_json(
    json_data=my_json, 
    single_problems=list_of_dict_READING_1_PROBLEM_TYPE_1_1, 
    topik_type='TOPIK_1_READING', 
    problem_type='READING_1_PROBLEM_TYPE_1', 
    detailed_problem_type='READING_1_PROBLEM_TYPE_1_1'
    )
my_json = update_json(
    json_data=my_json, 
    single_problems=list_of_dict_READING_1_PROBLEM_TYPE_1_2, 
    topik_type='TOPIK_1_READING', 
    problem_type='READING_1_PROBLEM_TYPE_1', 
    detailed_problem_type='READING_1_PROBLEM_TYPE_1_2'
    )
my_json = update_json(
    json_data=my_json, 
    single_problems=list_of_dict_READING_1_PROBLEM_TYPE_2, 
    topik_type='TOPIK_1_READING', 
    problem_type='READING_1_PROBLEM_TYPE_2', 
    detailed_problem_type='READING_1_PROBLEM_TYPE_2'
    )
my_json = update_json(
    json_data=my_json, 
    single_problems=list_of_dict_READING_1_PROBLEM_TYPE_3, 
    topik_type='TOPIK_1_READING', 
    problem_type='READING_1_PROBLEM_TYPE_3', 
    detailed_problem_type='READING_1_PROBLEM_TYPE_3'
    )
my_json = update_json(
    json_data=my_json, 
    single_problems=list_of_dict_READING_1_PROBLEM_TYPE_4, 
    topik_type='TOPIK_1_READING', 
    problem_type='READING_1_PROBLEM_TYPE_4', 
    detailed_problem_type='READING_1_PROBLEM_TYPE_4'
    )
my_json = update_json(
    json_data=my_json, 
    single_problems=list_of_dict_READING_1_PROBLEM_TYPE_5, 
    topik_type='TOPIK_1_READING', 
    problem_type='READING_1_PROBLEM_TYPE_5', 
    detailed_problem_type='READING_1_PROBLEM_TYPE_5'
    )
my_json = update_json(
    json_data=my_json, 
    single_problems=list_of_dict_READING_1_PROBLEM_TYPE_6, 
    topik_type='TOPIK_1_READING', 
    problem_type='READING_1_PROBLEM_TYPE_6', 
    detailed_problem_type='READING_1_PROBLEM_TYPE_6'
    )
my_json = update_json(
    json_data=my_json, 
    single_problems=list_of_dict_READING_1_PROBLEM_TYPE_7, 
    topik_type='TOPIK_1_READING', 
    problem_type='READING_1_PROBLEM_TYPE_7', 
    detailed_problem_type='READING_1_PROBLEM_TYPE_7'
    )
my_json = update_json(
    json_data=my_json, 
    single_problems=list_of_dict_READING_1_PROBLEM_TYPE_8, 
    topik_type='TOPIK_1_READING', 
    problem_type='READING_1_PROBLEM_TYPE_8', 
    detailed_problem_type='READING_1_PROBLEM_TYPE_8'
    )

file_name = 'reading_1_problem_type_1_generated.json'
# base_path = 'C:/Users/User/Desktop/temp/uni/final_project/topik-korea-data/crawling/data/json'
base_path = '' 
if base_path == '':
    raise ValueError('Please specify the base path')
create_json_file(base_path, file_name, my_json)