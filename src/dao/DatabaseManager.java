package dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.derby.jdbc.EmbeddedDriver;

import model.Notification;
import model.User;
import model.WallPost;

import dao.NotificationDAO;
import dao.WallPostDAO;
import dao.UserDAO;


public class DatabaseManager {
	private Driver driver;
	private Connection conn;
	private NotificationDAO notificationDAO;
	private UserDAO userDAO;
	private WallPostDAO wallPostDAO;
	
	private final String url = "jdbc:derby:FacebookDatabase";

	public DatabaseManager() {
		driver = new EmbeddedDriver();
		
		Properties prop = new Properties();
		prop.put("create", "false");
		
		// try to connect to an existing database
		try {
			conn = driver.connect(url, prop);
			conn.setAutoCommit(false);
		}
		catch(SQLException e) {
			// database doesn't exist, so try creating it
			try {
				prop.put("create", "true");
				conn = driver.connect(url, prop);
				conn.setAutoCommit(false);
				create(conn);
			}
			catch (SQLException e2) {
				throw new RuntimeException("cannot connect to database", e2);
			}
		}
		
		notificationDAO = new NotificationDAO(conn, this);
		userDAO = new UserDAO(conn, this);
		wallPostDAO = new WallPostDAO(conn, this);
	}

	/**
	 * Initialize the tables and their constraints in a newly created database
	 *
	 * @throws SQLException
	 */
	private void create(Connection conn) throws SQLException {
		NotificationDAO.create(conn);
		UserDAO.create(conn);
		WallPostDAO.create(conn);
		NotificationDAO.addConstraints(conn);
		WallPostDAO.addConstraints(conn);
		conn.commit();
	}

	//***************************************************************
	// Data retrieval functions -- find a model object given its key
	
	//***************************************************************
	//***************************************************************
	//***************************************************************
	//***************************************************************
	//capitalization issues?
	
	public Notification findNotification(int notificationid) {
		return notificationDAO.findByNotificationID(notificationid);
	}

	public User findUser(int userid) {
		return userDAO.findByUserID(userid);
	}

	public WallPost findWallPost(int userId) {
		return wallPostDAO.find(userId);
	}
	
	public User findUserByName(String firstName, String lastName) {
		return userDAO.findByName(firstName, lastName);
	}
	
	//***************************************************************
	//***************************************************************
	//***************************************************************
	//***************************************************************
	//need more like finduserbyname?

	//***************************************************************
	// Data insertion functions -- create new model object from attributes
	
	
	

	//1115, "What's up", 1, 1
	//
	public Notification insertNotification(String content, String type, int notificationID, int userID) {
		return notificationDAO.insert(content, type, notificationID, userID); //userid = userthatgotnotificationID? (obselete comment i think)
	}
	//String content, String type, int notificationid, int userthatgotnotificationid

	public User insertUser(int userID, String firstName, String lastName) {
		return userDAO.insert(firstName, lastName, userID);
	}
	//String firstName, String lastName, int userID

	public WallPost insertWallPost(java.sql.Timestamp posted, String contents, int wallPostID, int userThatPostedID, int userID) {
		return wallPostDAO.insert(posted, userID, contents, userThatPostedID, wallPostID);
	}
	//java.sql.Timestamp posted, int userID, String contents, int userthatpostedid, int wallpostid

	//***************************************************************
	// Utility functions
	
	/**
	 * Commit changes since last call to commit
	 */
	public void commit() {
		try {
			conn.commit();
		}
		catch(SQLException e) {
			throw new RuntimeException("cannot commit database", e);
		}
	}

	/**
	 * Abort changes since last call to commit, then close connection
	 */
	public void cleanup() {
		try {
			conn.rollback();
			conn.close();
		}
		catch(SQLException e) {
			System.out.println("fatal error: cannot cleanup connection");
		}
	}

	/**
	 * Close connection and shutdown database
	 */
	public void close() {
		try {
			conn.close();
		}
		catch(SQLException e) {
			throw new RuntimeException("cannot close database connection", e);
		}
		
		// Now shutdown the embedded database system -- this is Derby-specific
		try {
			Properties prop = new Properties();
			prop.put("shutdown", "true");
			conn = driver.connect(url, prop);
		} catch (SQLException e) {
			// This is supposed to throw an exception...
			System.out.println("Derby has shut down successfully");
		}
	}

	/**
	 * Clear out all data from database (but leave empty tables)
	 */
	public void clearTables() {
		try {
			// This is not as straightforward as it may seem, because
			// of the cyclic foreign keys -- I had to play with
			// "on delete set null" and "on delete cascade" for a bit
			notificationDAO.clear();
			userDAO.clear();
			wallPostDAO.clear();
		} catch (SQLException e) {
			throw new RuntimeException("cannot clear tables", e);
		}
	}
}
