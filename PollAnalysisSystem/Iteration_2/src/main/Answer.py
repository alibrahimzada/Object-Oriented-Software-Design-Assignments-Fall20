class Answer:

	def __init__(self, _id, text, is_correct=False):
		self.__id = _id
		self.__text = text
		self.__is_correct = is_correct
		self.__poll = None
		self.__poll_submissions = []

	@property
	def id(self):
		return self.__id

	@id.setter
	def id(self, value):
		self.__id = value

	# text attribute
	@property
	def text(self):
		return self.__text

	@text.setter
	def text(self, value):
		self.__text = value

	# Correct or Not attribute
	@property
	def is_correct(self):
		return self.__is_correct

	@is_correct.setter
	def is_correct(self, value):
		self.__is_correct = value

	@property
	def poll(self):
		return self.__poll

	@poll.setter
	def poll(self, poll):
		self.__poll = poll

	# Submissions attribute
	@property
	def poll_submissions(self):
		return self.__poll_submissions

	def add_poll_submission(self, poll_submission):
		self.__poll_submissions.append(poll_submission)
