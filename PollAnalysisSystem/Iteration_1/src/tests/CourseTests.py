import unittest
from main.Course import Course

class TestCourseMethods(unittest.TestCase):
    def setUp(self):
        self.__course_object = Course('code', 'name', 'theoretical_credit', 'practical_credit', 'ects')

    def test_code(self):
        # test getter
        self.assertEqual(self.__course_object.code, 'code') 
        # test setter
        self.__course_object.code = 'new code' 
        self.assertEqual(self.__course_object.code, 'new code') 

    def test_name(self):
        # test getter
        self.assertEqual(self.__course_object.name, 'name') 
        # test setter
        self.__course_object.name = 'new name' 
        self.assertEqual(self.__course_object.name, 'new name') 

    def test_theoretical_credit(self):
        # test getter
        self.assertEqual(self.__course_object.theoretical_credit, 'theoretical_credit') 
        # test setter
        self.__course_object.theoretical_credit = 'new theoretical_credit' 
        self.assertEqual(self.__course_object.theoretical_credit, 'new theoretical_credit')  

    def test_practical_credit(self):
        # test getter
        self.assertEqual(self.__course_object.practical_credit, 'practical_credit') 
        # test setter
        self.__course_object.practical_credit = 'new practical_credit' 
        self.assertEqual(self.__course_object.practical_credit, 'new practical_credit')  

    def test_ects(self):
        # test getter
        self.assertEqual(self.__course_object.ects, 'ects') 
        # test setter
        self.__course_object.ects = 'new ects' 
        self.assertEqual(self.__course_object.ects, 'new ects') 

    def test_add_registration(self):
        placeholder = 'Registration Placeholder'
        self.__course_object.add_registration(placeholder)
        self.assertIn(placeholder, self.__course_object.registrations)
