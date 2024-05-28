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
		Timetable t = generateTimetable();
		
		for(Student s : students) {
			s.addToCourses();
		}
		
		for(Student s : students) {
			ArrayList <Course> c = s.getActualCourses();
			System.out.println(s.getID());
			for(Course a : c) {
				System.out.println(a.getName());
			}
			System.out.println("----------------------------");
		}
		
		
	}// main
	
	public static Timetable generateTimetable() {
		Timetable t = new Timetable();
		
		for(Course c : courses) {
			int slot = (int) Math.random()*8;
			t.addCourse(slot, c);
		}
		
		return t;
	}// generateTimetable
	
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
			if(line.contains("Course")) continue;
			data[i] = line.split(",");
			for (int j = 0; j < data[i].length; j++) {
				System.out.println(data[i][j]);
			}
			System.out.println("-----------------------------------------");
		}
		
		Student student = null;
		for(int i = 0; i < lines; i++) {
			if(data[i][0] != null && data[i][0].equals("ID")) {
				if(student != null) {
					students.add(student);
				} 
				student = new Student(data[i][1]);
			} else {
				Course c = getCourse(data[i][0]);
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
			if(line.contains("Course"));
			data[i] = line.split(",");
		}
		
		Course course = null;
		for(int i = 0; i < lines; i++) {
			course = new Course(data[i][1], data[i][0]);
			courses.add(course);
		}// for i
		courses.add(course);
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
			if(line.contains("Course"));
			data[i] = line.split(",");
			for (int j = 0; j < data[i].length; j++) {
				System.out.println(data[i][j]);
			}
			System.out.println("-----------------------------------------");
		}
		
		Course course = null;
		for(int i = 0; i < lines; i++) {
			course = new Course(data[i][1], data[i][0], data[i][7], data[i][8]);
			courses.add(course);
		}// for i
		courses.add(course);
	}
	
	// Find course in course arrayList
	public static Course getCourse(String code) {
		for(Course c : courses) {
			if(c.getCode().equals(code)) {
				return c;
			}
		}
		System.out.println(code);
		return null;
	}
}// Main
