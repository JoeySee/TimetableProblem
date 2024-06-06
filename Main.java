import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	static ArrayList<Course> courses = new ArrayList<Course>();
	static ArrayList<Student> students = new ArrayList<Student>();
	
	public static void main(String[] args) throws IOException {
		generateCourses();
		generateStudents();
		generateCourseSeqRules();
		generateBlockingRules();
		
		Timetable t = generateTimetable();
		
		for(Student s : students) {
			s.addToCourses();
		}
		
		for(Student s : students) {
			ArrayList <CourseSection> cs = s.getTimeTable().getAllCourseSections();
			//System.out.println(s.getID());
			for(CourseSection a : cs) {
				//System.out.println(a.getCourse().getName());
			}
//			System.out.println("----------------------------");
		}
		System.out.println(t);
		System.out.println(students.get(0).getTimeTable());
	
//		for (int i = 0; i < students.size(); i++) {
//			s = students.get(i);
//			System.out.println("student id " + s.getID());
//			System.out.println(s.getActualCourseSections().size());
//			for (int j = 0; j < s.getActualCourseSections().size(); j++) {
//				a = s.getActualCourseSections().get(j);
//				System.out.println(a.getCourse().getName() + " at section " + a.getSecNum() + " at block " + a.getBlock());
//			}
//			System.out.println("---------------------------------------------");
//		}
		
		System.out.println("Percent of all requested courses placed: " + genReqCourseMetrics() * 100 + "%");
		System.out.println("Percent of all students who have 8/8 requested classes: " + genFullReqMetrics() * 100 + "%");
		System.out.println("Percent of all students have 7-8/8 requested classes: " + genSufficientReqMetrics() * 100 + "%");
		
		// WRITTEN BY JOEY and owne
		// int j = 0;
		// int k = 0;
		// Timetable t1 = null;
		// double highScore = genReqCourseMetrics();
		// double oldScore = highScore;
		// Timetable bestTable = t.clone();
		// int loopCount = 0;
		// Timetable t2 = t.clone();
		// while(true) {
		// 	k = (int) (Math.random() * 8);
  //           j = (int) (Math.random()*t.getTimetable()[k].size());
  //           CourseSection c = t.getTimetable()[k].get(j);
  //           t.deleteSection(k, t.getTimetable()[k].get(j).getCourse());
  //           for(int i = 0 ; i < 8; i++) {
  //           	t1 = t.clone();
  //               t1.addSection(i, c);
                
                
                
  //               students = new ArrayList<Student>();
  //               generateStudents();
  //               for(Student s : students) {
  //       			s.addToCourses();
        			
  //       		}
                
  //               if(genReqCourseMetrics() >= highScore) {
  //                  bestTable = t1.clone();
  //                  highScore = genReqCourseMetrics();
  //               }
  //           }
            
  //           if(highScore >= oldScore) {
  //           	t = bestTable.clone();
  //           }
            
//            System.out.println(t2.toString().equals(t.toString()));
//            System.out.print(loopCount + ": ");
//            System.out.println(highScore);
//            System.out.println(t);
  //           loopCount++;
  //           t2 = t.clone();
            
  //           if(highScore > oldScore || highScore > .7 || loopCount > 20001) {
  //           	break;
  //           }
  //       }
		// System.out.println(t);
		// System.out.println("Percent of all requested courses placed: " + genReqCourseMetrics() * 100 + "%");
		// System.out.println("Percent of all students who have 8/8 requested classes: " + genFullReqMetrics() * 100 + "%");
		// System.out.println("Percent of all students have 7-8/8 requested classes: " + genSufficientReqMetrics() * 100 + "%");

		// Optimization loop to find the best timetable
		Timetable bestTable = t.clone(); // Initialize bestTable with the initial timetable
		double highScore = genReqCourseMetrics(students); // Initialize highScore with the metrics of the initial
															// timetable
		double curScore = 0;

		for (int it = 0; it < 100; it++) { // Example: Run the optimization loop 10 times
			t = generateTimetable(); // Generate a new timetable

			// Populate the timetable with students and their courses
			students = new ArrayList<>();
			generateStudents(students);
			for (Student s : students) {
				s.addToCourses();
			}

			// Calculate the metrics for the current timetable
			curScore = genReqCourseMetrics(students);

			// Update the best timetable if the current score is higher
			if (curScore > highScore) {
				bTableStudents = students;
				bestTable = t.clone(); // Update the best timetable
				highScore = genReqCourseMetrics(bTableStudents); // Update the high score
			}

			// Output the current score and high score for monitoring
			System.out.print(it + ": ");
			System.out.println(curScore);
			System.out.println("high score: " + highScore);

			if (highScore > 0.7) { // Example: Stop optimization if high score exceeds 0.7
				break;
			}
		}

		// Reset the sections of the best timetable
		resetSections(bestTable);

		// Re-generate students and their courses for the best timetable

		for (int i = 0; i < bTableStudents.size(); i++) {
			bTableStudents.get(i).addToCourses();
		}

		System.out.println(bTableStudents.get(0).getTimeTable());
		System.out.println(students.get(0).getTimeTable());

		// Output the best timetable and metrics
		System.out.println(bestTable);
		System.out.println(reqCoursePlaced(bTableStudents));
		System.out
				.println("Percent of all requested courses placed: " + genReqCourseMetrics(bTableStudents) * 100 + "%");
		System.out.println("Percent of all students who have 8/8 requested classes: "
				+ genFullReqMetrics(bTableStudents) * 100 + "%");
		System.out.println("Percent of all students have 7-8/8 requested classes: "
				+ genSufficientReqMetrics(bTableStudents) * 100 + "%");
		System.out.println("The % of students with 8/8 courses (requested or alternate): "
				+ genFullCorMetrics(bTableStudents) * 100 + "%");
		
	}// main
	
	// returns the metrics all requested courses placed
	public static double genReqCourseMetrics(ArrayList<Student> stuList) {
		int totalReqCourses = 0;
		int totalPlacedReqCourses = 0;

		for (Student s : stuList) {
			totalReqCourses += s.getRequestedCourses().size();
			for (Course reqCourse : s.getRequestedCourses()) {
				for (CourseSection actualCourse : s.getTimeTable().getAllCourseSections()) {
					// check if a given actualCourse was requested
					if (reqCourse.getCode().equals(actualCourse.getCourse().getCode())) {
						totalPlacedReqCourses++;
						break;
					} // if
				} // for (student s' requested courses)
			} // for (student s' actual courses)
		} // for (student)

		return (double) totalPlacedReqCourses / (double) totalReqCourses;
	} // genReqCourseMetrics
	
	public static String reqCoursePlaced(ArrayList<Student> stuList) {
		int totalReqCourses = 0;
		int totalPlacedReqCourses = 0;

		for (Student s : stuList) {
			totalReqCourses += s.getRequestedCourses().size();
			for (Course reqCourse : s.getRequestedCourses()) {
				for (CourseSection actualCourse : s.getTimeTable().getAllCourseSections()) {
					// check if a given actualCourse was requested
					if (reqCourse.getCode().equals(actualCourse.getCourse().getCode())) {
						totalPlacedReqCourses++;
						break;
					} // if
				} // for (student s' requested courses)
			} // for (student s' actual courses)
		} // for (student)
		
		return totalPlacedReqCourses + "/" + totalReqCourses;
	}

	// return the metrics of all students have 8/8 requested classes
	public static double genFullCorMetrics(ArrayList<Student> stuList) {
		int totalNumStudent = stuList.size();
		int numFullCorStudents = 0;

		// for every student
		for (Student s : stuList) {
			int numReqCoursesGiven = 0;
			ArrayList<Course> reqAndAltCourses = s.getRequestedCourses();
			for (Course alt : s.getAlternateCourses()) {
				reqAndAltCourses.add(alt);
			}

			for (Course reqOrAltCourse : reqAndAltCourses) {
				for (CourseSection actualCourse : s.getTimeTable().getAllCourseSections()) {
					if (actualCourse.getCourse().getCode().equals(reqOrAltCourse.getCode())) {
						numReqCoursesGiven++;
						break;
					}
				} // for (student s' requested courses)
			} // for (student s' actual courses)
			if (numReqCoursesGiven == 8) {
				numFullCorStudents++;
			} // if
		} // for (students)

		return (double) numFullCorStudents / totalNumStudent;
	}

	// return the metrics of all students have 8/8 requested classes
	public static double genFullReqMetrics(ArrayList<Student> stuList) {
		int totalNumStudent = stuList.size();
		int numFullReqStudents = 0;

		// for every student
		for (Student s : stuList) {
			int numReqCoursesGiven = 0;
			for (Course reqCourse : s.getRequestedCourses()) {
				for (CourseSection actualCourse : s.getTimeTable().getAllCourseSections()) {
					if (actualCourse.getCourse().getCode().equals(reqCourse.getCode())) {
						numReqCoursesGiven++;
						break;
					}
				} // for (student s' requested courses)
			} // for (student s' actual courses)
			if (numReqCoursesGiven == 8) {
				System.out.println(s.getID());
				System.out.println(s.getTimeTable());
				numFullReqStudents++;
			} // if
		} // for (students)

		return (double) numFullReqStudents / totalNumStudent;
	}

	// return the metrics all students have 7-8/8 requested classes
	public static double genSufficientReqMetrics(ArrayList<Student> stuList) {
		int totalNumStudent = stuList.size();
		int numFullReqStudents = 0;

		for (Student s : stuList) {
			int numReqCoursesGiven = 0;
			for (Course reqCourse : s.getRequestedCourses()) {
				for (CourseSection actualCourse : s.getTimeTable().getAllCourseSections()) {
					if (actualCourse.getCourse().getCode().equals(reqCourse.getCode())) {
						numReqCoursesGiven++;
						break;
					}
				} // for (student s' requested courses)
			} // for (student s' actual courses)
			if (numReqCoursesGiven >= 7 && numReqCoursesGiven < 9) {
				numFullReqStudents++;
			} // if
		} // for (students)

		return (double) numFullReqStudents / totalNumStudent;
	}
	
	public static Timetable generateTimetable() {
		Timetable t = new Timetable();
		
		for(Course c : courses) {
			for (int i = 0; i < c.getNumSections(); i++) {
				int slot = (int) (Math.random() * 8.0);
				t.addSection(slot, c.getSection(i));
				c.getSection(i).setBlock(slot);
				c.getSection(i).setIndex(t.getSchedule(slot).size());
			}
		}
		
		return t;
	}// generateTimetable

	public static void resetSections(Timetable table) {

		for (int i = 0; i < 8; i++) {
			for (CourseSection c : table.getTimetable()[i]) {
				c.clearSection();
			}
		}
	}// resetSections
	
	public static void generateStudents() throws IOException{
		BufferedReader in = null;
		
		
		// read in data
		try {
			in = new BufferedReader(new FileReader("cleanedstudentrequests.csv")); // Read student requests 
		} catch (Exception e) {
			System.out.println("File input error");
		}

		int lines = 0;
		while(in.readLine() != null) {
			lines++;
		}
		in = new BufferedReader(new FileReader("cleanedstudentrequests.csv"));
		String [][] data = new String [lines][6];
		String line;
		for (int i = 0; i < lines; i++) {
			line = in.readLine();
			//System.out.println(line);
			data[i] = line.split(",");
		}
		
		Student student = null;
		Course c = null;
		for(int i = 0; i < lines; i++) {
			if(data[i][0].equals("ID")) {
				if(student != null) {
					students.add(student);
				} 
				student = new Student(data[i][1]);
			} else if(!data[i][0].equals("Course")) {
				c = getCourse(data[i][0]);
				if (c != null) {
					if(data[i][3].equals("Y")) {
						student.addAlternateCourse(c);
					} else if (data[i][3].equals("N")) {
						student.addRequestedCourse(c);
					}
				}
				
				
			}
		}// for i
	}
	
	public static void generateBlockingRules() throws IOException{
		BufferedReader in = null;
		
		// read in data
		try {
			in = new BufferedReader(new FileReader("Course Blocking Rules.csv")); // Read student requests 
		} catch (Exception e) {
			System.out.println("File input error");
		}

		int lines = 0;
		while(in.readLine() != null) {
			lines++;
		}
		in = new BufferedReader(new FileReader("Course Blocking Rules.csv"));
		String line;
		for (int i = 0; i < lines; i++) {
			line = in.readLine();
			String[] lineSplit = line.split(",");
			if(getCourse(lineSplit[0]) == null) continue;
			for(int j = 1; j < lineSplit.length-1; j++) {
				if(getCourse(lineSplit[j]) == null) continue;
				
				if(lineSplit[lineSplit.length-1].equals("Simultaneous")) {
					getCourse(lineSplit[0]).addSimultaneousCourseReciprocal(getCourse(lineSplit[j]));
				} else if(lineSplit[lineSplit.length-1].equals("NotSimultaneous")) {
					getCourse(lineSplit[0]).addNotSimultaneousCourseReciprocal(getCourse(lineSplit[j]));
				}
			}
		}
	}
	
	public static void generateCourses() throws IOException{
		BufferedReader in = null;
		
		
		// read in data
		try {
			in = new BufferedReader(new FileReader("Course Information (tally).csv")); // Read student requests 
		} catch (Exception e) {
			System.out.println("File input error");
		}

		int lines = 0;
		while(in.readLine() != null) {
			lines++;
		}
		in = new BufferedReader(new FileReader("Course Information (tally).csv"));
		String [][] data = new String [lines][9];
		String line;
		for (int i = 0; i < lines; i++) {
			line = in.readLine();
			//System.out.println(line);
//			if(line.contains("Course"));
			data[i] = line.split(",");
			for (int j = 0; j < data[i].length; j++) {
//				System.out.println(data[i][j]);
			}
//			System.out.println("-----------------------------------------");
		}
		
		Course course = null;
		for(int i = 0; i < lines; i++) {
			if(data[i].length == 9) {
				course = new Course(data[i][1], data[i][0], data[i][7], data[i][8]);
				courses.add(course);
			}
		}// for i
		courses.add(course);
	}

	public static void generateCourseSeqRules() throws IOException {
		BufferedReader br = null;
		int lines = 0;

		try {
			br = new BufferedReader(new FileReader("Course Sequencing Rules.csv"));
		} catch (Exception e) {
			System.out.println("File input error");
		}

		while (br.readLine() != null) {
			lines++;
		}

		br = new BufferedReader(new FileReader("Course Sequencing Rules.csv"));
		String[][] data = new String[lines][8];
		String line;
		for (int i = 0; i < lines; i++) {
			line = br.readLine();
			data[i] = line.split(",");
			Course courseZero = null; // uses to store the course corresponding to data[i][0]
			for (Course cr : courses) {
				if (cr.getCode().equals(data[i][0])) {
					courseZero = cr;
				}
			}
			
			//populate CourBefore when needed
			for (int j = 1; j < data[i].length; j++) {
				for (Course c : courses) {
					if (c.getCode().equals(data[i][j]) && courseZero != null) {
						c.addCourBefore(courseZero);
					} // if
				} // enhanced for
			} // for j
		} // for i


//		for(Course c : courses) {
//			if(c.getCourBefore().size() == 0) continue;
//			System.out.print("The following courses must appear before " + c.getName() + ": ");
//			for(int i = 0; i < c.getCourBefore().size(); i++) {
//				System.out.print(c.getCourBefore().get(i).getName() + ",");
//			}
//			System.out.println();
//		} // for
	} // generateCourseSeqRules
	
	// Find course in course arrayList
	public static Course getCourse(String code) {
		for(Course c : courses) { 
			if(c.getCode().equals(code)) {
				return c;
			}
		}
		return null;
	}
}// Main
