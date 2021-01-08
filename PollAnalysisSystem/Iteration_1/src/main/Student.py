class Student:
    def __init__(self, id, name, surname, email, department,registrations,poll_submissions):
        self.__id = id
        self.__name = name
        self.__surname = surname
        self.__email = email
        self.__department = department
        self.__registrations= []
        self.__poll_submissions = []

    # id Year attribute
    @property
    def id(self):
        return self.__id

    @id.setter
    def id(self, value):
        self.__id = value

    # name attribute
    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, value):
        self.__name = value

    # surname attribute
    @property
    def surname(self):
        return self.__surname

    @surname.setter
    def student(self, value):
        self.__surname = value

    # email attribute
    @property
    def email(self):
        return self.__email

    @email.setter
    def email(self, value):
        self.__email = value

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

    # poll_submissions attribute
    @property
    def poll_submissions(self):
        return self.__poll_submissions
