### Raise Error if Python version is below 3.4
import sys
if sys.version_info < (3, 4):
    raise Exception("Please use Python version 3.4 or above to use the library 'pathlib'.")

import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import requests
from bs4 import BeautifulSoup
import re
import os
from pathlib import Path
    
# Set up the Selenium webdriver (e.g., Chrome)
driver = webdriver.Chrome()  # Make sure you have the appropriate webdriver installed
# driver.maximize_window()
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
    btn_to_start_exam = wait.until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, 'a.btns.btns_l.btns_l.btns_front[title="시험 시작하기"]')))
    strong_tags_containing_exam_types = wait.until(
        EC.presence_of_all_elements_located((By.TAG_NAME, "strong"))
        )
    
    ### Extract the text from each <strong> tag
    topik_level, _, listen_or_read, _ = [tag.text for tag in strong_tags_containing_exam_types]
    print(f'topik_level: {topik_level}, listen_or_read: {listen_or_read}')
    
    if listen_or_read == '듣기':
        if btn_to_start_exam:
            # raise Exception('Stop here')
            print(f"Found >> {btn_to_start_exam}")
            driver.execute_script("arguments[0].click();", btn_to_start_exam)
            print(f'Enter problem page')

            ### Get the current page source
            page_source = driver.page_source

            # Create a BeautifulSoup object and parse the HTML
            soup = BeautifulSoup(page_source, 'html.parser')

            base_url = 'https://www.topik.go.kr'

            # Specify the directory to save the images
            BASE_PATH = Path(__file__).parent
            save_directory_audio = (BASE_PATH / "../data/audio").resolve()
            # save_directory_audio = 'C:/Users/wlsdu/Desktop/developer/final_project/topik/topik-korea-data/crawling/data/audio'
            
            # Create the directory if it doesn't exist
            os.makedirs(save_directory_audio, exist_ok=True)
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
                
                print(f'Audio downloaded: {filename}')
            
        else:
            print("No elements found")
            ### Close the browser
            driver.quit()

    driver.close()

    # Switch back to the main window or tab
    driver.switch_to.window(driver.window_handles[0])

    print(f'{i}th Test just finished')

### Close the browser
driver.quit()