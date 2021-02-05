

class Poll(object):

	def __init__(self, name, date, day):
		self._name = name
		self._date = date
		self._day = day
		self._time = ''
		self._poll_submissions = []
		self._questions_answers = {}

	def add_poll_submission(self, value):
		self._poll_submissions.append(value)

	def add_questions_answers(self, questions, answers):
		for i in range(len(questions)):
			self._questions_answers.setdefault(questions[i], [])
			for answer in answers[i]:
				if answer not in self._questions_answers[questions[i]]:
					self._questions_answers[questions[i]].append(answer)
