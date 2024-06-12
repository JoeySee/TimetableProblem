import java.util.ArrayList;

public class Student {
	private ArrayList<Course> requestedCourses = new ArrayList<Course>();
	private ArrayList<Course> alternateCourses = new ArrayList<Course>();
	//private ArrayList<CourseSection> actualCourseSections = new ArrayList<CourseSection>();
	Timetable t;
	private int id;
	
	public Student(String id) {
		this.id = Integer.parseInt(id);
		clearTimetable();
	}
	
	public int getID() {
		return id;
	}
	
	public void addToCourses() {
//		System.out.println(requestedCourses.size());

		for(int i = 0; i < requestedCourses.size(); i++) {
			if(requestedCourses.get(i) != null) {
				/*if (requestedCourses.get(i).getCode().equals("ACAL-12---")) {
					System.out.println("one AP calc");
				}*/
				requestedCourses.get(i).addStudent(this);
				
			}
		}
		
		//System.out.println(Math.min(requestedCourses.size() - actualCourseSections.size(), alternateCourses.size()));
		for(int i = 0; i < Math.min(requestedCourses.size() - t.getAllCourseSections().size(), alternateCourses.size()); i++) {
			alternateCourses.get(i).addStudent(this);
		}
	}
	
	/*public void addToCoursesIgnoreBlocking() {
//		System.out.println(requestedCourses.size());

		for(int i = 0; i < requestedCourses.size(); i++) {
			if(requestedCourses.get(i) != null) {

				requestedCourses.get(i).addStudentIgnoreBlocking(this);
				
			}
		}
		
		//System.out.println(Math.min(requestedCourses.size() - actualCourseSections.size(), alternateCourses.size()));
		for(int i = 0; i < Math.min(requestedCourses.size() - t.getAllCourseSections().size(), alternateCourses.size()); i++) {
			alternateCourses.get(i).addStudentIgnoreBlocking(this);
		}
	}*/
	
	public ArrayList<Course> getRequestedCourses() {
		return requestedCourses;
	}
	
	public ArrayList<Course> getAlternateCourses() {
		return alternateCourses;
	}
	
	public Timetable getTimeTable() {
		return t;
	}
	
	public void addRequestedCourse(Course newCourse) {
		requestedCourses.add(newCourse);
	}
	
	public void addAlternateCourse(Course newCourse) {
		alternateCourses.add(newCourse);
	}
	
	public void clearTimetable() {
		t = new Timetable();
	}
	/*public void addActualCourse(CourseSection newCourseSec) {
		t.addSection(newCourseSec.getBlock(), newCourseSec);
	}*/
}
