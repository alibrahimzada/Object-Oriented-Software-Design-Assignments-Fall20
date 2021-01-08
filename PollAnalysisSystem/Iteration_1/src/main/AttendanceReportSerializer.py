

class AttendanceReportSerializer(object):

	def __init__(self, poll_analysis_system):
		self.__poll_analysis_system = poll_analysis_system

	@property
	def poll_analysis_system(self):
		return self.__poll_analysis_system

	@poll_analysis_system.setter
	def poll_analysis_system(self, value):
		self.__poll_analysis_system = value
