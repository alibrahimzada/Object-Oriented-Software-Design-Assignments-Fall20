import unittest 
from main.Instructor import Instructor


class TestInstructorMethods(unittest.TestCase):

    def setUp(self):
        self.__instructor_object = Instructor('name')

    def test_name(self):
        # test getter
        self.assertEqual(self.__instructor_object.name, 'name') 
        # test setter
        self.__instructor_object.name = 'new name' 
        self.assertEqual(self.__instructor_object.name, 'new name')

    def test_department(self):
        # test setter & getter
        self.__instructor_object.department = 'new name' 
        self.assertEqual(self.__instructor_object.department, 'new name')

    def test_add_registration(self):
        key = 'key'
        actual_value = 'value'
        self.__instructor_object.add_registration(key, actual_value)
        self.assertIn(key, self.__instructor_object.registrations.keys())
        expected_value_list = self.__instructor_object.registrations[key]
        self.assertIn(actual_value, expected_value_list)