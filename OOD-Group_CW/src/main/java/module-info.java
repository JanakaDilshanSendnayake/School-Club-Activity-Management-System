module com.example.oodgroup_cw {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.oodgroup_cw to javafx.fxml;
    exports com.example.oodgroup_cw;
}