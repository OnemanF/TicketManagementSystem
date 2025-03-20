package dk.easv.ticketmanagementsystem.BE;

import dk.easv.ticketmanagementsystem.BLL.UserBLL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.UUID;

public class UserManager {
    private static final UserManager instance = new UserManager();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final UserBLL userBLL = new UserBLL();

    private UserManager() {}

    public static UserManager getInstance() {
        return instance;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void addUser(String username, String password, String role) throws SQLException {
        userBLL.registerUser(username, password, role);
        User user = new User(UUID.randomUUID(), username, password, role, true);
        users.add(user);
    }

    public ObservableList<User> getCoordinators() {
        return users.filtered(user -> "Coordinator".equalsIgnoreCase(user.getRole()));
    }
}

