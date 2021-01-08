class Answer:

    def __init__(self, text, is_correct=False):
        self.__text = text
        self.__is_correct = is_correct
        self.__poll_submissions = []
    
    @property
    def text(self):
        return self.__text
    
    @text.setter
    def text(self, value):
        self.__text = value
    
    @property
    def is_correct(self):
        return self.__is_correct

    @is_correct.setter
    def is_correct(self, value):
        self.__is_correct = value
    
    @property
    def poll_submissions(self):
        return self.__poll_submissions

    def add_poll_submission(self, value):
        self.__poll_submissions.append(value)
    
