## 토픽II 읽기 생성 품질 테스트

### 1,2번 유형1 알맞은 문법 고르기


```python
from Problem_Type_Template.reading_2_problem_type_1 import generate_reading_2_problem_type_1

result = generate_reading_2_problem_type_1(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '그녀의 목소리가 너무 (  ).',
     'selector': ['아름답다', '아름다워서', '아름답지', '아름답게'],
     'answer': '아름답다',
     'eval_answer': '아름답다',
     'eval_explain': '그녀의 목소리가 너무 아름답다.',
     'total_cost': 0.351}



```python
from Problem_Type_Template.reading_2_problem_type_1 import generate_reading_2_problem_type_1

result = generate_reading_2_problem_type_1(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '대학에 입학하기 위해서는 (  ) 한다.',
     'selector': ['열심히 공부해야', '열심히 공부하면서', '열심히 공부하길', '열심히 공부하고'],
     'answer': '열심히 공부해야',
     'eval_answer': '열심히 공부해야',
     'eval_explain': '대학에 입학하기 위해서는 열심히 공부해야 한다.',
     'total_cost': 0.3915}


### 3,4번 유형2 알맞은 유의 문법, 표현 고르기


```python
from Problem_Type_Template.reading_2_problem_type_2 import generate_reading_2_problem_type_2

result = generate_reading_2_problem_type_2(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '<ins>사무실을 확장하고자</ins> 새로운 공간을 찾고 있습니다.',
     'selector': ['사무실을 확장하는 대신에',
      '사무실을 확장하는 반면에',
      '사무실을 확장하는 동시에',
      '사무실을 확장하기 위해서'],
     'answer': '사무실을 확장하기 위해서',
     'eval_answer': '사무실을 확장하기 위해서',
     'total_cost': 12.959999999999999}



```python
from Problem_Type_Template.reading_2_problem_type_2 import generate_reading_2_problem_type_2

result = generate_reading_2_problem_type_2(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '길에 쓰레기를 <ins>버린 탓에</ins> 환경이 오염되었습니다.',
     'selector': ['버린 후에', '버린 덕분에', '버린 바람에', '버린 대신에'],
     'answer': '버린 바람에',
     'eval_answer': '버린 바람에',
     'eval_explain': '',
     'total_cost': 12.068999999999999}


### 5-8번 유형3 광고,소재(그림)


```python
from Problem_Type_Template.reading_2_problem_type_3 import generate_reading_2_problem_type_3

result = generate_reading_2_problem_type_3(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': 'https://topikkorea.s3.amazonaws.com/topik_image/495e6f86-23ef-11ef-9c42-e5a43cd400fd.png',
     'selector': ['여행 일정', '자연 보호', '기상 정보', '공원 설명'],
     'answer': '자연 보호',
     'eval_answer': '자연 보호',
     'eval_explain': '',
     'total_cost': 0.216}



```python
from Problem_Type_Template.reading_2_problem_type_3 import generate_reading_2_problem_type_3

result = generate_reading_2_problem_type_3(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': 'https://topikkorea.s3.amazonaws.com/topik_image/77f21c81-23ef-11ef-b290-e5a43cd400fd.png',
     'selector': ['소방 안전', '이웃돕기', '웰니스', '환경 보전'],
     'answer': '환경 보전',
     'eval_answer': '환경 보전',
     'eval_explain': '',
     'total_cost': 0.22949999999999998}


### 9-12번 유형4 같은 것 고르기(글,도표,그래프)


```python
from Problem_Type_Template.reading_2_problem_type_4_1 import generate_reading_2_problem_type_4_1

result = generate_reading_2_problem_type_4_1(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '십일은 한국의 전통 명절 중 하나로, 주로 가을에 수확을 기념하고 조상에게 감사하는 의미를 담고 있다. 이 날은 가족들이 모여 함께 음식을 나누고, 조상에게 차례를 지내며, 풍요로운 수확을 기원하는 다양한 놀이를 즐긴다. 특히, 송편, 전, 그리고 여러 가지 전통 음식이 준비되며, 이때의 음식은 가족의 정을 더욱 깊게 만들어 준다.',
     'selector': ['십일은 주로 친구들과 함께 보내는 날이다.',
      '십일은 여름에 열리는 명절이다.',
      '십일은 조상을 기리는 날이다.',
      '십일에는 주로 고기를 많이 먹는다.'],
     'answer': '십일은 조상을 기리는 날이다.',
     'eval_answer': '십일은 조상을 기리는 날이다.',
     'eval_explain': '',
     'total_cost': 0.9450000000000001}



```python
from Problem_Type_Template.reading_2_problem_type_4_2 import generate_reading_2_problem_type_4_2

result = generate_reading_2_problem_type_4_2(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': 'https://topikkorea.s3.amazonaws.com/topik_image/52ca06a0-23ef-11ef-b1bf-e5a43cd400fd.png',
     'selector': ['동아리 신청은 1년간 지속된다.',
      '동아리는 오직 고등학생들로 이루어져야 한다.',
      '동아리 활동비는 최대 이백만 원까지 지원받을 수 있다.',
      '동아리 회원 수가 두세 명일 때도 신청이 가능하다.'],
     'answer': '동아리 활동비는 최대 이백만 원까지 지원받을 수 있다.',
     'eval_answer': '동아리 활동비는 최대 이백만 원까지 지원받을 수 있다.',
     'eval_explain': '',
     'total_cost': 0.36450000000000005}


### 13-15번 유형5 순서배열


```python
from Problem_Type_Template.reading_2_problem_type_5 import generate_reading_2_problem_type_5

result = generate_reading_2_problem_type_5(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '(가) 그들은 매일 아침 밝은 날씨에 감사하며 시작하는 습관을 기른다. (나) 이렇게 작은 기쁨을 소중히 여기는 삶이 그들에게 큰 행복을 가져다주고 있다. (다) 주말에는 가족과 친구들과 소중한 시간을 보내며 소소한 행복을 나눈다. (라) 작은 일상 속에서도 행복을 찾는 사람들이 늘어나고 있다.',
     'selector': ['(나) - (라) - (가) - (다)',
      '(라) - (가) - (다) - (나)',
      '(라) - (다) - (나) - (가)',
      '(다) - (나) - (라) - (가)'],
     'answer': '(라) - (가) - (다) - (나)',
     'eval_answer': '(라) - (가) - (다) - (나)',
     'eval_explain': '작은 일상 속에서도 행복을 찾는 사람들이 늘어나고 있다.  \n그들은 매일 아침 밝은 날씨에 감사하며 시작하는 습관을 기른다.  \n주말에는 가족과 친구들과 소중한 시간을 보내며 소소한 행복을 나눈다.  \n이렇게 작은 기쁨을 소중히 여기는 삶이 그들에게 큰 행복을 가져다주고 있다.',
     'total_cost': 0.6345000000000001}



```python
from Problem_Type_Template.reading_2_problem_type_5 import generate_reading_2_problem_type_5

