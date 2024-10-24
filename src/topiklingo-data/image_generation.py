import os
import json
from openai import OpenAI

api_key_file = "API_KEY/api_key.json"
with open(api_key_file) as f:
    api_key = json.load(f)
os.environ["OPENAI_API_KEY"] = api_key["OPENAI_API_KEY"]
# os.environ["ANTHROPIC_API_KEY"] = api_key["ANTHROPIC_API_KEY"]

client = OpenAI()

# input_img_info = 'The image depicts a milk carton with Korean text on it. The date "2022.07.20" is printed at the top of the carton, likely indicating the packaging or expiration date of July 20, 2022. The carton shows an illustration of two strawberries. Below the strawberries, there is Korean text that says "딸기우유" which translates to "Strawberry Milk" in English. At the bottom of the carton, the quantity "1,000원" is listed, which represents a price or value of 1,000 Korean won (approximately 0.77 USD based on current exchange rates).'
# prompt1 = f'Generate a similar image based on an original image while preserving the Korean language. Change texts based on the generated image. Here is the information of the original image. {input_img_info}'
# prompt2 = 'A realistic product image of a Korean banana milk carton on a plain white background. The carton is rectangular with a gable top. On the front of the carton, there is a photorealistic illustration of a ripe yellow banana. Below the banana picture, the Korean text "바나나 우유" is prominently displayed, which translates to "Banana Milk". Near the top of the carton, there is a printed date in the format "YYYY.MM.DD" representing the product/'s expiration date. At the bottom of the carton, the price is shown in Korean won using the "₩" symbol followed by the amount. All text on the carton is in Korean.'
# prompt3 = 'Generate an image of a milk pack of banana flavor, there should be a price, a banana picture, the product name, and expiry date. All the texts are Korean.'
# prompt4 = 'Generate a product image of banana milk. The image has to have the following texts. "2022년 8월 15일", "바나나 우유", "1,200원"'
# prompt4 = 'Generate a product image of banana milk. The image has to have the following texts. "2022년 8월 15일", "바나나 우유", "1,200원"'
# prompt = ''

# target_prompt = prompt4
# print(f'prompt: {target_prompt}')
# response = client.images.generate(
#   model="dall-e-3",
#   prompt=target_prompt,
#   size="1024x1024",
#   quality="standard",
#   n=1,
# )

response = client.images.edit(
  image=open("C:/Users/User/Desktop/temp/uni/final_project/topik-korea-data/crawling/data/img/35_2_2_5.png", "rb"),
  prompt='Use data analysis: replace the text "세상을 보는 눈" in the attached image with "미래를 보는 눈". Use any Korean font.',
  n=1,
  size="1024x1024"
)


image_url = response.data[0].url
print(image_url)