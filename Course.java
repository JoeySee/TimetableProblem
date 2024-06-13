
import java.util.ArrayList;

public class Course {
	private String name;
	private String code;
	private int capacity;
	private int numSections;
	private CourseSection [] sections;
	private ArrayList<Course> courBefore = new ArrayList <Course>(); // these courses must appear before the current course
	private ArrayList<Course> courAfter = new ArrayList <Course>(); // these courses must appear after the current course
	private ArrayList<Course> simultaneousCourses; // Courses that can occur as a split with this one
	private ArrayList<Course> notSimultaneousCourses; // Courses that can occur linearly with this one
	private ArrayList<Student> requestedStudents = new ArrayList<Student>(); // Students who have requested this course
	private int s1Requests; // Requests for course to be in s1, based on seq rules
	private int s2Requests; // Requests for course to be in s2, based on seq rules
	private int totalRequests; // Total requests for this course by students with a placement preference
	private double[] placementPreference = new double[8]; // Preference for block to appear in certain positions
	private double totalPrefs = 0;
	
	public Course(String name, String c, String cap, String s) {
		this.name = name;
		this.code = c;
		numSections = Integer.parseInt(s);
		capacity =  Integer.parseInt(cap);
		simultaneousCourses = new ArrayList<Course>();
		notSimultaneousCourses = new ArrayList<Course>();
		sections = new CourseSection [numSections];
		for (int i = 0; i < sections.length; i++) {
			sections[i] = new CourseSection (this, i);
		}
		for(int i = 0; i < placementPreference.length; i++) {
			placementPreference[i] = 0;
		}
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
			n += sections[i].getNumStudents();
		}
		return n;
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
	public void addPercentSections(Timetable t, int slot) {
		double sectionsToRun = ((placementPreference[slot]/totalPrefs)*numSections);
		for(int i = 0; i < Math.min(Math.ceil(sectionsToRun), sections.length); i++) {
			if(sections[i].getBlock() == -1) {
				sections[i].setIndex(t.getSchedule(slot).size());
				sections[i].setBlock(slot);
				t.addSection(slot, sections[i]);
			} else {
				sectionsToRun++;
			}
		}
	}
	
	// Assign any unassigned sections to random positions
	public void addExcessSections(Timetable t) {
		for(int i = 0; i < sections.length; i++) {
			if(sections[i].getBlock() == -1) {
				int slot = (int)(Math.random()*(7-2)+2);
				System.out.println(slot);
				sections[i].setIndex(t.getSchedule(slot).size());
				sections[i].setBlock(slot);
				t.addSection(slot, sections[i]);
			}
		}
	}
	
	public double getTotalPref() {
		return totalPrefs;
	}
	
	// Add preference to slots
	public void addPreferences(ArrayList<Integer> slots) {
		for(int i : slots) {
			placementPreference[i] += 1.0/(double)slots.size();
			totalPrefs += 1.0/(double)slots.size();
		}
	}
	
	public double[] getPreferences() {
		return placementPreference;
	}
	
	public void resetPreference() {
		for(int i = 0; i < placementPreference.length; i++) {
			placementPreference[i] = 0;
		}
	}
	
	public void addS1Request() {
		s1Requests++;
		totalRequests++;
	}
	
	public void addS2Request() {
		s2Requests++;
		totalRequests++;
	}
	
	public int getS1() {
		return s1Requests;
	}
	
	public int getS2() {
		return s2Requests;
	}
	
	// Return percent of students who have an preference for course to appear in s1
	public double getS2Percent() {
		//System.out.println(totalRequests);
		if(totalRequests == 0) return -1;
		return (double)s2Requests/totalRequests;
	}

	public ArrayList<Student> getStudentsInSection(int k) {
		return sections[k].getStudents();
	}
}
