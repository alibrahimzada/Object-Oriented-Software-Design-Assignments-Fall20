import unittest
from main.Question import Question 


class TestQuestionMethods(unittest.TestCase):
    
    def setUp(self):
        self.__question_object = Question(1, 'a question', True)

    def test_text(self):
        # test getter
        self.assertEqual(self.__question_object.text, 'a question') 
        # test setter
        self.__question_object.text = 'new question' 
        self.assertEqual(self.__question_object.text, 'new question') 

    def test_is_multiple_choice(self):
        # test getter
        self.assertEqual(self.__question_object.is_multiple_choice, True)
        # test setter
        self.__question_object.is_multiple_choice = False
        self.assertEqual(self.__question_object.is_multiple_choice, False)

    def test_poll(self):
        # test setter & getter
        self.__question_object.poll = 'poll placeholder'
        self.assertEqual(self.__question_object.poll, 'poll placeholder')

    def test_add_poll_submission(self):
        placeholder = 'Poll Submission Placeholder'
        self.__question_object.add_poll_submission(placeholder)
        self.assertIn(placeholder, self.__question_object.poll_submissions)