result = generate_reading_2_problem_type_5(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '(가) 시험 결과는 학생들의 진로와 앞으로의 학업에 큰 영향을 미치기 때문에 매우 중요하다. (나) 학생들은 시험을 준비하기 위해 다양한 학습 방법을 활용한다. (다) 시험은 학생들에게 중요한 평가의 수단이다. (라) 시험 당일에는 긴장과 불안이 많은 학생들에게 영향을 준다.',
     'selector': ['(가) - (라) - (나) - (다)',
      '(다) - (가) - (라) - (나)',
      '(라) - (가) - (다) - (나)',
      '(다) - (나) - (라) - (가)'],
     'answer': '(다) - (나) - (라) - (가)',
     'eval_answer': '(다) - (나) - (라) - (가)',
     'eval_explain': '시험은 학생들에게 중요한 평가의 수단이다.  \n학생들은 시험을 준비하기 위해 다양한 학습 방법을 활용한다.  \n시험 당일에는 긴장과 불안이 많은 학생들에게 영향을 준다.  \n시험 결과는 학생들의 진로와 앞으로의 학업에 큰 영향을 미치기 때문에 매우 중요하다.',
     'total_cost': 0.5535}


### 16-22번 유형6 빈칸채우기


```python
from Problem_Type_Template.reading_2_problem_type_6 import generate_reading_2_problem_type_6

result = generate_reading_2_problem_type_6(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '분리수거는 환경 보호와 자원 재활용을 위해 매우 중요한 과정이다. 많은 도시에서는 (  ) 제시하여 주민들이 올바르게 분리수거를 할 수 있도록 돕고 있다. 종이, 플라스틱, 유리, 금속 등 각 쓰레기 종류에 맞는 전용 통을 두어 시민들이 쉽게 구분할 수 있도록 하고, 이를 통해 재활용률을 높이려는 노력을 기울이고 있다.',
     'selector': ['쓰레기 분리 기준을 명확히', '분리 배출 시간에 맞춰', '재활용 산업을 조정해', '위험 물질을 분리해'],
     'answer': '쓰레기 분리 기준을 명확히',
     'eval_answer': '쓰레기 분리 기준을 명확히',
     'eval_explain': '분리수거는 환경 보호와 자원 재활용을 위해 매우 중요한 과정이다. 많은 도시에서는 쓰레기 분리 기준을 명확히 제시하여 주민들이 올바르게 분리수거를 할 수 있도록 돕고 있다. 종이, 플라스틱, 유리, 금속 등 각 쓰레기 종류에 맞는 전용 통을 두어 시민들이 쉽게 구분할 수 있도록 하고, 이를 통해 재활용률을 높이려는 노력을 기울이고 있다.',
     'total_cost': 0.81}



```python
from Problem_Type_Template.reading_2_problem_type_6 import generate_reading_2_problem_type_6

result = generate_reading_2_problem_type_6(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '약물은 증상 완화나 질병 치료를 위해 인체에 사용하는 화학적 물질이다. 그러나 일부 약물은 의사 처방 없이 사용할 수 없는 경우가 많으며, 오용하거나 남용할 경우 심각한 부작용이나 중독을 초래할 수 있다. 특히, (  )와 같은 특정 약물은 법적으로 엄격히 규제되며, 이를 무단으로 소지하거나 판매하는 것은 범죄로 간주된다. 따라서 약물을 사용할 때는 항상 전문가의 조언을 받는 것이 중요하다.',
     'selector': ['식이 보충제', '일반 의약품', '비타민', '마약류'],
     'answer': '마약류',
     'eval_answer': '마약류',
     'eval_explain': '약물은 증상 완화나 질병 치료를 위해 인체에 사용하는 화학적 물질이다. 그러나 일부 약물은 의사 처방 없이 사용할 수 없는 경우가 많으며, 오용하거나 남용할 경우 심각한 부작용이나 중독을 초래할 수 있다. 특히, 마약류와 같은 특정 약물은 법적으로 엄격히 규제되며, 이를 무단으로 소지하거나 판매하는 것은 범죄로 간주된다. 따라서 약물을 사용할 때는 항상 전문가의 조언을 받는 것이 중요하다.',
     'total_cost': 0.837}



```python
from Problem_Type_Template.reading_2_problem_type_6 import generate_reading_2_problem_type_6

result = generate_reading_2_problem_type_6(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '카페는 사람들이 모여서 대화하고, 쉬는 공간으로 많이 이용된다. 특히, 카페의 (  ) 덕분에 손님들은 편안하게 시간을 보낼 수 있다. 많은 카페에서는 커피뿐만 아니라 다양한 디저트와 음료를 제공하여 손님들의 선택 폭을 넓혀준다. 이와 같은 요소들은 카페가 사람들의 일상 속에서 중요한 역할을 하도록 만든다.',
     'selector': ['아늑한 분위기와 다양한 메뉴',
      '일상적인 대화를 나누기 위해 방문하는 곳',
      '음료만 제공하는 곳',
      '항상 붐비는 공간'],
     'answer': '아늑한 분위기와 다양한 메뉴',
     'eval_answer': '아늑한 분위기와 다양한 메뉴',
     'eval_explain': '카페는 사람들이 모여서 대화하고, 쉬는 공간으로 많이 이용된다. 특히, 카페의 아늑한 분위기와 다양한 메뉴 덕분에 손님들은 편안하게 시간을 보낼 수 있다. 많은 카페에서는 커피뿐만 아니라 다양한 디저트와 음료를 제공하여 손님들의 선택 폭을 넓혀준다. 이와 같은 요소들은 카페가 사람들의 일상 속에서 중요한 역할을 하도록 만든다.',
     'total_cost': 0.918}


### 23-26번 유형7 빈칸+같은것 고르기


```python
from Problem_Type_Template.reading_2_problem_type_7 import generate_reading_2_problem_type_7

result = generate_reading_2_problem_type_7(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '겨울이 다가오면서 많은 사람들이 따뜻한 옷차림으로 거리를 나선다. 눈이 내리면 아이들은 눈사람을 만들고 눈싸움을 하며 즐거운 시간을 보낸다. 그러나 겨울철에는 감기와 독감이 유행하기 때문에 건강 관리에 더욱 신경 (  ). 따뜻한 차 한 잔과 함께 책을 읽는 것도 겨울의 매력을 느끼는 좋은 방법이다. 또한, 겨울철에는 다양한 축제와 이벤트가 열려 사람들을 끌어모은다. 이처럼 겨울은 차가운 날씨 속에서도 따뜻한 추억을 만들어주는 특별한 계절이다.',
     'questions': [{'question': '(  ) 에 들어갈 알맞은 말을 고르십시오.',
       'selector': ['써야 한다.', '먹어야 한다.', '마셔야 한다.', '입어야 한다.'],
       'answer': '써야 한다.',
       'eval_answer': '써야 한다.',
       'eval_explain': '문맥상 겨울철에는 감기와 독감이 유행하기 때문에 건강 관리에 더욱 신경 써야 한다는 의미가 가장 적절하다.'},
      {'question': '이 글의 내용과 알맞은 것을 고르십시오.',
       'selector': ['겨울철에는 감기와 독감이 유행하지 않는다.',
        '겨울에는 따뜻한 옷차림을 하지 않아도 된다.',
        '겨울철에는 다양한 축제와 이벤트가 열린다.',
        '겨울에는 아이들이 눈사람을 만들지 않는다.'],
       'answer': '겨울철에는 다양한 축제와 이벤트가 열린다.',
       'eval_answer': '겨울철에는 다양한 축제와 이벤트가 열린다.',
       'eval_explain': '본문에서 "겨울철에는 다양한 축제와 이벤트가 열려 사람들을 끌어모은다"는 내용이 있으므로, 이 선택지가 올바르게 서술된 문장이다.'}],
     'total_cost': 29.916}



