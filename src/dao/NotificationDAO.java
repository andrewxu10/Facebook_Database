package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Notification;


public class NotificationDAO {
	private Connection conn;
	private DatabaseManager dbm;

	public NotificationDAO(Connection conn, DatabaseManager dbm) {
		this.conn = conn;
		this.dbm = dbm;
	}

	static void create(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "create table Notification("
				+ "Content varchar(1000), "
				+ "Type varchar(1000), "
				+ "NotificationId integer not null,"
				+ "UserThatGotNotificationID integer not null,"
				+  "primary key(NotificationId))";
		stmt.executeUpdate(s);
	}

	/**
	 * Modify the Course table to add foreign key constraints (needs to happen
	 * after the other tables have been created)
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void addConstraints(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "alter table Notification add constraint fk_UserThatGotNotification "
				+ "foreign key(UserThatGotNotificationID) references USER1 on delete cascade";
		stmt.executeUpdate(s);
	}
	
//	static void addConstraints(Connection conn) throws SQLException {
//		Statement stmt = conn.createStatement();
//		String s = "alter table WallPost add constraint fk_userThatPosted "
//				+ "foreign key(userID) references USER1 on delete cascade";
//		stmt.executeUpdate(s);
//	}
//	
//	static void addConstraints(Connection conn) throws SQLException {
//		Statement stmt = conn.createStatement();
//		String s = "alter table COURSE add constraint fk_coursefac "
//				+ "foreign key(FacSSN) references FACULTY on delete cascade";
//		stmt.executeUpdate(s);
//		s = "alter table COURSE add constraint fk_coursedept "
//				+ "foreign key(Dept) references DEPARTMENT on delete cascade";
//		stmt.executeUpdate(s);
//	}

	/**
	 * Retrieve a Course object given its key.
	 * 
	 */
	public Notification find(int userthatgotnotificationid) {
		try {
			String qry = "select Content, Type, NotificationId from NOTIFICATION where UserThatGotNotificationID = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, userthatgotnotificationid);
			ResultSet rs = pstmt.executeQuery();

			// return null if course doesn't exist
			if (!rs.next())
				return null;

			String content = rs.getString("Content");
			String type = rs.getString("Type");
			int notificationid = rs.getInt("NotificationId");

			rs.close();

		
			Notification notification = new Notification(this, content, type, notificationid,userthatgotnotificationid);

			return notification;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding notification", e);
		}
	}

	//findby notification ID

	public Notification findByNotificationID(int notificationID) {
		try {
			String qry = "select Content, Type, userthatgotnotificationid from NOTIFICATION where notificationID = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, notificationID);
			ResultSet rs = pstmt.executeQuery();

			// return null if course doesn't exist
			if (!rs.next())
				return null;

			String content = rs.getString("Content");
			String type = rs.getString("Type");
			int userthatgotnotificationid = rs.getInt("userthatgotnotificationid");


			rs.close();

		
			Notification notification = new Notification(this, content, type, notificationID, userthatgotnotificationid);

			return notification;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding notification", e);
		}
	}


	
	public Notification insert(String content, String type, int notificationid, int userid) {
		try {
			// make sure that the dept, num pair is currently unused
			if (findByNotificationID(notificationid) != null)
				return null;

			String cmd = "insert into NOTIFICATION(Content, Type, NotificationId, UserThatGotNotificationID) "
					+ "values(?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, content);
			pstmt.setString(2, type);
			pstmt.setInt(3, notificationid);
			pstmt.setInt(4, userid);
			pstmt.executeUpdate();

			Notification notification = new Notification(this, content, type, notificationid, userid);

			return notification;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new notification", e);
		}
	}

	/**
	 * Title was changed in the model object, so propagate the change to the
	 * database.
	 * 
	 * @param dept
	 * @param num
	 * @param title
	 */
	public void changeContent (int notificationid, String content) {
		try {
			String cmd = "update NOTIFICATION set Content = ? where NotificationId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, content);
			pstmt.setInt(2, notificationid);
			
	
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing content", e);
		}
	}
	
	public void changeType (int notificationid, String type) {
		try {
			String cmd = "update NOTIFICATION set Type = ? where NotificationId = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, type);
			pstmt.setInt(2, notificationid);
			
	
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing type", e);
		}
	}

	/**
	 * Clear all data from the Course table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from NOTIFICATION";
		stmt.executeUpdate(s);
	}
}