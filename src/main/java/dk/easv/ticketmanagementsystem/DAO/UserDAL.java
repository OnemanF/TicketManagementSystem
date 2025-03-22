package dk.easv.ticketmanagementsystem.DAO;

import dk.easv.ticketmanagementsystem.BE.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDAL {
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO Users (id, username, password, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, user.getId());  // UUID
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getHashedPassword());  // Store hashed password
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        false
                ));
            }
        }
        return users;
    }

    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ?";
        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    return new User(
                            UUID.fromString(rs.getString("id")),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            false
                    );
                }
            }
        }
        return null;
    }

    // **NEW METHOD: Update user information in the database**
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE Users SET username = ?, role = ? WHERE id = ?";
        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getRole());
            stmt.setObject(3, user.getId());  // UUID
            stmt.executeUpdate();
        }
    }

    // **NEW METHOD: Delete user from the database**
    public void deleteUser(User user) throws SQLException {
        String query = "DELETE FROM Users WHERE id = ?";
        try (Connection con = DBConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setObject(1, user.getId());  // UUID
            stmt.executeUpdate();
        }
    }
}
