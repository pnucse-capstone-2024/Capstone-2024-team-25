import os
from pathlib import Path

### Print the current working directory
# print(f"PWD: {os.getcwd()}")
base_path = Path(__file__).parent
file_path = (base_path / "../data/csv/test_data.csv").resolve()
print(f"base_path: {base_path}")
print(f"file_path: {file_path}")

### read csv
import pandas as pd
data = pd.read_csv(file_path)
print(data.head())