import os
import pandas as pd
import matplotlib.pyplot as plt

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

	def export_quiz_report(self, poll_name, poll):
		if not os.path.exists('statistics'):
			os.mkdir('statistics')
		os.chdir('statistics')
		if not os.path.exists(poll_name):
			os.mkdir(poll_name)
		os.chdir(poll_name)

		self.__answer_distribution = {}
		self.__quiz_questions = {'n questions': [], 'success rate': [], 'success %': []}
		for question in self.__answer_key_parser.answer_keys[poll_name]:
			self.__quiz_questions.setdefault(question.text, [])
			self.__answer_distribution.setdefault(question, {})

		student_numbers, names, surnames, remarks = [], [], [], []
		for std_list in self.__student_list_parser.registrations:
			for registration in self.__student_list_parser.registrations[std_list]:
				student_numbers.append(registration.student.id)
				names.append(registration.student.name)
				surnames.append(registration.student.surname)
				remarks.append(registration.student.description)
				poll_submissions = registration.student.poll_submissions

				if poll_name not in poll_submissions:
					self.export_absent_student()
					continue
				for poll_submission in poll_submissions[poll_name]:
					self.validate_answers(poll_name, poll_submission)
					self.update_answer_distribution(poll_submission)

		self.create_chart()

		data_frame = pd.DataFrame()
		data_frame['Student ID'] = student_numbers
		data_frame['Name'] = names
		data_frame['Surname'] = surnames
		data_frame['Remarks'] = remarks

		question_counter = 1
		for question_text in self.__quiz_questions:
			if question_text not in ['n questions', 'success rate', 'success %']:
				data_frame['Q' + str(question_counter)] = self.__quiz_questions[question_text]
				question_counter += 1

		data_frame['n questions'] = self.__quiz_questions['n questions']
		data_frame['success rate'] = self.__quiz_questions['success rate']
		data_frame['success %'] = self.__quiz_questions['success %']
		data_frame.to_excel('quiz_report.xlsx', index=False)
		os.chdir('..')
		os.chdir('..')

	def export_absent_student(self):
		self.__quiz_questions['n questions'].append(0)
		self.__quiz_questions['success rate'].append('0/0')
		self.__quiz_questions['success %'].append('0%')

		for question_text in self.__quiz_questions:
			if question_text not in ['n questions', 'success rate', 'success %']:
				self.__quiz_questions[question_text].append('absent')
	
	def validate_answers(self, poll_name, poll_submission):
		self.__quiz_questions['n questions'].append(0)
		self.__quiz_questions['success rate'].append('0/0')
		self.__quiz_questions['success %'].append('')

		for question in self.__answer_key_parser.answer_keys[poll_name]:
			self.__quiz_questions['n questions'][-1] += 1
			if question in poll_submission.questions_answers:
				if poll_submission.questions_answers[question] == self.__answer_key_parser.answer_keys[poll_name][question]:
					self.__quiz_questions[question.text].append('1')
					new_correct = int(self.__quiz_questions['success rate'][-1][0]) + 1
					self.__quiz_questions['success rate'][-1] = str(new_correct) + self.__quiz_questions['success rate'][-1][1:]
				else:
					self.__quiz_questions[question.text].append('0')
			else:
				self.__quiz_questions[question.text].append('0')
			total_correct = int(self.__quiz_questions['success rate'][-1][0])
			self.__quiz_questions['success rate'][-1] = '{}/{}'.format(total_correct, self.__quiz_questions['n questions'][-1])
			self.__quiz_questions['success %'][-1] = '{}%'.format(round(total_correct * 100.0 / self.__quiz_questions['n questions'][-1], 2))

	def update_answer_distribution(self, poll_submission):
		for question in poll_submission.questions_answers:
			for answer in poll_submission.questions_answers[question]:
				self.__answer_distribution[question].setdefault(answer, 0)
				self.__answer_distribution[question][answer] += 1

	def create_chart(self):
		q_counter = 1
		for question in self.__answer_distribution:
			if question.is_multiple_choice:
				self.create_bar_chart(question, q_counter)
			else:
				self.create_pie_chart(question, q_counter)
			q_counter += 1

	def create_pie_chart(self, question, q_counter):
		labels = []
		sizes = []
		explode = []
		for answer in self.__answer_distribution[question]:
			if answer.is_correct:
				explode.append(0.1)
			else:
				explode.append(0)
			labels.append(answer.text)
			sizes.append(self.__answer_distribution[question][answer])

		fig1, ax1 = plt.subplots()
		ax1.pie(sizes, explode=explode, labels=labels, autopct='%1.1f%%', startangle=90, shadow=True)
		ax1.axis('equal')  # Equal aspect ratio ensures that pie is drawn as a circle.
		ax1.set_title(question.text, pad=20, fontfamily='serif')
		plt.savefig('Q' + str(q_counter) + '.png', bbox_inches='tight')

	def create_bar_chart(self, question, q_counter):
		pass
