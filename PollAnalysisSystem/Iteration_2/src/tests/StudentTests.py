import unittest
from main.Student import Student


class TestStudentMethods(unittest.TestCase):

    def setUp(self):
        self.__student_object = Student('id', 'name', 'surname', 'department', 'description')

    def test_id(self):
        # test getter
        self.assertEqual(self.__student_object.id, 'id') 
        # test setter
        self.__student_object.id = 'new id' 
        self.assertEqual(self.__student_object.id, 'new id')

    def test_name(self):
        # test getter
        self.assertEqual(self.__student_object.name, 'name') 
        # test setter
        self.__student_object.name = 'new name' 
        self.assertEqual(self.__student_object.name, 'new name')

    def test_surname(self):
        # test getter
        self.assertEqual(self.__student_object.surname, 'surname') 
        # test setter
        self.__student_object.surname = 'new surname' 
        self.assertEqual(self.__student_object.surname, 'new surname')

    def test_department(self):
        # test getter
        self.assertEqual(self.__student_object.department, 'department') 
        # test setter
        self.__student_object.department = 'new department' 
        self.assertEqual(self.__student_object.department, 'new department')

    def test_description(self):
        # test getter
        self.assertEqual(self.__student_object.description, 'description') 
        # test setter
        self.__student_object.description = 'new description' 
        self.assertEqual(self.__student_object.description, 'new description')
    
    def test_email(self):
        # test setter & getter
        self.__student_object.email = 'new email' 
        self.assertEqual(self.__student_object.email, 'new email')

    def test_add_registration(self):
        self.__student_object.add_registration('regstration')
        self.assertIn('regstration', self.__student_object.registrations)

    def test_add_poll_submission(self):
        key = 'key'
        actual_value = 'value'
        self.__student_object.add_poll_submission(key, actual_value)
        self.assertIn(key, self.__student_object.poll_submissions.keys())
        expected_value_list = self.__student_object.poll_submissions[key]
        self.assertIn(actual_value, expected_value_list)
    

    