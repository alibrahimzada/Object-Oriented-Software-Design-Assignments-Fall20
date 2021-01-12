from datetime import datetime


class PollSubmission(object):

	def __init__(self, date, poll, student):
		self.__date = date
		self.__poll = poll
		self.__student = student
		self.__questions_answers = {}

	# Date attribute
	@property
	def date(self):
		return self.__date

	@date.setter
	def date(self, value):
		self.__date = value

	# Poll attribute
	@property
	def poll(self):
		return self.__poll

	@poll.setter
	def poll(self, value):
		self.__poll = value

	# Student attribute
	@property
	def student(self):
		return self.__student

	@student.setter
	def student(self, value):
		self.__student = value
	
	@property
	def questions_answers(self):
		return self.__questions_answers

	def add_questions_answers(self, questions, answers):
		for i in range(len(questions)):
			self.__questions_answers.setdefault(questions[i], answers[i])
			questions[i].add_poll_submission(self)
			for answer in answers[i]:
				answer.add_poll_submission(self)
