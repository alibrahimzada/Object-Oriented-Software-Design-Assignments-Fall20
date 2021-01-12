import os
import pandas as pd

class StatsReportSerializer(object):

	def __init__(self, poll_analysis_system):
		self.__poll_analysis_system = poll_analysis_system
		self.__poll_parser = poll_analysis_system.poll_parser
		self.__student_list_parser = poll_analysis_system.student_list_parser
		self.__answer_key_parser = poll_analysis_system.answer_key_parser

	@property
	def poll_analysis_system(self):
		return self.__poll_analysis_system

	def export_reports(self):
		for poll_name in self.__poll_parser.polls:
			poll = self.__poll_parser.polls[poll_name]
			self.export_quiz_report(poll_name, poll)
			self.export_quiz_stats(poll_name, poll)

	def export_quiz_report(self, poll_name, poll):
		if not os.path.exists('quiz_reports'):
			os.mkdir('quiz_reports')
		os.chdir('quiz_reports')
		if not os.path.exists(poll_name):
			os.mkdir(poll_name)
		os.chdir(poll_name)

		quiz_questions = {}
		for question in self.__answer_key_parser.answer_keys[poll_name]:
			quiz_questions.setdefault(question.text, [])

		student_numbers, names, surnames, remarks = [], [], [], []
		for std_list in self.__student_list_parser.registrations:
			for registration in self.__student_list_parser.registrations[std_list]:
				student_numbers.append(registration.student.id)
				names.append(registration.student.name)
				surnames.append(registration.student.surname)
				remarks.append(registration.student.description)
				poll_submissions = registration.student.poll_submissions

				if poll_name not in poll_submissions:
					continue

				for poll_submission in poll_submissions[poll_name]:
					for question in poll_submission.questions_answers:
						pass
		

		data_frame = pd.DataFrame()
		data_frame['Student ID'] = student_numbers
		data_frame['Name'] = names
		data_frame['Surname'] = surnames
		data_frame['Remarks'] = remarks

		data_frame.to_excel('quiz_report.xlsx', index=False)

		os.chdir('..')
		os.chdir('..')

	def export_quiz_stats(self, poll_name, poll):
		pass
