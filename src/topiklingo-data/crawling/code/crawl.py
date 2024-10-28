##### Listening Test Crawling
import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import requests
from bs4 import BeautifulSoup
import re
import os
import sys

def is_number(s):
    try:
        # Try converting the string to an integer
        int(s)
        return True
    except ValueError:
        # If conversion to integer fails, return False
        return False
    
def find_pattern_for_number_and_character_in_parenthesis(text):
    pattern = r'\(\d{1,}\w{1}\)'
    matches = re.findall(pattern, text)
    return matches

def get_img_dict(filenames, topik_level):
    dict_img_url = {}
    for filename in filenames:
        filename_first_part, filename_second_part = filename.split('.')
        ### Get rid of any alphabet from the img name. Ex. 35_1_e63.png => 35_1_63.png
        filename_first_part = re.sub(r'[a-zA-Z]', '', filename_first_part)
        filename = f'{filename_first_part}.{filename_second_part}'
        list_of_img_name_elements = filename.split('.')[0].split('_')
        print(f'len(list_of_img_name_elements): {len(list_of_img_name_elements)}')
        if len(list_of_img_name_elements) == 5:
            img_prob_num = int(list_of_img_name_elements[-2])
        elif len(list_of_img_name_elements) == 4:
            if topik_level == 1:
                img_prob_num = int(list_of_img_name_elements[-2])
            elif topik_level == 2:
                img_prob_num = int(list_of_img_name_elements[-1])
            else:
                assert False
        elif len(list_of_img_name_elements) == 3:
            img_prob_num = int(list_of_img_name_elements[-1])
        else:
            assert False
            
        ## Check if the key exists
        if img_prob_num not in dict_img_url:
            dict_img_url[img_prob_num] = []
        dict_img_url[img_prob_num].append(filename)
    return dict_img_url

def get_sub_prob_nums(parent_text):
    # Use regular expression to find substrings embraced by '[' and ']'
    matches = re.findall(r'\[(.*?)\]', parent_text)
    # Use regular expression to find numbers
    numbers = list(map(int, re.findall(r'\d+(?:\.\d+)?', str(matches))))
    first_prob, last_prob = numbers[0], numbers[1]
    ### Init sub_prob_nums
    sub_prob_nums = list(range(first_prob, last_prob+1))
    return sub_prob_nums
    
def get_child_ids(base_id, sub_prob_nums):
    ### Init child ids
    child_ids = [base_id+j+1 for j in range(len(sub_prob_nums))]
    return child_ids

def get_point(text):
    # Find all integers inside parentheses
    elements_in_parentheses = re.findall(r'\((.*?)(\d+)(.*?)\)', text)
    points = [int(item) for sublist in elements_in_parentheses for item in sublist if is_number(item)]
    if len(points) > 0:
        points = points[0]
    return points

def strip_strings(obj):
    if isinstance(obj, str):
        return obj.strip().replace("\u00a0", '')
    elif isinstance(obj, list):
        return [strip_strings(item) for item in obj]
    else:
        return obj
    
korean_to_english = {
    '듣기': 'listening',
    '읽기': 'reading',
}
dict_topik_level = {
    'TOPIK I': 'topik-1',
    'TOPIK II': 'topik-2',
}

# Set up the Selenium webdriver (e.g., Chrome)
driver = webdriver.Chrome()  # Make sure you have the appropriate webdriver installed
driver.maximize_window()
# Navigate to the desired web page
driver.get('https://www.topik.go.kr/TWSTDY/TWSTDY0080.do')  # Replace with the actual URL of the web page
driver.get('https://www.topik.go.kr/TWSTDY/TWSTDY0080.do')  # Replace with the actual URL of the web page

# Find the element by its text content
elements = driver.find_elements(By.XPATH, "//a[contains(text(), '문제 풀기')]")

print(len(elements))

### Get the current page source
page_source = driver.page_source

# Create a BeautifulSoup object and parse the HTML
soup = BeautifulSoup(page_source, 'html.parser')

