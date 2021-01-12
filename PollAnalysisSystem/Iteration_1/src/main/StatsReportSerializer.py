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
		if not os.path.exists('statistics'):
			os.mkdir('statistics')
		os.chdir('statistics')
		if not os.path.exists(poll_name):
			os.mkdir(poll_name)
		os.chdir(poll_name)

		quiz_questions = {'n questions': [], 'success rate': [], 'success %': []}
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
					quiz_questions = self.export_absent_student(quiz_questions)
					continue
				for poll_submission in poll_submissions[poll_name]:
					quiz_questions = self.validate_answers(poll_name, quiz_questions, poll_submission)

		data_frame = pd.DataFrame()
		data_frame['Student ID'] = student_numbers
		data_frame['Name'] = names
		data_frame['Surname'] = surnames
		data_frame['Remarks'] = remarks

		question_counter = 1
		for question_text in quiz_questions:
			if question_text not in ['n questions', 'success rate', 'success %']:
				data_frame['Q' + str(question_counter)] = quiz_questions[question_text]
				question_counter += 1
		
		data_frame['n questions'] = quiz_questions['n questions']
		data_frame['success rate'] = quiz_questions['success rate']
		data_frame['success %'] = quiz_questions['success %']
		data_frame.to_excel('quiz_report.xlsx', index=False)
		os.chdir('..')
		os.chdir('..')

	def export_absent_student(self, quiz_questions):
		quiz_questions['n questions'].append(0)
		quiz_questions['success rate'].append('0/0')
		quiz_questions['success %'].append('0%')

		for question_text in quiz_questions:
			if question_text not in ['n questions', 'success rate', 'success %']:
				quiz_questions[question_text].append('absent')
		return quiz_questions
	
	def validate_answers(self, poll_name, quiz_questions, poll_submission):
		quiz_questions['n questions'].append(0)
		quiz_questions['success rate'].append('0/0')
		quiz_questions['success %'].append('')

		for question in self.__answer_key_parser.answer_keys[poll_name]:
			quiz_questions['n questions'][-1] += 1
			if question in poll_submission.questions_answers:
				if poll_submission.questions_answers[question] == self.__answer_key_parser.answer_keys[poll_name][question]:
					quiz_questions[question.text].append('1')
					new_correct = int(quiz_questions['success rate'][-1][0]) + 1
					quiz_questions['success rate'][-1] = str(new_correct) + quiz_questions['success rate'][-1][1:]
				else:
					quiz_questions[question.text].append('0')
			else:
				quiz_questions[question.text].append('0')
			total_correct = int(quiz_questions['success rate'][-1][0])
			quiz_questions['success rate'][-1] = '{}/{}'.format(total_correct, quiz_questions['n questions'][-1])
			quiz_questions['success %'][-1] = '{}%'.format(round(total_correct * 100.0 / quiz_questions['n questions'][-1], 2))
			
		return quiz_questions

	def export_quiz_stats(self, poll_name, poll):
		pass
