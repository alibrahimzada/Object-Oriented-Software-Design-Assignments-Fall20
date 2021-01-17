import logging

from tests.TestSuitRunner import TestSuitRunner
from main.AnswerKeyParser import AnswerKeyParser
from main.AttendanceReportSerializer import AttendanceReportSerializer
from main.PollParser import PollParser
from main.StatsReportSerializer import StatsReportSerializer
from main.StudentListParser import StudentListParser


class PollAnalysisSystem(object):
	def __init__(self, user_interface):
		self.__user_interface = user_interface
		self.run_tests()
		self.__student_list_parser = StudentListParser(self)
		self.__answer_key_parser = AnswerKeyParser(self)
		self.__poll_parser = PollParser(self)
		self.__attendance_report_serializer = AttendanceReportSerializer(self)
		self.__stats_report_serializer = StatsReportSerializer(self)
		self.__logger = self.create_logger()

	@property
	def answer_key_parser(self):
		return self.__answer_key_parser
		
	@property
	def student_list_parser(self):
		return self.__student_list_parser
		
	@property
	def poll_parser(self):
		return self.__poll_parser

	@property
	def logger(self):
		return self.__logger

	def run_tests(self):
		TestSuitRunner().run_tests() # run all tests
	
	def create_logger(self):
		log_file = 'PollAnalysisSystem.log'
		handlers = [logging.FileHandler(log_file), logging.StreamHandler()] # write to file & stdout
		logging.basicConfig(level=logging.NOTSET,  # set root logger to NOSET 
							handlers = handlers,
							format="%(asctime)s;%(levelname)s;%(message)s",  # log format
							datefmt='%Y-%m-%d %H:%M:%S')  # date format
		logging.getLogger('matplotlib.font_manager').disabled = True  # avoid the warnings thrown by matplotlib
		return logging.getLogger()
	

		
	def load_student_list(self, student_list_files):
		self.__student_list_parser.parse_student_list(student_list_files)

	def load_answer_key(self, answer_key_files):
		self.__answer_key_parser.read_answer_keys(answer_key_files)

	def load_polls(self, poll_reports_files):
		self.__poll_parser.read_poll_reports(poll_reports_files)

	def export_attendance(self):
		self.__attendance_report_serializer.export_attendance_report()

	def export_statistics(self):
		self.__stats_report_serializer.export_reports()
