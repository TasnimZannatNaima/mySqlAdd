package org.example.mysqladd;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class BirthdayDAO {
    public void addBirthday(Birthday b) throws SQLException {
        String sql = "INSERT INTO new_table (id, name, birthday) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, b.getId());
            stmt.setString(2, b.getName());
            stmt.setDate(3, java.sql.Date.valueOf(b.getBirthdate()));
            stmt.executeUpdate();
        }
    }

    public void updateBirthday(Birthday b) throws SQLException {
        String sql = "UPDATE new_table SET name=?, birthday=? WHERE id=?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, b.getName());
            stmt.setDate(2, java.sql.Date.valueOf(b.getBirthdate()));
            stmt.setInt(3, b.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteBirthday(int id) throws SQLException {
        String sql = "DELETE FROM new_table WHERE id=?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Birthday> getAllBirthdays() throws SQLException {
        String sql = "SELECT * FROM new_table ORDER BY MONTH(birthday), DAY(birthday)";
        List<Birthday> list = new ArrayList<>();
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Birthday b = new Birthday();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setBirthdate(rs.getDate("birthday").toLocalDate());
                list.add(b);
            }
        }
        return list;
    }

    public List<Birthday> searchByNameOrMonth(String keyword) throws SQLException {
        String sql = "SELECT * FROM new_table WHERE name LIKE ? OR MONTH(birthday) = ?";
        List<Birthday> list = new ArrayList<>();
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            int month = 0;
            try {
                month = Integer.parseInt(keyword);
            } catch (NumberFormatException ignored) {}
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Birthday b = new Birthday();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setBirthdate(rs.getDate("birthday").toLocalDate());
                list.add(b);
            }
        }
        return list;
    }

    public List<Birthday> getTodaysBirthdays() throws SQLException {
        String sql = "SELECT * FROM new_table WHERE MONTH(birthday) = ? AND DAY(birthday) = ?";
        List<Birthday> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, today.getMonthValue());
            stmt.setInt(2, today.getDayOfMonth());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Birthday b = new Birthday();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setBirthdate(rs.getDate("birthday").toLocalDate());
                list.add(b);
            }
        }
        return list;
    }
}