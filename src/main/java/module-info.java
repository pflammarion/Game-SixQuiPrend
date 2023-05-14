module org.isep.sixquiprend {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    opens org.isep.sixquiprend to javafx.fxml;
    exports org.isep.sixquiprend;
}