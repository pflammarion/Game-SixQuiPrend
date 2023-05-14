package org.isep.sixquiprend.view.GUI.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import org.isep.sixquiprend.model.Player;

import java.util.List;

public class WelcomeView {
    
    private final Button buttonQuit;
    private final Button buttonAjouter;
    private final Button buttonPlay;
    private final TextField playerNameTextField;
    private final ListView<String> playerList;
    private final Scene scene;
    public WelcomeView() {

        //ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/isep/harrypotter/assets/img/background/home-background.jpg"))));
        this.buttonQuit = new Button("Quit");

        Label playerNameLabel = new Label("Nom du joueur");
        Label playerListLabel = new Label("Joueurs");
        Label gameName = new Label("SUPER six qui prend");

        this.playerNameTextField = new TextField();
        this.playerNameTextField.setMaxWidth(200);
        this.playerNameTextField.setPromptText("Entrer le nom");

        VBox playerNameVBox = new VBox(playerNameLabel, playerNameTextField);
        playerNameVBox.setSpacing(20);
        playerNameVBox.setAlignment(Pos.CENTER_LEFT);

        this.buttonAjouter = new Button("Ajouter");
        this.buttonAjouter.setAlignment(Pos.CENTER);
        this.buttonAjouter.setPrefSize(200, 50);

        HBox playerAddHBox = new HBox(playerNameVBox, buttonAjouter);
        playerAddHBox.setSpacing(20);
        playerAddHBox.setAlignment(Pos.BASELINE_CENTER);


        this.buttonPlay = new Button("Jouer !");
        this.buttonPlay.setAlignment(Pos.CENTER);
        this.buttonPlay.setPrefSize(400, 100);
        buttonPlay.getStyleClass().add("play_button");

        VBox playerSetVBox = new VBox(playerAddHBox, buttonPlay);
        playerSetVBox.setAlignment(Pos.CENTER);

       this.playerList = new ListView<>();
       this.playerList.setMaxSize(200, 200);

        VBox playerListVBox = new VBox(playerListLabel, playerList);
        playerListVBox.setAlignment(Pos.CENTER);

        HBox playerContainerHBox = new HBox(playerListVBox, playerSetVBox);
        playerContainerHBox.setSpacing(50);
        playerContainerHBox.setAlignment(Pos.CENTER);


        VBox vbox = new VBox(gameName, playerContainerHBox);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);


        //AnchorPane anchorPane = new AnchorPane(imageView, vbox);

        AnchorPane anchorPane = new AnchorPane(vbox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setTopAnchor(vbox, 100.0);
        AnchorPane.setBottomAnchor(vbox, 100.0);
        AnchorPane.setLeftAnchor(vbox, 300.0);
        AnchorPane.setRightAnchor(vbox, 300.0);

        this.scene = new Scene(anchorPane);

        //imageView.fitWidthProperty().bind(scene.widthProperty());
        //imageView.fitHeightProperty().bind(scene.heightProperty());
    }

    public Scene getScene() {
        return scene;
    }

    public Button getButtonQuit() {
        return buttonQuit;
    }

    public Button getButtonAjouter() {
        return buttonAjouter;
    }

    public Button getButtonPlay() {
        return buttonPlay;
    }

    public void setPlayerList(List<Player> list) {
        List<String> strings = list.stream()
                .map(Player::toString)
                .toList();
        ObservableList<String> observableList = FXCollections.observableArrayList(strings);
        this.playerList.setItems(observableList);
        this.playerList.setSelectionModel(null);
        this.playerList.getStyleClass().add("player-list");
    }
}
