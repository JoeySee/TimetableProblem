import java.util.ArrayList;

public class CourseSection {
	private Course course;
	private int block; // Position within timetable (0-7)
	private int index; // Position within block (0-?)
	private int secNum;
	private ArrayList<Student> students;
	
	public CourseSection(Course c, int n) {
		course = c;
		block = -1;
		index = -1;
		secNum = n;
		/*block = new int [sections];
		index = new int [sections];*/
		students = (ArrayList<Student>)new ArrayList();
	}
	
	public ArrayList<Student> getStudents(){
		return students;
	}
	
	public void addStudent(Student newStudent) {
		if (students.size() < course.getCapacity()) {
			if(newStudent.getTimeTable().getSchedule(block).size() > 0) {
				return;
			}
			students.add(newStudent);
			newStudent.getTimeTable().addSection(block, this);
		}
	}
	
	public void addStudentIgnoreBlocking(Student newStudent) {
		if (students.size() < course.getCapacity()) {
			students.add(newStudent);
		}
	}
	
	public void clearSection() {
		students = new ArrayList<Student>();
	}
	
	public Course getCourse() {
		return course;
	}
	
	public int getSecNum() {
		return secNum;
	}
	
	public int getBlock() {
		return block;
	}
	
	public int getIndex() {
		return index;
	}

	public int getNumStudents() {
		return students.size();
	}
	
	public void setBlock(int b) {
		block = b;
	}
	
	public void setIndex(int i) {
		index = i;
	}
}
