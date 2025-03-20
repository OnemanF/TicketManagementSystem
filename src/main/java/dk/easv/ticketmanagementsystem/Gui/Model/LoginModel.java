package dk.easv.ticketmanagementsystem.Gui.Model;

import dk.easv.ticketmanagementsystem.BE.User;
import dk.easv.ticketmanagementsystem.BLL.UserBLL;

import java.sql.SQLException;

public class LoginModel {
    private final UserBLL userBLL;

    public LoginModel() {
        this.userBLL = new UserBLL();
    }

    public boolean authenticate(String username, String password) {
        try {
            return userBLL.authenticateUser(username, password) != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getAuthenticatedUser(String username, String password) {
        try {
            return userBLL.authenticateUser(username, password); // Pass actual password
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

