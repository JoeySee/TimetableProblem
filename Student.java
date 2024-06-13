import java.util.ArrayList;

public class Student {
	private ArrayList<Course> requestedCourses = new ArrayList<Course>();
	private ArrayList<Course> alternateCourses = new ArrayList<Course>();
	//private ArrayList<CourseSection> actualCourseSections = new ArrayList<CourseSection>();
	Timetable t = new Timetable();
	private int id;
	
	public Student(String id) {
		this.id = Integer.parseInt(id);
	}
	
	public int getID() {
		return id;
	} 
	
	public void removeCourse(int block) {
		t.clearBlock(block);
	}
	
	public void addToCourses() {
//		System.out.println(requestedCourses.size());
		
		for(int i = 0; i < requestedCourses.size(); i++) {
			if(requestedCourses.get(i) != null) {
				requestedCourses.get(i).addStudent(this);
			}
		}
		
		//System.out.println(Math.min(requestedCourses.size() - actualCourseSections.size(), alternateCourses.size()));
		for(int i = 0; i < Math.min(requestedCourses.size() - t.getAllCourseSections().size(), alternateCourses.size()); i++) {
			alternateCourses.get(i).addStudent(this);
		}
		
		
	}
	
	public void addToReqCourses() {
//		System.out.println(requestedCourses.size());
		
		for(int i = 0; i < requestedCourses.size(); i++) {
			if(requestedCourses.get(i) != null) {
				requestedCourses.get(i).addStudent(this);
			}
		}
	}
	
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
	
	public void addActualCourse(CourseSection newCourseSec) {
		t.addSection(newCourseSec.getBlock(), newCourseSec);
	}
	
	public ArrayList<Integer> getEmptySlots(){
		ArrayList<Integer> emptySlots = new ArrayList<Integer>();
		for(int slot = 0; slot < 8; slot++) {
			if(t.getTimetable()[slot].size() == 0) emptySlots.add(slot);
		}
		return emptySlots;
	}
}