package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserModel {
    private final ObservableList<User> users = FXCollections.observableArrayList();

    public ObservableList<User> getUsers() {
        return users;
    }

    public void addUser(String username, String password, String role) {
        int id = users.size() + 1; // Generate a simple unique ID
        User newUser = new User(id, username, password, role);
        users.add(newUser);
    }

    public void deleteUser(User user) {
        users.remove(user);
    }
}
