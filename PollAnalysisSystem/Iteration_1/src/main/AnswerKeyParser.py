import os
import csv
from collections import defaultdict
from Question import Question
from Answer import Answer

class AnswerKeyParser(object):

	def __init__(self, poll_analysis_system):
		self.__answer_keys = {}   # {poll_name: {Question: [Answer, ...], ...}, ...}
		self.__poll_analysis_system = poll_analysis_system

	@property
	def answer_keys(self):
		return self.__answer_keys

	def read_answer_keys(self, answer_key_files):
		"""
			Reads all the answer key files and populates the dictionary
			with the parsed answer keys. 
		"""

		for file_name in answer_key_files:
			title, question_answers_dict = self.parse_answer_key(file_name)
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
