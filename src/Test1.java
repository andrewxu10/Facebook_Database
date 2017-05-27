

import java.util.Collection;
import java.util.Date;

import dao.DatabaseManager;
import model.Notification;
import model.User;
import model.WallPost;

/**
 * Simple client that inserts sample data then runs a query.
 * 
 */
public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		
		dbm.clearTables();
		
		// department heads are set to null for now; see below
		User andrew = dbm.insertUser(1, "Andrew", "Xu");
		User rachel = dbm.insertUser(2, "Rachel", "Stewart");
		User alexis = dbm.insertUser(3, "Alexis", "Wiltermood");
		User montana = dbm.insertUser(4, "Montana", "Hoenig");
		User alex = dbm.insertUser(5, "Alex", "Spears");



		dbm.insertNotification("Andrew posted on your wall", "Wall Post", 1, 5);
		dbm.insertNotification("Alexis posted on your wall", "Wall Post", 2, 2);
		dbm.insertNotification("Rachel posted on your wall", "Wall Post", 6, 3);
		dbm.insertNotification("Montana friend requested you", "Friend Request", 3, 1);
		dbm.insertNotification("Andrew friend requested you", "Friend Request", 4, 5);
		dbm.insertNotification("Alexis friend requested you", "Friend Request", 5, 5);

		//java.sql.Timestamp timestamp1 = Timestamp.valueOf("2007-09-23 10:10:10.0");
		//Timestamp timestamp2 = Timestamp.valueOf("2003-02-11 22:00:34.2");
		//Timestamp timestamp = Timestamp.valueOf("2007-12-23 09:01:06.000000003");
		
		java.sql.Timestamp timestamp1 = new java.sql.Timestamp(Date.parse("Sat, 12 Aug 1995 13:30:00 GMT"));
		java.sql.Timestamp timestamp2 = new java.sql.Timestamp(Date.parse("Sat, 12 Aug 1995 13:30:00 GMT"));
		//Timestamp timestamp3 = new Timestamp(2002, 01, 17, 11, 15, 05, 10);
		
		//wallpostid , userthatpostedid, userid (userid currently arbitrary value which is = to userthatpostedid)
		dbm.insertWallPost(timestamp1, "You can die a hero", 1, 1, 5);
		dbm.insertWallPost(timestamp2, "or you can live long", 2, 2 , 5);
		dbm.insertWallPost(timestamp2, "enough to see yourself become a villain", 3, 2 , 5);

		System.out.println("Done");
	

		dbm.commit();

		//retrieve a table for Alex, and his notifications
		//try to get type of wall post too?
		Collection<Notification> notification = alex.getNotification();
		for (Notification not : notification) {
			System.out.println(not);
		}
		
		//retrieve a table for alex and his wallposts
		Collection<WallPost> wallpost = alex.getWallPost();
		for (WallPost w : wallpost) {
			System.out.println(" " + w);
		}


//		Collection<Faculty> faculty = mathcs.getFaculty();
//		for (Faculty fac : faculty) {
//			System.out.println(fac);
//			Collection<Course> courses = fac.getCourses();
//			for (Course c : courses) {
//				System.out.println("  " + c + " [Head: " + c.getDept().getHead() + "]");
//			}
//		}
	

		dbm.commit();

		dbm.close();

		System.out.println("Done");
	}
}	