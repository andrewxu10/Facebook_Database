package dao;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.WallPost;
import model.Notification;
import model.User;


public class WallPostDAO {
	private Connection conn;
	private DatabaseManager dbm;

	public WallPostDAO(Connection conn, DatabaseManager dbm) {
		this.conn = conn;
		this.dbm = dbm;
	}

	static void create(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "create table WallPost("
				+ "UserID integer not null,"
				+ "Posted timestamp, "
				+ "Contents varchar(10000) not null, "
				+ "UserThatPosted integer not null,"
				+ "WallPostid integer not null,"
				+  "primary key(WallPostid))";
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
		String s = "alter table WallPost add constraint fk_userThatPosted "
				+ "foreign key(userID) references USER1 on delete cascade";
		stmt.executeUpdate(s);
	}

	/**
	 * 
	 * 
	 */
	public WallPost find(int wallPostID) {
		try {
			String qry = "select UserID, Contents, UserThatPosted, Posted  from WALLPOST where WallPostID = ?";
			PreparedStatement pstmt = conn.prepareStatement(qry);
			pstmt.setInt(1, wallPostID);
			ResultSet rs = pstmt.executeQuery();

			// return null if course doesn't exist
			if (!rs.next())
				return null;

			String contents = rs.getString("Contents");
			java.sql.Timestamp posted = rs.getTimestamp("Posted");
			int userthatposted = rs.getInt("UserThatPosted");
			int userID = rs.getInt("UserID");

			rs.close();

		
			WallPost wallpost = new WallPost(this, userID, posted, contents, userthatposted, wallPostID);

			return wallpost;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error finding post", e);
		}
	}

	
	public WallPost insert(java.sql.Timestamp posted, int userID, String contents, int userthatpostedid, int wallpostid) {
		try {
			// make sure that the dept, num pair is currently unused
			if (find(wallpostid) != null)
				return null;

			String cmd = "insert into WALLPOST(UserID, Posted, Contents, UserthatPosted, WallPostid) "
					+ "values(?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setInt(1, userID);
			pstmt.setTimestamp(2, posted);
			pstmt.setString(3, contents);
			pstmt.setInt(4, userthatpostedid);
			pstmt.setInt(5, wallpostid);
			pstmt.executeUpdate();

			WallPost wallpost = new WallPost(this, userID, posted, contents, userthatpostedid, wallpostid);

			return wallpost;
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error inserting new wallpost", e);
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
	public void changeContents(int wallpostid, String contents) {
		try {
			String cmd = "update WALLPOST set Contents = ? where Wallpostid = ?";
			PreparedStatement pstmt = conn.prepareStatement(cmd);
			pstmt.setString(1, contents);
			pstmt.setInt(2, wallpostid);
	
			pstmt.executeUpdate();
		} catch (SQLException e) {
			dbm.cleanup();
			throw new RuntimeException("error changing contents", e);
		}
	}

	/**
	 * Clear all data from the Course table.
	 * 
	 * @throws SQLException
	 */
	void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		String s = "delete from WALLPOST";
		stmt.executeUpdate(s);
	}
}

