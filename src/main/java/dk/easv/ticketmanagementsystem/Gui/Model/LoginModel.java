package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.User;

public class LoginModel {
    private UserModel userModel;

    public LoginModel() {
        this.userModel = new UserModel();
    }

    public boolean authenticate(String username, String password) {
        for (User user : userModel.getUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User getAuthenticatedUser(String username) {
        for (User user : userModel.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}

