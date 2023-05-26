module org.isep.sixquiprend {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires javafx.media;

    opens org.isep.sixquiprend to javafx.fxml;
    exports org.isep.sixquiprend;
}