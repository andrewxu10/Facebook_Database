package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;


import model.Notification;
import model.User;
import model.WallPost;

/**
 * Data Access Object for the Users table.
 * Encapsulates all of the relevant SQL commands.
 */

public class UserDAO {
	private Connection conn;
	private DatabaseManager dbm;

	public UserDAO(Connection conn, DatabaseManager dbm) {
		this.conn = conn;
		this.dbm = dbm;
	}

	/**
	 * Create the Course table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "create table USER1("
				+ "lastName varchar(100) not null, "
				+ "firstName varchar(100) not null, "
				+ "userID integer not null, "
				+ "primary key (userID), "
				+ "check (userID >= 0))";
		stmt.executeUpdate(s);
	}

	public User findByUserID(int userid) {
		try {
			String qry = "select lastName, firstName, userID from USER1 where UserId = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, userid);
			ResultSet rs = pstmt.executeQuery();

			//return null if user doesn't exist
			if (!rs.next())
				return null;

			int userID = rs.getInt("userID");
			String lastName = rs.getString("lastName");
			String firstName = rs.getString("firstName");

			User user = new User(this, firstName, lastName, userID); //verify if thats how constructor duz it

			return user;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding user", e);
		}
	} 

	/**
	 *
	 * @param firstName, lastName
	 * @return the Department object, or null if not found
	 */
	public User findByName(String firstName, String lastName) {
		try {
			String qry = "select userID from USER1 where firstName = ? AND lastName = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			ResultSet rs = pstmt.executeQuery();

			// return null if department doesn't exist
			if (!rs.next())
				return null;

			int userID = rs.getInt("userID");

			rs.close();

			//unnecessary line below?
			//User user = dbm.findUserID(userID);
			User user = new User(this, firstName, lastName, userID);

			return user;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding user by name", e);
		}
	}

	public User insert(String firstName, String lastName, int userID) {
		try {
			if (findByUserID(userID) != null)
				return null;

			String cmd = "insert into USER1(firstName, lastName, userID) "
					+ "values(?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setInt(3, userID);
			pstmt.executeUpdate();

			User user = new User(this, firstName, lastName, userID);

			return user;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new user", e);

		}
	}

	public void changeFirstName(int userID, String firstName) {
		try {
			String cmd = "update USER1 set firstName = ? where userID ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, firstName);
			pstmt.setInt(2, userID);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing first name", e);
		}
	}


	public void changeLastName(int userID, String lastName) {
		try {
			String cmd = "update USER1 set lastName = ? where userID ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, lastName);
			pstmt.setInt(2, userID);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing last name", e);
		}
	}

	// should we do a get for user?

	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from USER1";
		stmt.executeUpdate(s);
	}

	public Collection<Notification> getNotification(int userID) {
		try {
		Collection<Notification> notification = new ArrayList<Notification>();
		String qry = "select NotificationId from NOTIFICATION where UserThatGotNotificationID = ? ";
		PreparedStatement pstmt = conn.prepareStatement(qry);
		pstmt.setInt(1, userID);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int userID1 = rs.getInt("NotificationID"); //what is this
			notification.add(dbm.findNotification(userID1));
		}
		rs.close();
		return notification;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error getting department faculty", e);
		}
		
	}

	public Collection<WallPost> getWallPost(int userID) {
		try {
			Collection<WallPost> wallPost = new ArrayList<WallPost>();
			String qry = "select WallPostid from WALLPOST where userID = ? ";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, userID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int wallPostID = rs.getInt("wallpostid");
				wallPost.add(dbm.findWallPost(wallPostID));
			}
			rs.close();
			return wallPost;
			} catch (SQLException e) {
				dbm.cleanup();
				throw new RuntimeException("error getting department faculty", e);
			}
	}
	
//	public Collection<Faculty> getFaculty(int deptid) {
//		try {
//			Collection<Faculty> faculty = new ArrayList<Faculty>();
//			String qry = "select SSN from FACULTY where DId = ?";
//			PreparedStatement pstmt = conn.prepareStatement(qry);
//			pstmt.setInt(1, deptid);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				int ssn = rs.getInt("SSN");
//				faculty.add(dbm.findFaculty(ssn));
//			}
//			rs.close();
//			return faculty;
//		} catch (SQLException e) {
//			dbm.cleanup();
//			throw new RuntimeException("error getting department faculty", e);
//		}
//	}

}