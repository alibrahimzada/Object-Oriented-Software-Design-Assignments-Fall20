import os
import pandas as pd
import matplotlib.pyplot as plt
from datetime import date

from main.AttendancePoll import AttendancePoll

class StatsReportSerializer(object):

	def __init__(self, poll_analysis_system):
		self.__poll_analysis_system = poll_analysis_system
		self.__global_df = self.__create_global_df()
		self.__quiz_student_report = {}
		self.__global_accuracy = {}

	def export_reports(self):
		if len(self.__poll_analysis_system.poll_parser.polls) < 1:
			self.__poll_analysis_system.logger.error("Failed to export the stats and global due to unloaded files.")
			return 
		for poll_name in self.__poll_analysis_system.poll_parser.polls:
			poll = self.__poll_analysis_system.poll_parser.polls[poll_name]
			if isinstance(poll, AttendancePoll): continue
			self.__export_quiz_report(poll_name, poll)
			self.__poll_analysis_system.logger.info(f'Statistics Report for {poll_name} was exported successfully.')
		self.__poll_analysis_system.logger.info('All Statistics Reports were exported successfully.')
		self.__export_global_report()
		self.__poll_analysis_system.logger.info('Global Report was exported successfully')
		self.__export_quiz_student_reports()
		self.__poll_analysis_system.logger.info('Quiz-Student Reports was exported successfully')

	def __export_quiz_report(self, poll_name, poll):
		if not os.path.exists('statistics'):
			os.mkdir('statistics')
		os.chdir('statistics')
		if not os.path.exists(poll_name):
			os.mkdir(poll_name)
		os.chdir(poll_name)

		self.__answer_distribution = {}
		self.__quiz_questions = {'number of questions': [], 
								 'number of correctly answered questions': [], 
								 'number of wrongly answered questions': [],
								 'number of empty questions': [],
								 'rate of correctly answered questions': [],
								 'accuracy percentage': []}

		poll_name_without_date = ' '.join(poll_name.split()[:-1])
		for question in self.__poll_analysis_system.answer_key_parser.answer_keys[poll_name_without_date]:
			self.__answer_distribution.setdefault(question, {})

		student_numbers, names, surnames, remarks = [], [], [], []
		for std_list in self.__poll_analysis_system.student_list_parser.registrations:
			for registration in self.__poll_analysis_system.student_list_parser.registrations[std_list]:
				student_numbers.append(registration.student.id)
				names.append(registration.student.name)
				surnames.append(registration.student.surname)
				remarks.append(registration.student.description)
				poll_submissions = registration.student.poll_submissions
				df_name = '{} {} {} {} {} {}'.format(poll.name, poll.day, poll.time, registration.student.name, registration.student.surname, registration.student.id).replace(' ', '_')
				self.__global_accuracy.setdefault(registration.student.id, [0, 0])
				self.__quiz_student_report.setdefault(df_name, pd.DataFrame(columns=['question text',
																					 'given answer',
																					 'correct answer',
																					 'Correctness']))

				if poll_name not in poll_submissions:
					self.__export_absent_student()
					self.__global_accuracy[registration.student.id][1] += len(poll.questions_answers)
					continue
				for poll_submission in poll_submissions[poll_name]:
					self.__validate_answers(poll_name_without_date, df_name, poll_submission, registration.student.id)
					self.__update_answer_distribution(poll_submission)

		self.__create_chart()

		quiz_df = pd.DataFrame()
		quiz_df['Student ID'] = student_numbers
		quiz_df['Name'] = names
		quiz_df['Surname'] = surnames
		quiz_df['Remarks'] = remarks
		student_info = {'Student ID': student_numbers, 'Name': names, 'Surnames': surnames, 'Remarks': remarks}

		quiz_df['number of questions'] = self.__quiz_questions['number of questions']
		quiz_df['number of correctly answered questions'] = self.__quiz_questions['number of correctly answered questions']
		quiz_df['number of wrongly answered questions'] = self.__quiz_questions['number of wrongly answered questions']
		quiz_df['number of empty questions'] = self.__quiz_questions['number of empty questions']
		quiz_df['rate of correctly answered questions'] = self.__quiz_questions['rate of correctly answered questions']
		quiz_df['accuracy percentage'] = self.__quiz_questions['accuracy percentage']

		filename = '{} {} {}.xlsx'.format(poll.name, poll.day, poll.time).replace(' ', '_')
		quiz_df.to_excel(filename, index=False)
		os.chdir('..')
		os.chdir('..')
		self.__add_global_report(poll, student_info)

	def __add_global_report(self, poll, student_info):
		for column in student_info:
			if column in self.__global_df: continue
			self.__global_df[column] = student_info[column]
		
		column_name = '{} {}'.format(poll.name, poll.time).replace(' ', '_')
		if column_name not in self.__global_df:
			self.__global_df[column_name] = self.__quiz_questions['rate of correctly answered questions']

	def __create_global_df(self):
		if not os.path.exists('global_report'):
			return pd.DataFrame()
		else:
			return pd.read_csv(os.path.join('global_report', 'CSE3063_2020FALL_QuizGrading.csv'))

	def __export_global_report(self):
		if not os.path.exists('global_report'):
			os.mkdir('global_report')
		os.chdir('global_report')

		global_accuracy = []
		for student_id in self.__global_df['Student ID']:
			global_accuracy.append(round(self.__global_accuracy[student_id][0] / self.__global_accuracy[student_id][1] * 100, 2))

		if 'global accuracy' not in self.__global_df:
			self.__global_df['global accuracy'] = global_accuracy
		self.__global_df.to_csv('CSE3063_2020FALL_QuizGrading.csv')
		os.chdir('..')
	
	def __export_quiz_student_reports(self):
		if not os.path.exists('quiz_student_report'):
			os.mkdir('quiz_student_report')
		os.chdir('quiz_student_report')

		for df_name in self.__quiz_student_report:
			self.__quiz_student_report[df_name].to_excel(df_name + '.xlsx', index=False)

		os.chdir('..')

	def __export_absent_student(self):
		self.__quiz_questions['number of questions'].append(0)
		self.__quiz_questions['number of correctly answered questions'].append(0)
		self.__quiz_questions['number of wrongly answered questions'].append(0)
		self.__quiz_questions['number of empty questions'].append(0)
		self.__quiz_questions['rate of correctly answered questions'].append('0/0')
		self.__quiz_questions['accuracy percentage'].append(0)
	
	def __validate_answers(self, poll_name, df_name, poll_submission, student_id):
		self.__quiz_questions['number of questions'].append(0)
		self.__quiz_questions['number of correctly answered questions'].append(0)
		self.__quiz_questions['number of wrongly answered questions'].append(0)
		self.__quiz_questions['number of empty questions'].append(0)
		self.__quiz_questions['rate of correctly answered questions'].append('0/0')
		self.__quiz_questions['accuracy percentage'].append('')

		answer_key_parser = self.__poll_analysis_system.answer_key_parser
		df_row = ['', '', '', '']
		for question in answer_key_parser.answer_keys[poll_name]:   # loop over all available questions in the given poll
			df_row[0] = question.text
			self.__quiz_questions['number of questions'][-1] += 1   # increment number of questions
			self.__global_accuracy[student_id][1] += 1
			if question in poll_submission.questions_answers:   # check if student answered the question
				my_answers = [answer.text for answer in poll_submission.questions_answers[question]]
				correct_answers = [answer.text for answer in answer_key_parser.answer_keys[poll_name][question]]
				intersection = set(my_answers) & set(correct_answers)
				df_row[1] = ';'.join(my_answers)
				df_row[2] = ';'.join(correct_answers)
				if len(intersection) > 0:
					df_row[3] = '1'
					self.__global_accuracy[student_id][0] += 1
					self.__quiz_questions['number of correctly answered questions'][-1] += 1
					new_correct = int(self.__quiz_questions['rate of correctly answered questions'][-1].split('/')[0]) + 1
					total = self.__quiz_questions['rate of correctly answered questions'][-1].split('/')[1]
					self.__quiz_questions['rate of correctly answered questions'][-1] = str(new_correct) + '/' + total
				else:
					df_row[3] = '0'
					self.__quiz_questions['number of wrongly answered questions'][-1] += 1
			else:   # increment number of empty questions, since student did not answered the question
				self.__quiz_questions['number of empty questions'][-1] += 1

			self.__quiz_student_report[df_name].loc[len(self.__quiz_student_report[df_name])] = df_row
			total_correct = int(self.__quiz_questions['rate of correctly answered questions'][-1].split('/')[0])
			self.__quiz_questions['rate of correctly answered questions'][-1] = '{}/{}'.format(total_correct, self.__quiz_questions['number of questions'][-1])
			self.__quiz_questions['accuracy percentage'][-1] = '{}%'.format(round(total_correct * 100.0 / self.__quiz_questions['number of questions'][-1], 2))

	def __update_answer_distribution(self, poll_submission):
		for question in poll_submission.questions_answers:
			for answer in poll_submission.questions_answers[question]:
				self.__answer_distribution[question].setdefault(answer, 0)
				self.__answer_distribution[question][answer] += 1

	def __create_chart(self):
		q_counter = 1
		for question in self.__answer_distribution:
			if question.is_multiple_choice:
				self.__create_bar_chart(question, q_counter)
			else:
				self.__create_pie_chart(question, q_counter)
			q_counter += 1

	def __create_pie_chart(self, question, q_counter):
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

	def __create_bar_chart(self, question, q_counter):
		fig = plt.figure()
		colors = []
		sizes = []
		labels = {}
		label_counter = 1
		for answer in self.__answer_distribution[question]:
			if answer.is_correct:
				colors.append('green')
			else:
				colors.append('red')
			labels.setdefault('Ans. ' + str(label_counter), answer.text)
			sizes.append(self.__answer_distribution[question][answer])
			plt.bar(['Ans. ' + str(label_counter)], sizes[-1], color=colors[-1], label='Ans. ' + 
					str(label_counter) + ' : ' + labels['Ans. ' + str(label_counter)])
			label_counter += 1
		plt.ylabel('Number Of Students')
		plt.title(question.text)
		plt.legend(loc='lower center', bbox_to_anchor=[0.5, -0.35])
		plt.savefig('Q' + str(q_counter) + '.png', bbox_inches='tight')
