

import java.util.Collection;

import dao.DatabaseManager;
import model.Notification;
import model.User;
import model.WallPost;

/**
 * Simple client that retrieves data from an already created database.
 * Running this after Test will check that the same data may be retrieved
 * from the database and not just from the in-memory cache.
 */
public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		
		User alex = dbm.findUserByName("Alex", "Spears");
		
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
		
		dbm.commit();
		
		dbm.close();
		
		System.out.println("Done");
	}

}