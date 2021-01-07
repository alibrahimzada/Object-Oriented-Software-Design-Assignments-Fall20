class Answer:
    def __init__(self, text):
        self.__text = text
        self.__isCorrect = False
    
    @property
    def text(self):
        return self.__text
    
    @text.setter
    def academic_year(self, value):
        self.__text = value
    
    @property
    def is_correct(self):
        return self.__isCorrect
    #Will add the setter for iscorrect soon but I have to read about this first.