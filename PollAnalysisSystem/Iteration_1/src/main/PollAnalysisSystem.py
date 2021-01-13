import logging
from tests import TestSuitRunner
from main.AttendanceReportSerializer import AttendanceReportSerializer
from main.StatsReportSerializer import StatsReportSerializer
from main.StudentListParser import StudentListParser
from main.AnswerKeyParser import AnswerKeyParser
from main.PollParser import PollParser

class PollAnalysisSystem(object):

	def __init__(self, user_interface):
		self.__user_interface = user_interface
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

	def create_logger(self):
		logging.basicConfig(level=logging.NOTSET) # set root logger to NOSET & write to stdout
		return logging.getLogger()
		
	def load_student_list(self, student_list_files):
		self.__student_list_parser.parse_student_list(student_list_files)
		self.__logger.info('The Student Lists were loaded successfully.')

	def load_answer_key(self, answer_key_files):
		self.__answer_key_parser.read_answer_keys(answer_key_files)
		self.__logger.info('The Answer Keys were loaded successfully.')

	def load_polls(self, poll_reports_files):
		self.__poll_parser.read_poll_reports(poll_reports_files)
		self.__logger.info('The Poll Reports were loaded successfully.')

	def export_attendance(self):
		pass

	def export_stats_global(self):
		self.__stats_report_serializer.export_reports()
