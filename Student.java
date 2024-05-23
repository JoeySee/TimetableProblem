import java.util.ArrayList;

public class Student {
	private ArrayList<Course> requestedCourses = new ArrayList<Course>();
	private ArrayList<Course> alternateCourses = new ArrayList<Course>();
	private ArrayList<Course> actualCourses = new ArrayList<Course>();
	private int id;
	
	public Student(String id) {
		this.id = Integer.parseInt(id);
	}
	
	public ArrayList<Course> getRequestedCourses() {
		return requestedCourses;
	}
	
	public ArrayList<Course> getAlternateCourses() {
		return alternateCourses;
	}
	
	public ArrayList<Course> getActualCourses() {
		return actualCourses;
	}
	
	public void addRequestedCourse(Course newCourse) {
		requestedCourses.add(newCourse);
	}
	
	public void addAlternateCourse(Course newCourse) {
		alternateCourses.add(newCourse);
	}
	
	public void addActualCourse(Course newCourse) {
		actualCourses.add(newCourse);
	}
}
