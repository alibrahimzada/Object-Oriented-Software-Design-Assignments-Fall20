import xlrd
from unidecode import unidecode
import ntpath

from main.Course import Course
from main.Department import Department
from main.Instructor import Instructor
from main.Student import Student
from main.Registration import Registration

class StudentListParser(object):
	def __init__(self, poll_analysis_system):
		self.__poll_analysis_system = poll_analysis_system
		self.__registrations = {}

	@property
	def poll_analysis_system(self):
		return self.__poll_analysis_system

	@poll_analysis_system.setter
	def poll_analysis_system(self, value):
		self.__poll_analysis_system = value

	@property
	def registrations(self):
		return self.__registrations

	@registrations.setter
	def registrations(self, value):
		self.__registrations = value
	
	def get_student(self, full_name):
		if full_name == 'Ayşe Karahasan': full_name = 'Ayşenur Karahasan'

		for student_list in self.__registrations:
			for registration in self.__registrations[student_list]:
				first_name = registration.student.name.lower()
				last_name = registration.student.surname.lower()
				lower_full_name = full_name.lower()
				splitted_full_name = lower_full_name.split()
				first_last = first_name + ' ' + last_name
				splitted_first_last = first_last.split()

				counter = 0
				for word in set(splitted_first_last):
					if unidecode(word) in unidecode(lower_full_name):
						counter += 1
				if unidecode(first_last) == 'ahmet fazil emir' and unidecode(lower_full_name) == 'ahmet hamza demir':
					continue
				if unidecode(first_last) == 'mehmet ali gozukizil' and unidecode(lower_full_name) == 'mehmet ali yuksel':
					continue
				elif counter > 1:
					return registration.student
		return None
	
	def __parse_sheet(self, sheet, file_path):
		file_name = ntpath.basename(file_path).split('.')[0]
		self.__registrations.setdefault(file_name, [])

		i = 0
		academic_year, academic_semester = '', ''
		while i < sheet.nrows and sheet.row_values(i):
			# first we will check for the general info that starts with a Date and ends with department name
			if type(sheet.row_values(i)[0]) == float and sheet.row_values(i)[1] == '':
				i += 2
				year_semester = sheet.row_values(i)[0]
				academic_year = ' '.join(year_semester.split(' ')[:2])
				academic_semester = ' '.join(year_semester.split(' ')[2:])

				i += 4
				# getting information about the course
				codes = sheet.row_values(i)[6].split(" ")
				course_code = codes[0]
				theoritical_credit = codes[1][1:]
				practical_credit = codes[3][:-1]
				ects = codes[4]
				course_name = ' '.join(codes[6:])
				course = Course(code=course_code, name=course_name, theoretical_credit=theoritical_credit,
								practical_credit=practical_credit, ects=ects)

				i += 2
				# getting information about the instructor
				instructor_name = sheet.row_values(i)[6]
				instructor = Instructor(name=instructor_name)

				i += 2

			elif sheet.row_values(i)[1] == "No":
				if "(" in sheet.row_values(i - 2)[0]:
					department = Department(name=sheet.row_values(i - 2)[0].split("(")[0])
				else:
					department = Department(name=sheet.row_values(i - 2)[0])
				i += 1

			elif type(sheet.row_values(i)[1] == float):
				if sheet.row_values(i)[1] != "":
					std_num = sheet.row_values(i)[2]
					name = sheet.row_values(i)[4]
					surname = sheet.row_values(i)[7]
					description = sheet.row_values(i)[10]

					student = Student(_id=std_num, name=name, surname=surname, department=department, description=description)

					instructor.department = department
					
					registration = Registration(academic_year=academic_year, academic_semester=academic_semester,
												student=student, course=course, instructor=instructor)
					
					instructor.add_registration(course, registration)
					department.add_instructor(instructor.name, instructor)
					department.add_student(student.id, student)
					course.add_registration(registration)
					student.add_registration(registration)
					self.__registrations[file_name].append(registration)

					i += 1
				else:
					i += 1
			else:
				i += 1

	def parse_student_list(self, student_list_files):
		for filename in student_list_files:
			workbook = xlrd.open_workbook(filename, encoding_override='cp1252')
			sheet = workbook.sheet_by_index(0)
			self.__parse_sheet(sheet, filename)
			self.poll_analysis_system.logger.info(f'Student List: {filename.split("/")[-1]} was parsed successfully.')
		self.poll_analysis_system.logger.info('All Student Lists were parsed successfully')
