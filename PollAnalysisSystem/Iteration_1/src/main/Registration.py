

class Registration(object):
    def __init__(self, academic_year, academic_semester, student, course, instructor):
        self.__academic_year = academic_year
        self.__academic_semester = academic_semester
        self.__student = student
        self.__course = course
        self.__instructor = instructor

    # Academic Year attribute
    @property
    def academic_year(self):
        return self.__academic_year

    @academic_year.setter
    def academic_year(self, value):
        self.__academic_year = value

    # Academic Semester attribute
    @property
    def academic_semester(self):
        return self.__academic_semester

    @academic_semester.setter
    def academic_semester(self, value):
        self.__academic_semester = value

    # Student attribute
    @property
    def student(self):
        return self.__student

    @student.setter
    def student(self, value):
        self.__student = value

    # Course attribute
    @property
    def course(self):
        return self.__course

    @course.setter
    def course(self, value):
        self.__course = value

    # Instructor attribute
    @property
    def instructor(self):
        return self.__instructor

    @instructor.setter
    def instructor(self, value):
        self.__instructor = value
