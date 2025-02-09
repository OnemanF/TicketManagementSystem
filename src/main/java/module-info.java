module dk.easv.mytunes.ticketmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.mytunes.ticketmanagementsystem to javafx.fxml;
    exports dk.easv.mytunes.ticketmanagementsystem;
    exports dk.easv.mytunes.ticketmanagementsystem.Gui;
    opens dk.easv.mytunes.ticketmanagementsystem.Gui to javafx.fxml;
    exports dk.easv.mytunes.ticketmanagementsystem.Gui.Controller;
    opens dk.easv.mytunes.ticketmanagementsystem.Gui.Controller to javafx.fxml;
}