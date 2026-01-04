package biletsistemi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {

    public boolean addFlight(String firm, String fromCity, String toCity,
                             String flightDate, String flightTime, double price) {

        String sql = """
                INSERT INTO flights (firm, from_city, to_city, flight_date, flight_time, price)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firm);
            ps.setString(2, fromCity);
            ps.setString(3, toCity);
            ps.setString(4, flightDate);
            ps.setString(5, flightTime);
            ps.setDouble(6, price);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<FlightRow> getAllFlights() {

        String sql = """
                SELECT id, firm, from_city, to_city, flight_date, flight_time, price
                FROM flights
                ORDER BY id DESC
                """;

        List<FlightRow> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new FlightRow(
                        rs.getInt("id"),
                        rs.getString("firm"),
                        rs.getString("from_city"),
                        rs.getString("to_city"),
                        rs.getString("flight_date"),
                        rs.getString("flight_time"),
                        rs.getDouble("price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public FlightInfo findFlight(String firm, String fromCity, String toCity) {

        String sql = """
                SELECT flight_date, flight_time, price
                FROM flights
                WHERE firm = ?
                  AND from_city = ?
                  AND to_city = ?
                ORDER BY RANDOM()
                LIMIT 1
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firm);
            ps.setString(2, fromCity);
            ps.setString(3, toCity);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FlightInfo(
                            rs.getString("flight_date"),
                            rs.getString("flight_time"),
                            rs.getDouble("price")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public FlightInfo findFlight(String firm, String fromCity, String toCity,
                                 String flightDate, String flightTime) {

        String sql = """
                SELECT flight_date, flight_time, price
                FROM flights
                WHERE firm = ?
                  AND from_city = ?
                  AND to_city = ?
                  AND flight_date = ?
                  AND flight_time = ?
                LIMIT 1
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firm);
            ps.setString(2, fromCity);
            ps.setString(3, toCity);
            ps.setString(4, flightDate);
            ps.setString(5, flightTime);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FlightInfo(
                            rs.getString("flight_date"),
                            rs.getString("flight_time"),
                            rs.getDouble("price")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class FlightRow {
        public final int id;
        public final String firm;
        public final String fromCity;
        public final String toCity;
        public final String date;
        public final String time;
        public final double price;

        public FlightRow(int id, String firm, String fromCity, String toCity,
                         String date, String time, double price) {
            this.id = id;
            this.firm = firm;
            this.fromCity = fromCity;
            this.toCity = toCity;
            this.date = date;
            this.time = time;
            this.price = price;
        }
    }


    public static class FlightInfo {
        public final String date;
        public final String time;
        public final double price;

        public FlightInfo(String date, String time, double price) {
            this.date = date;
            this.time = time;
            this.price = price;
        }
    }
}
