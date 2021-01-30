class Question:
	def __init__(self, _id, text, is_multiple_choice=False):
		self.__id = _id
		self.__text = text
		self.__is_multiple_choice = is_multiple_choice
		self.__poll = None
		self.__poll_submissions = []

	@property
	def id(self):
		return self.__id

	@id.setter
	def id(self, new_id):
		self.__id = new_id

	@property
	def text(self):
		return self.__text

	@text.setter
	def text(self, new_text):
		self.__text = new_text

	@property
	def is_multiple_choice(self):
		return self.__is_multiple_choice

	@is_multiple_choice.setter
	def is_multiple_choice(self, value):
		self.__is_multiple_choice = value

	@property
	def poll(self):
		return self.__poll

	@poll.setter
	def poll(self, poll):
		self.__poll = poll

	@property
	def poll_submissions(self):
		return self.__poll_submissions
	
	def add_poll_submission(self, poll_submission):
		self.__poll_submissions.append(poll_submission)
