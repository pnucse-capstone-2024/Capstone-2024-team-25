import pandas as pd
import csv

file = pd.read_csv('KoreanVocaDict.txt', delimiter = '\t')
print(file.head())