# Find all the h3 tags in the HTML and extract the text
h3_tags = soup.find_all('h3')
h3_texts = [tag.text for tag in h3_tags]
### Extract numbers from the h3 tags
years = list(map(int, re.findall(r'\d+', str(h3_texts))))
# print(years)
# raise Exception('Stop here')
# try:
# Open the first 3 elements
rows = []
unique_id = 0
n_th_exam = 0
for i in range(len(elements)):
    href_value = elements[i].get_attribute('href')
    driver.execute_script(href_value)

    # Switch to the newly opened window or tab (if applicable)
    driver.switch_to.window(driver.window_handles[-1])

    # Perform any additional actions or assertions on the opened page
    # Wait for the overlay to disappear
    WebDriverWait(driver, 10).until(
        EC.invisibility_of_element((By.CLASS_NAME, "loading_box"))
    )

    '''Modify here'''
    wait = WebDriverWait(driver, 60)
    # btn_to_start_exam = wait.until(
    #     EC.presence_of_all_elements_located((By.XPATH, "//a[contains(text(), '시험 시작하기')]"))
    #     )
    btn_to_start_exam = wait.until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, 'a.btns.btns_l.btns_l.btns_front[title="시험 시작하기"]')))
    strong_tags_containing_exam_types = wait.until(
        EC.presence_of_all_elements_located((By.TAG_NAME, "strong"))
        )
    
    ### Extract the text from each <strong> tag
    topik_level, _, listen_or_read, _ = [tag.text for tag in strong_tags_containing_exam_types]

    print(f'topik_level: {topik_level}, listen_or_read: {listen_or_read}')
    if btn_to_start_exam:
        try:
            year = years[i//4]
        except IndexError:
            year = years[-1]
            if i % 4 == 0:
                n_th_exam += 1
        tag = [str(year), f'{year}-{n_th_exam}']
        title = f'{dict_topik_level[topik_level]}-{korean_to_english[listen_or_read]}'
        # print('tag:', tag)
        # print('title:', title)
        # raise Exception('Stop here')
        print(f"Found >> {btn_to_start_exam}")
        # btn_to_start_exam[0].click()
        # btn_to_start_exam.click()
        driver.execute_script("arguments[0].click();", btn_to_start_exam)
        print(f'Enter problem page')

        '''Modify here'''
        ### Find all <div> tags with the class 'exam_sample'
        exam_sample_divs = wait.until(
        EC.presence_of_all_elements_located((By.XPATH, "//div[@class='exam_sample']"))
        )

        ### Get the current page source
        page_source = driver.page_source

        # Create a BeautifulSoup object and parse the HTML
        soup = BeautifulSoup(page_source, 'html.parser')

        # Find all the img tags in the HTML
        img_tags = soup.find_all('img')

        # Extract the source of each img tag
        img_sources = [img['src'] for img in img_tags]
        base_url = 'https://www.topik.go.kr'

        # Specify the directory to save the images
        save_directory_img = 'C:/Users/wlsdu/Desktop/developer/final_project/topik/topik-korea-data/crawling/data/img'
        save_directory_audio = 'C:/Users/wlsdu/Desktop/developer/final_project/topik/topik-korea-data/crawling/data/audio'
        
        # Create the directory if it doesn't exist
        os.makedirs(save_directory_img, exist_ok=True)
        os.makedirs(save_directory_audio, exist_ok=True)

        img_filenames = []
        # Download and save each image
        for img_url in img_sources:
            # Send a GET request to the image URL
            response = requests.get(base_url+img_url)
            
            # Extract the filename from the URL
            filename = os.path.basename(img_url)
            filename_first_part, filename_second_part = filename.split('.')
            ### Get rid of any alphabet from the img name. Ex. 35_1_e63.png => 35_1_63.png
            filename_first_part = re.sub(r'[a-zA-Z]', '', filename_first_part)
            filename = f'{filename_first_part}.{filename_second_part}'
            img_filenames.append(filename)

            # Check if a file with the same name already exists
            file_path = os.path.join(save_directory_img, filename)
            # print(f'filename: {filename}')

            if not os.path.exists(file_path):
                # Save the image to the specified directory
                with open(file_path, 'wb') as file:
                    file.write(response.content)
                
                # print(f'Image downloaded: {filename}')

        if listen_or_read == '듣기':
            ### Extract Audio
            # Find an audio tag in the HTML
            audio_tag = soup.find('audio')
            # Find the source tag within the audio tag
            audio_source_tag = audio_tag.find('source')
            # Extract the source of each img tag
            audio_url = audio_source_tag['src']

            # Download and save the audio
            # Send a GET request to the audio URL
            response = requests.get(base_url+audio_url)
            # Extract the filename from the URL
            filename = os.path.basename(audio_url)
            # Check if a file with the same name already exists
            file_path = os.path.join(save_directory_audio, filename)
            if not os.path.exists(file_path):
                # Save the image to the specified directory
                with open(file_path, 'wb') as file:
                    file.write(response.content)
                
                # print(f'Audio downloaded: {filename}')


        if topik_level == 'TOPIK I':
            dict_img_url = get_img_dict(img_filenames, 1)
            groups_of_problems = soup.find_all('div', class_='exam_sample')
            for j, group in enumerate(groups_of_problems):
                ### Init Parent
                topik_level, prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box = None, None, None, None, None, None, None, None, None, None, None, None
                topik_level = 1
                question = group.find('b').text
                parent_or_child = 'Parent'
                sub_prob_nums = get_sub_prob_nums(question)
                child_ids = get_child_ids(unique_id, sub_prob_nums)
                img_tag = group.find('img')
                if img_tag:
                    if sub_prob_nums[0] in dict_img_url.keys():
                        img_name = dict_img_url[sub_prob_nums[0]]
                        # img_name = img_tag['src'].split('/')[-1]
                # print(group.find('b').text)
                exam_sample_q = group.find('div', class_='exam_sample_q')
                # text_in_box = text_in_box.find('div', class_='q_title')
                if exam_sample_q:
                    ### get the index of selected choice number
                    # Find all <li> tags with class 'answer_2 active'
                    active_li_tags = soup.find_all('li', class_='answer_2 active')
                    # Get the index of the first <li> tag with class 'answer_2 active'
                    if active_li_tags:
                        index = soup.find_all('li', class_='answer_2').index(active_li_tags[0]) + 1
                        print("Index of the <li> tag with class 'answer_2 active':", index)
                        selected_choice_number = int(index)
                    else:
                        print("No <li> tag found with class 'answer_2 active'")
                    q_title = exam_sample_q.find('div', class_='q_title')
                    b_tags = q_title.find_all('b')
                    text_in_box = []
                    for b_tag in b_tags:
                        text_in_box.append(b_tag.text)
                    # print(text_in_box)
                    multiple_li = exam_sample_q.find('ul').find_all('li')
                    choices = []
                    for li in multiple_li:
                        choice_text = li.text.strip()
                        if choice_text != '':
                            choices.append(choice_text)
                    if len(choices) == 0:
                        choices = None
                    # print(choices)
                else:
                    question_outline_div = group.find('div', class_='question_outline')
                    if question_outline_div:
                        text_in_box = [question_outline_div.text]
                # print(unique_id, listen_or_read, prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box)
                point_extracted = get_point(question)
                # print(f'point: {point}')
                # Apply strip() to the variables
                unique_id             = strip_strings(unique_id)
                prob_num              = strip_strings(prob_num)
                parent_or_child       = strip_strings(parent_or_child)
                child_ids             = strip_strings(child_ids)
                point                 = strip_strings(point)
                question              = strip_strings(question)
                text_in_box           = strip_strings(text_in_box)
                choices               = strip_strings(choices)
                img_name              = strip_strings(img_name)
                underline_in_question = strip_strings(underline_in_question)
                underline_in_box      = strip_strings(underline_in_box)
                rows.append([tag, title, topik_level, listen_or_read, unique_id, prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box])
                unique_id += 1
                ### Init Child
                for k, sub_prob_num in enumerate(sub_prob_nums):
                    # print(f'sub_prob_num: {sub_prob_num}')
                    prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box = None, None, None, None, None, None, None, None, None, None, None
                    test_i = group.find('div', id=f'test_{sub_prob_num}')
                    prob_num = sub_prob_num
                    parent_or_child = 'Child'
                    l = 1
                    while test_i is None:
                        test_i = groups_of_problems[j-l].find('div', id=f'test_{sub_prob_num}')
                        l+=1
                    question_div = test_i.find('div', class_='question')
                    if question_div:
                        ### Get the text directly under the outer <div> tag
                        question = question_div.text
                        question_outline_div = question_div.find('div', class_='question_outline')
                        if question_outline_div:
                            string_in_box = question_outline_div.text
                            question = question.replace(string_in_box, '')
                    ### Initialize Point
                    if point_extracted:
                        point = point_extracted   
                    else:
                        point = get_point(question)
                    question_outline_div = test_i.find('div', class_='question_outline')
                    if question_outline_div:
                        text_in_box = question_outline_div.get_text(strip=True, separator='\n').split('\n')

                    multiple_li = test_i.find('ul').find_all('li')
                    choices = []
                    for li in multiple_li:
                        choice_text = li.text.strip()
                        if choice_text != '':
                            choices.append(choice_text)
                    if len(choices) == 0:
                        choices = None
                    img_tag = test_i.find('img')
                    if img_tag:
                        if prob_num in dict_img_url.keys():
                            img_name = dict_img_url[prob_num]
                    ### Init question to None if question contains the question number and its point
                    if len(question) <= 7:
                        question = None
                    # Apply strip() to the variables
                    unique_id             = strip_strings(unique_id)
                    prob_num              = strip_strings(prob_num)
                    parent_or_child       = strip_strings(parent_or_child)
                    child_ids             = strip_strings(child_ids)
                    point                 = strip_strings(point)
                    question              = strip_strings(question)
                    text_in_box           = strip_strings(text_in_box)
                    choices               = strip_strings(choices)
                    img_name              = strip_strings(img_name)
                    underline_in_question = strip_strings(underline_in_question)
                    underline_in_box      = strip_strings(underline_in_box)
                    rows.append([tag, title, topik_level, listen_or_read, unique_id, prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box])
                    # print(unique_id, listen_or_read, prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box)
                    unique_id += 1
        elif topik_level == 'TOPIK II':
            dict_img_url = get_img_dict(img_filenames, 2)
            divs_parent_problems = soup.find_all('div', class_='exam_sample')
            divs_parent_problems = soup.find_all('div', class_='exam_sample')
            for div_parent_problem in divs_parent_problems:
                topik_level, prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box = None, None, None, None, None, None, None, None, None, None, None, None
                topik_level = 2
                temp = []
                parent_text = div_parent_problem.text
                parent_or_child = 'Parent'
                # Use regular expression to find substrings embraced by '[' and ']'
                matches = re.findall(r'\[(.*?)\]', parent_text)
                # Use regular expression to find numbers
                numbers = list(map(int, re.findall(r'\d+(?:\.\d+)?', str(matches))))
                first_prob, last_prob = numbers[0], numbers[1]
                ### Init sub_prob_nums
                sub_prob_nums = list(range(first_prob, last_prob+1))
                ### Init child ids
                child_ids = [unique_id+j+1 for j in range(len(sub_prob_nums))]
                ### Initialize Question
                question = div_parent_problem.find('b').text
                ### If question contains '다음을 읽고' then it has also a text_in_box
                ### Initialize text_in_box
                question_outline_div = div_parent_problem.find('div', class_='question_outline')
                if question_outline_div:
                    text_in_box = [question_outline_div.text]
                    u_tag = question_outline_div.find('u')
                    if u_tag:
                        underline_in_box = u_tag.text
                ### Init underline part

                # Apply strip() to the variables
                unique_id             = strip_strings(unique_id)
                prob_num              = strip_strings(prob_num)
                parent_or_child       = strip_strings(parent_or_child)
                child_ids             = strip_strings(child_ids)
                point                 = strip_strings(point)
                question              = strip_strings(question)
                text_in_box           = strip_strings(text_in_box)
                choices               = strip_strings(choices)
                img_name              = strip_strings(img_name)
                underline_in_question = strip_strings(underline_in_question)
                underline_in_box      = strip_strings(underline_in_box)
                ### Insert columns
                rows.append([tag, title, topik_level, listen_or_read, unique_id, prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box])
                # Find all integers inside parentheses
                integers_in_parentheses = re.findall(r'\((.*?)(\d+)(.*?)\)', parent_text)
                # Filter out only the numbers from the list
                points = [int(item) for sublist in integers_in_parentheses for item in sublist if is_number(item)]
                # print(f'points: {points}')
                unique_id += 1
                # print(f'sub prob #: {numbers}')
                for num in sub_prob_nums:
                    temp = []
                    prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box = None, None, None, None, None, None, None, None, None, None, None
                    prob_num = num
                    parent_or_child = 'Child'
                    ### Initialize Point
                    if len(points) == 1:
                        point = points[0]    
                    else:
                        point = points[k-1]
                    sub_prob_div = soup.find('div', id=f'test_{num}')
                    question_div = sub_prob_div.find('div', id=f'q1')
                    ### Init text_in_box
                    ### question_outline_div is div in a box
                    question_outline_divs = question_div.find_all('div', class_='question_outline')
                    text_in_box = []
                    underline_in_box = []
                    for question_outline_div in question_outline_divs:
                        if question_outline_div:
                            text_in_box.append(question_outline_div.get_text(strip=True, separator='\n').split('\n'))
                            u_tag = question_outline_div.find('u')
                            if u_tag:
                                underline_in_box.append(u_tag.text)
                    if len(text_in_box)==0:
                        text_in_box = None
                    if len(underline_in_box)==0:
                        underline_in_box = None
                    ### Init question if there is no text box found
                    if len(question_outline_divs)==0:
                        question = question_div.text
                    ### Init question to None if question contains the question number and its point
                    if question is not None and len(question) <= 7:
                        question = None
                    u_tag = question_div.find('u')
                    if u_tag:
                        underline_in_question = u_tag.text
                    ul_choices = sub_prob_div.find('ul', class_='answer_ui')
                    if ul_choices:
                        '''Whitespace to ,(comma)'''
                        choices = ul_choices.text.strip().split('\t\t')
                        choices = [choice.strip() for choice in choices]
                        choices = [choice for choice in choices if len(choice) > 0]
                        if len(choices) == 0:
                            choices = None
                    if prob_num in dict_img_url.keys():
                        img_name = dict_img_url[prob_num]
                    # assert 1 == 0
                    # Apply strip() to the variables
                    unique_id             = strip_strings(unique_id)
                    prob_num              = strip_strings(prob_num)
                    parent_or_child       = strip_strings(parent_or_child)
                    child_ids             = strip_strings(child_ids)
                    point                 = strip_strings(point)
                    question              = strip_strings(question)
                    text_in_box           = strip_strings(text_in_box)
                    choices               = strip_strings(choices)
                    img_name              = strip_strings(img_name)
                    underline_in_question = strip_strings(underline_in_question)
                    underline_in_box      = strip_strings(underline_in_box)
                    rows.append([tag, title, topik_level, listen_or_read, unique_id, prob_num, parent_or_child, child_ids, point, question, text_in_box, choices, selected_choice_number, img_name, underline_in_question, underline_in_box])
                    unique_id += 1

        else:       
            print('Neither TOPIK I nor II')
        
    else:
        print("No elements found")
        ### Close the browser
        driver.quit()

    ### Crawl answers
    ### Click on the '결과 보기' button
    wait = WebDriverWait(driver, 5)
    btn_to_show_results = wait.until(EC.element_to_be_clickable((By.ID, 'result_button')))
    
    print(f'btn_to_show_results: {btn_to_show_results}')
    if btn_to_show_results:
        btn_to_show_results.click()
        print(f'Enter answer page')

        link = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'btns_modal')))
        
        if link:
            # Click the link
            link.click()
            print(f'Enter answer page')

            ### Get the current page source
            page_source = driver.page_source

            # Create a BeautifulSoup object and parse the HTML
            soup = BeautifulSoup(page_source, 'html.parser')

            # Locate all span tags with the given class name
            spans = soup.find_all('span', class_='answer_ok')
            
            # Extract the id attribute of each span tag
            ids = [span.get('id') for span in spans]
            print(f'ids: {ids}')

            # Extract the question number and answer number from the id attribute
            # subract 'a' from the id
            ids = [id.replace('a', '') for id in ids]
            tuple_list_of_question_and_answer = [list(map(int, id.split('_'))) for id in ids]

            # reverse the list
            tuple_list_of_question_and_answer = tuple_list_of_question_and_answer[::-1]
            
            print(f'tuple_list_of_question_and_answer: {tuple_list_of_question_and_answer}')

            j = len(rows) - 1
            for question_num, answer_num in tuple_list_of_question_and_answer:
                while rows[j][5] != question_num:
                    j -= 1
                rows[j].append(answer_num)
            
            # raise Exception('Stop here')

    # Close the current window or tab (if needed)
    driver.close()

    # Switch back to the main window or tab
    driver.switch_to.window(driver.window_handles[0])

    print(f'{i}th Test just finished')
    # raise Exception('Stop here')
# except Exception as e:
#     print(f'Error Msg: {e}')

### Close the browser
driver.quit()

### Convert rows to a dataframe, and save it to a csv file
columns = ['tag', 'title', 'topik_level', 'listen_or_read', 'unique_id', 'prob_num', 'parent_or_child', 'child_ids', 'point', 'question', 'text_in_box', 'choices', 'selected_choice_number', 'img_name', 'underline_in_question', 'underline_in_box', 'answer_num']
df = pd.DataFrame(rows, columns=columns)
### Make the absolute path to save the dataframe
current_dir = os.path.dirname(os.path.realpath(__file__))
parent_dir = os.path.abspath(os.path.join(current_dir, os.pardir))  # Get the parent directory
csv_path = os.path.abspath(os.path.join(parent_dir, 'data/csv/test_data_0.csv'))
df.to_csv(csv_path, index=False)
print(f'The CSV file was saved to {csv_path}')