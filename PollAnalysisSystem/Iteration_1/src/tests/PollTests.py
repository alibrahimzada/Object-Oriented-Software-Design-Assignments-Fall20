import unittest
from main.Poll import Poll


class TestPollMethods(unittest.TestCase):
    
    def setUp(self):
        self.__poll_object = Poll('name', 'date', 'day')

    def test_poll_submissions(self):
        self.__poll_object.add_poll_submission('poll submission') 
        self.assertTrue(self.__poll_object.poll_submissions == ['poll submission'])

    def test_questions_answers(self):
        expected_dict = {'quesiton1': ['answer1'], 'question2': ['answer2']}
        self.__poll_object.add_questions_answers(['quesiton1', 'question2'], [['answer1'], ['answer2']]) 
        self.assertTrue(self.__poll_object.questions_answers == expected_dict)