from retrying import retry

# 문제 생성 함수들을 임포트
from Problem_Type_Template.reading_1_problem_type_1_1 import generate_reading_1_problem_type_1_1
from Problem_Type_Template.reading_1_problem_type_1_2 import generate_reading_1_problem_type_1_2
from Problem_Type_Template.reading_1_problem_type_2 import generate_reading_1_problem_type_2
from Problem_Type_Template.reading_1_problem_type_3 import generate_reading_1_problem_type_3
from Problem_Type_Template.reading_1_problem_type_4 import generate_reading_1_problem_type_4
from Problem_Type_Template.reading_1_problem_type_5 import generate_reading_1_problem_type_5
from Problem_Type_Template.reading_1_problem_type_6 import generate_reading_1_problem_type_6
from Problem_Type_Template.reading_1_problem_type_7 import generate_reading_1_problem_type_7
from Problem_Type_Template.reading_1_problem_type_8_1 import generate_reading_1_problem_type_8_1
from Problem_Type_Template.reading_1_problem_type_8_2 import generate_reading_1_problem_type_8_2

from Problem_Type_Template.reading_2_problem_type_1 import generate_reading_2_problem_type_1
from Problem_Type_Template.reading_2_problem_type_2 import generate_reading_2_problem_type_2
from Problem_Type_Template.reading_2_problem_type_3 import generate_reading_2_problem_type_3
from Problem_Type_Template.reading_2_problem_type_4_1 import generate_reading_2_problem_type_4_1
from Problem_Type_Template.reading_2_problem_type_4_2 import generate_reading_2_problem_type_4_2
from Problem_Type_Template.reading_2_problem_type_5 import generate_reading_2_problem_type_5
from Problem_Type_Template.reading_2_problem_type_6 import generate_reading_2_problem_type_6
from Problem_Type_Template.reading_2_problem_type_7 import generate_reading_2_problem_type_7
from Problem_Type_Template.reading_2_problem_type_8 import generate_reading_2_problem_type_8
from Problem_Type_Template.reading_2_problem_type_9 import generate_reading_2_problem_type_9
from Problem_Type_Template.reading_2_problem_type_10 import generate_reading_2_problem_type_10
from Problem_Type_Template.reading_2_problem_type_11 import generate_reading_2_problem_type_11
from Problem_Type_Template.reading_2_problem_type_12 import generate_reading_2_problem_type_12
from Problem_Type_Template.reading_2_problem_type_13 import generate_reading_2_problem_type_13
from Problem_Type_Template.reading_2_problem_type_14 import generate_reading_2_problem_type_14
from Problem_Type_Template.reading_2_problem_type_15 import generate_reading_2_problem_type_15
from Problem_Type_Template.reading_2_problem_type_16 import generate_reading_2_problem_type_16


import asyncio
from multiprocessing import Pool, Manager
from tqdm.asyncio import tqdm
from tqdm import tqdm as tqdm_sync
import time
import json

