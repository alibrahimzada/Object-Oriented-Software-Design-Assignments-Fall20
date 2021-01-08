import xlrd

# course_code - theoritical_credit - practical_credit - ects - course_name - instructor_name - department_name - 
# student_number - student_name - student_surname - description 
from main.Course import Course
from main.Department import Department
from main.Instructor import Instructor
from main.Student import Student

file = xlrd.open_workbook('../../student_lists/CES3063_Fall2020_rptSinifListesi.XLS', encoding_override='cp1252')
sheet = file.sheet_by_index(0)

i = 0
general = ""

# Initializing objects
instructor = None
current_department = None
student = None
department = None
course = None

while i < sheet.nrows:
    if sheet.row_values(i):
        # First we will check for the general info that starts with a Date and ends with department name
        if type(sheet.row_values(i)[0]) == float and sheet.row_values(i)[1] == "":
            i += 2
            semester = sheet.row_values(i)[0]

            i += 2
            # Getting the Department of the course
            department = Department(name=sheet.row_values(i)[0].split("Ders")[0])

            i += 2
            # Getting information about the course
            codes = sheet.row_values(i)[6].split(" ")
            course_code = codes[0]
            theorcred = codes[1][1:]
            praccred = codes[3][:-1]
            ects = codes[4]
            course_name = ' '.join(codes[6:])
            course = Course(code=course_code, name=course_name, theoretical_credits=theorcred,
                            practical_credits=praccred, ects=ects)

            i += 2

            # Putting them all together
            general = (str(semester) + " " + str(course.code) + " " + str(course.theoretical_credits) + " " + str(
                course.practical_credits) + " " + str(course.ects) + " " + str(course.name))

            # Getting information about the instructor
            instr_name = sheet.row_values(i)[6]
            instructor = Instructor(name=instr_name, department=department)

            i += 2

        elif sheet.row_values(i)[1] == "No":
            if "(" in sheet.row_values(i - 2)[0]:
                current_department = Department(name=sheet.row_values(i - 2)[0].split("(")[0])
            else:
                current_department = Department(name=sheet.row_values(i - 2)[0])
            i += 1

        elif type(sheet.row_values(i)[1] == float):
            if sheet.row_values(i)[1] != "":
                std_num = sheet.row_values(i)[2]
                name = sheet.row_values(i)[4]
                surname = sheet.row_values(i)[7]
                description = sheet.row_values(i)[10]

                # Getting information about the Student
                student = Student(id=std_num, name=name, surname=surname, email="", department=current_department)

                student_info = (str(student.id) + " " + str(student.name) + " " + str(student.surname) + " " + str(
                    description))
                print(general + " " + instructor.name + " " + student.department.name + " " + student_info)
                i += 1
            else:
                i += 1
        else:
            i += 1
