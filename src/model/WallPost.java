package model;

import java.security.Timestamp;

import dao.WallPostDAO;

/**
 * Model object for a row in the Course table.
 * Accesses the underlying database through a CourseDAO.
 * Based on Sciore, Section 9.1.
 * 
 */
public class WallPost {
	private WallPostDAO WallPost;
	private int userID;
	private java.sql.Timestamp posted;
	private String contents;
	private int userThatPosted;
	private int wallPostID;

	public WallPost(WallPostDAO WallPost, int userID, java.sql.Timestamp posted, String contents, int userThatPosted, int wallPostID) {
		this.WallPost = WallPost;
		this.userID = userID;
		this.posted = posted; 
		this.contents = contents;
		this.userThatPosted = userThatPosted;
		this.wallPostID = wallPostID;
	}
	
	public String toString() {
		return "User that Posted:" + userThatPosted + " " + posted + " " + contents;
	}

	public java.sql.Timestamp getPosted() {
		return posted;
	}

	public int getUser() {
		return userID;
	}
	
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
		WallPost.changeContents(wallPostID, contents); // right 
	}
	
	public int getUserThatPosted() {
		return userThatPosted;
	}

	public int getWallPostID () {
		return wallPostID;
	}
	
	public WallPostDAO getWallPost() {
		return WallPost;
	}

}

