package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserModel {
    private final ObservableList<User> users = FXCollections.observableArrayList();

    public ObservableList<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void deleteUser(User user) {
        users.remove(user);
    }
}
