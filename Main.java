import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	static ArrayList<Course> courses = new ArrayList<Course>();
	static ArrayList<Student> students = new ArrayList<Student>();
	static Student[] friends = null;

	public static void main(String[] args) throws IOException {
		ArrayList<Student> bTableStudents = new ArrayList<Student>();
		generateCourses();
		generateCourseSeqRules();
		generateStudents(students);
		generateBlockingRules();
		friends = new Student[students.size()];

		bTableStudents = new ArrayList<Student>();
		generateStudents(bTableStudents);

		Timetable t = generateTimetable();

//		for(Student s : students) {
//			s.addToCourses();
//		}

		for (Student s : students) {
			ArrayList<CourseSection> cs = s.getTimeTable().getAllCourseSections();
			// System.out.println(s.getID());
//			for (CourseSection a : cs) {
//				// System.out.println(a.getCourse().getName());
//			}
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

//		 WRITTEN BY JOEY and owne
//		int j = 0;
//		int k = 0;
//		Timetable t1 = null;
//		double highScore = genReqCourseMetrics(students);
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
//            	
//            	t1 = t.clone();
//                t1.addSection(i, c);
//                
//                
//                
//                students = new ArrayList<Student>();
//                generateStudents(students);
//                for(Student s : students) {
//        			s.addToCourses();
//        			
//        		}
//                
//                if(genReqCourseMetrics(students) > highScore) {
//                   bestTable = t1.clone();
//                   bTableStudents = students;
//                   highScore = genReqCourseMetrics(students);
//                }
//                
////                resetSections(t);
//                
//                for(int p = 0; p < 20; p++) {
//    				purgeExcessCourses();
//    				students = new ArrayList<Student>();
//    				resetSections(t);
//    				generateStudents(students);
//    				for(Student s : students) {
//    					s.addToCourses();	
//    				}
//    			}
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
//            if(highScore > oldScore || highScore > .7 || loopCount > 2001) {
//            	break;
//            }
//        }
//		System.out.println(t);

		// Optimization loop to find the best timetable
		Timetable bestTable = t.clone(); // Initialize bestTable with the initial timetable
		double highScore = genReqCourseMetrics(students); // Initialize highScore with the metrics of the initial
															// timetable
		double curScore = 0;

		for (int it = 0; it < 100; it++) { // Example: Run the optimization loop 10 times

			resetSections(t);
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
			if (curScore > highScore || it == 0) {
				bTableStudents = students;
				bestTable = t.clone(); // Update the best timetable
				highScore = genReqCourseMetrics(bTableStudents); // Update the high score
				System.out.println(reqCoursePlaced(students));
			}

			// Output the current score and high score for monitoring
			System.out.print(it + ": ");
			System.out.println(curScore);
			System.out.println("high score: " + highScore);

//			for (int p = 0; p < 2; p++) {
//				purgeExcessCourses();
//				students = new ArrayList<Student>();
//				resetSections(t);
//				generateStudents(students);
//				for (Student s : students) {
//					s.addToCourses();
//				}
//			}

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
		// Written by Joey and owne

	public static Timetable friendshipSolution(Timetable friendT) {

		if (students.size() == 0)
			return friendT;

		Timetable solution = new Timetable();
		int friendNum = (int) (Math.random() * students.size());
		Student friend = students.get(friendNum);
		students.remove(friendNum);
		friends[friend.getID() - 1000] = friend;
		ArrayList<Integer> emptySlots = new ArrayList<Integer>();
		Course[] without = new Course[8];
		friend.addToReqCourses();

		Course[] friendCourses = new Course[8];
		for (int i = 0; i < 8; i++) {
			if (friend.getTimeTable().getSchedule(i) == null) {
				friendCourses[i] = friend.getTimeTable().getSchedule(i).get(0).getCourse();
			}
		}

		for (int j = 0; j < friend.getRequestedCourses().size(); j++) {
			for (int i = 0; i < 8; i++) {
				if (i == j) {
					if (!friend.getRequestedCourses().get(j).equals(friendCourses[i])) {
						emptySlots.add(i);
						without[i] = friend.getRequestedCourses().get(j);
					}
				}
			}
		}

		for (int i : emptySlots) {
			int ran = (int) (Math.random() * without[i].getNumSections());
			if (without[i].getNumSections() != 0) {
				friendT.deleteSection(without[i].getSection(ran).getBlock(), without[i]);
				friendT.addSection(i, without[i].getSection(ran));
			}
		}

		System.out.println(friendT);
		friendshipSolution(friendT);

		return solution;

	}

	public static void resetSections(Timetable table) {
		for (int i = 0; i < 8; i++) {
			for (CourseSection c : table.getTimetable()[i]) {
				c.clearSection();
			}
		}
	}// resetSections

	// purge courses from course array running at less than half cap
	public static void purgeExcessCourses() {
		for (int i = courses.size() - 1; i >= 0; i--) {
			for (int k = 0; k < courses.get(i).getNumSections(); k++) {
				if (courses.get(i).getSection(k) == null)
					continue;
				if ((double) courses.get(i).getSection(k).getNumStudents() / courses.get(i).getCapacity() < .5
						|| (double) courses.get(i).getSection(k).getNumStudents() / courses.get(i).getCapacity() == 0) {
					courses.get(i).removeSection(k);
				}
			}

		}
	}

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

		for (Course c : courses) {
			for (int i = 0; i < c.getNumSections(); i++) {
				int slot;
				// System.out.println(c.getS2Percent());
				if (c.getS2Percent() < 0) {
					slot = (int) (Math.random() * 8.0);
				} else {
					int offset = 0;
					if ((double) i / c.getNumSections() <= c.getS2Percent()) {
						offset = 4;
					}
					slot = offset + (int) (Math.random() * 4.0);
				}
				if (c.getSection(i) != null) {
					t.addSection(slot, c.getSection(i));
					c.getSection(i).setBlock(slot);
					c.getSection(i).setIndex(t.getSchedule(slot).size());
				}
			}
		}

		return t;
	}// generateTimetable

	public static void generateStudents(ArrayList<Student> st) throws IOException {
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
					st.add(student);
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
			//
		for (int i = 0; i < students.size(); i++) {
			student = students.get(i);
			for (int j = 0; j < student.getRequestedCourses().size(); j++) {
				c = student.getRequestedCourses().get(j);
				// System.out.println(c.getCourBefore());
				// System.out.println("s1 req: " + d.getCode() + "| s2 req: " + c.getCode());

				/*
				 * System.out.println("d: " + d.getCode()); System.out.println("courBefores:");
				 */
				/*
				 * for (int k = 0; k < courBefores.size(); k++) {
				 * System.out.println(courBefores.get(k).getCode()); }
				 */
				// System.out.println("c: " + c.getCode());

				/*
				 * if (student.getID() == 1002) { System.out.println("student 1002 has " +
				 * c.getCode() + "with courBefores:"); for (int k = 0; k < courBefores.size();
				 * k++) { System.out.println(courBefores.get(k).getCode()); } }
				 */
				ArrayList<Course> courBefores = c.getCourBefore();
				Course c1 = null;
				// Course c1;
				for (int k = 0; k < courBefores.size(); k++) {
					// c1 = courBefores.get(k);

					for (int l = 0; l < student.getRequestedCourses().size(); l++) {
						c1 = student.getRequestedCourses().get(l);
						if (courBefores.get(k).getCode().equals(c1.getCode())) {
//							System.out.println("student ID:" + student.getID() + "| s1 req: " + c1.getCode() + "| s2 req: " + c.getCode());
							c1.addS1Request();
							c.addS2Request();
						}
					}
				}
				// to finish

				/*
				 * if(c.getCourBefore().contains(d)) { System.out.println("student ID:" +
				 * student.getID() + "| s1 req: " + d.getCode() + "| s2 req: " + c.getCode());
				 * d.addS1Request(); c.addS2Request();
				 * 
				 * } else {
				 * 
				 * }
				 */

			}
		}

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
			if (getCourse(lineSplit[0]) == null) {
				continue;
			}
			for (int j = 1; j < lineSplit.length - 1; j++) {
				if (getCourse(lineSplit[j]) == null) {
					continue;
				}

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
				// System.out.println
				if (cr.getCode().equals(data[i][0])) {
					courseZero = cr;

				}
			}
			for (int j = 0; j < data[i].length; j++) {
				// System.out.println(data[i][j]);
			}
			// System.out.println("------------------------------");
			// populate CourBefore when needed
			if (courseZero != null) {
				// System.out.println(courseZero.getCode() + " before:");
			}

			for (int j = 1; j < data[i].length; j++) {
				for (int k = 0; k < courses.size(); k++) {

					if (courses.get(k).getCode().equals(data[i][j]) && courseZero != null) {

						// System.out.println("added: " + courses.get(k).getCode());
						courses.get(k).addCourBefore(courseZero);
					} // if
				}
			} // for j
		} // for i

		System.out.println("list of courBefores:");
		ArrayList<Course> courBefores = null;
		for (int i = 0; i < courses.size(); i++) {
			courBefores = courses.get(i).getCourBefore();
			System.out.println("course is " + courses.get(i).getCode() + "\ncourBefores are:");
			for (int j = 0; j < courBefores.size(); j++) {
				System.out.println(courBefores.get(j).getCode());
			}
		}
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