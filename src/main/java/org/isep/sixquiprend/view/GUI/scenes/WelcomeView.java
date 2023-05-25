package org.isep.sixquiprend.view.GUI.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import org.isep.sixquiprend.model.player.Player;

import java.util.List;
import java.util.Objects;

public class WelcomeView {
    
    private final Button buttonQuit;
    private final Button buttonAjouter;
    private final Button buttonAjouterAI;
    private final Button buttonPlay;
    private final Button buttonOnline;
    private final TextField playerNameTextField;
    private final ListView<String> playerList;
    private final Scene scene;
    public WelcomeView() {

        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));
        this.buttonQuit = new Button("Quit");

        Label playerNameLabel = new Label("Nom du joueur");
        Label playerListLabel = new Label("Joueurs");
        Label gameName = new Label("SUPER six qui prend");

        gameName.getStyleClass().add("game_name");

        this.playerNameTextField = new TextField();
        this.playerNameTextField.setMaxWidth(300);
        this.playerNameTextField.setMinWidth(150);
        this.playerNameTextField.setMinHeight(50);
        this.playerNameTextField.setPromptText("Entrer le nom");


        VBox playerNameVBox = new VBox(playerNameLabel, playerNameTextField);
        playerNameVBox.setSpacing(10);
        playerNameVBox.setAlignment(Pos.CENTER_LEFT);


        this.buttonAjouter = new Button("Ajouter");
        this.buttonAjouter.setAlignment(Pos.CENTER);
        this.buttonAjouter.setPrefSize(200, 50);
        this.buttonAjouter.setMinWidth(100);

        this.buttonAjouterAI = new Button("Ajouter AI");
        this.buttonOnline = new Button("Jouer en ligne");


        HBox playerAddHBox = new HBox(playerNameVBox, buttonAjouter, buttonAjouterAI, buttonOnline);
        playerAddHBox.setSpacing(20);
        playerAddHBox.getStyleClass().add("player_add_hbox");
        playerAddHBox.setAlignment(Pos.BOTTOM_CENTER);


        this.buttonPlay = new Button("Jouer !");
        this.buttonPlay.setAlignment(Pos.CENTER);
        this.buttonPlay.setPrefSize(400, 100);
        buttonPlay.getStyleClass().add("play_button");



        VBox playerSetVBox = new VBox(playerAddHBox, buttonPlay);
        playerSetVBox.setAlignment(Pos.CENTER);

       this.playerList = new ListView<>();
       this.playerList.setMaxSize(200, 300);

        VBox playerListVBox = new VBox(playerListLabel, playerList);
        playerListVBox.setAlignment(Pos.CENTER);

        HBox playerContainerHBox = new HBox(playerListVBox, playerSetVBox);
        playerContainerHBox.setSpacing(50);
        playerContainerHBox.setAlignment(Pos.CENTER);


        VBox vbox = new VBox(gameName, playerContainerHBox);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);


        AnchorPane anchorPane = new AnchorPane(imageView, vbox);

        //AnchorPane anchorPane = new AnchorPane(vbox);
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

    public String getPlayerName(){
        return playerNameTextField.getText();
    }

    public void setPlayerNameTextField(String string) {
        playerNameTextField.setText(string);
    }

    public void addNameToPlayerList(String name) {
        ObservableList<String> observableList = this.playerList.getItems();
        observableList.add(name);
        this.playerList.setItems(observableList);
    }

    public void resetPlayerList() {
        this.playerList.setItems(FXCollections.observableArrayList());
    }

    public Button getButtonAjouterAI() {
        return buttonAjouterAI;
    }

    public Button getButtonOnline() {
        return buttonOnline;
    }
}
