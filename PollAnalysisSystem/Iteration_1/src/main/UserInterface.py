

import sys

from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QApplication, QFileDialog, QMainWindow

class UserInterface(object):

    def setupUi(self, UserInterface):
        UserInterface.setObjectName("UserInterface")
        UserInterface.resize(730, 461)
        UserInterface.setStyleSheet("background-color:white")
        font = QtGui.QFont()
        font.setBold(True)
        font.setWeight(75)
        self.centralwidget = QtWidgets.QWidget(UserInterface)
        self.centralwidget.setObjectName("centralwidget")
        self.TitleLabel = QtWidgets.QLabel(self.centralwidget)
        self.TitleLabel.setGeometry(QtCore.QRect(0, 0, 751, 81))
        self.LoadStdListsBtn = QtWidgets.QPushButton(self.centralwidget)
        self.LoadStdListsBtn.setGeometry(QtCore.QRect(30, 150, 200, 70))
        self.LoadStdListsBtn.setMinimumSize(QtCore.QSize(200, 70))
        self.LoadStdListsBtn.setFont(font)
        self.LoadStdListsBtn.setStyleSheet("background-color:green; color:white")
        self.LoadStdListsBtn.setObjectName("LoadStdListsBtn")

        self.LoadAnswerKey = QtWidgets.QPushButton(self.centralwidget)
        self.LoadAnswerKey.setGeometry(QtCore.QRect(260, 150, 200, 70))
        self.LoadAnswerKey.setMinimumSize(QtCore.QSize(200, 70))
        self.LoadAnswerKey.setFont(font)
        self.LoadAnswerKey.setObjectName("LoadAnswerKey")

        self.LoadPolls = QtWidgets.QPushButton(self.centralwidget)
        self.LoadPolls.setGeometry(QtCore.QRect(490, 150, 200, 70))
        self.LoadPolls.setMinimumSize(QtCore.QSize(200, 70))
        self.LoadPolls.setFont(font)
        self.LoadPolls.setObjectName("LoadPolls")

        self.ExportAttendReportBtn = QtWidgets.QPushButton(self.centralwidget)
        self.ExportAttendReportBtn.setGeometry(QtCore.QRect(30, 310, 200, 70))
        self.ExportAttendReportBtn.setMinimumSize(QtCore.QSize(200, 70))
        self.ExportAttendReportBtn.setFont(font)
        self.ExportAttendReportBtn.setObjectName("ExportAttendReportBtn")
        

        self.ExportStatsBtn = QtWidgets.QPushButton(self.centralwidget)
        self.ExportStatsBtn.setGeometry(QtCore.QRect(260, 310, 200, 70))
        self.ExportStatsBtn.setMinimumSize(QtCore.QSize(200, 70))
        self.ExportStatsBtn.setFont(font)
        self.ExportStatsBtn.setStyleSheet("background-color:green; color:white;")
        self.ExportStatsBtn.setObjectName("ExportStatsBtn")

        self.ExportGlobalReportBtn = QtWidgets.QPushButton(self.centralwidget)
        self.ExportGlobalReportBtn.setGeometry(QtCore.QRect(490, 310, 200, 70))
        self.ExportGlobalReportBtn.setMinimumSize(QtCore.QSize(200, 70))
        self.ExportGlobalReportBtn.setFont(font)
        self.ExportGlobalReportBtn.setObjectName("ExportGlobalReportBtn")

        ## CUSTOMIZATION, you probably won't change anything here so please don't change unless you ask me first.
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Preferred, QtWidgets.QSizePolicy.Preferred)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.TitleLabel.sizePolicy().hasHeightForWidth())
        self.TitleLabel.setSizePolicy(sizePolicy)
        font = QtGui.QFont()
        font.setFamily("MS Sans Serif")
        font.setPointSize(35)
        font.setBold(True)
        font.setWeight(75)
        font.setKerning(True)
        self.TitleLabel.setFont(font)
        self.TitleLabel.setStyleSheet("background-color: green; color: white")
        self.TitleLabel.setLineWidth(20)
        self.TitleLabel.setMidLineWidth(20)
        self.TitleLabel.setAlignment(QtCore.Qt.AlignCenter)
        self.TitleLabel.setObjectName("TitleLabel")
        UserInterface.setCentralWidget(self.centralwidget)
        self.statusbar = QtWidgets.QStatusBar(UserInterface)
        self.statusbar.setObjectName("statusbar")
        UserInterface.setStatusBar(self.statusbar)

        self.LoadAnswerKey.setCursor(QtCore.Qt.PointingHandCursor)
        self.LoadPolls.setCursor(QtCore.Qt.PointingHandCursor)
        self.LoadStdListsBtn.setCursor(QtCore.Qt.PointingHandCursor)
        self.ExportAttendReportBtn.setCursor(QtCore.Qt.PointingHandCursor)
        self.ExportGlobalReportBtn.setCursor(QtCore.Qt.PointingHandCursor)
        self.ExportStatsBtn.setCursor(QtCore.Qt.PointingHandCursor)


        ##### MAPPING:
        self.LoadStdListsBtn.clicked.connect(self.uploadStdList)
        self.LoadAnswerKey.clicked.connect(self.uploadAnswerKey)
        self.LoadPolls.clicked.connect(self.uploadPolls)
        self.ExportAttendReportBtn.clicked.connect(self.downloadAttendanceReport)
        self.ExportStatsBtn.clicked.connect(self.downloadStats)
        self.ExportGlobalReportBtn.clicked.connect(self.downloadGlobalReport)


        self.retranslateUi(UserInterface)
        QtCore.QMetaObject.connectSlotsByName(UserInterface)


    def retranslateUi(self, UserInterface):
        ''' 
        This is somewhat self explantory, just a function that sets the text/style of the widgets we have.
        This is how we do it in pyqt but we can always get rid of this function and just write whatever we have
        here into the setup ui function but this is the correct way to do it.
        '''
        _translate = QtCore.QCoreApplication.translate
        UserInterface.setWindowTitle(_translate("UserInterface", "MainWindow"))
        
        self.TitleLabel.setText(_translate("UserInterface", "Poll Analysis System"))

        self.LoadStdListsBtn.setText(_translate("UserInterface", "Load Student Lists"))
        self.LoadStdListsBtn.setStatusTip(_translate("UserInterface","This is just a button, why are you hovering over many things weirdly?"))
        self.LoadAnswerKey.setStyleSheet(_translate("UserInterface", "background-color:green; color:white"))
        self.LoadAnswerKey.setText(_translate("UserInterface", "Load Answer Keys"))

        self.LoadPolls.setStyleSheet(_translate("UserInterface", "background-color:green; color:white"))
        self.LoadPolls.setText(_translate("UserInterface", "Load Polls"))

        self.ExportAttendReportBtn.setStyleSheet(_translate("UserInterface", "background-color:green; color:white"))
        self.ExportAttendReportBtn.setText(_translate("UserInterface", "Export Attendance Report"))

        self.ExportStatsBtn.setText(_translate("UserInterface", "Export Stats"))

        self.ExportGlobalReportBtn.setStyleSheet(_translate("UserInterface", "background-color:green; color:white"))
        self.ExportGlobalReportBtn.setText(_translate("UserInterface", "Export Global Report"))

        self.LoadStdListsBtn.setStatusTip(_translate("UserInterface","This is also just a button, come on"))
        self.TitleLabel.setStatusTip(_translate("UserInterface", "This is a normal label, nothing to see here."))


    ############# BINDINGS
    # READ ME: I feel liks this is somewhat repatative so I made a function that opens a dialog for all buttons and then we just return the path of the file opened to the function that was triggered. 
    # However, we can still make this better but not sure how so will leave it for now.
    def choseFileDialog(self, DialogName="Choose File"):
        filename = QFileDialog.getOpenFileName(caption=DialogName) #Did not add a file type because not sure what type we are always getting
        path = filename[0] 
        #the function returns a tuple, the first value of the tuple is the path and the second value is the type of files you are allowing to choose.
        return path

    def uploadStdList(self):
        path = self.choseFileDialog()
        print(path)

        # with open(path, "r") as StdList:
            # print("bla")

        

    def uploadAnswerKey(self):
        path = self.choseFileDialog()

    def uploadPolls(self):
        path = self.choseFileDialog()

    def downloadAttendanceReport(self):
        path = self.choseFileDialog()

    def downloadGlobalReport(self):
        path = self.choseFileDialog()

    def downloadStats(self):
        path = self.choseFileDialog()







def main():
    app = QApplication([]) #Qapplication requires that we pass it system arguments, but since we have none, we just put an empty list.
    app.setStyle("Breeze")
    window = QMainWindow() #the main window to which we pass our widgets
    window.setFixedSize(730, 461)
    ui = UserInterface()
    ui.setupUi(window)
    window.show()
    sys.exit(app.exec_()) #app.exec is what shows our window basically, without it, we won't see anything. 


if __name__ == '__main__':
    main()
