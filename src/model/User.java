package model;

import dao.UserDAO;
import dao.WallPostDAO;

import dao.NotificationDAO;

import java.util.Collection;

import dao.DatabaseManager;

/**
 * Model object for a row in the Course table.
 * Accesses the underlying database through a CourseDAO.
 * Based on Sciore, Section 9.1.
 * 
 */
public class User {
	private UserDAO dao;
	private String firstName;
	private String lastName;
	private int userID;
	private Collection<Notification> notification;
	private Collection<WallPost> wallPost;

	public User(UserDAO userDAO, String firstName, String lastName, int userID) {
		this.dao = userDAO;
		this.firstName =firstName; 
		this.lastName = lastName;
		this.userID = userID;
	}
	
	public String toString() {
		return "UserID:" + userID + " " + "First Name: " + firstName + " Last Name: " + lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		dao.changeFirstName(userID, firstName);
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		dao.changeLastName(userID, lastName);
	}

	public int getUserID() {
		return userID;
	}
	
	public Collection<Notification> getNotification() {
		if (notification == null) notification = dao.getNotification(userID);
		return notification;
	}

	public Collection<WallPost> getWallPost() {
		if (wallPost == null) wallPost = dao.getWallPost(userID);
		return wallPost;
	}
	
//	public Collection<Faculty> getFaculty() {
//		if (faculty == null) faculty = dao.getFaculty(deptid);
//		return faculty;
//	}
//
//	public Collection<Course> getCourses() {
//		if (courses == null) courses = dao.getCourses(deptid);
//		return courses;
//	}

}