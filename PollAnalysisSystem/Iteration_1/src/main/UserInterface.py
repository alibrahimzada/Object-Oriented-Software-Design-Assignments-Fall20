

import sys
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QApplication, QMainWindow

class UserInterface(object):
  def setupUi(self, UserInterface):
        UserInterface.setObjectName("UserInterface")
        UserInterface.resize(748, 491)
        UserInterface.setStyleSheet("background-color:white")
        self.centralwidget = QtWidgets.QWidget(UserInterface)
        self.centralwidget.setObjectName("centralwidget")
        self.TitleLabel = QtWidgets.QLabel(self.centralwidget)
        self.TitleLabel.setGeometry(QtCore.QRect(0, 0, 751, 81))
        self.LoadStdListsBtn = QtWidgets.QPushButton(self.centralwidget)
        self.LoadStdListsBtn.setGeometry(QtCore.QRect(60, 150, 171, 50))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.LoadStdListsBtn.setFont(font)
        self.LoadStdListsBtn.setStyleSheet("background-color:green; color:white")
        self.LoadStdListsBtn.setObjectName("LoadStdListsBtn")
        self.LoadPollsBtn = QtWidgets.QPushButton(self.centralwidget)
        self.LoadPollsBtn.setGeometry(QtCore.QRect(290, 150, 171, 50))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.LoadPollsBtn.setFont(font)
        self.LoadPollsBtn.setObjectName("LoadPollsBtn")
        self.LoadAnswerKeysBtn = QtWidgets.QPushButton(self.centralwidget)
        self.LoadAnswerKeysBtn.setGeometry(QtCore.QRect(520, 150, 171, 50))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.LoadAnswerKeysBtn.setFont(font)
        self.LoadAnswerKeysBtn.setObjectName("LoadAnswerKeysBtn")




def main():
    app = QApplication([])
    window = QMainWindow()
    ui = UserInterface()
    ui.setupUi(window)
    window.show()
    sys.exit(app.exec_())