class ExamGenerator:
    def __init__(self, exam_config, exam_type, verbose=False):
        self.exam_config = exam_config
        self.exam_type = exam_type
        self.verbose = verbose
        self.exam = []

    async def generate(self):
        loop = asyncio.get_event_loop()
        tasks = [loop.run_in_executor(None, self.generate_problems, problem_type)
                 for problem_type in self.exam_config]

        results = []
        for task in tqdm(asyncio.as_completed(tasks), total=len(tasks), desc="Generating Problems"):
            result = await task
            results.append(result)
        
        # 결과를 요청한 순서대로 정렬
        sorted_results = [result for result in await asyncio.gather(*tasks)]
        self.exam.extend(sorted_results)
        
        return self.exam

    def generate_problems(self, problem_config):
        with Manager() as manager:
            progress_bar = manager.Value('i', 0)
            lock = manager.Lock()
            total = problem_config['num_problems']
            
            with Pool(processes=problem_config['num_processes']) as pool:
                results = list(tqdm_sync(
                    pool.imap(self.call_problem_generator_with_progress, 
                              [(problem_config['type'], problem_config['difficulty'], self.verbose, problem_config.get('target'), progress_bar, lock) 
                               for _ in range(problem_config['num_problems'])]),
                    total=total,
                    desc=f"Generating {problem_config['type']}",
                    position=0,
                    leave=True
                ))
            return results

    @staticmethod
    @retry(stop_max_attempt_number=3, wait_fixed=2000)
    def call_problem_generator_with_progress(params):
        problem_type, difficulty, verbose, target, progress_bar, lock = params
        # 문제 타입에 따라 적절한 문제 생성 함수 호출
        result = None
        if problem_type == "reading_1_problem_type_1_1":
            time.sleep(3)
            result = generate_reading_1_problem_type_1_1(difficulty=difficulty, verbose=verbose)
        elif problem_type == "reading_1_problem_type_1_2":
            time.sleep(3)
            result = generate_reading_1_problem_type_1_2(target=target, verbose=verbose)
        elif problem_type == "reading_1_problem_type_2":
            time.sleep(3)
            result = generate_reading_1_problem_type_2(verbose=verbose)
        elif problem_type == "reading_1_problem_type_3":
            time.sleep(3)
            result = generate_reading_1_problem_type_3(verbose=verbose)
        elif problem_type == "reading_1_problem_type_4":
            time.sleep(3)
            result = generate_reading_1_problem_type_4(verbose=verbose)
        elif problem_type == "reading_1_problem_type_5":
            time.sleep(3)
            result = generate_reading_1_problem_type_5(verbose=verbose)
        elif problem_type == "reading_1_problem_type_6":
            time.sleep(3)
            result = generate_reading_1_problem_type_6(verbose=verbose)
        elif problem_type == "reading_1_problem_type_7":
            time.sleep(3)
            result = generate_reading_1_problem_type_7(verbose=verbose)
        elif problem_type == "reading_1_problem_type_8_1":
            time.sleep(3)
            result = generate_reading_1_problem_type_8_1(verbose=verbose)
        elif problem_type == "reading_1_problem_type_8_2":
            time.sleep(3)
            result = generate_reading_1_problem_type_8_2(verbose=verbose)
        elif problem_type == "reading_2_problem_type_1":
            time.sleep(3)
            result = generate_reading_2_problem_type_1(verbose=verbose)
        elif problem_type == "reading_2_problem_type_2":
            time.sleep(3)
            result = generate_reading_2_problem_type_2(verbose=verbose)
        elif problem_type == "reading_2_problem_type_3":
            time.sleep(3)
            result = generate_reading_2_problem_type_3(verbose=verbose)
        elif problem_type == "reading_2_problem_type_4_1":
            time.sleep(3)
            result = generate_reading_2_problem_type_4_1(verbose=verbose)
        elif problem_type == "reading_2_problem_type_4_2":
            time.sleep(3)
            result = generate_reading_2_problem_type_4_2(verbose=verbose)
        elif problem_type == "reading_2_problem_type_5":
            time.sleep(3)
            result = generate_reading_2_problem_type_5(verbose=verbose)
        elif problem_type == "reading_2_problem_type_6":
            time.sleep(3)
            result = generate_reading_2_problem_type_6(verbose=verbose)
        elif problem_type == "reading_2_problem_type_7":
            time.sleep(3)
            result = generate_reading_2_problem_type_7(verbose=verbose)
        elif problem_type == "reading_2_problem_type_8":
            time.sleep(3)
            result = generate_reading_2_problem_type_8(verbose=verbose)
        elif problem_type == "reading_2_problem_type_9":
            time.sleep(3)
            result = generate_reading_2_problem_type_9(verbose=verbose)
        elif problem_type == "reading_2_problem_type_10":
            time.sleep(3)
            result = generate_reading_2_problem_type_10(verbose=verbose)
        elif problem_type == "reading_2_problem_type_11":
            time.sleep(3)
            result = generate_reading_2_problem_type_11(verbose=verbose)
        elif problem_type == "reading_2_problem_type_12":
            time.sleep(3)
            result = generate_reading_2_problem_type_12(verbose=verbose)
        elif problem_type == "reading_2_problem_type_13":
            time.sleep(3)
            result = generate_reading_2_problem_type_13(verbose=verbose)
        elif problem_type == "reading_2_problem_type_14":
            time.sleep(3)
            result = generate_reading_2_problem_type_14(verbose=verbose)
        elif problem_type == "reading_2_problem_type_15":
            time.sleep(3)
            result = generate_reading_2_problem_type_15(verbose=verbose)
        elif problem_type == "reading_2_problem_type_16":
            time.sleep(3)
            result = generate_reading_2_problem_type_16(verbose=verbose)
        else:
            raise ValueError(f"Unknown problem type: {problem_type}")
        
        with lock:
            progress_bar.value += 1
        
        return result

