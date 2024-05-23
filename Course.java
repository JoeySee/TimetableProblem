import java.util.ArrayList;

public class Course {
	private String name;
	private String code;
	private ArrayList<Student> students = new ArrayList<Student>();
	
	public Course(String name, String c) {
		this.name = name;
		this.code = c;
	}
	
	public ArrayList<Student> getStudents(){
		return students;
	}
	
	public void addStudent(Student newStudent) {
		students.add(newStudent);
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
}
