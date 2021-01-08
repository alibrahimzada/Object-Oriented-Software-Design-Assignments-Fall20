class Instructor:
    def _init_(self, name, department):
        self.__name = name
        self.__department = department
        self.__registrations= []

    # name attribute
    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, value):
        self.__name = value


    # department attribute
    @property
    def department(self):
        return self.__department

    @department.setter
    def department(self, value):
        self.__department = value
        
    # registrations attribute
    @property
    def registrations(self):
        return self.__registrations

    def add_registration(self, value):
        self.__registrations.append(value)
