import sys, os

from tests import TestSuitRunner
from main.AttendanceReportSerializer import AttendanceReportSerializer
from main.GlobalReportSerializer import GlobalReportSerializer
from main.StatsReportSerializer import StatsReportSerializer

class PollAnalysisSystem(object):

	def __init__(self, user_interface):
		self.__user_interface = user_interface
		self.__attendance_report_serialize = AttendanceReportSerializer(self)
		self.__global_report_serializer = GlobalReportSerializer(self)
		self.__stats_report_serializer = StatsReportSerializer(self)

	def load_student_list(self):
		pass

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
