


class Poll(object):
	def __init__(self, name, date, day):
		self.__name = name
		self.__date = date
		self.__day = day
		self.__poll_submissions = []

	@property
    def name(self):
        return self.__name
    
    @name.setter
    def name(self, value):
        self.__name = value

	@property
    def date(self):
        return self.__date
    
    @date.setter
    def date(self, value):
        self.__date = value

	@property
    def day(self):
        return self.__day
    
    @day.setter
    def day(self, value):
        self.__day = value
