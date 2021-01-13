import unittest
from main.PollSubmission import PollSubmission
from main.Question import Question
from main.Answer import Answer


class TestPollSubmissionMethods(unittest.TestCase):
    def setUp(self):
        self.__poll_submission_object = PollSubmission('date', 'poll', 'student')

    def test_date(self):
        # test getter
        self.assertEqual(self.__poll_submission_object.date, 'date') 
        # test setter
        self.__poll_submission_object.date = 'new date' 
        self.assertEqual(self.__poll_submission_object.date, 'new date') 

    def test_poll(self):
        # test getter
        self.assertEqual(self.__poll_submission_object.poll, 'poll') 
        # test setter
        self.__poll_submission_object.poll = 'new poll' 
        self.assertEqual(self.__poll_submission_object.poll, 'new poll') 

    def test_student(self):
        # test getter
        self.assertEqual(self.__poll_submission_object.student, 'student') 
        # test setter
        self.__poll_submission_object.student = 'new student' 
        self.assertEqual(self.__poll_submission_object.student, 'new student') 

    def test_add_questions_answers(self):
        question1, question2 = Question('q1'), Question('q2')
        answer1, answer2  = Answer('a1'), Answer('a2')
        questions, answers = [question1, question2], [[answer1], [answer2]]
        self.__poll_submission_object.add_questions_answers(questions, answers)
        expected_dict = {question1: [answer1], question2: [answer2]}
        actual_dict = self.__poll_submission_object.questions_answers
        self.assertEqual(expected_dict, actual_dict)
        # tests if poll submission was added to each question and answer
        for question, answers in actual_dict.items():
            self.assertIn(self.__poll_submission_object, question.poll_submissions)
            for answer in answers:
                self.assertIn(self.__poll_submission_object, answer.poll_submissions)

