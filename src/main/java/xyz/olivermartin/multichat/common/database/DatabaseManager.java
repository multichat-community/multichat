package xyz.olivermartin.multichat.common.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DatabaseManager {

	private static DatabaseManager instance;

	public static DatabaseManager getInstance() {
		return instance;
	}

	static {
		instance = new DatabaseManager();
	}

	/* END STATIC */

	private File databasePathSQLite;
	private String databaseURLMySQL;
	private String databaseUsernameMySQL;
	private String databasePasswordMySQL;
	private List<String> databaseFlagsMySQL;

	private int defaultPoolSize = 10;

	private DatabaseMode databaseMode = DatabaseMode.SQLite;

	private Map<String, GenericPooledDatabase> databases;

	private DatabaseManager() {
		databases = new HashMap<String, GenericPooledDatabase>();
	}

	////////////

	public static void main(String args[]) throws SQLException {

		DatabaseManager.getInstance().setPathSQLite(new File("C:\\multichat\\db\\"));
		DatabaseManager.getInstance().createDatabase("multichat.db");

		Optional<GenericPooledDatabase> odb = DatabaseManager.getInstance().getDatabase("multichat.db");

		if (odb.isPresent()) {

			GenericPooledDatabase db = odb.get();
			UUID uuid1 = UUID.randomUUID();
			UUID uuid2 = UUID.randomUUID();

			SimpleConnection conn = null;

			try {

				conn = db.getConnection();

				//db.connectToDatabase();
				conn.safeExecute("DROP TABLE IF EXISTS name_data;");
				conn.safeExecute("DROP TABLE IF EXISTS nick_data;");

				conn.safeUpdate("CREATE TABLE IF NOT EXISTS name_data(id VARCHAR(128), f_name VARCHAR(255), u_name VARCHAR(255), PRIMARY KEY (id));");
				conn.safeUpdate("CREATE TABLE IF NOT EXISTS nick_data(id VARCHAR(128), u_nick VARCHAR(255), f_nick VARCHAR(255), PRIMARY KEY (id));");

				conn.safeUpdate("INSERT INTO name_data VALUES (?, 'Revilo410', 'revilo410');", uuid1.toString());
				conn.safeUpdate("INSERT INTO nick_data VALUES (?, '&3Revi', 'revi');", uuid1.toString());
				conn.safeUpdate("INSERT INTO name_data VALUES (?, 'Revilo510', 'revilo510');", uuid2.toString());
				ResultSet results = conn.safeQuery("SELECT * FROM name_data;");
				while (results.next()) {
					System.out.println(results.getString("id"));
				}

				results = conn.safeQuery("SELECT * FROM nick_data;");
				while (results.next()) {
					System.out.println(results.getString("id"));
				}

				results = conn.safeQuery("SELECT * FROM name_data INNER JOIN nick_data ON name_data.id = nick_data.id;");
				while (results.next()) {
					System.out.println(results.getString("id"));
				}

				results = conn.safeQuery("SELECT * FROM name_data LEFT JOIN nick_data ON name_data.id = nick_data.id;");
				while (results.next()) {
					System.out.println(results.getString("id"));
					if (results.getString("f_nick") == null) {
						System.out.println(results.getString("f_name"));
					} else {
						System.out.println(results.getString("f_nick"));
					}
				}

				results = conn.safeQuery("SELECT f_name, f_nick FROM name_data LEFT JOIN nick_data ON name_data.id = nick_data.id WHERE name_data.id = ?;", uuid1.toString());
				while (results.next()) {
					if (results.getString("f_nick") == null) {
						System.out.println(results.getString("f_name"));
					} else {
						System.out.println(results.getString("f_nick"));
					}

				}

				//db.disconnectFromDatabase();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

				if (conn != null) conn.closeAll();

			}

		}

		/*DatabaseManager.getInstance().setPathSQLite(new File("C:\\multichat\\db\\"));
		DatabaseManager.getInstance().createDatabase("multichat.db");

		Optional<GenericDatabase> odb = DatabaseManager.getInstance().getDatabase("multichat.db");

		if (odb.isPresent()) {

			GenericDatabase db = odb.get();
			UUID uuid1 = UUID.randomUUID();

			try {
				db.connectToDatabase();
				db.execute("DROP TABLE IF EXISTS muted_users;");
				db.execute("DROP TABLE IF EXISTS ignored_users;");
				// TODO Announcements
				// TODO Bulletins
				// TODO Casts
				// TODO Mod & Admin Chat Preferences
				// TODO Group Chat Info
				// TODO Group Spy Info
				// TODO Social Spy Info
				// TODO Global Chat Info

				db.update("CREATE TABLE muted_users(id VARCHAR(128) PRIMARY KEY);");
				db.update("CREATE TABLE ignored_users(ignorer_id VARCHAR(128) PRIMARY KEY, ignoree_id VARCHAR(128));");

				db.update("INSERT INTO muted_users VALUES ('" + uuid1.toString() + "');");
				ResultSet results = db.query("SELECT * FROM muted_users;");
				while (results.next()) {
					System.out.println(results.getString("id"));
				}
				db.disconnectFromDatabase();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			/*SQLNameManager sqlnm = new SQLNameManager();
			System.out.println(sqlnm.getCurrentName(uuid1));
			System.out.println(sqlnm.getName(uuid1));

			//if (sqlnm.getUUIDFromUnformattedNickname("Test").isPresent()) System.out.println(":(");
			//if (sqlnm.getUUIDFromUnformattedNickname("revilo").isPresent()) System.out.println(":)");

			System.out.println(sqlnm.getUUIDFromName("Revilo410").get());

			System.out.println(sqlnm.getUUIDFromNickname("Revilo").get());

			System.out.println("!!!");

			UUID uuid2 = UUID.randomUUID();
			sqlnm.testRegisterFakePlayer(uuid2, "Johno");

			System.out.println(sqlnm.getUUIDFromName("Johno").get());

			sqlnm.testRegisterFakePlayer(uuid2, "Johno2");

			System.out.println(sqlnm.getUUIDFromName("Johno2").get());

			sqlnm.setNickname(uuid2, "JonnyBoy");

			System.out.println(sqlnm.getCurrentName(uuid2));

			sqlnm.removeNickname(uuid2);

			System.out.println(sqlnm.getCurrentName(uuid2));

		}*/

	}

	////////////

	public void setDefaultPoolSize(int defaultPoolSize) {
		this.defaultPoolSize = defaultPoolSize;
	}

	public void setPathSQLite(File path) {
		this.databasePathSQLite = path;
	}

	public void setURLMySQL(String url) {
		this.databaseURLMySQL = url;
	}

	public void setUsernameMySQL(String user) {
		this.databaseUsernameMySQL = user;
	}

	public void setPasswordMySQL(String pass) {
		this.databasePasswordMySQL = pass;
	}

	public void setFlagsMySQL(List<String> flags) {
		this.databaseFlagsMySQL = flags;
	}

	public void setMode(DatabaseMode dbm) {
		databaseMode = dbm;
	}

	public GenericPooledDatabase createDatabase(String name) throws SQLException {
		return createDatabase(name, name);
	}

	public boolean isReady() {
		switch (databaseMode) {
		case MySQL:
			if (databaseURLMySQL != null && databaseUsernameMySQL != null && databasePasswordMySQL != null) {
				return true;
			} else {
				return false;
			}
		case SQLite:
		default:
			return databasePathSQLite != null;
		}

	}

	/**
	 * Generic class to create a sqlite database
	 * @throws SQLException 
	 */
	public GenericPooledDatabase createDatabase(String databaseName, String fileName) throws SQLException {

		if (!isReady()) throw new RuntimeException("MultiChat Database Manager Not Ready!");

		switch (databaseMode) {
		case MySQL:

			String databaseFlagsString = "";

			if (databaseFlagsMySQL != null && databaseFlagsMySQL.size() > 0) {
					databaseFlagsString = "?" + String.join("&", databaseFlagsMySQL);
			}

			databases.put(databaseName.toLowerCase(), new MySQLPooledDatabase(databaseURLMySQL, fileName + databaseFlagsString, databaseUsernameMySQL, databasePasswordMySQL, defaultPoolSize));

			return databases.get(databaseName.toLowerCase());

		case SQLite:
		default:

			if (!databasePathSQLite.exists()) {
				databasePathSQLite.mkdirs();
			}

			databases.put(databaseName.toLowerCase(), new SQLitePooledDatabase(databasePathSQLite, fileName, defaultPoolSize));

			return databases.get(databaseName.toLowerCase());

		}

	}

	public Optional<GenericPooledDatabase> getDatabase(String databaseName) {
		if (databases.containsKey(databaseName.toLowerCase())) {
			return Optional.of(databases.get(databaseName.toLowerCase()));
		} else {
			return Optional.empty();
		}
	}

	public void removeDatabase(String databaseName) throws SQLException {
		if (databases.containsKey(databaseName.toLowerCase())) {
			GenericPooledDatabase gdb = databases.get(databaseName.toLowerCase());
			gdb.disconnectFromDatabase();
			databases.remove(databaseName.toLowerCase());
		}
	}

	public void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {

			if (conn != null) conn.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();

		} catch (SQLException e) { /* EMPTY */ }
	}

	public void close(Connection conn, PreparedStatement ps) {
		close(conn, ps, null);
	}

}