# 예제 사용
async def main_generate_reading_1():
    num_processes = 2
    exam_config = [
        {"type": "reading_1_problem_type_1_1", "difficulty": "Normal", "num_problems": 3, "num_processes": num_processes},
        {"type": "reading_1_problem_type_1_2", "difficulty": "Normal", "num_problems": 1, "num_processes": 1, "target": "조사"},
        {"type": "reading_1_problem_type_1_2", "difficulty": "Normal", "num_problems": 1, "num_processes": 1, "target": "명사"},
        {"type": "reading_1_problem_type_1_2", "difficulty": "Normal", "num_problems": 1, "num_processes": 1, "target": "동사"},
        {"type": "reading_1_problem_type_1_2", "difficulty": "Normal", "num_problems": 2, "num_processes": num_processes, "target": "형용사"},
        {"type": "reading_1_problem_type_1_2", "difficulty": "Normal", "num_problems": 1, "num_processes": 1, "target": "동사"},
        {"type": "reading_1_problem_type_2", "difficulty": "Normal", "num_problems": 3, "num_processes": num_processes},
        {"type": "reading_1_problem_type_3", "difficulty": "Normal", "num_problems": 3, "num_processes": num_processes},
        {"type": "reading_1_problem_type_4", "difficulty": "Normal", "num_problems": 3, "num_processes": num_processes},
        {"type": "reading_1_problem_type_5", "difficulty": "Normal", "num_problems": 4, "num_processes": num_processes},
        {"type": "reading_1_problem_type_6", "difficulty": "Normal", "num_problems": 2, "num_processes": num_processes},
        {"type": "reading_1_problem_type_7", "difficulty": "Normal", "num_problems": 1, "num_processes": 1},
        {"type": "reading_1_problem_type_8_1", "difficulty": "Normal", "num_problems": 1, "num_processes": 1},
        {"type": "reading_1_problem_type_8_2", "difficulty": "Normal", "num_problems": 4, "num_processes": num_processes}
    ]
    exam_generator = ExamGenerator(exam_config, exam_type="Reading", verbose=False)
    exam = await exam_generator.generate()
    
    base_path = '/home/shinbg/topik-korea-data/data/reading_1_v2/problem_data'
    #making file path base current time
    file_path = f"generate_exam_{time.strftime('%Y%m%d_%H%M')}.json"
    file_path = f"{base_path}/{file_path}"
    
    with open(file_path, 'w') as outfile:
        json.dump(exam, outfile, indent=4, ensure_ascii=False)

async def main_generate_reading_2():
    num_processes = 2
    exam_config = [
        {"type": "reading_2_problem_type_1", "difficulty": "Normal", "num_problems": 2, "num_processes": num_processes},
        {"type": "reading_2_problem_type_2", "difficulty": "Normal", "num_problems": 2, "num_processes": num_processes},
        {"type": "reading_2_problem_type_3", "difficulty": "Normal", "num_problems": 4, "num_processes": num_processes},
        {"type": "reading_2_problem_type_4_1", "difficulty": "Normal", "num_problems": 2, "num_processes": num_processes},
        {"type": "reading_2_problem_type_4_2", "difficulty": "Normal", "num_problems": 2, "num_processes": num_processes},
        {"type": "reading_2_problem_type_5", "difficulty": "Normal", "num_problems": 3, "num_processes": num_processes},
        {"type": "reading_2_problem_type_6", "difficulty": "Normal", "num_problems": 7, "num_processes": num_processes},
        {"type": "reading_2_problem_type_7", "difficulty": "Normal", "num_problems": 2, "num_processes": num_processes},
        {"type": "reading_2_problem_type_8", "difficulty": "Normal", "num_problems": 1, "num_processes": 1},
        {"type": "reading_2_problem_type_9", "difficulty": "Normal", "num_problems": 3, "num_processes": num_processes},
        {"type": "reading_2_problem_type_10", "difficulty": "Normal", "num_problems": 3, "num_processes": num_processes},
        {"type": "reading_2_problem_type_11", "difficulty": "Normal", "num_problems": 4, "num_processes": num_processes},
        {"type": "reading_2_problem_type_12", "difficulty": "Normal", "num_problems": 3, "num_processes": num_processes},
        {"type": "reading_2_problem_type_13", "difficulty": "Normal", "num_problems": 1, "num_processes": 1},
        {"type": "reading_2_problem_type_14", "difficulty": "Normal", "num_problems": 1, "num_processes": 1},
        {"type": "reading_2_problem_type_15", "difficulty": "Normal", "num_problems": 1, "num_processes": 1},
        {"type": "reading_2_problem_type_16", "difficulty": "Normal", "num_problems": 1, "num_processes": 1},
    ]
    exam_generator = ExamGenerator(exam_config, exam_type="Reading", verbose=False)
    exam = await exam_generator.generate()
    
    base_path = '/home/shinbg/topik-korea-data/data/reading_2_v2/problem_data'
    #making file path base current time
    file_path = f"generate_exam_{time.strftime('%Y%m%d_%H%M')}.json"
    file_path = f"{base_path}/{file_path}"
    
    with open(file_path, 'w') as outfile:
        json.dump(exam, outfile, indent=4, ensure_ascii=False)


if __name__ == "__main__":
    asyncio.run(main_generate_reading_2())