```python
from Problem_Type_Template.reading_2_problem_type_7 import generate_reading_2_problem_type_7

result = generate_reading_2_problem_type_7(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '식사는 우리 몸에 필요한 에너지를 공급하는 중요한 활동이다. 일반적으로 하루에 세 끼를 먹는 것이 권장되지만, 개인의 생활 패턴에 따라 다를 수 있다. 균형 잡힌 식사는 단백질, 탄수화물, 지방, 비타민, 미네랄을 (  ). 또한, 식사 시간은 가족이나 친구와의 소통의 기회가 되기도 한다. 최근에는 건강을 고려한 다양한 식사 방법이 인기를 끌고 있다. 예를 들어, 간헐적 단식이나 비건 식단 등이 많은 사람들에게 주목받고 있다.',
     'questions': [{'question': '(  ) 에 들어갈 알맞은 말을 고르십시오.',
       'selector': ['포함해야 한다', '피해야 한다', '무시해야 한다', '제한해야 한다'],
       'answer': '포함해야 한다',
       'eval_answer': '포함해야 한다',
       'eval_explain': '균형 잡힌 식사는 단백질, 탄수화물, 지방, 비타민, 미네랄을 포함해야 한다는 의미가 문맥에 맞다.'},
      {'question': '이 글의 내용과 알맞은 것을 고르십시오.',
       'selector': ['식사는 우리 몸에 불필요한 에너지를 공급한다.',
        '균형 잡힌 식사는 단백질과 탄수화물을 피해야 한다.',
        '일반적으로 하루에 세 끼를 먹는 것이 권장된다.',
        '최근에는 건강을 고려하지 않은 식사 방법이 인기를 끌고 있다.'],
       'answer': '일반적으로 하루에 세 끼를 먹는 것이 권장된다.',
       'eval_answer': '일반적으로 하루에 세 끼를 먹는 것이 권장된다.',
       'eval_explain': '본문에서 "일반적으로 하루에 세 끼를 먹는 것이 권장되지만"이라는 문장이 있으므로, 이 선택지가 올바르다.'}],
     'total_cost': 28.323}


### 27-28번 유형8 심정+같은 것 고르기


```python
from Problem_Type_Template.reading_2_problem_type_8 import generate_reading_2_problem_type_8

result = generate_reading_2_problem_type_8(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '내가 맡은 프로젝트의 진행 상황을 점검하기 위해 팀원들과 회의를 가졌다. 각자의 의견이 오가며 분위기는 한층 뜨거워졌다. 그러나 그들의 목소리 중 일부는 나를 깎아내리는 듯한 기분이 들었다. 내가 그들을 이끌어야 할 위치에 있지만, 가끔은 서먹한 관계가 큰 장벽처럼 느껴졌다. 시간이 지남에 따라 팀원들이 서로의 아이디어에 날카롭게 반응하는 것을 보며, 앞으로 어떻게 나아가야 할지 고민이 깊어졌다. <ins>과연 내가 충분히 그들을 믿고 이끌 수 있을까?</ins> 회의가 끝난 후에도 그들의 시선이 마음에 걸렸다. 나는 그들 곁에서 더 많은 지지를 받아야 할 것 같다는 생각이 머리를 떠나지 않았다. 오늘의 회의가 우리 진전을 막고 있는 게 아닐까 하는 불안함이 스쳐 지나갔다.',
     'questions': [{'question': '밑줄 친 부분에 나타난 ‘나’의 심정으로 가장 알맞은 것을 고르십시오.',
       'selector': ['불안하다', '확신하다', '안정적이다', '편안하다'],
       'answer': '불안하다',
       'eval_answer': '불안하다'},
      {'question': '윗글의 내용과 같은 것을 고르십시오.',
       'selector': ['내가 맡은 프로젝트의 진행 상황을 점검하기 위해 팀원들과 회의를 가졌다.',
        '회의 중 팀원들은 서로의 의견을 무시하고 싸우기만 했다.',
        '나는 팀원들과의 관계가 매우 좋다고 느끼고 있다.',
        '회의가 끝난 후 모든 팀원들이 나를 칭찬해 주었다.'],
       'answer': '내가 맡은 프로젝트의 진행 상황을 점검하기 위해 팀원들과 회의를 가졌다.',
       'eval_answer': '내가 맡은 프로젝트의 진행 상황을 점검하기 위해 팀원들과 회의를 가졌다.'}],
     'total_cost': 1.107}



```python
from Problem_Type_Template.reading_2_problem_type_8 import generate_reading_2_problem_type_8

result = generate_reading_2_problem_type_8(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '오랜 시간 동안 준비해온 발표의 날이 다가왔다. 아침부터 설레는 마음에 손에 땀을 쥐었다. 준비한 자료를 정리하고, 거울 앞에서 거듭 연습을 했다. 마침내 시간이 되어 무대에 올라서니 관객의 눈빛이 나를 향하고 있었다. 한순간 모든 것이 정지되는 듯했다. 깊은 숨을 들어 마시고, 첫 문장을 떨리는 목소리로 시작했다. 점차 내 말이 힘을 얻으며 흐르고, 잊지 못할 순간이 펼쳐졌다. 발표가 끝나고 박수를 받는 그 순간, 모든 긴장이 풀린 듯했다. 발끝에서부터 차오르는 기쁨이 가득 차올랐다. <ins>마침내 나는 내가 꿈꿔온 정점에 서 있는 것 같았다.</ins>',
     'questions': [{'question': '밑줄 친 부분에 나타난 ‘나’의 심정으로 가장 알맞은 것을 고르십시오.',
       'selector': ['자랑스럽다', '우울하다', '불안하다', '지루하다'],
       'answer': '자랑스럽다',
       'eval_answer': '자랑스럽다'},
      {'question': '윗글의 내용과 같은 것을 고르십시오.',
       'selector': ['발표가 끝나고 박수를 받는 그 순간, 모든 긴장이 풀린 듯했다.',
        '나는 발표를 준비하는 데 많은 시간을 투자했다.',
        '무대에 서기 전, 나는 긴장감을 느끼지 않았다.',
        '관객의 반응은 나에게 큰 영향을 미치지 않았다.'],
       'answer': '발표가 끝나고 박수를 받는 그 순간, 모든 긴장이 풀린 듯했다.',
       'eval_answer': '발표가 끝나고 박수를 받는 그 순간, 모든 긴장이 풀린 듯했다.'}],
     'total_cost': 1.0665}


### 29-30번 유형9 신문 기사 제목 고르기


```python
from Problem_Type_Template.reading_2_problem_type_9 import generate_reading_2_problem_type_9

result = generate_reading_2_problem_type_9(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '주목받는 시선의 힘, 사회 변화 이끌다',
     'selector': ['시선이 사회적 이슈에 대한 인식을 변화시키고 있다.',
      '시선은 사람들 간의 관계에 큰 영향을 미치지 않는다.',
      '사회 변화는 시선과는 무관하게 이루어지고 있다.',
      '시선의 변화는 개인의 감정에만 영향을 준다.'],
     'answer': '시선이 사회적 이슈에 대한 인식을 변화시키고 있다.',
     'eval_answer': '시선이 사회적 이슈에 대한 인식을 변화시키고 있다.',
     'total_cost': 0.324}



