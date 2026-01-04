package biletsistemi.database;

import java.io.File;
import java.sql.*;

public class Database {

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver bulunamadı. sqlite-jdbc jar classpath'te mi?", e);
        }

        File dbFile = resolveDbFile();

        
        File parent = dbFile.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        System.out.println("user.dir = " + System.getProperty("user.dir"));
        System.out.println("DB absolute path = " + dbFile.getAbsolutePath());
        System.out.println("DB exists? " + dbFile.exists()
                + " | canRead? " + dbFile.canRead()
                + " | canWrite? " + dbFile.canWrite());

        return DriverManager.getConnection(url);
    }

    private static File resolveDbFile() {
        String userDir = System.getProperty("user.dir");
        File base = new File(userDir);

        File c1 = new File(base, "data/havayolu.db");
        if (c1.exists()) return c1;

        File c2 = new File(base.getParentFile() != null ? base.getParentFile() : base, "data/havayolu.db");
        if (c2.exists()) return c2;

        File c3 = new File(base, "Havayolu-Bilet-Sistemi-main/data/havayolu.db");
        if (c3.exists()) return c3;

        return c1;
    }

    public static void init() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT NOT NULL,
                        email TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    )
                    """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS flights(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        firm TEXT NOT NULL,
                        from_city TEXT NOT NULL,
                        to_city TEXT NOT NULL,
                        flight_date TEXT NOT NULL,
                        flight_time TEXT NOT NULL,
                        price REAL NOT NULL,
                        created_at TEXT DEFAULT CURRENT_TIMESTAMP
                    )
                    """);

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS tickets(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_email TEXT NOT NULL,
                        from_city TEXT NOT NULL,
                        to_city TEXT NOT NULL,
                        trip_type TEXT NOT NULL,
                        flight_date TEXT NOT NULL,
                        return_date TEXT,
                        seat TEXT NOT NULL,
                        full_name TEXT NOT NULL,
                        tc_no TEXT NOT NULL,
                        passenger_email TEXT NOT NULL,
                        price REAL NOT NULL
                    )
                    """);

            // ================== ADMINS TABLOSU ==================
            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS admins(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    )
                    """);

            
            try (ResultSet rsA = st.executeQuery("SELECT COUNT(*) AS c FROM admins")) {
                if (rsA.next() && rsA.getInt("c") == 0) {
                    st.executeUpdate("INSERT INTO admins(username, password) VALUES('admin', '1234')");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
