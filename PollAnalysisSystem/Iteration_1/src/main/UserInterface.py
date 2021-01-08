import sys, os
sys.path.insert(1, os.getcwd() + '/src')

from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QApplication, QFileDialog, QMainWindow

from main.PollAnalysisSystem import PollAnalysisSystem

class UserInterface(object):

	def __init__(self):
		self.poll_analysis_system = PollAnalysisSystem(self)

	def __setup_ui(self, window):
		window.setObjectName("window")
		window.resize(730, 461)
		window.setStyleSheet("background-color:white")
		font = QtGui.QFont()
		font.setBold(True)
		font.setWeight(75)
		self.centralwidget = QtWidgets.QWidget(window)
		self.centralwidget.setObjectName("centralwidget")
		self.title_label = QtWidgets.QLabel(self.centralwidget)
		self.title_label.setGeometry(QtCore.QRect(0, 0, 751, 81))
		self.load_std_list_btn = QtWidgets.QPushButton(self.centralwidget)
		self.load_std_list_btn.setGeometry(QtCore.QRect(30, 150, 200, 70))
		self.load_std_list_btn.setMinimumSize(QtCore.QSize(200, 70))
		self.load_std_list_btn.setFont(font)
		self.load_std_list_btn.setStyleSheet("background-color:green; color:white")
		self.load_std_list_btn.setObjectName("load_std_list_btn")
		
		self.load_answer_key_btn = QtWidgets.QPushButton(self.centralwidget)
		self.load_answer_key_btn.setGeometry(QtCore.QRect(260, 150, 200, 70))
		self.load_answer_key_btn.setMinimumSize(QtCore.QSize(200, 70))
		self.load_answer_key_btn.setFont(font)
		self.load_answer_key_btn.setObjectName("load_answer_key_btn")
		
		self.load_polls_btn = QtWidgets.QPushButton(self.centralwidget)
		self.load_polls_btn.setGeometry(QtCore.QRect(490, 150, 200, 70))
		self.load_polls_btn.setMinimumSize(QtCore.QSize(200, 70))
		self.load_polls_btn.setFont(font)
		self.load_polls_btn.setObjectName("load_polls_btn")
		
		self.export_attendance_report_btn = QtWidgets.QPushButton(self.centralwidget)
		self.export_attendance_report_btn.setGeometry(QtCore.QRect(30, 310, 200, 70))
		self.export_attendance_report_btn.setMinimumSize(QtCore.QSize(200, 70))
		self.export_attendance_report_btn.setFont(font)
		self.export_attendance_report_btn.setObjectName("export_attendance_report_btn")
		
		self.export_stats_btn = QtWidgets.QPushButton(self.centralwidget)
		self.export_stats_btn.setGeometry(QtCore.QRect(260, 310, 200, 70))
		self.export_stats_btn.setMinimumSize(QtCore.QSize(200, 70))
		self.export_stats_btn.setFont(font)
		self.export_stats_btn.setStyleSheet("background-color:green; color:white;")
		self.export_stats_btn.setObjectName("export_stats_btn")
		
		self.export_global_report_btn = QtWidgets.QPushButton(self.centralwidget)
		self.export_global_report_btn.setGeometry(QtCore.QRect(490, 310, 200, 70))
		self.export_global_report_btn.setMinimumSize(QtCore.QSize(200, 70))
		self.export_global_report_btn.setFont(font)
		self.export_global_report_btn.setObjectName("export_global_report_btn")
		
		## CUSTOMIZATION, you probably won't change anything here so please don't change unless you ask me first.

		font = QtGui.QFont()
		font.setFamily("MS Sans Serif")
		font.setPointSize(35)
		font.setBold(True)
		font.setWeight(75)
		font.setKerning(True)
		self.title_label.setFont(font)
		self.title_label.setStyleSheet("background-color: green; color: white")
		self.title_label.setLineWidth(20)
		self.title_label.setMidLineWidth(20)
		self.title_label.setAlignment(QtCore.Qt.AlignCenter)
		self.title_label.setObjectName("title_label")
		window.setCentralWidget(self.centralwidget)
		self.statusbar = QtWidgets.QStatusBar(window)
		self.statusbar.setObjectName("statusbar")
		window.setStatusBar(self.statusbar)

		self.load_answer_key_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.load_polls_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.load_std_list_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.export_attendance_report_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.export_global_report_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.export_stats_btn.setCursor(QtCore.Qt.PointingHandCursor)


   

		self.__retranslate_ui(window)
		QtCore.QMetaObject.connectSlotsByName(window)
		
	def __retranslate_ui(self, window):
		''' 
		This is somewhat self explantory, just a function that sets the text/style of the widgets we have.
		This is how we do it in pyqt but we can always get rid of this function and just write whatever we have
		here into the setup ui function but this is the correct way to do it.
		'''
		_translate = QtCore.QCoreApplication.translate
		window.setWindowTitle(_translate("window", "MainWindow"))
		
		self.title_label.setText(_translate("window", "Poll Analysis System"))

		self.load_std_list_btn.setText(_translate("window", "Load Student Lists"))
		self.load_std_list_btn.setStatusTip(_translate("window","This is just a button, why are you hovering over many things weirdly?"))
		self.load_answer_key_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))
		self.load_answer_key_btn.setText(_translate("window", "Load Answer Keys"))

		self.load_polls_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))
		self.load_polls_btn.setText(_translate("window", "Load Polls"))

		self.export_attendance_report_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))
		self.export_attendance_report_btn.setText(_translate("window", "Export Attendance Report"))

		self.export_stats_btn.setText(_translate("window", "Export Stats"))

		self.export_global_report_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))
		self.export_global_report_btn.setText(_translate("window", "Export Global Report"))

		self.load_std_list_btn.setStatusTip(_translate("window","This is also just a button, come on"))
		self.title_label.setStatusTip(_translate("window", "This is a normal label, nothing to see here."))

		##### MAPPING:
		self.load_std_list_btn.clicked.connect(self.__upload_std_list)
		self.load_answer_key_btn.clicked.connect(self.__upload_answer_key)
		self.load_polls_btn.clicked.connect(self.__upload_polls)
		self.export_attendance_report_btn.clicked.connect(self.__export_attendance_report)
		self.export_stats_btn.clicked.connect(self.__export_stats)
		self.export_global_report_btn.clicked.connect(self.__export_global_report)



	############# BINDINGS.

	def __files_loader(self, dialogue_name = "Choose Directory"):
		''' Opens a directory, loads all files in it and makes a IO.textIoWrapper objects list and returns it'''
		dirname = QFileDialog.getExistingDirectory(caption = dialogue_name)
		self.files = [] #this will be a list of files that are inside the directory you load.
		print(os.path.join(os.getcwd()))
		for filename in os.listdir(dirname):
			full_path = dirname + '/' + filename
			# print(full_path) #just in case you want to check
			with open(full_path, 'r') as single_file:
				# print(singleFile)
				self.files.append(single_file)
		print(self.files)
		return self.files

	def __upload_std_list(self):
		self.std_list = self.__files_loader()

	def __upload_answer_key(self):
		self.answer_key_list = self.__files_loader()

	def __upload_polls(self):
		self.polls_list = self.__files_loader()

	def __export_attendance_report(self):
		pass #we will not be saving by opening a dialog so this shall remain like this  till we figure out how we will export.
	def __export_global_report(self):
		pass
	def __export_stats(self):
		pass

def main():
    app = QApplication([]) #Qapplication requires that we pass it system arguments, but since we have none, we just put an empty list.
    app.setStyle("Breeze")
    window = QMainWindow() #the main window to which we pass our widgets
    window.setFixedSize(730, 461)
    ui = UserInterface()
    ui._UserInterface__setup_ui(window)
    window.show()
    sys.exit(app.exec_()) #app.exec is what shows our window basically, without it, we won't see anything. 

if __name__ == '__main__':
	main()