```python
from Problem_Type_Template.reading_2_problem_type_9 import generate_reading_2_problem_type_9

result = generate_reading_2_problem_type_9(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '도시 거리, 새로운 문화 공간으로 탈바꿈',
     'selector': ['도시의 거리가 다양한 문화 행사로 가득 차고 있다.',
      '거리는 여전히 교통 혼잡으로 문제가 되고 있다.',
      '거리의 안전 문제로 인해 시민들이 불안해하고 있다.',
      '도시 거리의 변화는 경제에 큰 영향을 미치지 않는다.'],
     'answer': '도시의 거리가 다양한 문화 행사로 가득 차고 있다.',
     'eval_answer': '도시의 거리가 다양한 문화 행사로 가득 차고 있다.',
     'total_cost': 0.324}


### 32-34번 유형10 같은 것 고르기


```python
from Problem_Type_Template.reading_2_problem_type_10 import generate_reading_2_problem_type_10

result = generate_reading_2_problem_type_10(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '문턱은 집이나 방의 입구에 위치한 부분으로, 바닥과 문틀 사이를 연결해주는 역할을 한다. 문턱은 일반적으로 높은 곳에서 낮은 곳으로 이동할 때 발생하는 낙차를 줄이고, 외부의 먼지나 물이 실내로 들어오는 것을 방지하는 기능을 가지고 있다. 또한 문턱은 전통적으로 집의 경계를 나타내는 상징적인 의미도 지니며, 집안과 집밖을 구분짓는 중요한 요소로 여겨진다.',
     'selector': ['문턱은 집안과 집밖을 구분짓는 역할을 한다.',
      '문턱은 집에 들어가기 위해 반드시 넘어야 하는 장애물이다.',
      '문턱은 외부의 먼지나 물이 실내로 들어오는 것을 방지한다.',
      '문턱은 문틀과 바닥을 연결해주는 기능이 없다.'],
     'answer': '문턱은 외부의 먼지나 물이 실내로 들어오는 것을 방지한다.',
     'eval_answer': '문턱은 외부의 먼지나 물이 실내로 들어오는 것을 방지한다.',
     'eval_explain': '',
     'total_cost': 1.161}



```python
from Problem_Type_Template.reading_2_problem_type_10 import generate_reading_2_problem_type_10

result = generate_reading_2_problem_type_10(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '재판은 법원이 사건에 대한 판결을 내리는 과정으로, 공정한 법의 적용을 통해 사회 정의를 실현하는 중요한 역할을 한다. 재판은 일반적으로 원고와 피고가 법정에서 증거를 제시하고, 법관이 이를 검토하여 판결을 내리는 방식으로 진행된다. 재판의 종류에는 형사 재판과 민사 재판이 있으며, 형사 재판은 범죄에 대한 처벌을 다루고, 민사 재판은 개인 간의 분쟁을 해결하기 위한 것이다. 또한 재판은 공개적으로 진행되며, 모든 시민이 그 과정을 지켜볼 수 있는 권리를 가진다.',
     'selector': ['재판은 원고와 피고가 법정에서 증거를 제시하는 방식으로 진행된다.',
      '재판은 비공식적으로 진행되며 시민이 지켜볼 수 없다.',
      '민사 재판은 범죄에 대한 처벌을 다루는 재판이다.',
      '재판은 법원이 사건을 심리하지 않는 과정이다.'],
     'answer': '재판은 원고와 피고가 법정에서 증거를 제시하는 방식으로 진행된다.',
     'eval_answer': '재판은 원고와 피고가 법정에서 증거를 제시하는 방식으로 진행된다.',
     'eval_explain': '',
     'total_cost': 1.323}


### 35-38번 유형11 주제 찾기


```python
from Problem_Type_Template.reading_2_problem_type_11 import generate_reading_2_problem_type_11

result = generate_reading_2_problem_type_11(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '신뢰는 인간 관계의 기초가 되며, 개인과 사회의 발전에 필수적인 요소로 여겨진다. 신뢰가 형성되면 사람들은 서로를 더 잘 이해하고 협력하게 된다. 그러나 신뢰는 쉽게 형성되지 않으며, 한번 깨지면 회복하기 어려운 경우가 많다. 따라서 신뢰를 쌓기 위해서는 정직하고 일관된 행동이 필요하다. 신뢰가 있는 관계는 갈등을 줄이고, 소통을 원활하게 하여 긍정적인 결과를 이끌어낸다.',
     'selector': ['신뢰는 일관된 행동으로 쉽게 쌓을 수 있다.',
      '신뢰가 없는 관계는 소통이 어려워진다.',
      '신뢰는 인간 관계의 발전에 있어 부차적인 요소이다.',
      '신뢰를 쌓기 위해서는 겉으로만 정직한 태도가 필요하다.'],
     'answer': '신뢰가 없는 관계는 소통이 어려워진다.',
     'eval_answer': '신뢰가 없는 관계는 소통이 어려워진다.',
     'eval_explain': '',
     'total_cost': 1.1204999999999998}



```python
from Problem_Type_Template.reading_2_problem_type_11 import generate_reading_2_problem_type_11

result = generate_reading_2_problem_type_11(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '현대 사회에서는 다양한 분야에서 협력이 필수적이다. 특히 기업 간의 협력은 경쟁력을 높이고, 혁신을 촉진하는 중요한 요소로 작용한다. 서로 다른 전문성을 가진 기업들이 협력하여 시너지를 창출함으로써, 시장에서의 지속 가능한 성장을 이루는 것이 가능하다. 따라서 협력의 중요성을 인식하고 이를 적극적으로 추진하는 것이 필요하다.',
     'selector': ['기업 간 협력은 경쟁력을 높이는 데 중요한 역할을 한다.',
      '협력은 기업의 성장에 필요한 요소가 아니다.',
      '협력의 필요성은 현대 사회에서 감소하고 있다.',
      '협력은 단기적인 성과를 추구하는 데만 집중해야 한다.'],
     'answer': '기업 간 협력은 경쟁력을 높이는 데 중요한 역할을 한다.',
     'eval_answer': '기업 간 협력은 경쟁력을 높이는 데 중요한 역할을 한다.',
     'eval_explain': '',
     'total_cost': 1.08}



```python
from Problem_Type_Template.reading_2_problem_type_11 import generate_reading_2_problem_type_11

result = generate_reading_2_problem_type_11(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '형태는 물체나 개체의 구조와 모양을 의미하며, 이는 그 기능과 특성에 큰 영향을 미친다. 예를 들어, 날카로운 형태의 도구는 절삭력에 유리하며, 둥근 형태의 구조물은 압력을 분산시키는 데 효과적이다. 형태는 또한 미적 요소로 작용하여 사람들에게 시각적인 아름다움을 제공하기도 한다. 따라서 형태는 단순한 외형을 넘어서 기능적, 미적 측면에서도 중요한 역할을 한다.',
     'selector': ['모든 형태는 동일한 기능을 수행할 수 있다.',
      '형태는 오직 미적 요소로만 작용한다.',
      '형태는 물체의 기능과 미적 요소에 모두 영향을 미친다.',
      '형태는 물체의 기능을 결정짓는 유일한 요소이다.'],
     'answer': '형태는 물체의 기능과 미적 요소에 모두 영향을 미친다.',
     'eval_answer': '형태는 물체의 기능과 미적 요소에 모두 영향을 미친다.',
     'eval_explain': '',
     'total_cost': 1.2015}


### 35-38번 유형12 문맥에 맞는 위치 고르기


