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
    private final VBox boardPane;
    private final Scene scene;
    public LoadingView() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));
        this.roundCounter = new Label();
        this.boardPane = new VBox();
        this.boardPane.setAlignment(Pos.CENTER);
        this.boardPane.setMaxWidth(500.0);
        this.boardPane.setSpacing(10);
        this.continueButton = new Button("Continuer");
        this.concernedPlayer = new Label();

        VBox sBox = new VBox(roundCounter, concernedPlayer, boardPane, continueButton);
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

    public void updateBoard(List<List<Card>> board) {
        boardPane.getChildren().clear();
        for (List<Card> row : board) {
            HBox rowBox = new HBox();
            rowBox.setSpacing(10);
            rowBox.setAlignment(Pos.BOTTOM_LEFT);

            for (Card card : row) {
                ImageView cardImagePlayed = new ImageView();
                cardImagePlayed.getStyleClass().add("card");
                String imagePath = ("/org/isep/sixquiprend/assets/img/cards/"+ card.getNumber() +".png");
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                cardImagePlayed.setImage(image);
                cardImagePlayed.setFitHeight(80);
                cardImagePlayed.setFitWidth(55);

                rowBox.getChildren().add(cardImagePlayed);
            }

            boardPane.getChildren().add(rowBox);
        }
    }
}
