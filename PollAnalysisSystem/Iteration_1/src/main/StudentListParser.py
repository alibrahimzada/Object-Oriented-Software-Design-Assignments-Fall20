import xlrd

# course_code - theoritical_credit - practical_credit - ects - course_name - instructor_name - department_name - 
# student_number - student_name - student_surname - description 

file = xlrd.open_workbook('../../student_lists/CES3063_Fall2020_rptSinifListesi.XLS', encoding_override='cp1252')
sheet = file.sheet_by_index(0)

i = 0
general = ""
current_department = " "
while i < sheet.nrows:
    if sheet.row_values(i):
        # First we will check for the general info that starts with a Date and ends with department name
        if type(sheet.row_values(i)[0]) == float and sheet.row_values(i)[1] == "":
            i += 2
            semester = sheet.row_values(i)[0]
            i += 4
            codes = sheet.row_values(i)[6].split(" ")
            course_code = codes[0]
            theorcred = codes[1][1:]
            praccred = codes[3][:-1]
            ects = codes[4]
            course_name = ' '.join(codes[6:])
            i += 2
            prof = sheet.row_values(i)[6]
            i += 2
            general = (str(semester) + " " + str(course_code) + " "+ str(theorcred) + " " + str(praccred) + " " +  str(ects) + " " + str(course_name) + " " + " " + str(prof))

        elif sheet.row_values(i)[1] == "No":
            current_department = sheet.row_values(i-2)[0].split("(")[0]
            i+=1
            
        elif type(sheet.row_values(i)[1] == float):
            if(sheet.row_values(i)[1] != ""):    
                std_num = sheet.row_values(i)[2]
                name = sheet.row_values(i)[4]
                surname = sheet.row_values(i)[7]
                description = sheet.row_values(i)[10]
                student_info = (str(std_num) + " " + str(name) + " " + str(surname) + " " + str(description))

                print(general + " " + current_department + " " + student_info)
                i += 1
            else:
                i+=1
        else:
            i += 1
