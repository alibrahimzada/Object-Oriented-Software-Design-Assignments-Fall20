import os
from main import clusters

class QuizClusterAnalyzer(object):

	def __init__(self, poll_analysis_system):
		self.__poll_analysis_system = poll_analysis_system


	def create_hcluster(self, poll_name):
		poll = self.__poll_analysis_system.poll_parser.polls[poll_name]       
		grade_matrix, student_ids = [], []
		for poll_submission in poll.poll_submissions:
			grade_list = []
			student_ids.append(poll_submission.student.id)
			for question in poll.questions_answers:
				correct_answers = self.__poll_analysis_system.answer_key_parser.answer_keys[poll_name][question]                    
				grade = self.get_grade_for_question(question, correct_answers, poll_submission)
				grade_list.append(grade)
			grade_matrix.append(grade_list)

		if not os.path.exists('cluster_analysis'):
			os.mkdir('cluster_analysis')
		save_to = f'cluster_analysis/{poll_name}.png'
		cluster = clusters.hcluster(grade_matrix, clusters.sim_distance)
		clusters.drawdendrogram(cluster, student_ids, save_to)
			

	def get_grade_for_question(self, question, correct_answers, poll_submission):
		"""Grade a question, 1 if true otherwise 0"""
		for submission_q in poll_submission.questions_answers:
			if submission_q == question:
				submission_answers = poll_submission.questions_answers[submission_q]
				if len(submission_answers) == len(correct_answers):
					return self.is_answer_correct(submission_answers, correct_answers)
		return 0 


	def is_answer_correct(self, answers, correct_answers):
		""" compares the correct answer and the submitted answer """
		correct_answers_texts = [answer.text for answer in correct_answers]
		for poll_submission_answer in answers:
			if poll_submission_answer.text not in correct_answers_texts:
				return 0
		return 1