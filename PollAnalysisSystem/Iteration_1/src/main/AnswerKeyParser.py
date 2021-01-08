import os
import csv
from collections import defaultdict
from PollAnalysisSystem import PollAnalysisSystem
from Question import Question
from Answer import Answer

class AnswerKeyParser:

    def __init__(self):
        self.__answer_keys = {} #{poll_name: {Question: Answers_list}}
        self.__poll_analysis_system = PollAnalysisSystem()

    @property
    def answer_keys(self):
        return self.__answer_keys

    def read_answer_keys(self):
        """
            Reads all the answer key files and populates the dictionary
            with the parsed answer keys. 
        """
        answer_keys_dir_path = os.path.join(os.getcwd(), 'Iteration_1/answer_keys')
        for file_name in os.listdir(answer_keys_dir_path):
            if file_name.endswith(".csv"): 
                full_path = os.path.join(answer_keys_dir_path, file_name)
                title, question_answers_dict = self.parse_answer_key(full_path)
                self.__answer_keys[title] = question_answers_dict

    def parse_answer_key(self, file_path):
        """
            Parses a given an answer key file. 
        """
        question_answers_dict = defaultdict(list)
        with open(file_path) as csv_file:
            csv_reader = csv.reader(csv_file, delimiter=',')
            for idx, row in enumerate(csv_reader):
                if idx == 0:
                    title = row[0]
                else: 
                    answers_text_list = row[1].split(';') # a semicolon separates the multiple answers
                    # is a multiple choice question if more than one answer is given
                    is_multiple_choice = True if len(answers_text_list) > 1 else False 
                    question = Question(row[0], is_multiple_choice)
                    for answer_text in answers_text_list:
                        answer = Answer(answer_text, is_correct = True)
                        question_answers_dict[question].append(answer)

        return title, question_answers_dict


#test example
# x = AnswerKeyParser()
# x.read_answer_keys()
# for k, v in x.answer_keys.items():
#     print(k)
#     for a, b in v.items():
#         print(a)
#         print(b)