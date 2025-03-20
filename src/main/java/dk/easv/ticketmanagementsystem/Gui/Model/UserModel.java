package dk.easv.ticketmanagementsystem.Gui.Model;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BLL.UserBLL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.UUID;

public class UserModel {
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final UserBLL userBLL;

    public UserModel() {
        this.userBLL = new UserBLL();
        loadUsersFromDatabase(); // Load existing users when the model is created
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void addUser(String username, String password, String role) {
        try {
            userBLL.registerUser(username, password, role);
            users.setAll(userBLL.getAllUsers()); // Reload users from database
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

    private void loadUsersFromDatabase() {
        try {
            users.addAll(userBLL.getAllUsers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
