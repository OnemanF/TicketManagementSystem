package dk.easv.ticketmanagementsystem.BE;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManager {
    private static final UserManager instance = new UserManager();
    private final ObservableList<User> users = FXCollections.observableArrayList();

    private UserManager() {}

    public static UserManager getInstance() {
        return instance;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public ObservableList<User> getCoordinators() {
        return users.filtered(user -> "Coordinator".equalsIgnoreCase(user.getRole()));
    }
}

