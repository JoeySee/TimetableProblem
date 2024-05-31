import java.util.ArrayList;

public class Course {
	private String name;
	private String code;
	private int capacity;
	private int lastIndex;
	private int numSections;
	private CourseSection [] sections;
	private ArrayList<Course> courBefore = new ArrayList <Course>(); // these courses must appear before the current course
	private ArrayList<Course> simultaneousCourses; // Courses that can occur as a split with this one
	private ArrayList<Course> notSimultaneousCourses; // Courses that can occur linearly with this one
	
	public Course(String name, String c, String cap, String s) {
		this.name = name;
		this.code = c;
		lastIndex = -1;
		numSections = Integer.parseInt(s);
		capacity =  Integer.parseInt(cap);
		simultaneousCourses = new ArrayList<Course>();
		notSimultaneousCourses = new ArrayList<Course>();
		sections = new CourseSection [numSections];
		for (int i = 0; i < sections.length; i++) {
			sections[i] = new CourseSection (this, i);
		}
	}
	
	public CourseSection getSection(int i){
		return sections[i];
	}
	
	/*public ArrayList<Student> getStudents(int sec){
		return sections[sec].getStudents();
	}*/
	
	public void addCourBefore(Course c) {
		courBefore.add(c);
	}
	
	public ArrayList<Course> getCourBefore() {
		return courBefore;
	}
	
	public void addStudent(Student newStudent) {
		for (int i = 0; i < sections.length; i++) {
			if (sections[i].getStudents().size() < capacity) {
				sections[i].addStudent(newStudent);
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
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getNumSections() {
		return numSections;
	}
	
	public int getNumStudents() {
		int n = 0;
		for (int i = 0; i < numSections; i++) {
			n += sections[i].getNumStudents();
		}
		return n;
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
	
	public void setTimetablePos(int block, int index) {
		lastIndex++;
		if(lastIndex < numSections){
			sections[lastIndex].setBlock(block);
			sections[lastIndex].setIndex(index);
		}	
	}
}