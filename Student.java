import java.util.ArrayList;

public class Student {
	private ArrayList<Course> requestedCourses = new ArrayList<Course>();
	private ArrayList<Course> alternateCourses = new ArrayList<Course>();
	private ArrayList<CourseSection> actualCourseSections = new ArrayList<CourseSection>();
	private int id;
	
	public Student(String id) {
		this.id = Integer.parseInt(id);
	}
	
	public int getID() {
		return id;
	}
	
	public void addToCourses() {
		for(Course c : requestedCourses) {
			//System.out.println(c.getName());
		}
		//System.out.println("-------------------------------------------------");
		for(int i = 0; i < requestedCourses.size(); i++) {
			if(requestedCourses.get(i) != null) {
				requestedCourses.get(i).addStudent(this);
				//actualCourseSections.add(requestedCourses.get(i));
			}
		}
		//System.out.println(Math.min(requestedCourses.size() - actualCourseSections.size(), alternateCourses.size()));
		for(int i = 0; i < Math.min(requestedCourses.size() - actualCourseSections.size(), alternateCourses.size()); i++) {
			alternateCourses.get(i).addStudent(this);
		}
		
		
	}
	
	public ArrayList<Course> getRequestedCourses() {
		return requestedCourses;
	}
	
	public ArrayList<Course> getAlternateCourses() {
		return alternateCourses;
	}
	
	public ArrayList<CourseSection> getActualCourseSections() {
		return actualCourseSections;
	}
	
	public void addRequestedCourse(Course newCourse) {
		requestedCourses.add(newCourse);
	}
	
	public void addAlternateCourse(Course newCourse) {
		alternateCourses.add(newCourse);
	}
	
	public void addActualCourse(CourseSection newCourseSec) {
		actualCourseSections.add(newCourseSec);
	}
}