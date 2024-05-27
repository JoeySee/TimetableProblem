import java.util.ArrayList;

public class Course {
	private String name;
	private String code;
	private int capacity = 30;
	private int block; // Position within timetable (0-7)
	private int index; // Position within block (0-?)
	private int sessions;
	private ArrayList<Student> students = new ArrayList<Student>();
	
	public Course(String name, String c, String cap, String s) {
		this.name = name;
		this.code = c;
		block = 0;
		index = 0;
		capacity = Integer.parseInt(cap) * Integer.parseInt(s);
		sessions = Integer.parseInt(s);
	}
	
	public ArrayList<Student> getStudents(){
		return students;
	}
	
	public void addStudent(Student newStudent) {
		students.add(newStudent);
		capacity--;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public int getBlock() {
		return block;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setTimetablePos(int block, int index) {
		this.block = block;
		this.index = index;
	}
}
