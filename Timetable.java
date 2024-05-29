import java.util.ArrayList;

public class Timetable {
	private ArrayList<CourseSection> [] schedule = (ArrayList<CourseSection> [])new ArrayList [8];
	
	public Timetable() {
		for(int i = 0; i < schedule.length; i++) {
			schedule[i] = new ArrayList<CourseSection>();
		}
	}
	
	public void addSection(int slot, CourseSection sec) {
		schedule[slot].add(sec);
	}
	
	public void deleteSection(int slot, Course c) {
		ArrayList<CourseSection> courses = schedule[slot];
		
		for(int i = courses.size(); i >= 0; i++) {
			if(courses.get(i).equals(c)) {
				schedule[slot].remove(i);
				return;
			}
		}
	}
	
	public void deleteAllSections(int slot, Course c) {
		ArrayList<CourseSection> courses = schedule[slot];
		
		for(int i = courses.size(); i >= 0; i++) {
			if(courses.get(i).equals(c)) {
				schedule[slot].remove(i);
			}
		}
	}
	
	public void addStudents(int slot, Course c) {
		
	}
	
	public String toString() {
		String s = "";
		CourseSection aSection;
		Student aStudent;
		for (int i = 0; i < schedule.length; i++) {
			s += "Block " + i + ":\n";
			for (int j = 0; j < schedule[i].size(); j++) {
				aSection = schedule[i].get(j);
				s += aSection.getCourse().getCode() + " | " + aSection.getCourse().getName() + "\n";
				s += "section " + aSection.getSecNum() + " (of " + aSection.getCourse().getNumSections() + ") :\n";
				for (int k = 0; k < aSection.getStudents().size(); k++) {
					aStudent = aSection.getStudents().get(k);
					s += aStudent.getID() + "\n";
				}
			}
		}
		return s;
	}
}