```python
from Problem_Type_Template.reading_2_problem_type_12 import generate_reading_2_problem_type_12

result = generate_reading_2_problem_type_12(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': "[['슬픔은 개인의 내면에서 일어나는 반응으로, 때로는 눈물을 통해 표출되기도 한다.'], ['슬픔은 인간이 느끼는 깊고 복잡한 감정 중 하나이다. (㉠) 이 감정은 종종 상실, 고통, 혹은 실망에서 비롯된다. (㉡) 하지만 슬픔은 단순히 부정적인 감정만이 아니라, 치유와 성장의 과정에서 중요한 역할을 한다. (㉢) 이를 통해 우리는 자신의 감정을 이해하고, 더 나아가 타인과의 공감능력을 키울 수 있다. (㉣) 결국, 슬픔은 우리 삶의 일부분으로, 이를 받아들이고 극복하는 과정이 중요한 의미를 가진다.']]",
     'selector': ['㉠', '㉡', '㉢', '㉣'],
     'answer': '㉡',
     'eval_answer': '㉡',
     'eval_explain': '슬픔은 인간이 느끼는 깊고 복잡한 감정 중 하나이다. 이 감정은 종종 상실, 고통, 혹은 실망에서 비롯된다. 슬픔은 개인의 내면에서 일어나는 반응으로, 때로는 눈물을 통해 표출되기도 한다. 하지만 슬픔은 단순히 부정적인 감정만이 아니라, 치유와 성장의 과정에서 중요한 역할을 한다. 이를 통해 우리는 자신의 감정을 이해하고, 더 나아가 타인과의 공감능력을 키울 수 있다. 결국, 슬픔은 우리 삶의 일부분으로, 이를 받아들이고 극복하는 과정이 중요한 의미를 가진다.',
     'total_cost': 0.9585}



```python
from Problem_Type_Template.reading_2_problem_type_12 import generate_reading_2_problem_type_12

result = generate_reading_2_problem_type_12(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': "[['또한, 과일은 수분 함량이 높아 체내 수분을 보충하는 데 효과적이다.'], ['과일은 건강에 좋은 영양소가 풍부한 식품이다. (㉠) 다양한 비타민과 미네랄이 포함되어 있어 면역력을 강화하는 데 도움을 준다. (㉡) 특히, 과일 속의 식이섬유는 소화에 유익하고 장 건강을 촉진하는 역할을 한다. (㉢) 각기 다른 색과 맛을 가진 과일들은 식단에 다양성을 더해준다. (㉣) 따라서 매일 과일을 섭취하는 것은 건강한 생활습관을 유지하는 데 중요한 요소라고 할 수 있다.']]",
     'selector': ['㉠', '㉡', '㉢', '㉣'],
     'answer': '㉢',
     'eval_answer': '㉢',
     'eval_explain': '과일은 건강에 좋은 영양소가 풍부한 식품이다. 다양한 비타민과 미네랄이 포함되어 있어 면역력을 강화하는 데 도움을 준다. 특히, 과일 속의 식이섬유는 소화에 유익하고 장 건강을 촉진하는 역할을 한다. 또한, 과일은 수분 함량이 높아 체내 수분을 보충하는 데 효과적이다. 각기 다른 색과 맛을 가진 과일들은 식단에 다양성을 더해준다. 따라서 매일 과일을 섭취하는 것은 건강한 생활습관을 유지하는 데 중요한 요소라고 할 수 있다.',
     'total_cost': 0.945}



```python
from Problem_Type_Template.reading_2_problem_type_12 import generate_reading_2_problem_type_12

result = generate_reading_2_problem_type_12(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': "[['또한, 기초 문장은 외국어를 배우는 데에도 필수적이다.'], ['기초 문장은 의사소통의 가장 기본적인 단위로, 사람들의 생각이나 감정을 전달하는 데 중요한 역할을 한다. (㉠) 간단한 주어와 서술어로 구성되어 있어 누구나 쉽게 이해할 수 있다. (㉡) 이러한 기초 문장은 어린이들이 언어를 배우는 첫 단계에서도 중요한 요소로 작용한다. (㉢) 기초 문장을 통해 사람들은 점차 복잡한 문장 구조를 익히게 된다. (㉣) 이를 통해 언어의 기초를 다지고 더 나아가 다양한 표현을 시도할 수 있는 토대를 마련하게 된다.']]",
     'selector': ['㉠', '㉡', '㉢', '㉣'],
     'answer': '㉣',
     'eval_answer': '㉣',
     'eval_explain': '기초 문장은 의사소통의 가장 기본적인 단위로, 사람들의 생각이나 감정을 전달하는 데 중요한 역할을 한다. 간단한 주어와 서술어로 구성되어 있어 누구나 쉽게 이해할 수 있다. 이러한 기초 문장은 어린이들이 언어를 배우는 첫 단계에서도 중요한 요소로 작용한다. 기초 문장을 통해 사람들은 점차 복잡한 문장 구조를 익히게 된다. 또한, 기초 문장은 외국어를 배우는 데에도 필수적이다. 이를 통해 언어의 기초를 다지고 더 나아가 다양한 표현을 시도할 수 있는 토대를 마련하게 된다.',
     'total_cost': 0.9045000000000001}


### 42-43번 유형13 소설


```python
from Problem_Type_Template.reading_2_problem_type_13 import generate_reading_2_problem_type_13

result = generate_reading_2_problem_type_13(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '한적한 도서관의 구석에서, 로하와 미나는 조용히 책을 읽고 있었다. 날카로운 시선으로 스크롤하던 로하가 말문을 열었다. "미나, 이 책에서 성적에 대한 관점이 정말 흥미롭지 않아?" 미나는 살짝 미소를 지으며 고개를 끄덕였다. "응, 특히 그 부분에서 성적 정체성이 어떻게 형성되는지에 대한 연구가 마음에 들어."\n\n로하는 수줍게 고개를 숙이며 말했다. "<ins>나는 여전히 나의 성적 정체성이 혼란스럽고, 정체성을 찾는 과정이 힘들기도 해.</ins>" 미나는 이해하는 듯한 표정으로 대답했다. "그런 감정이 정말 복잡한 것 같아. 하지만 우리는 서로를 이해할 수 있을 것 같아." \n\n갑자기 도서관의 고요함을 깨고, 어딘가에서 아이들이 놀며 시끄럽게 떠드는 소리가 들려왔다. 로하는 괜히 마음이 불안해지며 중얼거렸다. "세상은 우리에게 다양한 시선을 요구하는 것 같아." 미나는 그를 바라보며 진지하게 말했다. "그렇지만 우리는 각자의 길을 걸어가야 해. 그게 우리에게 주어진 삶이니까."\n\n조용하던 도서관 안에서 두 친구는 서로의 고민을 나누며, 더욱 깊은 유대감을 느꼈다. 이렇게 서로의 이야기를 나누는 것이 자신에게 필요한 힘이 됨을 깨달았다. 미나는 마음속으로 다짐했다. "이런 대화를 통해 나의 성적 정체성도 조금씩 밝혀질 거라고 믿어." 로하의 눈빛은 더욱 진지해졌다. "우리는 언젠가 각자의 자리에서 빛날 거야."',
     'questions': [{'question': '밑줄 친 부분에 나타난 ‘로하’의 심정으로 가장 알맞은 것을 고르십시오.',
       'selector': ['안정적이다', '혼란스럽다', '무관심하다', '기쁘다'],
       'answer': '혼란스럽다',
       'eval_answer': '혼란스럽다'},
      {'question': '윗글의 내용으로 알 수 있는 것을 고르십시오.',
       'selector': ['로하와 미나는 도서관에서 성적 정체성에 대해 깊이 있는 대화를 나누었다.',
        '두 친구는 도서관에서 만난 것이 아니라 카페에서 대화를 나누었다.',
        '로하는 도서관에서 책을 읽는 대신 친구들과 놀고 싶어 했다.',
        '미나는 로하의 말을 무시하고 다른 주제로 이야기를 시작했다.'],
       'answer': '로하와 미나는 도서관에서 성적 정체성에 대해 깊이 있는 대화를 나누었다.',
       'eval_answer': '로하와 미나는 도서관에서 성적 정체성에 대해 깊이 있는 대화를 나누었다.'}],
     'total_cost': 1.836}



