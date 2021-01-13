import os
import pandas as pd


class AttendanceReportSerializer(object):

    def __init__(self, poll_analysis_system):
        self.__poll_analysis_system = poll_analysis_system
        self.__poll_parser = poll_analysis_system.poll_parser
        self.__student_list_parser = poll_analysis_system.student_list_parser
        self.__answer_key_parser = poll_analysis_system.answer_key_parser

    @property
    def poll_analysis_system(self):
        return self.__poll_analysis_system

    @poll_analysis_system.setter
    def poll_analysis_system(self, value):
        self.__poll_analysis_system = value

    def export_reports(self):
        self.full_data = pd.DataFrame()
        for poll_name in self.__poll_parser.polls:
            poll = self.__poll_parser.polls[poll_name]
            self.export_attendance_report(poll_name, poll)
            
        self.full_data = self.full_data.groupby(['Student ID', 'Name', 'Surname'])['Attendance'].agg(['sum']).rename(
            columns={'sum': 'Attendance out of ' + str(len(self.__poll_parser.polls))})
            
        self.full_data.to_excel("attendance_report.xlsx")
        os.chdir('..')
    def export_attendance_report(self, poll_name):
        if not os.path.exists('attendance_report'):
            os.mkdir('attendance_report')
        os.chdir('attendance_report')

        if not os.path.exists(poll_name):
            os.mkdir(poll_name)

        os.chdir(poll_name)
        student_numbers, names, surnames, attended = [], [], [], []
        for std_list in self.__student_list_parser.registrations:
            for registration in self.__student_list_parser.registrations[std_list]:
                poll_submissions = registration.student.poll_submissions

                if poll_name in poll_submissions:
                    student_numbers.append(registration.student.id)
                    names.append(registration.student.name)
                    surnames.append(registration.student.surname)
                    attended.append(1)

        data_frame = pd.DataFrame()
        data_frame['Student ID'] = student_numbers
        data_frame['Name'] = names
        data_frame['Surname'] = surnames
        data_frame['Attendance'] = attended

        self.full_data = self.full_data.append(data_frame)