

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

        self.ExportAttendReportBtn = QtWidgets.QPushButton(self.centralwidget)
        self.ExportAttendReportBtn.setGeometry(QtCore.QRect(60, 310, 171, 51))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.ExportAttendReportBtn.setFont(font)
        self.ExportAttendReportBtn.setObjectName("ExportAttendReportBtn")
        self.ExportStatsBtn = QtWidgets.QPushButton(self.centralwidget)
        self.ExportStatsBtn.setGeometry(QtCore.QRect(290, 310, 171, 51))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.ExportStatsBtn.setFont(font)
        self.ExportStatsBtn.setStyleSheet("background-color:green; color:white;")
        self.ExportStatsBtn.setObjectName("ExportStatsBtn")
        self.ExportGlobalReportBtn = QtWidgets.QPushButton(self.centralwidget)
        self.ExportGlobalReportBtn.setGeometry(QtCore.QRect(520, 310, 171, 51))
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.ExportGlobalReportBtn.setFont(font)
        self.ExportGlobalReportBtn.setAutoDefault(False)
        self.ExportGlobalReportBtn.setDefault(False)
        self.ExportGlobalReportBtn.setFlat(False)
        self.ExportGlobalReportBtn.setObjectName("ExportGlobalReportBtn")



        self.retranslateUi(UserInterface)
        QtCore.QMetaObject.connectSlotsByName(UserInterface)


  def retranslateUi(self, UserInterface):
      _translate = QtCore.QCoreApplication.translate
      UserInterface.setWindowTitle(_translate("UserInterface", "MainWindow"))
      self.LoadStdListsBtn.setText(_translate("UserInterface", "Load Student Lists"))
      self.LoadPollsBtn.setStyleSheet(_translate("UserInterface", "background-color:green; color:white"))
      self.LoadPollsBtn.setText(_translate("UserInterface", "Load Polls"))
      self.LoadAnswerKeysBtn.setStyleSheet(_translate("UserInterface", "background-color:green; color:white"))
      self.LoadAnswerKeysBtn.setText(_translate("UserInterface", "Load Answer Keys"))
      self.ExportAttendReportBtn.setStyleSheet(_translate("UserInterface", "background-color:green; color:white"))
      self.ExportAttendReportBtn.setText(_translate("UserInterface", "Export Attendance Report"))
      self.ExportStatsBtn.setText(_translate("UserInterface", "Export Stats"))
      self.ExportGlobalReportBtn.setStyleSheet(_translate("UserInterface", "background-color:green; color:white"))
      self.ExportGlobalReportBtn.setText(_translate("UserInterface", "Export Global Report"))
      self.TitleLabel.setStatusTip(_translate("UserInterface", "This is a normal label, nothing to see here."))
      self.TitleLabel.setText(_translate("UserInterface", "Poll Analysis System"))

def main():
    app = QApplication([])
    window = QMainWindow()
    ui = UserInterface()
    ui.setupUi(window)
    window.show()
    sys.exit(app.exec_())

