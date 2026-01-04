package biletsistemi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

   
    public boolean isSeatTaken(String firm, String fromCity, String toCity,
                              String flightDate, String flightTime, String seat) {

        String sql = """
                SELECT 1
                FROM tickets
                WHERE firm = ?
                  AND from_city = ?
                  AND to_city = ?
                  AND flight_date = ?
                  AND flight_time = ?
                  AND seat = ?
                LIMIT 1
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firm);
            ps.setString(2, fromCity);
            ps.setString(3, toCity);
            ps.setString(4, flightDate);
            ps.setString(5, flightTime);
            ps.setString(6, seat);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return true; 
        }
    }

    public boolean insertTicket(
            String userEmail,
            String firm,
            String fromCity,
            String toCity,
            String tripType,
            String flightDate,
            String flightTime,
            String returnDate,
            String seat,
            String fullName,
            String tcNo,
            String passengerEmail,
            double price
    ) {

        String sql = """
                INSERT INTO tickets(
                    user_email, firm, from_city, to_city, trip_type,
                    flight_date, flight_time, return_date,
                    seat, full_name, tc_no, passenger_email, price
                )
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userEmail);
            ps.setString(2, firm);
            ps.setString(3, fromCity);
            ps.setString(4, toCity);
            ps.setString(5, tripType);
            ps.setString(6, flightDate);
            ps.setString(7, flightTime);
            ps.setString(8, returnDate == null ? "" : returnDate);
            ps.setString(9, seat);
            ps.setString(10, fullName);
            ps.setString(11, tcNo);
            ps.setString(12, passengerEmail);
            ps.setDouble(13, price);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Ticket> getTicketsByUserEmail(String userEmail) {

        List<Ticket> list = new ArrayList<>();

        String sql = """
                SELECT id, firm, from_city, to_city, trip_type,
                       flight_date, flight_time, return_date,
                       seat, full_name, passenger_email, price
                FROM tickets
                WHERE user_email = ?
                ORDER BY id DESC
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userEmail);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket t = new Ticket(
                            rs.getInt("id"),
                            rs.getString("firm"),
                            rs.getString("from_city"),
                            rs.getString("to_city"),
                            rs.getString("trip_type"),
                            rs.getString("flight_date"),
                            rs.getString("flight_time"),
                            rs.getString("return_date"),
                            rs.getString("seat"),
                            rs.getString("full_name"),
                            rs.getString("passenger_email"),
                            rs.getDouble("price")
                    );
                    list.add(t);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
