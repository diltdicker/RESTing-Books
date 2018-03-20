package edu.asupoly.ser422.restexample.model;

public class Subject {
	private int id;
	private String subject;
	private String location;
	
	public Subject() {
		
	}
	
	public Subject (int id, String s, String l) {
		this.id = id; 
		subject = s;
		location = l;
	}
	public int getSubjectId() {
		return id;
	}
	public String getSubject() {
		return subject;
	}
	public String getLocation() {
		return location;
	}
	public void setSubjectId(int id) {
		this.id = id;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String toString() {
		return "ID " + this.id + " subject " + this.subject + " location " + this.location;
	}
}
