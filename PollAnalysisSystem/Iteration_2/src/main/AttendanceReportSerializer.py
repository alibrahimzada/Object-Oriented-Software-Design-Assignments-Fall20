import os
import pandas as pd
import json

class AttendanceReportSerializer(object):

	def __init__(self, poll_analysis_system):
		self.__poll_analysis_system = poll_analysis_system

	def export_attendance_report(self):
		try: 
			db = self.__get_db()
		except:
			self.__poll_analysis_system.logger.error("Failed to export the attendance report due to unloaded files.")
			return
		if not os.path.exists('attendance_report'):
			os.mkdir('attendance_report')
		os.chdir('attendance_report')

		attendance_stats = {'total': [], 'attended': []}
		student_list_parser = self.__poll_analysis_system.student_list_parser
		student_numbers, names, surnames, remarks = [], [], [], []

		if len(student_list_parser.registrations) < 1 or len(db)  < 1:
			self.__poll_analysis_system.logger.error("Failed to export the attendance report due to unloaded files.")
			return

		for std_list in student_list_parser.registrations:
			for registration in student_list_parser.registrations[std_list]:
				student_numbers.append(registration.student.id)
				names.append(registration.student.name)
				surnames.append(registration.student.surname)
				remarks.append(registration.student.description)

				attendance_stats['total'].append(len(db))
				attendance_stats['attended'].append(0)

				for date in db:
					if registration.student.id in db[date]:
						attendance_stats['attended'][-1] += 1

		 
		attendance_rate = [str(attendance_stats['attended'][i]) + '/' + str(attendance_stats['total'][i]) 
							for i in range(len(attendance_stats['total']))]
		attendance_percentage = ['{}%'.format(round(attendance_stats['attended'][i] * 100.0 / attendance_stats['total'][i], 2))
									for i in range(len(attendance_stats['total']))]
	

		data_frame = pd.DataFrame()
		data_frame['Student ID'] = student_numbers
		data_frame['Name'] = names
		data_frame['Surname'] = surnames
		data_frame['Remarks'] = remarks
		data_frame['total attendance'] = attendance_stats['total']
		data_frame['attendance rate'] = attendance_rate
		data_frame['attendance %'] = attendance_percentage

		data_frame.to_excel('attendance_report.xlsx', index=False)
		os.chdir('..')
		self.__poll_analysis_system.logger.info("Successfully exported Attendance Report")

	def __get_db(self):
		os.chdir('db')

		content = {}
		with open('db.json') as db:
			content = json.load(db)

		os.chdir('..')
		return content
