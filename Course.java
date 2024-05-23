import java.util.ArrayList;

public class Course {
	private String name;
	private ArrayList<Student> students = new ArrayList<Student>();
	
	public Course(String name) {
		this.name = name;
	}
	
	public ArrayList<Student> getStudents(){
		return students;
	}
	
	public void addStudent(Student newStudent) {
		students.add(newStudent);
	}
}
