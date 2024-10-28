# 1. 전체 프로세스
## 1-1. 듣기 파일 다운로드
- 듣기 파일 용량이 크므로 Github에 올라가있지 않은 상황
- 따라서, 듣기 파일 다운로드 코드(downlooad_mp3.py) 실행 필요
## 1-2. 데이터 삽입
- 시험 데이터(repository에 이미 존재)
- 이미지 데이터(repository에 이미 존재)
- 듣기 데이터(1-1에서 생성)

# 2. 가이드(Step by Step)
## 2-1. 듣기 파일 다운로드
- **crawling/code/downlooad_mp3.py** 실행(topik-korea-data repository에 존재) 
- 가끔씩 실행 중에 오류 발생. 그땐 그냥 재실행

## 2-2. 시험 데이터(json) 생성(**optional**)
- 실행 시 나오는 산출물이 이미 repository에 존재
- uuid 초기화하려면 다시 실행
- **crawling/code/to_json.py** 실행
- input: data/csv/test_data.csv(crawl.py 실행 시 생성되는 시험 데이터. repository에 이미 존재), data/img/\*.png(crawl.py 실행 시 생성되는 시험 데이터. repository에 이미 존재)
- output: data/json/\*.json, data/img_uuid/\*.png

## 2-3. 데이터 삽입
- **crawling/code/post_exam_data.py** 실행