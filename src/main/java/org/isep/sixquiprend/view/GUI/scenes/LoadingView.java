package org.isep.sixquiprend.view.GUI.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.player.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class LoadingView {
    private final Button continueButton;
    private final Label concernedPlayer;
    private final Label roundCounter;
    private final Scene scene;
    public LoadingView() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));
        this.roundCounter = new Label();
        this.continueButton = new Button("Continuer");
        this.concernedPlayer = new Label();

        VBox sBox = new VBox(roundCounter, concernedPlayer, continueButton);
        sBox.setAlignment(Pos.CENTER);
        sBox.setSpacing(20);

        AnchorPane anchorPane = new AnchorPane(imageView, sBox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setTopAnchor(sBox, 100.0);
        AnchorPane.setBottomAnchor(sBox, 100.0);
        AnchorPane.setLeftAnchor(sBox, 300.0);
        AnchorPane.setRightAnchor(sBox, 300.0);

        this.scene = new Scene(anchorPane);

        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());

    }

    public Scene getScene() {
        return scene;
    }

    public Button getContinueButton() {
        return continueButton;
    }

    public void setConcernedPlayer(String text) {
        concernedPlayer.setText(text);
    }

    public void setRoundCounter(String text) {roundCounter.setText(text);}
}
