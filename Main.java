import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	ArrayList<Course> courses = new ArrayList<Course>();
	ArrayList<Student> students = new ArrayList<Student>();
	
	public static void main(String[] args) throws IOException {
		
		generateCourses();
		//generateStudents();
	}// main
	
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
			if(line.contains("Course"));
			data[i] = line.split(",");
			for (int j = 0; j < data[i].length; j++) {
				System.out.println(data[i][j]);
			}
			System.out.println("-----------------------------------------");
		}
		
		Student student = null;
		for(int i = 0; i < lines; i++) {
			if(data[i][0].equals("ID")) {
				student = new Student(data[i][1]);
			} else {
				if(data[i][3].equals("Y")) {
					student.addAlternateCourse(null);
				}
				
			}
			
		}// for i
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
		
		
	}
	
	// Find course in course arrayList
	public Course getCourse(String name) {
		Course result = null;
		//for(int i = 0; i < )
		return result;
	}
}// Main
