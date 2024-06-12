import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	static ArrayList<Course> courses = new ArrayList<Course>();
	static ArrayList<Course> coursesToCheck = new ArrayList<Course>();
	static ArrayList<Student> students = new ArrayList<Student>();
	
	public static void main(String[] args) throws IOException {
		generateCourses();
		generateCourseSeqRules();
		generateStudents(students);
		generateBlockingRules();
		
		generateStudentPreferences();
		
		for(int i = 0; i < courses.size(); i++) {
			coursesToCheck.add(courses.get(i));
		}
		
		Timetable t = generateGreedyTable();
		
		for(Student s : students) {
			s.addToCourses();		
		}
		
		System.out.println(t);
		
		purgeExcessCourses(t);
		
		
		// Output the best timetable and metrics
		System.out.println(t);
		
		System.out.println(reqCoursePlaced(students));
		System.out
				.println("Percent of all requested courses placed: " + genReqCourseMetrics(students) * 100 + "%");
		System.out.println("Percent of all students who have 8/8 requested classes: "
				+ genFullReqMetrics(students) * 100 + "%");
		System.out.println("Percent of all students have 7-8/8 requested classes: "
				+ genSufficientReqMetrics(students) * 100 + "%");
		System.out.println("The % of students with 8/8 courses (requested or alternate): "
				+ genFullCorMetrics(students) * 100 + "%");
		
	}// main
	
	public static Timetable generateGreedyTable() {
		Timetable solution = new Timetable();
		// Get course with highest single requestPriority
		while(coursesToCheck.size() != 0) {
			int highIndices = 0;
			int[] prefsInOrder = {0,1,2,3,4,5,6,7}; // Slot Preference indices stored in order of preference
			double highPref = 0;
			for(int i = 0; i < coursesToCheck.size(); i++) {
				double[] preferences = coursesToCheck.get(i).getPreferences();
				double highPrefLoop = 0;
				int highIndexLoop = 0;
				for(int j = 0; j < preferences.length; j++) {
					if(preferences[j] > highPrefLoop) {
						highPrefLoop = preferences[j];
						highIndexLoop = j;
					}
				}
				if(highPrefLoop > highPref) {
					highPref = highPrefLoop;
					highIndices = i;
				}
			}// for
			double[] preferences = coursesToCheck.get(highIndices).getPreferences();
			
			// Sort preferences
			for(int i = 0; i < prefsInOrder.length; i++) {
				int temp = prefsInOrder[i];
				highPref = preferences[i];
				int highIndex = i;

				for(int j = i+1; j < prefsInOrder.length; j++) {
					if(preferences[j] > highPref || (preferences[j] == highPref)) {
						highIndex = j;
						highPref = preferences[j];
					}
				}
				prefsInOrder[i] = prefsInOrder[highIndex];
				prefsInOrder[highIndex] = temp;
			}
			
			// Assign the course to the timetable based on preferences
			for(int slot : prefsInOrder) {
				coursesToCheck.get(highIndices).addPercentSections(solution, slot);
			}
			coursesToCheck.get(highIndices).addExcessSections(solution);
			coursesToCheck.get(highIndices).addRequestedStudents();
			
			// Re-generate preferences with new student courses
			coursesToCheck.remove(highIndices);
			resetStudentPreferences(coursesToCheck);
			generateStudentPreferences();
		}
		return solution;
	}
	
	public static void generateStudentPreferences() {
		for(Student s : students) {
			// Iterate through this students courses
			for(Course c : s.getRequestedCourses()) {
				ArrayList<Integer> slotPreferences = s.getEmptySlots(); // Slots that this student (s) would like this course (c) to be in
				boolean semesterPriority = false;
				// Check if this courses has any sequencing rules that interact with this student's other courses
				for(Course cSeq : c.getCourBefore()) {
					if(s.getRequestedCourses().contains(cSeq)) {
						semesterPriority = true;
						for(int i = 0; i < slotPreferences.size(); i++) {
							if(i > 4) slotPreferences = (ArrayList<Integer>) copyArrayListPortion(slotPreferences, i, slotPreferences.size());
						}
					}// if
				}// for cSeq
				if(!semesterPriority) {
					for(Course cSeq : c.getCourAfter()) {
						if(s.getRequestedCourses().contains(cSeq)) {
							semesterPriority = true;
							for(int i = 0; i < slotPreferences.size(); i++) {
								if(i > 4) slotPreferences = (ArrayList<Integer>) copyArrayListPortion(slotPreferences, 0, i);
							}
						}// if
					}// for cSeq
				}
				c.addPreferences(slotPreferences);
			}// for c
		}// for s
	}// generateStudents
	
	// Copy portion of arrayList from i (inclusive) to j (exclusive)
	public static ArrayList copyArrayListPortion(ArrayList a, int i, int j) {
		ArrayList newA = new ArrayList();
		for(int n = i; n < j; n++) {
			newA.add(a.get(n));
		}
		return a;
	}
	
	public static void resetStudentPreferences(ArrayList<Course> c) {
		for(int i = 0; i < c.size(); i++) {
			c.get(i).resetPreference();
		}
	}
	
	public static void resetSections(Timetable table) {
		for(int i = 0; i < 8; i++) {
			for(CourseSection c : table.getTimetable()[i]) {
				c.clearSection();
			}
		}
	}// resetSections

	// purge courses from course array running at less than half cap
	public static void purgeExcessCourses(Timetable t) {
		for(int i = courses.size()-1; i >= 0; i--) {
			for(int k = 0; k < courses.get(i).getNumSections(); k++) {
				boolean innerBreak = false;
				if(courses.get(i).getSection(k) == null) continue;
				if((double)courses.get(i).getSection(k).getNumStudents() / courses.get(i).getCapacity() < .5) {
					for(CourseSection c : t.getTimetable()[courses.get(i).getSection(k).getBlock()]) {
						if(courses.get(i).getSimultaneousCourses().contains(c.getCourse())) {
							courses.get(i).getSection(k).addPairedCourse(c);
							innerBreak = true;
							break;
						}
					}
					if(innerBreak) continue;
					ArrayList<Student> studentsToEnroll = courses.get(i).getStudentsInSection(k);
					courses.get(i).removeSection(k);
					for(Student s : studentsToEnroll) {
						s.addToCourses();		
					}
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
		
		for(Course c : courses) {
			for (int i = 0; i < c.getNumSections(); i++) {
				int slot;
				//System.out.println(c.getS2Percent());
				if(c.getS2Percent() < 0) {
					slot = (int) (Math.random() * 8.0);
				} else {
					int offset = 0;
					if((double)i / c.getNumSections() <= c.getS2Percent()) {
						offset = 4;
					}
					slot = offset + (int) (Math.random() * 4.0);
				}
				if(c.getSection(i) != null) {
					t.addSection(slot, c.getSection(i));
					c.getSection(i).setBlock(slot);
					c.getSection(i).setIndex(t.getSchedule(slot).size());
				}
			}
		}
		
		return t;
	}// generateTimetable
	
	public static void generateStudents(ArrayList<Student> st) throws IOException{
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
					st.add(student);
				} 
				student = new Student(data[i][1]);
			} else if(!data[i][0].equals("Course")) {
				c = getCourse(data[i][0]);
				if (c != null) {	
					if(data[i][3].equals("Y")) {
						student.addAlternateCourse(c);
					} else if (data[i][3].equals("N")) {
						c.addRequestStudent(student);
						student.addRequestedCourse(c);
						
					}
				}
			}
		}// for i
		//
		for (int i = 0; i < students.size(); i++) {
			student = students.get(i);
			for(int j = 0; j < student.getRequestedCourses().size(); j++) {
				c = student.getRequestedCourses().get(j);
				//System.out.println(c.getCourBefore());
				//System.out.println("s1 req: " + d.getCode() + "| s2 req: " + c.getCode());
				
				/*System.out.println("d: " + d.getCode());
				System.out.println("courBefores:");*/
				/*for (int k = 0; k < courBefores.size(); k++) {
					System.out.println(courBefores.get(k).getCode());
				}*/
				//System.out.println("c: " + c.getCode());

				/*if (student.getID() == 1002) {
					System.out.println("student 1002 has " + c.getCode() + "with courBefores:");
					for (int k = 0; k < courBefores.size(); k++) {
						System.out.println(courBefores.get(k).getCode());
					}
				}*/
				ArrayList<Course> courBefores = c.getCourBefore();
				Course c1 = null;
				//Course c1;
				for(int k = 0; k < courBefores.size(); k++) {
					//c1 = courBefores.get(k);

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
				
				
				/*if(c.getCourBefore().contains(d)) {
					System.out.println("student ID:" + student.getID() + "| s1 req: " + d.getCode() + "| s2 req: " + c.getCode());
					d.addS1Request();
					c.addS2Request();
					
				} else {
					
				}*/
				
			}
		}
		
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
			if(getCourse(lineSplit[0]) == null) { continue;}
			for(int j = 1; j < lineSplit.length-1; j++) {
				if(getCourse(lineSplit[j]) == null) { continue;}
				
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
				//System.out.println
				if (cr.getCode().equals(data[i][0])) {
					courseZero = cr;
					
				}
			}
			for (int j = 0; j < data[i].length; j++) {
				//System.out.println(data[i][j]);
			}
			//System.out.println("------------------------------");
			//populate CourBefore when needed
			if (courseZero != null) {
				//System.out.println(courseZero.getCode() + " before:");
			}
			
			for (int j = 1; j < data[i].length; j++) {
				for(int k = 0; k < courses.size(); k++) {
					
					if (courses.get(k).getCode().equals(data[i][j]) && courseZero != null) {
						courseZero.addCourAfter(courses.get(k));
						courses.get(k).addCourBefore(courseZero);
					} // if
				}
			} // for j
		} // for i

		System.out.println("list of courBefores:");
		ArrayList<Course> courBefores = null;
		for (int i = 0; i < courses.size() ; i++) {
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
		for(Course c : courses) { 
			if(c.getCode().equals(code)) {
				return c;
			}
		}
		return null;
	}
}// Main
