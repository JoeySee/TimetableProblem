
import java.util.ArrayList;

public class Student {
	private ArrayList<Course> requestedCourses = new ArrayList<Course>();
	private ArrayList<Course> alternateCourses = new ArrayList<Course>();
	private ArrayList<Course> actualCourses = new ArrayList<Course>();
	Timetable t = new Timetable();
	private int block = 0;
	private int id;
	
	
	public Student(String id) {
		this.id = Integer.parseInt(id);
	}
	
	public int getID() {
		return id;
	}
	
	public void addToCourses(Timetable courses) {
		for(Course c : requestedCourses) {
			//System.out.println(c.getName());
		}
		//System.out.println("-------------------------------------------------");
		for(int i = 0; i < requestedCourses.size(); i++) {
			block = 0;
			while(block < 8) {
				if(requestedCourses.get(i).getCapacity() > 0 && requestedCourses.get(i) != null  && !isCourseInBlock()){
					if(!hasCourse(requestedCourses.get(i))) {
						t.addSection(block, getCourseSection(courses, requestedCourses.get(i)));
					}
				}
				block++;
			}
		}
		for(int i = 0; i < Math.min(requestedCourses.size() - actualCourses.size(), alternateCourses.size()); i++) {
				while(block < 8) {
				if(alternateCourses.get(i).getCapacity() > 0 && getCourseSection(courses, alternateCourses.get(i)) != null   && !isCourseInBlock()) {
					
					if(!hasCourse(alternateCourses.get(i))) {
						t.addSection(block, getCourseSection(courses, alternateCourses.get(i)));
					}
					
					
					
					
				}
				block++;
			}
		}
		
	}
	public boolean hasCourse(Course c) {
		
		for(ArrayList<CourseSection> s :t.getTimetable()) {
			for(CourseSection n : s) {
				if(n == null) {
					continue;
				}
				if(n.getCourse().equals(c)) {
					return true;
				}
			}
		}
		
		return false;		
	}
	
	
	public CourseSection getCourseSection(Timetable courses, Course c){
		
		ArrayList<CourseSection> g = courses.getTimetable()[block];
				
		for(int i = 0; i < g.size(); i++) {
			if(g.get(i).getCourse().equals(c)) {
				return  g.get(i);
			}
		}
		
		/*
		for(ArrayList<CourseSection> s :courses.getTimetable() ) {
			for(CourseSection n : s) {
				if(n.getCourse().equals(c)) {
					return n;
				}
			}
		}*/
		return null;
	}
	public boolean isCourseInBlock() {
		
		
		if(t.getTimetable()[block].size() == 0) {
			return false;
		}
		
		return true;
	}
	
	public String printTimetable() {
		return t.toString();
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
