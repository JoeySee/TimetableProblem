import java.util.ArrayList;

public class Timetable {
	private ArrayList<Course> [] schedule = (ArrayList<Course> [])new ArrayList [8];
	
	public Timetable() {
		for(int i = 0; i < schedule.length; i++) {
			schedule[i] = new ArrayList<Course>();
		}
	}
	
	public void addCourse(int slot, Course c) {
		schedule[slot].add(c);
	}
	
	public void deleteCourse(int slot, Course c) {
		ArrayList<Course> courses = schedule[slot];
		
		for(int i = courses.size(); i >= 0; i++) {
			if(courses.get(i).equals(c)) {
				schedule[slot].remove(i);
				return;
			}
		}
	}
	
	public void deleteAllCourses(int slot, Course c) {
		ArrayList<Course> courses = schedule[slot];
		
		for(int i = courses.size(); i >= 0; i++) {
			if(courses.get(i).equals(c)) {
				schedule[slot].remove(i);
			}
		}
	}
	
	public void addStudents(int slot, Course c) {
		
	}
	
	public String toString() {
		String table = "S1 A      S1 B     S1 C     S1 D     S2 A      S2 B     S2 C   S2 D";
		
		
		
		for(int i = 0; i < schedule.length; i++) {
			for(int j = 0; j < schedule[i].size(); j++) {
				System.out.print(schedule[i].get(j).getName() + " ,");
			}
			System.out.println();
		}
		
		return "TO DO";
	}
}
