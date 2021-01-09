from datetime import datetime


class PollSubmission(object):
    def __init__(self, datetime, poll, student, questions, answers):
        self.__datetime = datetime
        self.__poll = poll
        self.__student = student
        self.__questions = questions
        self.__answers = answers

    # Date attribute
    @property
    def datetime(self):
        return self.__datetime

    @datetime.setter
    def datetime(self, value):
        self.__datetime = value

    # Poll attribute
    @property
    def poll(self):
        return self.__poll

    @poll.setter
    def poll(self, value):
        self.__poll = value

    # Student attribute
    @property
    def student(self):
        return self.__student

    @student.setter
    def student(self, value):
        self.__student = value

    # Questions attribute
    @property
    def questions(self):
        return self.__questions

    @questions.setter
    def questions(self, value):
        self.__questions.append(value)

    # Answers attribute
    @property
    def answers(self):
        return self.__answers

    @answers.setter
    def answers(self, value):
        self.__answers.append(value)
