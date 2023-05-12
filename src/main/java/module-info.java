module org.isep.sixquiprend {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens org.isep.sixquiprend to javafx.fxml;
    exports org.isep.sixquiprend.views.GUI.example;
    opens org.isep.sixquiprend.views.GUI.example to javafx.fxml;
}