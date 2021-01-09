

class Student(object):
	def __init__(self, _id, name, surname, department):
		self.__id = _id
		self.__name = name
		self.__surname = surname
		self.__email = ''
		self.__department = department
		self.__registrations= []
		self.__poll_submissions = {}

	# id  attribute
	@property
	def id(self):
		return self.__id

	@id.setter
	def id(self, value):
		self.__id = value

	# name attribute
	@property
	def name(self):
		return self.__name

	@name.setter
	def name(self, value):
		self.__name = value

	# surname attribute
	@property
	def surname(self):
		return self.__surname

	@surname.setter
	def surname(self, value):
		self.__surname = value

	# email attribute
	@property
	def email(self):
		return self.__email

	@email.setter
	def email(self, value):
		self.__email = value

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
		
	def add_registration(self, value):
		self.__registrations.append(value)

	# poll_submissions attribute
	@property
	def poll_submissions(self):
		return self.__poll_submissions
		
	def add_poll_submission(self, key, value):
		self.__poll_submissions.setdefault(key, [])
		self.__poll_submissions[key].append(value)
