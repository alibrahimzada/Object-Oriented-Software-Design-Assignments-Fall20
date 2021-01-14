import csv
from main.Question import Question
from main.Answer import Answer

class AnswerKeyParser(object):

	def __init__(self, poll_analysis_system):
		self.__answer_keys = {}   # {poll_name: {Question: [Answer, ...], ...}, ...}
		self.__wrong_answers = {}
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
					if title in self.__answer_keys:
						self.__poll_analysis_system.logger.info(f'Answer Key: Your loaded answer key is already loaded. System did not load it again.')
						return
					else:
						self.__poll_analysis_system.logger.info(f'Answer Key: {title} was parsed successfully.')
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

	def get_questions(self, poll_name, questions_set):
		submission_questions = []
		self.__wrong_answers.setdefault(poll_name, {})
		for question in self.__answer_keys[poll_name]:
			if question.text in questions_set:
				submission_questions.append(question)
		return submission_questions

	def get_answers(self, poll_name, submission_questions, answers_list):
		submission_answers = []   # list of lists
		correct_answer = {answer.text: (answer, question) for question in self.__answer_keys[poll_name] for answer in self.__answer_keys[poll_name][question]}
		for i in range(len(submission_questions)):
			question = submission_questions[i]
			answers = answers_list[i]
			current_answers = []
			for answer_text in answers:
				if answer_text in correct_answer and question == correct_answer[answer_text][1]:
					current_answers.append(correct_answer[answer_text][0])
				else:
					if answer_text in self.__wrong_answers[poll_name]:
						current_answers.append(self.__wrong_answers[poll_name][answer_text])
					else:
						wrong_answer = Answer(answer_text)
						current_answers.append(wrong_answer)
						self.__wrong_answers[poll_name][answer_text] = wrong_answer
			submission_answers.append(current_answers)
		return submission_answers
