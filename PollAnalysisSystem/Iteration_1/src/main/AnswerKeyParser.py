import csv
from main.Question import Question
from main.Answer import Answer

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
			self.__parse_answer_key(file_name)

	def __parse_answer_key(self, file_path):
		"""
			Parses a given an answer key file. 
		"""

		with open(file_path) as csv_file:
			csv_reader = csv.reader(csv_file, delimiter=',')
			for idx, row in enumerate(csv_reader):
				if row[1] == '':
					title = row[0]
					self.__answer_keys.setdefault(title, {})
				else:
					answers_text_list = row[1].split(';') # a semicolon separates the multiple answers
				# 	# is a multiple choice question if more than one answer is given
					is_multiple_choice = True if len(answers_text_list) > 1 else False 
					question = Question(row[0], is_multiple_choice)
					self.__answer_keys[title].setdefault(question, [])
					for answer_text in answers_text_list:
						answer = Answer(answer_text, is_correct = True)
						self.__answer_keys[title][question].append(answer)
