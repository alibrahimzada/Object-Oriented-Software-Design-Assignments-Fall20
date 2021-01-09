import os
import csv
from collections import defaultdict
from Question import Question
from Answer import Answer
from Poll import Poll
from PollSubmission import PollSubmission
from Student import Student


class PollParser(object):

	def __init__(self, poll_analysis_system, answer_keys):
		self.__polls = {}   # {poll_name: Poll}
		self.__poll_analysis_system = poll_analysis_system
		self.__answer_keys = self.__poll_analysis_system.answer_key_parser.answer_keys

	@property
	def polls(self):
		return self.__polls

	def read_poll_reports(self, poll_reports_files): 
		""" Reads all poll reports and parses them """

		for file_name in os.listdir(poll_reports_files): 
				self.parse_poll_report(file_name) 
				
	def parse_poll_report(self, file_name):
		"""
			Given a file name, it goes through the rows and process one row at a time
		"""
		with open(file_name) as csv_file:
			csv_reader = csv.reader(csv_file, delimiter=',')
			for idx, row in enumerate(csv_reader):
				if idx != 0:
					self.process_row(row)
					

	def process_row(self, row):
		"""
			Given a row from the csv file, extracts the information from the row, 
			then finds the poll_name corresponding to the row by matching the questions
			with the answer_keys. Finally, the informaiont is passed so the polls could
			be created/updated.
		"""
		student_name, student_email, datetime = row[1].split(' '), row[2], row[3]
		questions_answers = row[4:]
		questions_set, answers_list = self.separate_questions_and_answers(questions_answers) 
		match = self.get_poll_name_and_questions(questions_set) # look for a match
		if match is None: return 
		poll_name, questions = match
		poll_info = (poll_name, questions, answers_list, student_name, student_email, datetime)
		self.update_polls(poll_info) 
		
	def update_polls(self, poll_info):
		"""
			Given information about a row, creates a poll submission corresponding to the 
			row, then appends it to the the poll's submissions.
		"""
		poll_name, questions, answers_list, student_name, student_email, datetime = poll_info
		if poll_name not in self.polls: # create a poll if it does not exist
			poll = Poll(poll_name, datetime, 'day')
		else:
			poll = self.polls[poll_name]
		#Student
		student = Student('id', student_name[0:-1], student_name[-1], 'dept') 
		student.email = student_email # setter
		#PollSubmission
		poll_submission = PollSubmission(datetime, poll, student, questions, answers_list)
		poll.add_poll_submission(poll_submission)
		self.polls[poll_name] = poll
	
	def separate_questions_and_answers(self, questions_answers):
		"""
			Separates the questions and the answers. returns a set of the questins' texts,
			and a list of the answers' texts
		"""
		questions_set = set([])
		answers_list = []
		for i in range(len(questions_answers)):
			if i % 2 == 0:
				question_text = questions_answers[i]
			else: 
				answers_text_list = questions_answers[i].split(';')
				questions_set.add(question_text)
				answers_list.append(answers_text_list)

		return questions_set, answers_list


	def get_poll_name_and_questions(self, questions_set):
		"""
			Matching a question set from a row from a csv file to an answer key to get the poll's name.
		"""
		for poll_name, questions_answers in self.__answer_keys.items(): 
			this_poll = True
			for question in questions_answers:
				if question.text not in questions_set:
					this_poll = False
			if this_poll:
				return poll_name, list(questions_answers.keys())
		return None # No answer key does not correspond to the passed questions_set
