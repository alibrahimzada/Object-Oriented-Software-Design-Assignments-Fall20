

class Instructor(object):
	def __init__(self, name):
		self.__name = name
		self.__department = ''
		self.__registrations= {}   # {Course : [Registration, ...], ...}

	# name attribute
	@property
	def name(self):
		return self.__name

	@name.setter
	def name(self, value):
		self.__name = value

	# department attribute
	@property
	def department(self):
		return self.__department

	@department.setter
	def department(self, value):
		self.__department = value
		
	# registrations attribute
	@property
	def registrations(self):
		return self.__registrations

	def add_registration(self, key, value):
		self.__registrations.setdefault(key, [])
		self.__registrations[key].append(value)
