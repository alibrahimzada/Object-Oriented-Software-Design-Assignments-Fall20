class Question:
    def __init__(self, text, is_multiple_choice=False):
        self.__text = text
        self.__is_multiple_choice = is_multiple_choice
        self.__poll_submissions = []
    
    @property
    def text(self):
        return self.__text
    
    @text.setter
    def text(self, new_text):
        self.__text = new_text
    
    @property
    def is_multiple_choice(self):
        return self.__is_multiple_choice

    @is_multiple_choice.setter
    def is_multiple_choice(self, value):
        self.__is_multiple_choice = value

    @property
    def poll_submissions(self):
        return self.__poll_submissions
