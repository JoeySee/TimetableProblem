import java.util.ArrayList;

public class Timetable {
	private ArrayList<CourseSection> [] schedule = (ArrayList<CourseSection> [])new ArrayList [8];
	
	public Timetable() {
		for(int i = 0; i < schedule.length; i++) {
			schedule[i] = new ArrayList<CourseSection>();
		}
	}
	public ArrayList<CourseSection> [] getTimetable() {
		return schedule;
	}
	public ArrayList<CourseSection> getBlockSections(int block){
		return schedule[block];
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
//		for (int i = 0; i < schedule.length; i++) {
//			s += "Block " + i + ":\n";
//			for (int j = 0; j < schedule[i].size(); j++) {
//				aSection = schedule[i].get(j);
//				s += aSection.getCourse().getCode() + " | " + aSection.getCourse().getName() + "\n";
//				s += "section " + aSection.getSecNum() + " (of " + aSection.getCourse().getNumSections() + ") :\n";
//				for (int k = 0; k < aSection.getStudents().size(); k++) {
//					aStudent = aSection.getStudents().get(k);
//					s += aStudent.getID() + "\n";
//				}
//			}
//		}
		
		// Course Code Columns
		int maxSize = Math.max(Math.max(Math.max(schedule[0].size(), schedule[1].size()), Math.max(schedule[2].size(), schedule[3].size())), Math.max(Math.max(schedule[4].size(), schedule[5].size()), Math.max(schedule[6].size(), schedule[7].size())));
		s += "   S1  Ba   *   S1  bB   *   S1  bC   *   S1  bD   *   S2  Ba   *   S2  bB   *   S2  bC   *   S2  bD   \n";
		for(int i = 0; i < maxSize; i++) {
			for(int j = 0; j < 8; j++) {
				if(j != 0) s += "*";
				if(i < schedule[j].size() && schedule[j].get(i) != null) {
					s += " " + schedule[j].get(i).getCourse().getCode() + " ";
				} else {
					s += "            ";
				}
			}
			s += "\n";
		}	
		return s;
	}
}