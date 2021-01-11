
from main.Poll import Poll

class AttendancePoll(Poll):

	def __init__(self, name, date, day):
		Poll.__init__(self, name, date, day)

	@property
	def name(self):
		return self._name

	@name.setter
	def name(self, value):
		self._name = value

	@property
	def date(self):
		return self._date

	@date.setter
	def date(self, value):
		self._date = value

	@property
	def day(self):
		return self._day

	@day.setter
	def day(self, value):
		self._day = value

	@property
	def poll_submissions(self):
		return self._poll_submissions

	@property
	def questions_answers(self):
		return self._questions_answers
