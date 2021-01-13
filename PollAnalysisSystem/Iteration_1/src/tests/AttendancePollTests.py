import unittest
from main.Poll import Poll
from main.AttendancePoll import AttendancePoll


class TestAttendancePollMethods(unittest.TestCase):
    
    def setUp(self):
        self.__attendance_poll_object = AttendancePoll('name', 'date', 'day')

    def test_name(self):
        # test getter
        self.assertEqual(self.__attendance_poll_object.name, 'name') 
        # test setter
        self.__attendance_poll_object.name = 'new name' 
        self.assertEqual(self.__attendance_poll_object.name, 'new name') 

    def test_date(self):
        # test getter
        self.assertEqual(self.__attendance_poll_object.date, 'date') 
        # test setter
        self.__attendance_poll_object.date = 'new date' 
        self.assertEqual(self.__attendance_poll_object.date, 'new date') 

    def test_day(self):
        # test getter
        self.assertEqual(self.__attendance_poll_object.day, 'day') 
        # test setter
        self.__attendance_poll_object.day = 'new day' 
        self.assertEqual(self.__attendance_poll_object.day, 'new day') 

    def test_poll_submissions(self):
        self.__attendance_poll_object.add_poll_submission('poll submission') # inherited method
        self.assertTrue(self.__attendance_poll_object.poll_submissions == ['poll submission'])

    def test_questions_answers(self):
        expected_dict = {'quesiton1': ['answer1'], 'question2': ['answer2']}
        # inherited method
        self.__attendance_poll_object.add_questions_answers(['quesiton1', 'question2'], [['answer1'], ['answer2']]) 
        self.assertTrue(self.__attendance_poll_object.questions_answers == expected_dict)

