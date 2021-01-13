import unittest
from main.Department import Department


class TestDepartmentMethods(unittest.TestCase):

    def setUp(self):
        self.__department_object = Department('name')

    def test_name(self):
        # test getter
        self.assertEqual(self.__department_object.name, 'name') 
        # test setter
        self.__department_object.name = 'new name' 
        self.assertEqual(self.__department_object.name, 'new name')
    
    def test_add_instructor(self):
        key = 'key'
        value = 'value'
        self.__department_object.add_instructor(key, value)
        self.assertIn(key, self.__department_object.instructors.keys())
        self.assertIn(value, self.__department_object.instructors.values())

    def test_add_student(self):
        key = 'key'
        value = 'value'
        self.__department_object.add_student(key, value)
        self.assertIn(key, self.__department_object.students.keys())
        self.assertIn(value, self.__department_object.students.values())