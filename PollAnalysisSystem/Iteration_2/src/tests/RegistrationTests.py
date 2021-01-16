import unittest
from main.Registration import Registration


class TestRegistrationMethods(unittest.TestCase):

    def setUp(self):
        self.__registration_object = Registration('academic_year', 'academic_semester', 'student', 'course', 'instructor')

    def test_academic_year(self):
        # test getter
        self.assertEqual(self.__registration_object.academic_year, 'academic_year') 
        # test setter
        self.__registration_object.academic_year = 'new academic_year' 
        self.assertEqual(self.__registration_object.academic_year, 'new academic_year') 

    def test_academic_semester(self):
        # test getter
        self.assertEqual(self.__registration_object.academic_semester, 'academic_semester') 
        # test setter
        self.__registration_object.academic_semester = 'new academic_semester' 
        self.assertEqual(self.__registration_object.academic_semester, 'new academic_semester') 

    def test_student(self):
        # test getter
        self.assertEqual(self.__registration_object.student, 'student') 
        # test setter
        self.__registration_object.student = 'new student' 
        self.assertEqual(self.__registration_object.student, 'new student')  

    def test_course(self):
        # test getter
        self.assertEqual(self.__registration_object.course, 'course') 
        # test setter
        self.__registration_object.course = 'new course' 
        self.assertEqual(self.__registration_object.course, 'new course')  

    def test_instructor(self):
        # test getter
        self.assertEqual(self.__registration_object.ects, 'instructor') 
        # test setter
        self.__registration_object.ects = 'new instructor' 
        self.assertEqual(self.__registration_object.ects, 'new instructor') 
