

class Department(object):
	def __init__(self, name):
		self.__name = name
		self.__instructors = {}
		self.__students = {}

	@property
	def name(self):
		return self.__name

	@name.setter
	def name(self, value):
		self.__name = value

	@property
	def instructors(self):
		return self.__instructors

	def add_instructor(self, key, value):
		self.__instructors.setdefault(key, value)
		self.__instructors[key] = value

	@property
	def students(self):
		return self.__students

	def add_student(self, key, value):
		self.__students.setdefault(key, value)
		self.__students[key] = value
