

class Course(object):
	def __init__(self, code, name, theoretical_credit, practical_credit, ects):
		self.__code = code
		self.__name = name
		self.__theoretical_credit = theoretical_credit
		self.__practical_credit = practical_credit
		self.__ects = ects
		self.__registrations = []

	# Course code attribute
	@property
	def code(self):
		return self.__code

	@code.setter
	def code(self, value):
		self.__code = value

	# Course name attribute
	@property
	def name(self):
		return self.__name

	@name.setter
	def name(self, value):
		self.__name = value

	# Course Theoretical credits attribute
	@property
	def theoretical_credit(self):
		return self.__theoretical_credit

	@theoretical_credit.setter
	def theoretical_credit(self, value):
		self.__theoretical_credit = value

	# Course Practical credits attribute
	@property
	def practical_credit(self):
		return self.__practical_credit

	@practical_credit.setter
	def practical_credit(self, value):
		self.__practical_credit = value

	# Course ECTS attribute
	@property
	def ects(self):
		return self.__ects

	@ects.setter
	def ects(self, value):
		self.__ects = value

	@property
	def registrations(self):
		return self.__registrations

	def add_registration(self, value):
		self.registrations.append(value)
