import java.util.ArrayList;

public class Course {
	private String name;
	private String code;
	private int capacity;
	private int numSections;
	private CourseSection [] sections;
	private ArrayList<Course> courBefore; // these courses must appear before the current course
	private ArrayList<Course> courAfter; // these courses must appear after the current course
	private ArrayList<Course> simultaneousCourses; // Courses that can occur as a split with this one
	private ArrayList<Course> notSimultaneousCourses; // Courses that can occur linearly with this one
	private ArrayList<Student> requestedStudents; // Students who have requested this course
	private boolean isCourseLinear;
	private double[] preferenceStudent = new double[8]; // Preference for block to appear in certain positions
	private double[] preferenceSequencing = new double[8]; // Preference for block to appear in certain positions
	private double[] preferenceLinear = new double[8]; // Preference for block to appear in certain positions
	private double totalPrefsStudent = 0;
	private double totalPrefsSequencing = 0;
	private double totalPrefsLinear = 0;
	
	public Course(String n, String c, String cap, String s, boolean isLinear) {
		name = n;
		code = c;
		numSections = Integer.parseInt(s);
		capacity =  Integer.parseInt(cap);
		isCourseLinear = isLinear;
		courBefore = new ArrayList <Course>(); // these courses must appear before the current course
		courAfter = new ArrayList <Course>(); // these courses must appear after the current course
		simultaneousCourses = new ArrayList<Course>();
		notSimultaneousCourses = new ArrayList<Course>();
		requestedStudents = new ArrayList<Student>();
		isCourseLinear = false;
		sections = new CourseSection [numSections];
		resetSections();
		resetPreferences();
	}
	
	public CourseSection getSection(int i){
		return sections[i];
	}
	
	/*public ArrayList<Student> getStudents(int sec){
		return sections[sec].getStudents();
	}*/
	
	public void addCourBefore(Course c) {
		courBefore.add(c);
	}
	
	public void addCourAfter(Course c) {
		courAfter.add(c);
	}
	
	public ArrayList<Course> getCourBefore() {
		return courBefore;
	}
	
	public ArrayList<Course> getCourAfter() {
		return courAfter;
	}
	
	public void addStudent(Student newStudent) {
		for (int i = 0; i < sections.length; i++) {
			if (sections[i] != null && sections[i].getStudents().size() < capacity) {
				sections[i].addStudent(newStudent);
				//System.out.println("added student in session " + i);
				break;
			} else {
				//System.out.println("full");
			}
			/*if (code == "ACAL-12---" && i == sections.length - 1) {
				System.out.println("unable to find section");
			}*/
		}
		
	}
	
	public void addRequestedStudents() {
		for(Student s : requestedStudents) {
			addStudent(s);
		}
	}
	
	public void addRequestStudent(Student s) {
		requestedStudents.add(s);
	}
	
	public ArrayList<Course> getSimultaneousCourses(){
		return simultaneousCourses;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getNumSections() {
		return numSections;
	}
	
	public int getNumStudents() {
		int n = 0;
		for (int i = 0; i < numSections; i++) {
			if (sections[i] != null) {
				n += sections[i].getNumStudents();
			}
		}
		return n;
	}
	
	public ArrayList<Student> getRequestedStudents() {
		return requestedStudents;
	}
	
	public void addSimultaneousCourseReciprocal(Course c) {
		simultaneousCourses.add(c);
		c.addSimultaneousCourse(this);
	}
	
	public void addSimultaneousCourse(Course c) {
		simultaneousCourses.add(c);
	}
	
	public void addNotSimultaneousCourseReciprocal(Course c) {
		notSimultaneousCourses.add(c);
		c.addNotSimultaneousCourse(this);
	}
	
	public void addNotSimultaneousCourse(Course c) {
		notSimultaneousCourses.add(c);
	}
	
	public boolean isCourseNotSimultaneous(Course c) {
		boolean isFound = false;
		for(Course d : notSimultaneousCourses) {
			if(d.equals(c)) isFound = true;
		}
		return isFound;
	}
	
	public boolean isCourseSimultaneous(Course c) {
		boolean isFound = false;
		for(Course d : simultaneousCourses) {
			if(d.equals(c)) {
				isFound = true;
			}
		}
		return isFound;
	}
	
	public boolean isCourseLinear() {
		return isCourseLinear;
	}
	
	public void removeSection(int i) {
		sections[i] = null;
	}
	
	public void addCourseToBlock(Timetable t, int slot) {
		for(int i = 0; i < sections.length; i++) {
			if(sections[i].getBlock() == -1) {
				sections[i].setIndex(t.getSchedule(slot).size());
				sections[i].setBlock(slot);
				t.addSection(slot, sections[i]);
				return;
			}
		}
	}
	
	// Add percent of sections to slot in t
	/*public void addPercentSections(Timetable t, int slot) {
		double sectionsToRun = ((preferenceStudent[slot]/totalPrefsStudent)*numSections);
		for(int i = 0; i < Math.min(Math.ceil(sectionsToRun), sections.length); i++) {
			if(sections[i].getBlock() == -1) {
				sections[i].setIndex(t.getSchedule(slot).size());
				sections[i].setBlock(slot);
				t.addSection(slot, sections[i]);
			} else {
				sectionsToRun++;
			}
		}
	}*/
	
	// Assign any unassigned sections to random positions
	public void addExcessSections(Timetable t) {
		for(int i = 0; i < sections.length; i++) {
			if(sections[i].getBlock() == -1) {
				int slot = (int)(Math.random()*(7-2)+2);
				//System.out.println(slot);
				sections[i].setIndex(t.getSchedule(slot).size());
				sections[i].setBlock(slot);
				t.addSection(slot, sections[i]);
			}
		}
	}
	
	public double getTotalPrefStudent() {
		return totalPrefsStudent;
	}
	
	public double getTotalPrefSequencing() {
		return totalPrefsSequencing;
	}
	
	public double getTotalPrefLinear() {
		return totalPrefsLinear;
	}
	
	// Add preference to slots
	public void addPreferencesStudent(ArrayList<Integer> slots) {
		for(int i : slots) {
			preferenceStudent[i] += 1.0/*/(double)slots.size()*/;
			totalPrefsStudent += 1.0/*/(double)slots.size()*/;
		}
	}
	
	public void addPreferencesSequencing(ArrayList<Integer> slots) {
		for(int i : slots) {
			preferenceSequencing[i] += 1.0/*/(double)slots.size()*/;
			totalPrefsSequencing += 1.0/*/(double)slots.size()*/;
		}
	}
	
	public void addPreferencesLinear(ArrayList<Integer> slots) {
		for(int i : slots) {
			preferenceLinear[i] += 1.0/*/(double)slots.size()*/;
			totalPrefsLinear += 1.0/*/(double)slots.size()*/;
		}
	}
	
	public double[] getPreferencesStudent() {
		return preferenceStudent;
	}
	
	public double[] getPreferencesSequencing() {
		return preferenceSequencing;
	}
	
	public double[] getPreferencesLinear() {
		return preferenceLinear;
	}
	
	public void resetPreferences() {
		for(int i = 0; i < preferenceStudent.length; i++) {
			preferenceStudent[i] = 0;
			preferenceSequencing[i] = 0;
			preferenceLinear[i] = 0;
		}
	}

	public ArrayList<Student> getStudentsInSection(int k) {
		return sections[k].getStudents();
	}

	public void resetSections() {
		for (int i = 0; i < sections.length; i++) {
			sections[i] = new CourseSection (this, i);
		}
	}
}