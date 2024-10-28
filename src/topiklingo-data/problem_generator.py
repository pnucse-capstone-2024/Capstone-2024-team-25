import os
import sys
import json
import threading
from util import openai_callback, anthropic_callback
from langchain.globals import set_llm_cache
from langchain.cache import InMemoryCache
from langchain_openai.chat_models import ChatOpenAI
from langchain_anthropic.chat_models import ChatAnthropic
from problem_template import Problem_Template


class Problem_Generate_Model:
    def __init__(
        self,
        models,
        use_cache=False,
        temperature=0,
        api_key_path="API_KEY/llm_api_key.json",
        verbose=True,
    ):
        self.use_cache = use_cache
        self.verbose = verbose

        if self.use_cache:
            set_llm_cache(InMemoryCache())

        api_key_file = api_key_path
        if os.path.exists(api_key_file):
            with open(api_key_file) as f:
                api_key = json.load(f)
        else:
            api_key = {
                "OPENAI_API_KEY": "your_openai_api_key_here",
                "ANTHROPIC_API_KEY": "your_anthropic_api_key_here",
            }
            with open(api_key_file, "w") as f:
                json.dump(api_key, f)

        if (
            api_key["OPENAI_API_KEY"] == "your_openai_api_key_here"
            or api_key["ANTHROPIC_API_KEY"] == "your_anthropic_api_key_here"
        ):
            print("Please update your API keys in the API_KEY/api_key.json file")
            sys.exit()
        else:
            os.environ["OPENAI_API_KEY"] = api_key["OPENAI_API_KEY"]
            os.environ["ANTHROPIC_API_KEY"] = api_key["ANTHROPIC_API_KEY"]

            self.results = []
            self.models = []

            if models:
                for model in models:
                    if model == "claude-3-opus":
                        self.models.append(
                            ChatAnthropic(
                                temperature=temperature,
                                model_name="claude-3-opus-20240229",
                            )
                        )
                    elif model == "gpt-4o":
                        self.models.append(
                            ChatOpenAI(temperature=temperature, model_name="gpt-4o")
                        )
                    elif model == "gpt-4o-mini":
                        self.models.append(
                            ChatOpenAI(
                                temperature=temperature, model_name="gpt-4o-mini"
                            )
                        )
                    elif model == "gpt-4-turbo":
                        self.models.append(
                            ChatOpenAI(
                                temperature=temperature, model_name="gpt-4-turbo"
                            )
                        )
                    elif model == "claude-3-sonnet":
                        self.models.append(
                            ChatAnthropic(
                                temperature=temperature,
                                model_name="claude-3-sonnet-20240229",
                            )
                        )
                    elif model == "gpt-3.5-turbo":
                        self.models.append(
                            ChatOpenAI(
                                temperature=temperature, model_name="gpt-3.5-turbo"
                            )
                        )
                    else:
                        self.models.append(
                            ChatAnthropic(
                                temperature=temperature,
                                model_name="claude-3-haiku-20240307",
                            )
                        )
            else:
                self.models = [
                    ChatOpenAI(temperature=temperature, model_name="gpt-4o-mini")
                ]

    @staticmethod
    def model_response(model, messages, results):
        response = model.invoke(messages)
        try:
            result = (
                str(model.model),
                str(response.content),
                str(anthropic_callback(response)),
            )
            results.append(result)
        except:
            result = (
                response.response_metadata["model_name"],
                str(response.content),
                str(openai_callback(response)),
            )
            results.append(result)

    @staticmethod
    def model_eval_response(model, messages, results):
        response = model.invoke(messages)
        try:
            result = (
                str(model.model) + "_evaluator",
                str(response.content),
                str(anthropic_callback(response)),
            )
            results.append(result)
        except:
            result = (
                response.response_metadata["model_name"] + "_evaluator",
                str(response.content),
                str(openai_callback(response)),
            )
            results.append(result)

    @staticmethod
    def model_parser_response(model, messages, results):
        response = model.invoke(messages)
        try:
            result = (
                str(model.model) + "_parser",
                str(response.content),
                str(anthropic_callback(response)),
            )
            results.append(result)
        except:
            result = (
                response.response_metadata["model_name"] + "_parser",
                str(response.content),
                str(openai_callback(response)),
            )
            results.append(result)

    @staticmethod
    def print_models_responses(results):
        for result in results:
            print(result[0] + " | \n" + result[1])
            print()
            print(result[2])
            print(
                "--------------------------------------------------------------------------------------------------"
            )

    def request_models_responses(self, messages, type="generate"):
        results = []
        threads = []
        if type == "generate":
            target = self.model_response
        elif type == "evaluate":
            target = self.model_eval_response
        else:
            target = self.model_parser_response
        for model in self.models:
            thread = threading.Thread(target=target, args=(model, messages, results))
            threads.append(thread)
            thread.start()

        for thread in threads:
            thread.join()

        self.results = results
        if self.verbose:
            self.print_models_responses(results)

    def get_model_responses(self):
        return self.results


if __name__ == "__main__":
    # models = [
    #     "claude-3-opus",
    #     "gpt-4-turbo",
    #     "claude-3-sonnet",
    #     "gpt-3.5-turbo",
    #     "claude-3-haiku",
    # ]

    models = ["gpt-3.5-turbo"]
    problem_template = Problem_Template("reading_1", "problem_type_1_2")
    problem_generate_model = Problem_Generate_Model(
        models, use_cache=True, temperature=0.3
    )

    eval_models = ["gpt-4-turbo"]
    problem_evaluate_model = Problem_Generate_Model(
        eval_models, use_cache=True, temperature=0
    )

    problem_generate_model.request_models_responses(
        problem_template.get_model_message(), type="generate"
    )
    responses = problem_generate_model.get_model_responses()

    for response in responses:
        problem_evaluate_model.request_models_responses(
            problem_template.get_eval_message(response[1]), type="evaluate"
        )
        eval_responses = problem_evaluate_model.get_model_responses()

        # problem_generate_model.request_models_responses(
        #     problem_template.get_output_parser_message(eval_responses[0][1]), type="parser"
        # )
        # parser_responses = problem_generate_model.get_model_responses()
