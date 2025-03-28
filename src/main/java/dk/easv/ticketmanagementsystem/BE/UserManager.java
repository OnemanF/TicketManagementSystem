package dk.easv.ticketmanagementsystem.BE;

import dk.easv.ticketmanagementsystem.BLL.UserBLL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserManager {
    private static final UserManager instance = new UserManager();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final UserBLL userBLL;

    private UserManager() {
        this.userBLL = new UserBLL();
    }

    public static UserManager getInstance() {
        return instance;
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public List<User> getCoordinators() {
        try {
            return userBLL.getCoordinators();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

