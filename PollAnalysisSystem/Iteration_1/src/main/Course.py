class Course:
    def __init__(self, code, name, theoretical_credits, practical_credits, ects):
        self.__code = code
        self.__name = name
        self.__theoretical_credits = theoretical_credits
        self.__practical_credits = practical_credits
        self.__ects = ects


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
    def theoretical_credits(self):
        return self.__theoretical_credits

    @theoretical_credits.setter
    def theoretical_credits(self, value):
        self.__theoretical_credits = value


    # Course Practical credits attribute
    @property
    def practical_credits(self):
        return self.__practical_credits

    @practical_credits.setter
    def practical_credits(self, value):
        self.__practical_credits = value


    # Course ECTS attribute
    @property
    def ects(self):
        return self.__ects

    @ects.setter
    def ects(self, value):
        self.__ects = value