```python
from Problem_Type_Template.reading_2_problem_type_13 import generate_reading_2_problem_type_13

result = generate_reading_2_problem_type_13(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '한적한 도서관의 구석, 아름다운 책들이 나란히 서 있었다. 소영은 그곳에서 희망을 찾으려 애썼다. "이 책들 속엔 내가 모르는 세상이 가득해," 그녀는 혼잣말처럼 중얼거렸다. \n\n그녀의 친구 지훈은 소영의 옆에 앉아 책장을 넘기며 말했다. "정말 좋다, 이렇게 조용한 곳에서 책을 읽는 건." 그들은 오래된 책의 향기에 감동받고 있었다. \n\n소영은 한 순간, 자신이 이 책을 읽고 소설 속 주인공처럼 될 수 있을 것 같은 착각을 느꼈다. "읽다 보면, 나도 언젠가 이 이야기에 녹아들 수 있을까?" 그녀의 마음은 설렘과 두려움으로 복잡했다. \n\n지훈은 그런 소영을 보며 미소 지었다. "괜찮아, 우린 함께 이旅를 떠나고 있어." 그들은 책과 함께 서로의 꿈과 아픔을 나누며 점점 가까워졌다. \n\n소영은 속마음을 털어놓았다. "<ins>내가 원하는 삶은 이 책 속에 있어…</ins>" 그녀는 눈을 감고 그 세계에 빠져들던 순간, 더 많은 것을 찾고 싶었다. 두 친구는 더욱 깊이 있는 관계로 나아가고 있었다. "우리, 책과 함께 성장할 수 있을 거야."',
     'questions': [{'question': '밑줄 친 부분에 나타난 ‘소영’의 심정으로 가장 알맞은 것을 고르십시오.',
       'selector': ['지루하다', '무기력하다', '질투스럽다', '갈망한다'],
       'answer': '갈망한다',
       'eval_answer': '갈망한다'},
      {'question': '윗글의 내용으로 알 수 있는 것을 고르십시오.',
       'selector': ['두 친구는 도서관에서 책을 읽는 대신 음악을 듣고 있었다.',
        '지훈은 소영에게 책 대신 영화에 대해 이야기했다.',
        '소영은 도서관에서 책을 읽으며 새로운 세상을 꿈꾸고 있었다.',
        '소영은 도서관에서 친구와 함께 게임을 하고 있었다.'],
       'answer': '소영은 도서관에서 책을 읽으며 새로운 세상을 꿈꾸고 있었다.',
       'eval_answer': '소영은 도서관에서 책을 읽으며 새로운 세상을 꿈꾸고 있었다.'}],
     'total_cost': 1.539}


### 44-45번 유형14 설명문


```python
from Problem_Type_Template.reading_2_problem_type_14 import generate_reading_2_problem_type_14

result = generate_reading_2_problem_type_14(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '효율성은 자원 활용의 최적화를 통해 생산성과 경쟁력을 높이는 중요한 요소로 여겨진다. 기업들은 효율성을 극대화하기 위해 다양한 기술과 프로세스를 도입하고 있으며, 이는 비용 절감과 시간 단축으로 이어진다. 그러나 효율성을 추구하는 과정에서 직원들의 창의성과 혁신성이 저해될 수 있다는 우려도 존재한다. 따라서 효율성과 창의성 간의 균형을 맞추는 것이 필요하다. 이를 위해 기업은 직원들의 의견을 수렴하고, 유연한 근무 (  ). 궁극적으로 효율성은 지속 가능한 성장의 기반이 되어야 하며, 이를 위해 모든 구성원이 함께 노력해야 할 것이다.',
     'questions': [{'question': '(  ) 에 들어갈 가장 알맞은 말을 고르십시오.',
       'selector': ['환경을 조성하는 것이 중요하다.',
        '방안을 마련하는 것이 필요하다.',
        '조건을 강화하는 것이 필요하다.',
        '규칙을 엄격히 적용해야 한다.'],
       'answer': '환경을 조성하는 것이 중요하다.',
       'eval_answer': '환경을 조성하는 것이 중요하다.',
       'eval_explain': '문맥상 "유연한 근무 환경을 조성하는 것이 중요하다"가 가장 자연스럽고 적절하다. 이는 직원들의 창의성과 혁신성을 유지하면서도 효율성을 높이는 방법을 제시하는 것이다.'},
      {'question': '윗 글의 주제로 가장 알맞은 것을 고르십시오.',
       'selector': ['직원들의 창의성과 혁신성이 저해될 수 있다는 우려가 있다.',
        '효율성과 창의성 간의 균형을 맞추는 것이 필요하다.',
        '효율성을 극대화하기 위해 다양한 기술과 프로세스를 도입하고 있다.',
        '궁극적으로 효율성은 지속 가능한 성장의 기반이 되어야 한다.'],
       'answer': '효율성과 창의성 간의 균형을 맞추는 것이 필요하다.',
       'eval_answer': '효율성과 창의성 간의 균형을 맞추는 것이 필요하다.',
       'eval_explain': '주어진 글의 핵심은 효율성을 추구하면서도 창의성과 혁신성을 저해하지 않도록 균형을 맞추는 것이 중요하다는 점이다. 따라서 3번 선택지가 가장 적절한 요약이다.'}],
     'total_cost': 36.72}



```python
from Problem_Type_Template.reading_2_problem_type_14 import generate_reading_2_problem_type_14

result = generate_reading_2_problem_type_14(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '손톱은 우리 몸에서 가장 작은 부분 중 하나이지만, 그 중요성은 결코 (  ). 손톱은 손가락의 끝을 보호하고, 다양한 작업을 수행하는 데 도움을 준다. 또한 손톱의 건강 상태는 전반적인 신체 건강을 반영하기도 한다. 예를 들어, 손톱이 변색되거나 부서지기 쉬운 경우, 이는 영양 부족이나 질병의 신호일 수 있다. 손톱 관리 또한 미용의 일환으로 여겨져, 많은 사람들이 네일 아트를 즐기고 있다. 따라서 손톱은 단순한 신체의 일부가 아니라, 건강과 미의 상징으로 자리 잡고 있다.',
     'questions': [{'question': '(  ) 에 들어갈 가장 알맞은 말을 고르십시오.',
       'selector': ['간과될 수 있다.', '소홀히 다뤄질 수 있다.', '과소평가될 수 없다.', '무시될 수 있다.'],
       'answer': '과소평가될 수 없다.',
       'eval_answer': '과소평가될 수 없다.',
       'eval_explain': '문맥상 손톱의 중요성을 강조하고 있으므로, "과소평가될 수 없다"가 가장 적절하다.'},
      {'question': '윗 글의 주제로 가장 알맞은 것을 고르십시오.',
       'selector': ['손톱의 건강 상태는 미용과는 무관하다.',
        '손톱 관리는 단순히 미용적인 측면에 국한된다.',
        '손톱은 신체 건강과 미용의 중요한 지표이다.',
        '손톱은 손가락의 끝을 보호하는 역할만 한다.'],
       'answer': '손톱은 신체 건강과 미용의 중요한 지표이다.',
       'eval_answer': '손톱은 신체 건강과 미용의 중요한 지표이다.',
       'eval_explain': '본문에서는 손톱이 신체 건강을 반영하고 미용의 일환으로 여겨진다고 설명하고 있으므로, "손톱은 신체 건강과 미용의 중요한 지표이다"가 가장 적절하다.'}],
     'total_cost': 34.587}


### 46-47번 유형15 정보문


```python
from Problem_Type_Template.reading_2_problem_type_15 import generate_reading_2_problem_type_15

