import sys, os
sys.path.insert(1, os.getcwd() + '/src')
import time
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QApplication, QFileDialog, QMainWindow
from PyQt5.QtGui import QPixmap
from main.PollAnalysisSystem import PollAnalysisSystem
from main.QuizPoll import QuizPoll
from main.ImageWindow import Ui_image_MainWindow

class UserInterface(object):

	def __init__(self):
		self.poll_analysis_system = PollAnalysisSystem(self)

	def __setup_ui(self, window):
		window.setObjectName("window")
		window.resize(730, 461)
		window.setStyleSheet("background-color:white")
		font_for_title = QtGui.QFont()
		font_for_notice_label = QtGui.QFont()
		general_font = QtGui.QFont()


		self.centralwidget = QtWidgets.QWidget(window)
		self.centralwidget.setObjectName("centralwidget")
		self.style_sheet = "background-color:green; color:white"
		self.title_label = QtWidgets.QLabel(self.centralwidget)
		self.title_label.setGeometry(QtCore.QRect(0, 0, 751, 81))

		self.seprator_line = QtWidgets.QFrame(self.centralwidget)
		self.seprator_line.setGeometry(QtCore.QRect(10, 210, 711, 20))
		self.seprator_line.setFrameShape(QtWidgets.QFrame.HLine)
		self.seprator_line.setFrameShadow(QtWidgets.QFrame.Sunken)
		self.seprator_line.setObjectName("seprator_line")

		self.progressBar = QtWidgets.QProgressBar(self.centralwidget)
		self.progressBar.setGeometry(QtCore.QRect(30, 510, 690, 41))
		self.progressBar.setProperty("value", 0)
		self.progressBar.setObjectName("progressBar")

		self.load_std_list_btn = QtWidgets.QPushButton(self.centralwidget)
		self.load_std_list_btn.setGeometry(QtCore.QRect(30, 120, 200, 70))
		self.load_std_list_btn.setMinimumSize(QtCore.QSize(200, 70))
		self.load_std_list_btn.setObjectName("load_std_list_btn")

		self.load_answer_key_btn = QtWidgets.QPushButton(self.centralwidget)
		self.load_answer_key_btn.setGeometry(QtCore.QRect(260, 120, 200, 70))
		self.load_answer_key_btn.setMinimumSize(QtCore.QSize(200, 70))
		self.load_answer_key_btn.setObjectName("load_answer_key_btn")

		self.load_polls_btn = QtWidgets.QPushButton(self.centralwidget)
		self.load_polls_btn.setGeometry(QtCore.QRect(490, 120, 200, 70))
		self.load_polls_btn.setMinimumSize(QtCore.QSize(200, 70))
		self.load_polls_btn.setObjectName("load_polls_btn")

		self.export_attendance_report_btn = QtWidgets.QPushButton(self.centralwidget)
		self.export_attendance_report_btn.setGeometry(QtCore.QRect(QtCore.QRect(60, 240, 280, 70)))
		self.export_attendance_report_btn.setMinimumSize(QtCore.QSize(280, 70))
		self.export_attendance_report_btn.setObjectName("export_attendance_report_btn")

		self.export_stats_btn = QtWidgets.QPushButton(self.centralwidget)
		self.export_stats_btn.setGeometry(QtCore.QRect(380, 240, 280, 70))
		self.export_stats_btn.setMinimumSize(QtCore.QSize(280, 70))
		self.export_stats_btn.setObjectName("export_stats_btn")

		self.quiz_name_label = QtWidgets.QLabel(self.centralwidget)
		self.quiz_name_label.setGeometry(QtCore.QRect(30, 390, 121, 30))
		self.quiz_name_label.setObjectName("quiz_name_label")

		self.question_number_label = QtWidgets.QLabel(self.centralwidget)
		self.question_number_label.setGeometry(QtCore.QRect(330, 390, 40, 21))
		self.question_number_label.setObjectName("question_number_label")

		self.analyze_quiz_btn = QtWidgets.QPushButton(self.centralwidget)
		self.analyze_quiz_btn.setGeometry(QtCore.QRect(429, 389, 265, 45))
		self.analyze_quiz_btn.setObjectName("analyze_quiz_btn")


		self.cluster_analysis_btn = QtWidgets.QPushButton(self.centralwidget)
		self.cluster_analysis_btn.setGeometry(QtCore.QRect(430, 440, 265, 45))
		self.cluster_analysis_btn.setObjectName("cluster_analysis_btn")


		self.quiz_name_combobox = QtWidgets.QComboBox(self.centralwidget)
		self.quiz_name_combobox.setGeometry(QtCore.QRect(30, 420, 296, 38))
		self.quiz_name_combobox.setObjectName("quiz_name_combobox")

		self.question_number_combobox = QtWidgets.QComboBox(self.centralwidget)
		self.question_number_combobox.setGeometry(QtCore.QRect(330, 420, 76, 38))
		self.question_number_combobox.setObjectName("question_number_combobox")

		self.notice_label = QtWidgets.QLabel(self.centralwidget)
		self.notice_label.setGeometry(QtCore.QRect(80, 330, 601, 20))

		## CUSTOMIZATION, you probably won't change anything here so please don't change unless you ask me first.
		font_for_title.setBold(True)
		font_for_title.setWeight(75)
		font_for_notice_label.setPointSize(12)
		font_for_notice_label.setBold(True)
		font_for_notice_label.setWeight(75)
		font_for_title.setPointSize(35)
		general_font.setBold(True)
		general_font.setWeight(75)
		general_font.setKerning(True)
		self.title_label.setFont(font_for_title)
		self.notice_label.setFont(font_for_notice_label)
		self.cluster_analysis_btn.setFont(general_font)
		self.analyze_quiz_btn.setFont(general_font)
		self.question_number_label.setFont(general_font)
		self.quiz_name_label.setFont(general_font)
		self.export_stats_btn.setFont(general_font)
		self.export_attendance_report_btn.setFont(general_font)
		self.load_polls_btn.setFont(general_font)
		self.load_std_list_btn.setFont(general_font)
		self.load_answer_key_btn.setFont(general_font)
		self.title_label.setStyleSheet(self.style_sheet)		
		self.title_label.setLineWidth(20)
		self.title_label.setMidLineWidth(20)
		self.title_label.setAlignment(QtCore.Qt.AlignCenter)
		self.title_label.setObjectName("title_label")
		window.setCentralWidget(self.centralwidget)

		self.load_answer_key_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.load_polls_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.load_std_list_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.export_attendance_report_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.export_stats_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.analyze_quiz_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.cluster_analysis_btn.setCursor(QtCore.Qt.PointingHandCursor)
		self.question_number_combobox.setCursor(QtCore.Qt.PointingHandCursor)
		self.quiz_name_combobox.setCursor(QtCore.Qt.PointingHandCursor)




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

		self.load_std_list_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))

		self.load_answer_key_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))
		self.load_answer_key_btn.setText(_translate("window", "Load Answer Keys"))

		self.load_polls_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))
		self.load_polls_btn.setText(_translate("window", "Load Polls"))

		self.export_attendance_report_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))
		self.export_attendance_report_btn.setText(_translate("window", "Export Attendance Report"))

		self.export_stats_btn.setText(_translate("window", "Export Stats and Global Report"))
		self.export_stats_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))

		self.analyze_quiz_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))
		self.analyze_quiz_btn.setText(_translate("window", "Analyze Quiz"))

		self.cluster_analysis_btn.setStyleSheet(_translate("window", "background-color:green; color:white"))
		self.cluster_analysis_btn.setText(_translate("window", "Clustering Analysis (Cheat Detector)"))

		self.quiz_name_label.setText(_translate("UserInterface", "Report Name"))
		self.question_number_label.setText(_translate("UserInterface", "Q#"))

		self.notice_label.setText(_translate("UserInterface", "In order to analyze a quiz, please export stats and global report first."))

		##### MAPPING:
		self.load_std_list_btn.clicked.connect(self.__upload_std_list)
		self.load_answer_key_btn.clicked.connect(self.__upload_answer_key)
		self.load_polls_btn.clicked.connect(self.__upload_polls)
		self.export_attendance_report_btn.clicked.connect(self.__export_attendance_report)
		self.export_stats_btn.clicked.connect(self.__export_stats)
		self.analyze_quiz_btn.clicked.connect(self.__load_quiz_to_window)
		self.quiz_name_combobox.currentTextChanged.connect(self.__update_question_numb_comboox)
		self.cluster_analysis_btn.clicked.connect(self.__create_cluster)


	############# BINDINGS.

	def __files_loader(self, dialogue_name="Choose a file or multiple files!"):
		''' Opens a directory, loads all files in it and makes a IO.textIoWrapper objects list and returns it'''
		chosen_files = QFileDialog.getOpenFileNames(directory=os.getcwd(),
			caption=dialogue_name)
		
		# we will allow a certain type of file when we are sure, right now, we can choose any file.
		files_list = []  # this will be a list of file paths selected by the user
		# we are looping over the 0th index of chosen files because GetOpenFileNmaes returns a tuple of the files chosen, and the type of files we are allowing.
		for file_path in chosen_files[0]:
			files_list.append(file_path)
		if files_list:
			self.__progress_bar_loading()
		return files_list

	def __upload_std_list(self):
		std_list_files = self.__files_loader()
		self.poll_analysis_system.load_student_list(std_list_files)

	def __upload_answer_key(self):
		answer_key_files = self.__files_loader()
		self.poll_analysis_system.load_answer_key(answer_key_files)

	def __upload_polls(self):
		polls_files = self.__files_loader()
		self.poll_analysis_system.load_polls(polls_files)
		self.__populate_quiz_name_combobox()

	def __export_attendance_report(self):
		self.poll_analysis_system.export_attendance()

	def __export_stats(self):
		self.poll_analysis_system.export_statistics()

	def __create_cluster(self):
		poll_name = self.quiz_name_combobox.currentText()  # this will be taken from the current val of drop down menu
		self.poll_analysis_system.create_clusters(poll_name)

	def __progress_bar_loading(self):
		progress = 0
		while progress < 100:
			progress += .0002
			self.progressBar.setProperty("value", progress)
		time.sleep(
			1.5)  # jsut give a feel that it is actually loading. Everything we do takes at most 2 seconds so I just set it to load in a second or two. 
		self.progressBar.setProperty("value", 0)

	def __populate_quiz_name_combobox(self):
		self.quiz_name_combobox.clear()
		for pollname in self.poll_analysis_system.poll_parser.polls:
			self.quiz_name_combobox.addItem(pollname) # this is how you add item to a combobox. combobox box is what a dropdown menu is called in PYQT5.

	def __update_question_numb_comboox(self, new_value):
		self.question_number_combobox.clear()
		
		try: 
			selected_poll_name = self.poll_analysis_system.poll_parser.polls[new_value]._questions_answers
			for i in range(1, len(selected_poll_name)+1):
				self.question_number_combobox.addItem(str(i))
		except KeyError:
			pass

	def __load_quiz_to_window(self):
		current_quiz = self.quiz_name_combobox.currentText()
		current_quiz_num = self.question_number_combobox.currentText()
		img = os.path.join(os.getcwd(), 'statistics', f'{current_quiz}', f'Q{current_quiz_num}.png')
		self.__new_window = QtWidgets.QMainWindow()
		self.ui = Ui_image_MainWindow()
		self.ui.setupUi(self.__new_window)
		pixmap = QPixmap(img)
		self.ui.Image_placeholder.setPixmap((pixmap))
		self.__new_window.show()




def main():
    app = QApplication(
        [])  # Qapplication requires that we pass it system arguments, but since we have none, we just put an empty list.
    app.setStyle("Breeze")
    window = QMainWindow()  # the main window to which we pass our widgets
    window.setFixedSize(730, 574)
    ui = UserInterface()
    ui._UserInterface__setup_ui(window)
    window.show()
    sys.exit(app.exec_())  # app.exec is what shows our window basically, without it, we won't see anything. 


if __name__ == '__main__':
    main()
