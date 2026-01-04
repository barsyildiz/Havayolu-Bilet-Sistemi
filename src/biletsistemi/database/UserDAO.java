package biletsistemi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // ===================== PROFIL MODEL (AnaSayfa/Ayarlar ) =====================
    public static class UserProfile {
        public final String username;
        public final String email;
        public final String phone;
        public final String birthDate;
        public final String address;
        public final int balance;
        public final String avatarPath;

        public UserProfile(String username, String email, String phone, String birthDate,
                           String address, int balance, String avatarPath) {
            this.username = username;
            this.email = email;
            this.phone = phone;
            this.birthDate = birthDate;
            this.address = address;
            this.balance = balance;
            this.avatarPath = avatarPath;
        }
    }

    // ===================== REGISTER =====================
    public boolean register(String username, String email, String password) {
        String sql = "INSERT INTO users(username, email, password) VALUES(?,?,?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    // ===================== LOGIN (email+şifre) =====================
    public boolean login(String email, String password) {
        String sql = "SELECT id FROM users WHERE email=? AND password=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUsernameByEmail(String email) {
        String sql = "SELECT username FROM users WHERE email=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("username");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    // ===================== LOGIN (username+şifre) =====================
    public boolean loginByUsername(String username, String password) {
        String sql = "SELECT id FROM users WHERE username=? AND password=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getEmailByUsername(String username) {
        String sql = "SELECT email FROM users WHERE username=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("email");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    // =====================  PROFIL OKU (Ayarlar açılırken) =====================
    public UserProfile getProfileByUsername(String username) {
        String sql = """
                SELECT username, email,
                       COALESCE(phone,'')       AS phone,
                       COALESCE(birth_date,'')  AS birth_date,
                       COALESCE(address,'')     AS address,
                       COALESCE(balance,0)      AS balance,
                       COALESCE(avatar_path,'') AS avatar_path
                FROM users
                WHERE username = ?
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserProfile(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("birth_date"),
                            rs.getString("address"),
                            rs.getInt("balance"),
                            rs.getString("avatar_path")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateProfile(String username, String phone, String birthDate, String address, String avatarPath) {
        String sql = """
                UPDATE users
                SET phone = ?,
                    birth_date = ?,
                    address = ?,
                    avatar_path = ?
                WHERE username = ?
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone == null ? "" : phone);
            ps.setString(2, birthDate == null ? "" : birthDate);
            ps.setString(3, address == null ? "" : address);
            ps.setString(4, avatarPath == null ? "" : avatarPath);
            ps.setString(5, username);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =====================  BAKİYE =====================
    public int getBalanceByUsername(String username) {
        String sql = "SELECT COALESCE(balance,0) AS balance FROM users WHERE username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateBalance(String username, int newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newBalance);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
