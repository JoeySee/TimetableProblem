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
		
		for(int i = courses.size()-1; i >= 0; i--) {
			if(courses.get(i).getCourse().equals(c)) {
				schedule[slot].remove(i);
				return;
			}
		}
	}
	
	public void deleteSection(int slot, CourseSection c) {
		ArrayList<CourseSection> courses = schedule[slot];
		
		for(int i = courses.size()-1; i >= 0; i--) {
			if(courses.get(i).equals(c)) {
				schedule[slot].remove(i);
				return;
			}
		}
	}
	
	public int deleteAllSections(Course c) {
		int deleted = 0;
		for(int i = 0; i < c.getNumSections(); i++) {
			for(int j = 0 ; j < 8; j++) {
				if(schedule[j].contains(c.getSection(i))) {
					deleteSection(j,c);
					deleted++;
					System.out.println(deleted);
				}
			}
		}
		return deleted;
	}
	
	public void clearBlock(int block) {
		schedule[block] = new ArrayList<CourseSection>();
	}
	
	public ArrayList<CourseSection>[] getTimetable() {
		return schedule;
	}
	
	public ArrayList<CourseSection> getSchedule(int i) {
		return schedule[i];
	}
	
	public ArrayList<CourseSection> getAllCourseSections() {
		ArrayList<CourseSection> a = new ArrayList<CourseSection>();
		for (int i = 0; i < 8; i++) {
			for (CourseSection cs: schedule[i]) {
				a.add(cs);
			}
		}
		return a;
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
		s += "      S1 A     *      S1 B     *     S1 C      *     S1 D      *     S2 A      *    S2  B      *     S2  C     *     S2 D      \n";
		String s2 = null;
		
		for(int i = 0; i < maxSize; i++) {
			for(int j = 0; j < 8; j++) {
				if(j != 0) s += "*";
				if(i < schedule[j].size()) {
					s2 = " " + schedule[j].get(i).getCourse().getCode() + " " + schedule[j].get(i).getNumStudents() + " ";
					s += s2;
					// add spaces. assume that columns will never be wider than 15 characters
					for (int k = 0; k < 15 - s2.length(); k++) {
						s += " ";
					}
				} else {
					s += "               ";
				}
			}
			s += "\n";
		}	
		return s;
	}
	
	public Timetable clone() {
		Timetable newTable = new Timetable();
		for(int i = 0 ; i < 8; i++) {
			for(CourseSection c : schedule[i]) {
				newTable.addSection(i, c);
			}
		}
		return newTable;
	}
}