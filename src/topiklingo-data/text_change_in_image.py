# Google Vision
import pytesseract
from PIL import Image


# Open the image file
image = Image.open("C:/Users/wlsdu/Desktop/83_1_40.png")

# Perform OCR using PyTesseract
text = pytesseract.image_to_string(image)

# # Print the extracted text
# print(text)