module org.isep.sixquiprend {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens org.isep.sixquiprend to javafx.fxml;
    exports org.isep.sixquiprend;
}