import java.util.ArrayList;

public class Course {
	private String name;
	private String code;
	private int capacity;
	private int [] block; // Position within timetable (0-7)
	private int [] index; // Position within block (0-?)
	private int lastIndex;
	private int sections;
	private ArrayList<Student> [] students;
	
	public Course(String name, String c, String cap, String s) {
		this.name = name;
		this.code = c;
		lastIndex = -1;
		sections = Integer.parseInt(s);
		block = new int [sections];
		index = new int [sections];
		capacity =  Integer.parseInt(cap);
		students = (ArrayList<Student> [])new ArrayList [sections];
		for (int i = 0; i < students.length; i++) {
			students[i] = new ArrayList<Student>();
		}
	}
	
	public ArrayList<Student> [] getStudents(){
		return students;
	}
	
	public ArrayList<Student> getStudents(int sec){
		return students[sec];
	}
	
	public void addStudent(Student newStudent) {
		for (int i = 0; i < students.length; i++) {
			if (students[i].size() < capacity) {
				students[i].add(newStudent);
				System.out.println("added student in session " + i);
				break;
			} else {
				System.out.println("full");
			}
		}
		
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public int getBlock(int sec) {
		return block[sec];
	}
	
	public int getIndex(int sec) {
		return index[sec];
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getSections() {
		return sections;
	}
	
	public int getNumStudents() {
		int n = 0;
		for (int i = 0; i < students.length; i++) {
			n += students[i].size();
		}
		return n;
	}
	
	public int getNumStudents(int sec) {
		return students[sec].size();
	}
	
	public void setTimetablePos(int block, int index) {
		lastIndex++;
		this.block[lastIndex] = block;
		this.index[lastIndex] = index;
	}
}
