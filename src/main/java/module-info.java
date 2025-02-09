module dk.easv.mytunes.ticketmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.mytunes.ticketmanagementsystem to javafx.fxml;
    exports dk.easv.mytunes.ticketmanagementsystem;
}