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

		for (Student s : students) {
			s.addToCourses();
		}

		for (Student s : students) {
			ArrayList<CourseSection> cs = s.getTimeTable().getAllCourseSections();
			// System.out.println(s.getID());
			for (CourseSection a : cs) {
				// System.out.println(a.getCourse().getName());
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
		System.out
				.println("Percent of all students who have 8/8 requested classes: " + genFullReqMetrics() * 100 + "%");
		System.out.println(
				"Percent of all students have 7-8/8 requested classes: " + genSufficientReqMetrics() * 100 + "%");

		// WRITTEN BY JOEY and owne
//		int j = 0;
//		int k = 0;
//		Timetable t1 = null;
//		double highScore = genReqCourseMetrics();
//		double oldScore = highScore;
//		Timetable bestTable = t.clone();
//		int loopCount = 0;
//		Timetable t2 = t.clone();
//		while(true) {
//			k = (int) (Math.random() * 8);
//            j = (int) (Math.random()*t.getTimetable()[k].size());
//            CourseSection c = t.getTimetable()[k].get(j);
//            t.deleteSection(k, t.getTimetable()[k].get(j).getCourse());
//            for(int i = 0 ; i < 8; i++) {
//            	t1 = t.clone();
//                resetSections(t1);
//            	t1.addSection(i, c);
//                
//                
//                
//                students = new ArrayList<Student>();
//                generateStudents();
//                for(Student s : students) {
//        			s.addToCourses();
//        			
//        		}
//                
//                if(genReqCourseMetrics() >= highScore) {
//                   bestTable = t1.clone();
//                   highScore = genReqCourseMetrics();
//                }
//            }
//            
//            if(highScore >= oldScore) {
//            	t = bestTable.clone();
//            }
//            
//            System.out.println(t2.toString().equals(t.toString()));
//            System.out.print(loopCount + ": ");
//            System.out.println(highScore);
//            System.out.println(t);
//            loopCount++;
//            t2 = t.clone();
//            
//            if(highScore > .7 || loopCount > 2001) {
//            	break;
//            }
//        }
		
		Timetable bestTable = t.clone();
		double highScore = genReqCourseMetrics();
		double curScore = 0;
		Timetable t2 = t.clone();
		System.out.println(bestTable);
		System.out.println(highScore);
		
		for (int it = 0; it < 3000; it++) {
			t = generateTimetable();
			
			students = new ArrayList<Student>();
			generateStudents();
			System.out.println(students.size());
			for (Student s : students) {
				s.addToCourses();
			}
			
			curScore = genReqCourseMetrics();
			if (curScore > highScore) {
				bestTable = t.clone();
				highScore = curScore;
			}

			System.out.println(t2.toString().equals(t.toString()));
			System.out.print(it + ": ");
			System.out.println(curScore);
			System.out.println("high score: " + highScore);
			System.out.println(t);
			t2 = t.clone();

			if (highScore > .7) {
				break;
			}

		}
		
		t = bestTable.clone();
		students = new ArrayList<Student>();
		generateStudents();
		for (Student s : students) {
			s.addToCourses();
		}
		
		System.out.println(t);
		System.out.println("Percent of all requested courses placed: " + genReqCourseMetrics() * 100 + "%");
		System.out
				.println("Percent of all students who have 8/8 requested classes: " + genFullReqMetrics() * 100 + "%");
		System.out.println(
				"Percent of all students have 7-8/8 requested classes: " + genSufficientReqMetrics() * 100 + "%");

	}// main

	// returns the metrics all requested courses placed
	public static double genReqCourseMetrics() {
		int totalReqCourses = 0;
		int totalPlacedReqCourses = 0;

//		
		System.out.println("Student Size: " + students.size());
		
		for (Student s : students) {
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
		
//		System.out.println("Total Placed: " + totalPlacedReqCourses);
//		System.out.println("Total Req: " + totalReqCourses);
		
		return (double) totalPlacedReqCourses / (double) totalReqCourses;
	} // genReqCourseMetrics

	// return the metrics of all students have 8/8 requested classes
	public static double genFullReqMetrics() {
		int totalNumStudent = students.size();
		int numFullReqStudents = 0;

		//for every student
		for (Student s : students) {
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
	public static double genSufficientReqMetrics() {
		int totalNumStudent = students.size();
		int numFullReqStudents = 0;

		for (Student s : students) {
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
		

		for (Course c : courses) {
			for (int i = 0; i < c.getNumSections(); i++) {
				c.getSection(i).clearSection();
				int slot = (int) (Math.random() * 8.0);
				t.addSection(slot, c.getSection(i));
				c.getSection(i).setBlock(slot);
				c.getSection(i).setIndex(t.getSchedule(slot).size());
			}
		}

		return t;
	}// generateTimetable
	
	public static void resetSections(Timetable table) {

		
		for(int i = 0; i < 8; i++) {
			for(CourseSection c : table.getTimetable()[i]) {
				c.clearSection();
			}
		}
	}// resetSections

	public static void generateStudents() throws IOException {
		BufferedReader in = null;

		// read in data
		try {
			in = new BufferedReader(new FileReader("cleanedstudentrequests.csv")); // Read student requests
		} catch (Exception e) {
			System.out.println("File input error");
		}

		int lines = 0;
		while (in.readLine() != null) {
			lines++;
		}
		in = new BufferedReader(new FileReader("cleanedstudentrequests.csv"));
		String[][] data = new String[lines][6];
		String line;
		for (int i = 0; i < lines; i++) {
			line = in.readLine();
			// System.out.println(line);
			data[i] = line.split(",");
		}

		Student student = null;
		Course c = null;
		for (int i = 0; i < lines; i++) {
			if (data[i][0].equals("ID")) {
				if (student != null) {
					students.add(student);
				}
				student = new Student(data[i][1]);
			} else if (!data[i][0].equals("Course")) {
				c = getCourse(data[i][0]);
				if (c != null) {
					if (data[i][3].equals("Y")) {
						student.addAlternateCourse(c);
					} else if (data[i][3].equals("N")) {
						student.addRequestedCourse(c);
					}
				}

			}
		} // for i
	}

	public static void generateBlockingRules() throws IOException {
		BufferedReader in = null;

		// read in data
		try {
			in = new BufferedReader(new FileReader("Course Blocking Rules.csv")); // Read student requests
		} catch (Exception e) {
			System.out.println("File input error");
		}

		int lines = 0;
		while (in.readLine() != null) {
			lines++;
		}
		in = new BufferedReader(new FileReader("Course Blocking Rules.csv"));
		String line;
		for (int i = 0; i < lines; i++) {
			line = in.readLine();
			String[] lineSplit = line.split(",");
			if (getCourse(lineSplit[0]) == null)
				continue;
			for (int j = 1; j < lineSplit.length - 1; j++) {
				if (getCourse(lineSplit[j]) == null)
					continue;

				if (lineSplit[lineSplit.length - 1].equals("Simultaneous")) {
					getCourse(lineSplit[0]).addSimultaneousCourseReciprocal(getCourse(lineSplit[j]));
				} else if (lineSplit[lineSplit.length - 1].equals("NotSimultaneous")) {
					getCourse(lineSplit[0]).addNotSimultaneousCourseReciprocal(getCourse(lineSplit[j]));
				}
			}
		}
	}

	public static void generateCourses() throws IOException {
		BufferedReader in = null;

		// read in data
		try {
			in = new BufferedReader(new FileReader("Course Information (tally).csv")); // Read student requests
		} catch (Exception e) {
			System.out.println("File input error");
		}

		int lines = 0;
		while (in.readLine() != null) {
			lines++;
		}
		in = new BufferedReader(new FileReader("Course Information (tally).csv"));
		String[][] data = new String[lines][9];
		String line;
		for (int i = 0; i < lines; i++) {
			line = in.readLine();
			// System.out.println(line);
//			if(line.contains("Course"));
			data[i] = line.split(",");
			for (int j = 0; j < data[i].length; j++) {
//				System.out.println(data[i][j]);
			}
//			System.out.println("-----------------------------------------");
		}

		Course course = null;
		for (int i = 0; i < lines; i++) {
			if (data[i].length == 9) {
				course = new Course(data[i][1], data[i][0], data[i][7], data[i][8]);
				courses.add(course);
			}
		} // for i
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

			// populate CourBefore when needed
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
		for (Course c : courses) {
			if (c.getCode().equals(code)) {
				return c;
			}
		}
		return null;
	}
}// Main
