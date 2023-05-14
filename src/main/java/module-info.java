module org.isep.sixquiprend {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens org.isep.sixquiprend to javafx.fxml;
    opens org.isep.sixquiprend.view.GUI.example to javafx.fxml;
}