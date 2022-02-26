package main;

import java.util.Scanner;

public class mainClass {

	public static void main(String[] args) {
		new mainClass();
	}

	private String[] allTonesSharp = { "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#" };
	private String[] allTonesFlat = { "A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab" };

	private int[] majorScaleIntervals = { 2, 2, 1, 2, 2, 2, 1 };
	private int[] minorScaleIntervals = { 2, 1, 2, 2, 1, 2, 2 };
	private int[] melodicMinorScaleIntervals = { 2, 1, 2, 2, 2, 2, 1 };

	private String[] courseTuning;

	private int[] scaleTypeSelected = {};
	private String scaleKey;

	public mainClass() {

		// get all the input needed from the user
		getUserInput();

		// generate the scale array
		String[] ScaleArray = generateScaleArray(scaleKey);

		String[][] coursePattern = generateScaleArrayFromCourseStart(courseTuning, ScaleArray);

		displayResults(coursePattern);

	}

	private void displayResults(String[][] coursePattern) {
		for (int i = 0; i < courseTuning.length; i++) {
			System.out.print(coursePattern[i][0]);
			System.out.print("-||");
			for (int j = 1; j < 12; j++) {
				String symbol = coursePattern[i][j];
				System.out.print("-");
				System.out.print(symbol);
				if (!symbol.contains("#") && !symbol.contains("b")) {
					System.out.print("-");
				}
				System.out.print("-");
				System.out.print("|");
			}
			System.out.println();
		}
	}

	private void getUserInput() {
		Scanner input = new Scanner(System.in); // Create a Scanner object

		// input number of courses
		System.out.println("Number of Courses:");
		String noOfCoursesInputted = input.nextLine();// Read user input
		int numberOfCourses = Integer.parseInt(noOfCoursesInputted);
		courseTuning = new String[numberOfCourses];

		// input the key for the scale
		String scaleKey = "";
		while (scaleKey.equals("")) {
			System.out.println("enter scale key: ");
			scaleKey = getStringWithin(input.nextLine(), allTonesSharp);
		}
		this.scaleKey = scaleKey;

		// input the type of scale
		String scaleType = "";
		while (scaleType.equals("")) {
			System.out.println("enter scale type (M/m/mm): ");
			String[] acceptedInputs = { "M", "m", "mm" };
			scaleType = getStringWithin(input.nextLine(), acceptedInputs);
			if (scaleType.equals("M")) {
				scaleTypeSelected = majorScaleIntervals;
			} else if (scaleType.equals("m")) {
				scaleTypeSelected = minorScaleIntervals;
			} else if (scaleType.equals("mm")) {
				scaleTypeSelected = melodicMinorScaleIntervals;
			}
		}

		int i = 0;
		while (i < numberOfCourses) {
			System.out.println("tuning for course " + (i + 1) + ": ");
			String tuning = input.nextLine();
			boolean isCourseAssigned = false;
			for (String s : allTonesSharp) {
				if (tuning.equals(s)) {
					isCourseAssigned = true;
					courseTuning[i] = s;
					i++;
				}
			}
			if (!isCourseAssigned) {
				System.out.println("Error! Course incorrectly assigned!");
				continue;
			}

		}

	}

	private String getStringWithin(String searchTerm, String[] searchSet) {
		return getStringWithin(searchTerm, searchSet, "");

	}
	
	private String getStringWithin(String searchTerm, String[] searchSet, String defaultStr) {
		for (String s : searchSet) {
			if (searchTerm.equals(s)) {
				return searchTerm;
			}
		}
		return defaultStr;

	}

	// why oh why do object arrays not have an index of function in Java!!!
	// If only I had known beforehand
	private int indexOf(String searchTerm, String[] searchSet) {
		for (int i = 0; i < searchSet.length; i++) {
			String s = searchSet[i];
			if (searchTerm.equals(s)) {
				return i;
			}
		}
		return -1;

	}

	private String[] generateScaleArray(String key) {
		// find the start of the scale
		int scaleCounter = indexOf(key, allTonesSharp);

		String[] scaleTones = new String[7];

		for (int i = 0; i < scaleTones.length; i++) {
			scaleTones[i] = allTonesSharp[scaleCounter % 12];
			int semitonesToJump = this.scaleTypeSelected[i];
			scaleCounter += semitonesToJump;
		}
		return scaleTones;
	}

	private String[][] generateScaleArrayFromCourseStart(String[] courseTuning, String[] Scale) {
		String[][] fretPattern = new String[courseTuning.length][12];

		for (int course = 0; course<courseTuning.length;course++) {
			String courseTone = courseTuning[course];
			int toneCounter = indexOf(courseTone,allTonesSharp);
			
			fretPattern = assignFretPattern(fretPattern,course, toneCounter, courseTuning,Scale);
		
		}
		return fretPattern;
	}
	
	private String[][] assignFretPattern(String[][] fretPattern, int course, int toneCounter, String[] courseTuning, String[] scale) {
		for (int fretCounter = 0;fretCounter<12;fretCounter++) {
			String nextToneOnFretBoard = allTonesSharp[toneCounter%12];
			boolean isFretPatternAssigned = false;
			fretPattern[course][fretCounter] = getStringWithin(nextToneOnFretBoard, scale, "-");
			toneCounter++;
		}
		return fretPattern;
	}		
	
	
}
