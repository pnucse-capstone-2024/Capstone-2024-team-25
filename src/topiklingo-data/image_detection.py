from google.cloud import vision
from google.oauth2 import service_account
import json
def detect_text(input_path, output_path):
    """Detects text in the file."""
    credentials = service_account.Credentials.from_service_account_file(
        "API_KEY/google_vision_api_key.json"
    )
    client = vision.ImageAnnotatorClient(credentials=credentials)

    with open(input_path, "rb") as image_file:
        content = image_file.read()

    image = vision.Image(content=content)

    response = client.text_detection(image=image)
    
    file_name = input_path.split("/")[-1].split(".")[0] + ".json"
    
    response_json = {"text_all": "", "bounding_poly_all": [], "response": []}
    
    texts = response.text_annotations
    print("Texts:")
    idx = 0
    for text in texts:
        if idx == 0:
            response_json["text_all"] = text.description
            response_json["bounding_poly_all"] = [
                {"x": vertex.x, "y": vertex.y} for vertex in text.bounding_poly.vertices
            ]
            print(f'\n"{text.description}"')
            print("bounds: ", response_json["bounding_poly_all"])
            idx += 1
        else:
            print(f'\n"{text.description}"')
            vertices = [{"x": vertex.x, "y": vertex.y} for vertex in text.bounding_poly.vertices]
            print("bounds: ", vertices)
            response_json["response"].append({"text": text.description, "bounding_poly": vertices})
            idx += 1

    #save response json
    with open(output_path + file_name, "w", encoding='utf-8') as f:
        json.dump(response_json, f, ensure_ascii=False, indent="\t")
    
    if response.error.message:
        raise Exception(
            "{}\nFor more info on error messages, check: "
            "https://cloud.google.com/apis/design/errors".format(response.error.message)
        )


if __name__ == "__main__":
    detect_text("asset/sample_image/83_1_63.png", "data/image_ocr_data/")