result = generate_reading_2_problem_type_15(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '모형은 우리 주변에서 다양한 형태로 존재하며, 현실을 이해하는 데 도움을 줍니다. 교육 분야에서는 모형이 학생들에게 복잡한 개념을 시각적으로 전달하는 중요한 도구로 사용됩니다. 예를 들어, 지구의 자전과 공전을 설명하기 위한 구형 모형은 학생들이 이론을 더 쉽게 grasp할 수 있도록 도와줍니다. 또한, 과학 실험에서는 결과를 예측하거나 분석하기 위한 기본적인 프레임워크로 작용합니다. 그러나 모형이 항상 현실을 완벽하게 반영하지는 않기 때문에, 그 한계를 이해하는 것이 중요합니다. 그러므로 우리는 모형을 단순한 도구로만 보지 말고, 진정한 의미를 찾기 위해 노력해야 합니다. 궁극적으로, 모형은 세상을 바라보는 또 다른 시각을 제공하며, 우리의 사고를 확장하는 데 기여합니다.',
     'questions': [{'question': '윗글에 나타난 필자의 태도로 가장 알맞은 것을 고르십시오.',
       'selector': ['모형이 사고를 확장하는 데 기여하는 점에 감사를 표하고 있다.',
        '모형이 현실을 완벽하게 반영한다고 믿고 있다.',
        '모형의 한계를 이해하고, 그 진정한 의미를 찾으려는 노력이 필요하다고 강조하고 있다.',
        '모형은 교육에서 매우 유용한 도구이며, 그 중요성을 강조하고 있다.'],
       'answer': '모형의 한계를 이해하고, 그 진정한 의미를 찾으려는 노력이 필요하다고 강조하고 있다.',
       'eval_answer': '모형의 한계를 이해하고, 그 진정한 의미를 찾으려는 노력이 필요하다고 강조하고 있다.'},
      {'question': '윗글의 내용과 같은 것을 고르십시오.',
       'selector': ['모형은 세상을 바라보는 시각을 제공하지 않으며, 사고를 확장하는 데 기여하지 않는다.',
        '모형은 현실을 이해하는 데 도움을 주며, 교육 분야에서 복잡한 개념을 시각적으로 전달하는 중요한 도구로 사용된다.',
        '모형은 학생들이 이론을 이해하는 데 필요하지 않은 도구이다.',
        '모형은 항상 현실을 완벽하게 반영하기 때문에 그 한계를 이해할 필요가 없다.'],
       'answer': '모형은 현실을 이해하는 데 도움을 주며, 교육 분야에서 복잡한 개념을 시각적으로 전달하는 중요한 도구로 사용된다.',
       'eval_answer': '모형은 현실을 이해하는 데 도움을 주며, 교육 분야에서 복잡한 개념을 시각적으로 전달하는 중요한 도구로 사용된다.'}],
     'total_cost': 1.2015}



```python
from Problem_Type_Template.reading_2_problem_type_15 import generate_reading_2_problem_type_15

result = generate_reading_2_problem_type_15(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '운동은 신체 건강을 유지하는 데 필수적인 요소이다. 규칙적인 운동은 체력 향상뿐만 아니라 정신적인 스트레스 해소에도 도움을 준다. 현대 사회에서 많은 사람들이 바쁜 일상으로 인해 운동을 소홀히 하고 있지만, 이는 건강에 여러 가지 부정적인 영향을 미칠 수 있다. 또한, 운동은 사회적 활동으로, 친구와 가족과의 유대감을 강화하는 기회가 된다. 다양한 운동 프로그램과 취미 스포츠가 존재하는 만큼, 개인의 취향에 맞는 운동을 찾는 것이 중요하다. 운동을 통해 건강을 유지하는 것은 나뿐만 아니라 주변 사람들에게도 긍정적인 영향을 미치는 일이다. 따라서 우리는 일상 속에서 운동을 습관화하여 보다 건강하고 행복한 삶을 추구해야 한다.',
     'questions': [{'question': '윗글에 나타난 필자의 태도로 가장 알맞은 것을 고르십시오.',
       'selector': ['운동보다 다른 활동이 더 중요하다고 주장하고 있다.',
        '운동의 중요성을 강조하며, 일상에서 운동을 습관화할 필요성을 주장하고 있다.',
        '운동을 통해 사회적 유대감을 강화하는 것에 무관심하다.',
        '운동이 신체 건강에 미치는 긍정적인 영향을 과소평가하고 있다.'],
       'answer': '운동의 중요성을 강조하며, 일상에서 운동을 습관화할 필요성을 주장하고 있다.',
       'eval_answer': '운동의 중요성을 강조하며, 일상에서 운동을 습관화할 필요성을 주장하고 있다.'},
      {'question': '윗글의 내용과 같은 것을 고르십시오.',
       'selector': ['운동은 건강에 부정적인 영향을 미치는 요소이다.',
        '규칙적인 운동은 친구와 가족과의 유대감을 약화시킨다.',
        '운동은 신체 건강 유지와 정신적 스트레스 해소에 필수적이다.',
        '운동을 하는 것은 개인의 취향과 무관하게 중요하지 않다.'],
       'answer': '운동은 신체 건강 유지와 정신적 스트레스 해소에 필수적이다.',
       'eval_answer': '운동은 신체 건강 유지와 정신적 스트레스 해소에 필수적이다.'}],
     'total_cost': 1.08}


### 48-50번 유형16 논설문


```python
from Problem_Type_Template.reading_2_problem_type_16 import generate_reading_2_problem_type_16

