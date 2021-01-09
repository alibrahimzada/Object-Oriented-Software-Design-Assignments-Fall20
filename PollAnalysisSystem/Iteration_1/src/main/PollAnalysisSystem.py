from tests import TestSuitRunner
from main.AttendanceReportSerializer import AttendanceReportSerializer
from main.GlobalReportSerializer import GlobalReportSerializer
from main.StatsReportSerializer import StatsReportSerializer
from main.StudentListParser import StudentListParser

class PollAnalysisSystem(object):

	def __init__(self, user_interface):
		self.__user_interface = user_interface
		self.__student_list_parser = StudentListParser(self)
		self.__attendance_report_serialize = AttendanceReportSerializer(self)
		self.__global_report_serializer = GlobalReportSerializer(self)
		self.__stats_report_serializer = StatsReportSerializer(self)

	def load_student_list(self, student_lists):
		self.__student_list_parser.parse_student_list(student_lists)

	def load_answer_key(self):
		pass

	def load_polls(self):
		pass

	def export_attendance(self):
		pass

	def export_statistics(self):
		pass

	def export_global_report(self):
		pass
