

class Poll(object):

    def __init__(self, name, date, day):
        self._name = name
        self._date = date
        self._day = day
        self._poll_submissions = []

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

    def add_poll_submission(self, value):
        self._poll_submissions.append(value)
