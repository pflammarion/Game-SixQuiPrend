package org.isep.sixquiprend.view.GUI.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WelcomeView {
    private final Button buttonQuit;
    private final Button buttonAjouter;
    private final Button buttonAjouterAIEasy;
    private final Button buttonAjouterAIMedium;
    private final Button buttonAjouterAIHard;
    private final Button buttonPlay;
    private final Button buttonOnline;
    private final TextField playerNameTextField;
    private List<String> playerList = new ArrayList<>();
    private final VBox playerListText;
    private final Scene scene;
    private MenuButton menu;
    private HBox playerAddHBox;
    private VBox playerSetVBox;

    public WelcomeView() {

        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));
        this.buttonQuit = new Button("Quitter");

        Label playerListLabel = new Label("Joueurs");
        Label gameName = new Label("SUPER six qui prend");

        MenuItem menuItemAjouterUnJoueur = new MenuItem("Ajouter un joueur");
        MenuItem menuItemAjouterUneIA = new MenuItem("Ajouter une IA");
        MenuItem menuItemOnline = new MenuItem("Jouer en ligne");

        menuItemAjouterUnJoueur.setOnAction(event -> playerMode());
        menuItemAjouterUneIA.setOnAction(event -> IAMode());
        menuItemOnline.setOnAction(event -> onlineMode());

        menu = new MenuButton("Options");
        if (isServerUp()){
            menu.getItems().addAll(menuItemAjouterUnJoueur, menuItemAjouterUneIA, menuItemOnline);
        }
        else {
            menu.getItems().addAll(menuItemAjouterUnJoueur, menuItemAjouterUneIA);
        }

        gameName.getStyleClass().add("game_name");

        this.playerNameTextField = new TextField();
        this.playerNameTextField.setMaxWidth(300);
        this.playerNameTextField.setMinWidth(150);
        this.playerNameTextField.setMinHeight(50);
        this.playerNameTextField.setPromptText("Entrer le nom");

        this.buttonAjouter = new Button("Ajouter");
        this.buttonAjouter.setAlignment(Pos.CENTER);
        this.buttonAjouter.setPrefSize(200, 50);
        this.buttonAjouter.setMinWidth(100);

        this.buttonAjouterAIEasy = new Button("Ajouter IA facile");
        this.buttonAjouterAIMedium = new Button("Ajouter IA moyen");
        this.buttonAjouterAIHard = new Button("Ajouter IA fort");
        this.buttonOnline = new Button("Jouer en ligne !");
        this.buttonOnline.getStyleClass().add("play_button");


        this.playerAddHBox = new HBox();
        this.playerAddHBox.setSpacing(20);
        this.playerAddHBox.getStyleClass().add("player_add_hbox");
        this.playerAddHBox.setAlignment(Pos.BOTTOM_CENTER);
        this.playerAddHBox.setMinWidth(300);
        this.playerAddHBox.setMinHeight(100);


        this.buttonPlay = new Button("Jouer !");
        this.buttonPlay.setAlignment(Pos.CENTER);
        this.buttonPlay.setPrefSize(400, 100);
        buttonPlay.getStyleClass().add("play_button");

        this.playerSetVBox = new VBox();
        this.playerSetVBox.setAlignment(Pos.CENTER);
        this.playerSetVBox.setMinWidth(300);
        this.playerSetVBox.setMinHeight(100);

        this.playerListText = new VBox();
        this.playerListText.setMinWidth(300);
        this.playerListText.setMinHeight(100);
        this.playerListText.setAlignment(Pos.CENTER);

        VBox playerListVBox = new VBox(playerListLabel, this.playerListText);
        playerListVBox.setAlignment(Pos.CENTER);

        HBox playerContainerHBox = new HBox(menu, playerSetVBox);
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

        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());

        String path = "src/main/resources/org/isep/sixquiprend/assets/ThomasLeGoat.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();
        anchorPane.getChildren().add(mediaView);

        this.playerMode();
        //TODO after endgame on a un souci car ça ne s'affiche pas car scene non reconstruite
        this.playerListText.getChildren().add(new Text("Nom des joueurs : \n\n"));
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

    public List<String> getPlayerList() {
        return playerList;
    }

    public String getPlayerName(){
        return playerNameTextField.getText();
    }

    public void setPlayerNameTextField(String string) {
        playerNameTextField.setText(string);
    }

    public void addNameToPlayerList(String name) {
        playerList.add(name);
        this.playerListText.getChildren().clear();
        for (String playerName : this.playerList) {
            HBox playerHBox = new HBox();
            playerHBox.setMinWidth(250);
            Text playerNametxt = new Text("- " + playerName);
            Button removePlayer = new Button("X");
            Pane spacer = new Pane();
            spacer.setMinWidth(Region.USE_PREF_SIZE);
            HBox.setHgrow(spacer, Priority.ALWAYS);

            removePlayer.setOnAction(event -> {
                Button button = (Button) event.getSource();
                HBox parentHBox = (HBox) button.getParent();
                int index = playerListText.getChildren().indexOf(parentHBox);

                playerList.remove(index);
                playerListText.getChildren().remove(parentHBox);

                if (playerListText.getChildren().isEmpty()){
                    playerListText.getChildren().add(new Text("Nom des joueurs : \n\n"));
                }
            });

            playerHBox.getChildren().addAll(playerNametxt, spacer, removePlayer);
            this.playerListText.getChildren().add(playerHBox);
        }
    }

    public void resetPlayerList() {
        this.playerListText.getChildren().clear();
        this.playerList = new ArrayList<>();
    }

    public Button getButtonAjouterAIEasy() {
        return buttonAjouterAIEasy;
    }
    public Button getButtonAjouterAIMedium() {
        return buttonAjouterAIMedium;
    }
    public Button getButtonAjouterAIHard() {
        return buttonAjouterAIHard;
    }
    public Button getButtonOnline() {
        return buttonOnline;
    }

    private void IAMode(){
        playerAddHBox.getChildren().clear();
        playerSetVBox.getChildren().clear();

        VBox playerContainer = new VBox(playerListText);
        playerContainer.getStyleClass().add("player_list");

        playerAddHBox.getChildren().add(playerContainer);
        playerAddHBox.getChildren().add(buttonAjouterAIEasy);
        playerAddHBox.getChildren().add(buttonAjouterAIMedium);
        playerAddHBox.getChildren().add(buttonAjouterAIHard);

        this.playerSetVBox.getChildren().add(playerAddHBox);
        this.playerSetVBox.getChildren().add(buttonPlay);
    }
    private void onlineMode(){
        playerAddHBox.getChildren().clear();
        playerSetVBox.getChildren().clear();

        Label playerNameLabel = new Label("Nom du joueur");

        VBox playerNameVBox = new VBox(playerNameLabel, playerNameTextField);
        playerNameVBox.setSpacing(10);
        playerNameVBox.setAlignment(Pos.CENTER_LEFT);

        playerAddHBox.getChildren().add(playerNameVBox);

        this.playerSetVBox.getChildren().add(playerAddHBox);
        this.playerSetVBox.getChildren().add(buttonOnline);
    }

    private void playerMode(){
        playerAddHBox.getChildren().clear();
        playerSetVBox.getChildren().clear();

        Label playerNameLabel = new Label("Nom du joueur");

        VBox playerNameVBox = new VBox(playerNameLabel, playerNameTextField);
        playerNameVBox.setSpacing(10);
        playerNameVBox.setAlignment(Pos.BOTTOM_LEFT);

        VBox playerContainer = new VBox(playerListText);
        playerContainer.getStyleClass().add("player_list");

        playerAddHBox.getChildren().add(playerContainer);
        playerAddHBox.getChildren().add(playerNameVBox);
        playerAddHBox.getChildren().add(buttonAjouter);

        this.playerSetVBox.getChildren().add(playerAddHBox);
        this.playerSetVBox.getChildren().add(buttonPlay);
    }

    private boolean isServerUp(){
        try {
            Socket socket = new Socket("flaminfo.fr", 4444);
            // to not make crash the server
            new ObjectOutputStream(socket.getOutputStream());
            new ObjectInputStream(socket.getInputStream());

            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}