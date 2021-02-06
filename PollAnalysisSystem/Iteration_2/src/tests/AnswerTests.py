import unittest
from main.Answer import Answer 


class TestAnswerMethods(unittest.TestCase):
    
    def setUp(self):
        self.__answer_object = Answer(1, 'an answer', True)

    def test_text(self):
        # test getter
        self.assertEqual(self.__answer_object.text, 'an answer') 
        # test setter
        self.__answer_object.text = 'new answer' 
        self.assertEqual(self.__answer_object.text, 'new answer') 

    def test_is_correct(self):
        # test getter
        self.assertEqual(self.__answer_object.is_correct, True)
        # test setter
        self.__answer_object.is_correct = False
        self.assertEqual(self.__answer_object.is_correct, False)

    def test_add_poll_submissions(self):
        placeholder = 'Poll Submission Placeholder'
        self.__answer_object.add_poll_submission(placeholder)
        self.assertIn(placeholder, self.__answer_object.poll_submissions)

