import java.util.ArrayList;

public class Timetable {
	private ArrayList<ArrayList<Course>> schedule = new ArrayList<ArrayList<Course>>();
	
	public Timetable() {
		for(int i = 0; i < 8; i++) {
			schedule.add(new ArrayList<Course>());
		}
	}
	
	public void addCourse(int slot, Course c) {
		
		schedule.get(slot).add(c);
	}
	
	public void deleteCourse(int slot, Course c) {
		ArrayList<Course> courses = schedule.get(slot);
		
		for(int i = courses.size(); i >= 0; i++) {
			if(courses.get(i).equals(c)) {
				schedule.get(slot).remove(i);
				return;
			}
		}
	}
	
	public void deleteAllCourses(int slot, Course c) {
		ArrayList<Course> courses = schedule.get(slot);
		
		for(int i = courses.size(); i >= 0; i++) {
			if(courses.get(i).equals(c)) {
				schedule.get(slot).remove(i);
			}
		}
	}
	
	public void addStudents(int slot, Course c) {
	}
	
	public String toString() {
		return "TO DO";
	}
}
