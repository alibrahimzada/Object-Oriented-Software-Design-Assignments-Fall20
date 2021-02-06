import csv
from main.Question import Question
from main.Answer import Answer
import ntpath

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
			try:
				self.__parse_answer_key(file_name)
			except: 
				answer_key = ntpath.basename(file_name).split(".")[0]
				self.__poll_analysis_system.logger.error(f'The provided Answer Key: {answer_key} is not valid.')
		
		if 'attendance poll' not in self.__answer_keys:
			question = Question('1', 'Are you attending this lecture?')
			answer = Answer('1', 'Yes', True)
			self.__answer_keys['attendance poll'] = {question: [answer]}

	def __parse_answer_key(self, file_path):
		"""
			Parses a given an answer key file. 
		"""

		with open(file_path) as txt_file:
			lines = txt_file.readlines()
			for line_idx in range(2, len(lines), 1):
				if lines[line_idx] == '\n': continue
				line = lines[line_idx].replace('\n', '')
				if line.startswith(' '):   # current line is a poll name
					title = line.split('\t')[0].strip().replace(':', ' ')
					self.__poll_analysis_system.logger.info(f'Answer Key: {title} was parsed successfully.')
					self.__answer_keys.setdefault(title, {})
				elif not line.startswith('Answer'):
					question = line.split(' ')
					question_number = question[0]
					question_number = question_number.replace('.', '')
					question_text = ' '.join(question[1:-3]).strip()
					question_object = Question(question_number, question_text)
					if question[-2] == 'Multiple':
						question_object.is_multiple_choice = True
					self.__answer_keys[title].setdefault(question_object, [])
				else:
					answer = line.split(' ')
					answer_number = answer[1][0]
					answer_text = ' '.join(answer[2:]).strip()
					answer_object = Answer(answer_number, answer_text, True)
					self.__answer_keys[title][question_object].append(answer_object)

	def get_questions(self, poll_name, questions_set):
		submission_questions = []
		self.__wrong_answers.setdefault(poll_name, {})
		for question_text in questions_set:
			for question in self.__answer_keys[poll_name]:
				if question.text == question_text:
					submission_questions.append(question)
					break
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
						wrong_answer = Answer('1', answer_text)
						current_answers.append(wrong_answer)
						self.__wrong_answers[poll_name][answer_text] = wrong_answer
			submission_answers.append(current_answers)
		return submission_answers
