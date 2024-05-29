import java.util.ArrayList;

public class Course {
	private String name;
	private String code;
	private int capacity;
	private int lastIndex;
	private int numSections;
	private CourseSection [] sections;
	
	public Course(String name, String c, String cap, String s) {
		this.name = name;
		this.code = c;
		lastIndex = -1;
		numSections = Integer.parseInt(s);
		capacity =  Integer.parseInt(cap);
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
	
	public void addStudent(Student newStudent) {
		for (int i = 0; i < sections.length; i++) {
			if (sections[i].getStudents().size() < capacity) {
				sections[i].addStudent(newStudent);
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
	
	public void setTimetablePos(int block, int index) {
		lastIndex++;
		if(lastIndex < numSections){
			sections[lastIndex].setBlock(block);
			sections[lastIndex].setIndex(index);
		}	
	}
}
