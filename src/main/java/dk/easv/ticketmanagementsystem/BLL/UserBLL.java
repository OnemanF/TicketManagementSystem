package dk.easv.ticketmanagementsystem.BLL;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BE.UserManager;
import dk.easv.ticketmanagementsystem.DAO.EventDAL;
import dk.easv.ticketmanagementsystem.DAO.UserDAL;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserBLL {
    private final UserDAL userDAL;
    private final EventDAL eventDAL;

    public UserBLL() {
        this.userDAL = new UserDAL();
        this.eventDAL = new EventDAL();
    }

    public void registerUser(String username, String password, String role) throws SQLException {
        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            throw new IllegalArgumentException("Fields cannot be empty!");
        }

        if (role.equalsIgnoreCase("Event Coordinator")) {
            role = "Coordinator";
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        User newUser = new User(UUID.randomUUID(), username, hashedPassword, role, true);
        userDAL.addUser(newUser);
    }

    public User getUserByUsername(String username) throws SQLException {
        return userDAL.getUserByUsername(username);
    }

    public User authenticateUser(String username, String password) throws SQLException {
        User user = userDAL.getUserByUsername(username);

        if (user == null) {
            System.out.println("User not found: " + username);
            return null;
        }

        if (user.verifyPassword(password)) {
            return user;
        } else {
            return null;
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = userDAL.getAllUsers();
        UserManager.getInstance().getUsers().setAll(users);
        return users;
    }

    public void updateUser(User user) throws SQLException {
        if (user.getUsername().isEmpty() || user.getRole().isEmpty()) {
            throw new IllegalArgumentException("Fields cannot be empty!");
        }
        userDAL.updateUser(user);
    }

    public void deleteUser(User user) throws SQLException {
        userDAL.deleteUser(user);
    }

    public List<User> getCoordinators() throws SQLException {
        return userDAL.getCoordinators();
    }

    public List<Event> getAssignedEvents(UUID userId) {
        try {
            return eventDAL.getAssignedEvents(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}