import os
import csv
import ntpath
import json
from datetime import datetime, date

from main.Question import Question
from main.Answer import Answer
from main.AttendancePoll import AttendancePoll
from main.QuizPoll import QuizPoll
from main.PollSubmission import PollSubmission
from main.Student import Student


class PollParser(object):

	def __init__(self, poll_analysis_system):
		self.__polls = {}   # {poll_name: Poll}
		self.__poll_analysis_system = poll_analysis_system

	@property
	def polls(self):
		return self.__polls

	def read_poll_reports(self, poll_report_files):
		""" Reads all poll reports and parses them """

		for file_path in poll_report_files:
			self.__file_name = ntpath.basename(file_path).split('.')[0]
			self.__parse_poll_report(file_path)
			self.__poll_analysis_system.logger.info(f'Poll Report: {file_path.split("/")[-1]} was parsed successfully.')
		self.__export_db()

	def __parse_poll_report(self, file_path):
		"""
			Given a file name, it goes through the rows and process one row at a time
		"""
		with open(file_path) as csv_file:
			csv_reader = csv.reader(csv_file, delimiter=',')
			for idx, row in enumerate(csv_reader): #how to model this in a seq diagram?
				if idx == 0:
					continue
				self.__process_row(row)

	def __process_row(self, row):
		"""
			Given a row from the csv file, extracts the information from the row, 
			then finds the poll_name corresponding to the row by matching the questions
			with the answer_keys. Finally, the informaiont is passed so the polls could
			be created/updated.
		"""
		row[1] = ''.join([char for char in row[1] if not char.isdigit()]).strip()
		student_name, student_email, submission_datetime = row[1], row[2], row[3]
		student = self.__poll_analysis_system.student_list_parser.get_student(student_name)
		if student == None: return
		student.email = student_email
		questions_answers = row[4:]
		questions_set, answers_list = self.__process_questions_answers(questions_answers)
		poll_name = self.__get_poll_name(questions_set)
		if poll_name is None: return # no answer key does not correspond to the passed questions_set
		poll_info = (poll_name, questions_set, answers_list, student, submission_datetime)
		self.__update_polls(poll_info)

	def __update_polls(self, poll_info):
		"""
			Given information about a row, creates a poll submission corresponding to the 
			row, then appends it to the the poll's submissions.
		"""
		poll_name, questions_set, answers_list, student, submission_datetime = poll_info
		answer_key_parser = self.__poll_analysis_system.answer_key_parser
		poll_questions = list(answer_key_parser.answer_keys[poll_name].keys())   # list of all question objects of a poll
		submission_questions = answer_key_parser.get_questions(poll_name, questions_set)   # list of submitted question objects
		submission_answers = answer_key_parser.get_answers(poll_name, submission_questions, answers_list)   # list of submitted answer objects
		poll_weekday = poll_name.split('_')[2] 
		poll_date = datetime(int(poll_name.split('_')[1][:4]), int(poll_name.split('_')[1][4:6]), int(poll_name.split('_')[1][6:]))
		if poll_name not in self.__polls: # create a poll if it does not exist
			if 'Are you attending this lecture?' in [q.text for q in submission_questions]:
				poll = AttendancePoll(poll_name, poll_date, poll_weekday)
			else:
				poll = QuizPoll(poll_name, poll_date, poll_weekday)
		else:
			poll = self.__polls[poll_name]

		# #PollSubmission
		submission_datetime = datetime.strptime(submission_datetime, '%b %d, %Y %H:%M:%S')
		poll_submission = PollSubmission(submission_datetime, poll, student)
		poll_submission.add_questions_answers(submission_questions, submission_answers)
		poll.add_poll_submission(poll_submission)
		poll.add_questions_answers(submission_questions, submission_answers)
		student.add_poll_submission(poll_name, poll_submission)
		self.__polls[poll_name] = poll
	
	def __process_questions_answers(self, questions_answers):
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


	def __get_poll_name(self, questions_set):
		"""
			Matching a question set & (date of the poll_report and the poll) from a row 
			from a csv file to an answer key to get the poll's name.
			
		"""
		answer_key_parser = self.__poll_analysis_system.answer_key_parser
		for poll_name, questions_answers in answer_key_parser.answer_keys.items():
			poll_date = poll_name.split('_')[1].strip() # date taken from the name in the answer key
			report_date = self.__file_name.split('_')[1].strip()
			this_poll = True
			poll_questions = [q.text for q in questions_answers]
			for question in questions_set:
				if question not in poll_questions:
					this_poll = False
			if this_poll and poll_date == report_date:
				return poll_name
		return None # no answer key does not correspond to the passed questions_set

	def __export_db(self):
		is_db_available = True
		if not os.path.exists('db'):
			is_db_available = False
			os.mkdir('db')
		os.chdir('db')

		content = {}
		if is_db_available:
			with open('db.json') as db:
				content = json.load(db)

		for poll_name in self.__polls:
			poll_date = str(self.__polls[poll_name].date.date())
			for poll_submission in self.__polls[poll_name].poll_submissions:
				content.setdefault(poll_date, [])
				student_id = poll_submission.student.id
				if student_id in content[poll_date]: continue
				content[poll_date].append(student_id)

		with open('db.json', 'w') as fw:
			json.dump(content, fw, sort_keys=True, indent=4)
		os.chdir('..')
