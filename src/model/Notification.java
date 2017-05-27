package model;

import dao.NotificationDAO;

/**
 * Model object for a row in the Course table.
 * Accesses the underlying database through a CourseDAO.
 * Based on Sciore, Section 9.1.
 * 
 */

public class Notification {
	private NotificationDAO dao;
	private String content;
	private String type;
	private int notificationID;
	private int userID;

	public Notification(NotificationDAO dao, String Content, String Type, int notificationID, int userID) {
		this.dao = dao;
		this.content = Content;
		this.type = Type; 
		this.notificationID = notificationID;
		this.userID = userID;
	}
	
	public String toString() {
		return "UserID: " + userID + " " + notificationID + ": " + type + ", " + content;
	}

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
		dao.changeContent(notificationID, content);
	}
	
	public String getType() {
		return type;
	}


	public int getNotificationID() {
		return notificationID;
	}


	public int getUserID() {
		return userID;
	}

}
