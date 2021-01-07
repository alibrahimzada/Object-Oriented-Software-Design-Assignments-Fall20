class Course:
    def __init__(self, code, name, theoretical_credit, practical_credit, ects):
        self.__code = code
        self.name = name
        self.__theoretical_credit = theoretical_credit
        self.practical_credit = practical_credit
        self.ects = ects


    @property
    def code(self):
        return self.__code

    @code.setter
    def code(self, value):
        self.__code = value


    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, value):
        self.__name = value


    @property
    def theoretical_credit(self):
        return self.__theoretical_credit

    @theoretical_credit.setter
    def theoretical_credit(self, value):
        self.__theoretical_credit = value



    @property
    def practical_credit(self):
        return self.__practical_credit

    @practical_credit.setter
    def practical_credit(self, value):
        self.__practical_credit = value


    @property
    def ects(self):
        return self.__ects

    @ects.setter
    def ects(self, value):
        self.__ects = value