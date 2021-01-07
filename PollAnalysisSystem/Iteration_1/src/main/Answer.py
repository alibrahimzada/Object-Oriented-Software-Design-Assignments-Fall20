class Answer:
    def __init__(self, text, is_correct=False):
        self.__text = text
        self.__isCorrect = is_correct
    
    @property
    def text(self):
        return self.__text
    
    @text.setter
    def text(self, value):
        self.__text = value
    
    @property
    def isCorrect(self):
        return self.__isCorrect

    @isCorrect.setter
    def isCorrect(self, value):
        self.__isCorrect = value
    