result = generate_reading_2_problem_type_16(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '식기는 단순한 도구를 넘어 우리의 식생활과 문화에 깊은 영향을 미치는 중요한 요소이다. 현대 사회에서 식기는 다양한 형태와 기능으로 발전해 왔으며, 이는 우리 식사의 질과 경험을 향상시키는 데 기여하고 있다. 특히, 환경 문제에 대한 사회적 인식이 높아지면서 지속 가능한 식기 사용의 필요성이 대두되고 있다. 일회용 식기 대신 재사용 가능한 식기를 선택함으로써 우리는 환경 보호에 기여할 수 있다. 또한, 아름답고 실용적인 식기는 식탁을 더욱 풍성하게 만들어 주며, 가족 및 친구들과의 소중한 시간을 더욱 특별하게 만들어 준다. 최근에는 전통적인 식기에서 벗어나 다양한 소재와 디자인의 식기가 등장하면서 소비자들의 선택 폭이 넓어졌다. 그러나 이러한 다양한 선택이 무조건 좋은 것은 아니다. 저품질의 식기가 건강에 해로운 (  ) 있으므로, 신뢰할 수 있는 브랜드의 제품을 선택하는 것이 중요하다. 따라서, 우리는 식기의 선택에 있어 품질과 지속 가능성을 동시에 고려해야 한다. 이러한 변화를 통해 우리는 더 나은 식문화를 만들어 나갈 수 있을 것이다. 결국, 식기는 단순한 도구가 아니라 우리의 삶의 질을 높이고, 환경을 지키는 중요한 파트너임을 잊지 말아야 한다.',
     'questions': [{'question': '윗 글을 쓴 목적으로 가장 알맞은 것을 고르십시오.',
       'selector': ['지속 가능한 식기 사용의 필요성을 강조하려고  ',
        '식기의 역사적 변천 과정을 설명하려고  ',
        '다양한 식기 디자인의 트렌드를 소개하려고  ',
        '식기의 기능과 역할을 단순화하려고  '],
       'answer': '지속 가능한 식기 사용의 필요성을 강조하려고  ',
       'eval_answer': '지속 가능한 식기 사용의 필요성을 강조하려고  ',
       'eval_explain': ['이 논설문은 식기가 단순한 도구가 아니라 우리의 식생활과 문화에 중요한 영향을 미친다는 점을 강조하며, 특히 환경 문제에 대한 인식이 높아짐에 따라 지속 가능한 식기 사용의 필요성을 강조하고 있다. 따라서 2번이 올바른 답이다.']},
      {'question': '(  ) 에 들어갈 가장 알맞은 말을 고르십시오.',
       'selector': ['문제가 발생할 수  ', '디자인이 다양해질 수  ', '가격이 상승할 수  ', '영향을 미칠 수  '],
       'answer': '영향을 미칠 수  ',
       'eval_answer': '영향을 미칠 수  ',
       'eval_explain': ["저품질의 식기가 건강에 해로운 영향을 미칠 수 있다는 문맥에서, 빈칸에 들어갈 말은 '영향을 미칠 수'가 가장 적합하다. 저품질 식기가 건강에 부정적인 영향을 줄 수 있다는 점을 강조하고 있기 때문이다."]},
      {'question': '윗 글의 내용과 같은 것을 고르십시오.',
       'selector': ['식기는 우리의 식생활과 문화에 영향을 미치지 않는다.',
        '식기는 단순한 도구로서의 역할만을 한다.',
        '일회용 식기를 사용하는 것이 환경 보호에 기여한다.',
        '저품질의 식기는 건강에 해로운 영향을 미칠 수 있다.'],
       'answer': '저품질의 식기는 건강에 해로운 영향을 미칠 수 있다.  ',
       'eval_answer': '저품질의 식기는 건강에 해로운 영향을 미칠 수 있다.  ',
       'eval_explain': ['논설문에서는 저품질의 식기가 건강에 해로운 영향을 미칠 수 있다고 명시하고 있으며, 이는 식기의 중요성을 강조하는 내용 중 하나이다. 다른 선택지는 식기의 역할이나 환경 보호에 대한 잘못된 정보를 포함하고 있다.']}],
     'total_cost': 5.022}



```python
from Problem_Type_Template.reading_2_problem_type_16 import generate_reading_2_problem_type_16

result = generate_reading_2_problem_type_16(default_model="gpt-4o-mini",verbose=False)

display(result)
```


    {'example': '밥은 우리의 일상에서 없어서는 안 될 중요한 식사이다. 한국을 비롯한 많은 나라에서는 밥이 기본적인 주식으로 자리잡고 있으며, 이는 단순한 음식 이상의 (  ). 첫째, 밥은 영양학적으로도 매우 균형 잡힌 식사로, 탄수화물의 주된 공급원이다. 둘째, 밥은 가족이나 친구와 함께 나누는 소중한 순간을 만들어주는 매개체 역할을 한다. 식사 시간은 서로의 소통과 정을 나누는 중요한 시간으로, 밥이 없었다면 그 시간의 의미가 크게 퇴색했을 것이다.  셋째, 다양한 밥 요리는 각 문화의 정체성을 드러내는 중요한 요소이기도 하다. 예를 들어, 비빔밥은 한국의 전통적인 음식으로 다양한 재료가 조화를 이루어 만들어진다. 넷째, 밥은 단순히 먹는 것을 넘어서, 사람들에게 안정감과 편안함을 제공한다. 특히 힘든 하루를 보낸 후 따뜻한 밥 한 그릇은 많은 이들에게 위안이 된다.  그러나 현대 사회에서는 패스트푸드와 인스턴트 식사의 증가로 인해 밥의 중요성이 간과되고 있는 경향이 있다. 이러한 경향은 건강에 악영향을 미칠 수 있으며, 끼니를 거르거나 불균형한 식사를 초래할 수 있다. 따라서 우리는 밥의 소중함을 다시 한번 깨닫고, 건강한 식습관을 유지해야 한다. 결국 밥은 단순한 음식이 아니라, 우리의 삶과 문화, 관계를 형성하는 중요한 요소임을 잊지 말아야 할 것이다.',
     'questions': [{'question': '윗 글을 쓴 목적으로 가장 알맞은 것을 고르십시오.',
       'selector': ['다양한 밥 요리의 레시피를 소개하기 위해  ',
        '밥의 영양학적 가치를 강조하기 위해  ',
        '현대 사회에서 밥의 중요성을 재조명하기 위해  ',
        '패스트푸드의 확산을 비판하기 위해  '],
       'answer': '현대 사회에서 밥의 중요성을 재조명하기 위해  ',
       'eval_answer': '현대 사회에서 밥의 중요성을 재조명하기 위해  ',
       'eval_explain': ['이 논설문은 밥이 단순한 음식이 아니라 우리의 삶과 문화, 관계를 형성하는 중요한 요소임을 강조하며, 현대 사회에서 패스트푸드와 인스턴트 식사의 증가로 인해 밥의 중요성이 간과되고 있는 경향을 지적하고 있습니다. 따라서 밥의 중요성을 다시 한번 깨닫고 건강한 식습관을 유지해야 한다는 메시지를 전달하고 있습니다.']},
      {'question': '(  ) 에 들어갈 가장 알맞은 말을 고르십시오.',
       'selector': ['개인의 취향  ', '환경적 영향  ', '경제적 이익  ', '문화적 가치  '],
       'answer': '문화적 가치  ',
       'eval_answer': '문화적 가치  ',
       'eval_explain': ["글에서는 밥이 단순한 음식 이상의 의미를 지니고 있으며, 가족과 친구와의 소통, 문화의 정체성을 드러내는 요소로서의 역할을 강조하고 있다. 이러한 맥락에서 '문화적 가치'가 가장 적합한 답이다."]},
      {'question': '윗 글의 내용과 같은 것을 고르십시오.',
       'selector': ['밥은 현대 사회에서 가장 많이 소비되는 음식 중 하나로, 모든 사람들이 매일 먹어야 한다.',
        '밥은 단순히 먹는 것을 넘어서 사람들에게 안정감과 편안함을 제공한다.',
        '밥은 패스트푸드와 인스턴트 식사보다 영양가가 낮은 음식이다.',
        '밥은 가족과 친구와의 소통을 방해하는 요소로 작용한다.'],
       'answer': '밥은 단순히 먹는 것을 넘어서 사람들에게 안정감과 편안함을 제공한다.  ',
       'eval_answer': '밥은 단순히 먹는 것을 넘어서 사람들에게 안정감과 편안함을 제공한다.  ',
       'eval_explain': ['논설문에서는 밥이 단순한 음식 이상의 의미를 지니며, 특히 힘든 하루를 보낸 후 따뜻한 밥 한 그릇이 많은 이들에게 위안이 된다고 언급하고 있다. 이는 밥이 사람들에게 안정감과 편안함을 제공한다는 내용을 뒷받침한다. 다른 선택지들은 논설문에서 언급된 내용과 일치하지 않거나 잘못된 정보이다.']}],
     'total_cost': 5.7105}

