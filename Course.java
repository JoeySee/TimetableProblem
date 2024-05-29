import java.util.ArrayList;

public class Course {
	private String name;
	private String code;
	private int capacity;
	private int [] block; // Position within timetable (0-7)
	private int [] index; // Position within block (0-?)
	private int lastIndex;
	private int sections;
	private ArrayList<Course> simultaneousCourses; // Courses that can occur as a split with this one
	private ArrayList<Course> notSimultaneousCourses; // Courses that can occur linearly with this one
	private ArrayList<Student> [] students;
	
	public Course(String name, String c, String cap, String s) {
		this.name = name;
		this.code = c;
		lastIndex = -1;
		sections = Integer.parseInt(s);
		block = new int [sections];
		index = new int [sections];
		simultaneousCourses = new ArrayList<Course>();
		notSimultaneousCourses = new ArrayList<Course>();
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
				//System.out.println("added student in session " + i);
				break;
			} else {
				//System.out.println("full");
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
		if(lastIndex < sections){
			this.block[lastIndex] = block;
			this.index[lastIndex] = index;
		}	
	}
	
	public void addSimultaneousCourseReciprocal(Course c) {
		simultaneousCourses.add(c);
		c.addSimultaneousCourse(this);
	}
	
	public void addSimultaneousCourse(Course c) {
		simultaneousCourses.add(c);
	}
	
	public void addNotSimultaneousCourseReciprocal(Course c) {
		notSimultaneousCourses.add(c);
		c.addNotSimultaneousCourse(this);
	}
	
	public void addNotSimultaneousCourse(Course c) {
		notSimultaneousCourses.add(c);
	}
	
	public void test() {
		System.out.println(simultaneousCourses.get(0).getName());
	}
	
	public boolean isCourseNotSimultaneous(Course c) {
		boolean isFound = false;
		for(Course d : notSimultaneousCourses) {
			if(d.equals(c)) isFound = true;
		}
		return isFound;
	}
	
	public boolean isCourseSimultaneous(Course c) {
		boolean isFound = false;
		for(Course d : simultaneousCourses) {
			if(d.equals(c)) isFound = true;
		}
		return isFound;
	}
}
