import pandas as pd

data = pd.read_csv('C:/Users/User/Desktop/temp/uni/final_project/topik-korea-data/crawling/data/csv/test_data_0.csv')

def replace_text(text, underline):
    if pd.isnull(underline):
        return text
    return text.replace(underline, f'<ins>{underline}</ins>')

df = data.copy()
### Change type of question, text_in_box, unerline_in_question, underline_in_box to string
# df['question'] = df['question'].astype(str)
# df['text_in_box'] = df['text_in_box'].astype(str)

### Replace texts in question the same as underline_in_question with <ins>
### Do it using apply() function
df['question'] = df.apply(lambda row: replace_text(row['question'], row['underline_in_question']), axis=1)
### Replace texts in text_in_box the same as underline_in_box with <ins>
df['text_in_box'] = df.apply(lambda row: replace_text(row['text_in_box'], row['underline_in_box']), axis=1)

### change all values in answer_num to int except NaN
df['answer_num'] = df['answer_num'].apply(lambda x: int(x) if not pd.isnull(x) else x)

print(df[df['underline_in_question'].notnull()].head(1))
print(df[df['underline_in_box'].notnull()].head(1))

import re

def replace_text(text, pattern):
    # print(text)
    if pd.isnull(text):
        return text
    text = re.sub(pattern, '', text)
    text = text.replace('※ ', '※')
    return text

print(df.head(1))
### Replace any text closed with [] in question with ''
pattern = r'\[.*?\]'
df['question'] = df['question'].apply(lambda x: replace_text(x, pattern))
print(df.head(1))

df.to_csv('C:/Users/User/Desktop/temp/uni/final_project/topik-korea-data/crawling/data/csv/test_data.csv', index=False)