import java.util.ArrayList;

public class Student {
	private ArrayList<Course> requestedCourses = new ArrayList<Course>();
	private ArrayList<Course> alternateCourses = new ArrayList<Course>();
	private ArrayList<Course> actualCourses = new ArrayList<Course>();
	private int id;
	
	public Student(String id) {
		this.id = Integer.parseInt(id);
	}
	
	public int getID() {
		return id;
	}
	
	public void addToCourses() {
		for(Course c : requestedCourses) {
			System.out.println(c.getName());
		}
		System.out.println("-------------------------------------------------");
		for(int i = 0; i < requestedCourses.size(); i++) {
			if(requestedCourses.get(i).getCapacity() > 0 && requestedCourses.get(i) != null) {
				requestedCourses.get(i).addStudent(this);
				actualCourses.add(requestedCourses.get(i));
			}
		}
		
		for(int i = 0; i < Math.min(requestedCourses.size() - actualCourses.size(), alternateCourses.size()); i++) {
			if(alternateCourses.get(i).getCapacity() > 0) {
				alternateCourses.get(i).addStudent(this);
				actualCourses.add(requestedCourses.get(i));
			}
		}
		
		
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
