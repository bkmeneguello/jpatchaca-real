package com.meneguello.jira;

public class Interval {

	private final int weeks;
	
	private final int days;
	
	private final int hours;
	
	private final int minutes;

	public Interval(int weeks, int days, int hours, int minutes) {
		this.weeks = weeks;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
	}

	public int getWeeks() {
		return weeks;
	}

	public int getDays() {
		return days;
	}

	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}
	
}
