class Department:
    def __init__(self, name):
        self.name = name


    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, value):
        self.__name = value
