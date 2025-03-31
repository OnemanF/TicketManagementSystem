package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.Event;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BLL.UserBLL;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class UserModel {
    private static final UserModel instance = new UserModel();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final UserBLL userBLL;

    public UserModel() {
        this.userBLL = new UserBLL();
        loadUsersFromDatabase();
    }

    public static UserModel getInstance() {
        return instance;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void addUser(String username, String password, String role) {
        try {
            userBLL.registerUser(username, password, role);
            User newUser = userBLL.getUserByUsername(username);
            loadAssignedEvents(newUser);
            users.add(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(User user) {
        try {
            userBLL.deleteUser(user);
            users.remove(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void loadUsersFromDatabase() {
        try {
            List<User> loadedUsers = userBLL.getAllUsers();
            for (User user : loadedUsers) {
                loadAssignedEvents(user);
            }
            users.setAll(loadedUsers);
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to load users from database.");
            e.printStackTrace();
        }
    }

    public void loadAssignedEvents(User user) {
        List<Event> assignedEvents = userBLL.getAssignedEvents(user.getId());
        user.getAssignedEvents().clear();
        user.getAssignedEvents().addAll(assignedEvents);
    }

    public void updateAssignedEvents(User user) {
        List<Event> updatedEvents = userBLL.getAssignedEvents(user.getId());

        if (user.getAssignedEvents() instanceof ObservableList) {
            ((ObservableList<Event>) user.getAssignedEvents()).setAll(updatedEvents);
        } else {
            user.getAssignedEvents().clear();
            user.getAssignedEvents().addAll(updatedEvents);
        }
    }
}
