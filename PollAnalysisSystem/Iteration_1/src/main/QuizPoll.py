class QuizPoll:
    def __init__(self, name, day, date):
        self.__name = name
        self.__day = day
        self.__date = date

    # Name attribute
    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, value):
        self.__name = value

    # Day attribute
    @property
    def day(self):
        return self.__day

    @day.setter
    def day(self, value):
        self.__day = value

    # Date attribute
    @property
    def date(self):
        return self.__date

    @date.setter
    def date(self, value):
        self.__date = value
