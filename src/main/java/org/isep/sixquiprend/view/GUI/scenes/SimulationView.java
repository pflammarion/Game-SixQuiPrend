package org.isep.sixquiprend.view.GUI.scenes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationView {
    private List<String> AIList = new ArrayList<>();
    private final Label titleSimu;
    private final VBox generalVBox;
    private final HBox generalHBox;
    private final HBox choiceHBox;
    private final VBox AIListTxt;
    private final VBox gamesRepPart;
    private final Text gamesRepTitle;
    private final TextField gamesRep;
    private final Button buttonBack;
    private final Button buttonSimu;
    private final Button buttonAjouterAIEasy;
    private final Button buttonAjouterAIMedium;
    private final Button buttonAjouterAIHard;
    private final HBox summaryResults;
    private final VBox easyPart;
    private final VBox mediumPart;
    private final VBox hardPart;
    private Label easyResult;
    private Label mediumResult;
    private Label hardResult;
    private final Scene scene;

    public SimulationView(){
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));
        Text AIListTitle = new Text("Nom des IA : \n\n");
        Label easyTitle = new Label("Victoires des IA Facile");
        Label mediumTitle = new Label("Victoires des IA Moyenne");
        Label hardTitle = new Label("Victoires des IA Forte");
        this.easyResult = new Label();
        this.mediumResult = new Label();
        this.hardResult = new Label();
        this.titleSimu = new Label("Simulation: IA / Bots");
        this.buttonBack = new Button("Retour");
        this.buttonSimu = new Button("Simuler");
        this.gamesRepTitle = new Text("Nombre de répétitions");
        this.gamesRep = new TextField();
        this.gamesRep.setMinWidth(150);
        this.gamesRep.setMinHeight(25);
        gamesRep.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    gamesRep.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        this.gamesRepPart = new VBox(gamesRepTitle, gamesRep);
        this.buttonAjouterAIEasy = new Button("Ajouter IA facile");
        this.buttonAjouterAIMedium = new Button("Ajouter IA moyenne");
        this.buttonAjouterAIHard = new Button("Ajouter IA forte");
        gamesRepPart.setAlignment(Pos.CENTER);

        buttonAjouterAIEasy.setOnAction(event -> {
            addNameToPlayerList("IA: Facile");
        });

        buttonAjouterAIMedium.setOnAction(event -> {
            addNameToPlayerList("IA: Moyenne");
        });

        buttonAjouterAIHard.setOnAction(event -> {
            addNameToPlayerList("IA: Forte");
        });

        this.AIListTxt = new VBox();
        this.AIListTxt.setMaxWidth(300);
        this.AIListTxt.setMaxHeight(100);
        this.AIListTxt.setAlignment(Pos.CENTER);

        this.choiceHBox = new HBox(buttonSimu, buttonBack);
        choiceHBox.setSpacing(10);
        choiceHBox.setAlignment(Pos.CENTER);

        this.generalHBox = new HBox(AIListTxt, gamesRepPart, buttonAjouterAIEasy, buttonAjouterAIMedium, buttonAjouterAIHard);
        generalHBox.setSpacing(10);
        generalHBox.setAlignment(Pos.CENTER);

        this.easyPart = new VBox(easyTitle, easyResult);
        easyPart.setAlignment(Pos.CENTER);
        easyPart.setVisible(false);
        this.mediumPart = new VBox(mediumTitle, mediumResult);
        mediumPart.setAlignment(Pos.CENTER);
        mediumPart.setVisible(false);
        this.hardPart = new VBox(hardTitle, hardResult);
        hardPart.setAlignment(Pos.CENTER);
        hardPart.setVisible(false);

        this.summaryResults = new HBox(easyPart, mediumPart, hardPart);
        summaryResults.setVisible(false);
        summaryResults.setSpacing(20);
        summaryResults.setAlignment(Pos.CENTER);

        easyResult.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean visible = !newValue.isEmpty();
            easyPart.setVisible(visible);
            summaryResults.setVisible(visible || !mediumResult.getText().isEmpty() || !hardResult.getText().isEmpty());
        });

        mediumResult.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean visible = !newValue.isEmpty();
            mediumPart.setVisible(visible);
            summaryResults.setVisible(!easyResult.getText().isEmpty() || visible || !hardResult.getText().isEmpty());
        });

        hardResult.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean visible = !newValue.isEmpty();
            hardPart.setVisible(visible);
            summaryResults.setVisible(!easyResult.getText().isEmpty() || !mediumResult.getText().isEmpty() || visible);
        });

        this.generalVBox = new VBox(titleSimu, generalHBox, summaryResults, choiceHBox);
        generalVBox.setSpacing(10);
        generalVBox.setAlignment(Pos.CENTER);

        AnchorPane anchorPane = new AnchorPane(imageView, generalVBox);

        //AnchorPane anchorPane = new AnchorPane(vbox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setTopAnchor(generalVBox, 100.0);
        AnchorPane.setBottomAnchor(generalVBox, 100.0);
        AnchorPane.setLeftAnchor(generalVBox, 300.0);
        AnchorPane.setRightAnchor(generalVBox, 300.0);

        this.scene = new Scene(anchorPane);

        AIListTxt.getChildren().add(AIListTitle);
        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());
    }

    public Scene getScene() {return scene;}
    public int getGamesRep(){return Integer.parseInt(gamesRep.getText());}
    public Button getButtonBack(){return buttonBack;}
    public Button getButtonSimu(){return buttonSimu;}
    public Label getEasyResult(){return easyResult;}
    public Label getMediumResult(){return mediumResult;}
    public Label getHardResult(){return hardResult;}
    public List<String> getAIList(){return AIList;}
    public void addNameToPlayerList(String name) {
        AIList.add(name);
        this.AIListTxt.getChildren().clear();
        Text AIListTitle = new Text("Nom des IA : \n\n");
        this.AIListTxt.getChildren().add(AIListTitle);
        for (String playerName : this.AIList) {
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
                int index = AIListTxt.getChildren().indexOf(parentHBox);

                AIList.remove(index - 1);
                AIListTxt.getChildren().remove(parentHBox);

            });

            playerHBox.getChildren().addAll(playerNametxt, spacer, removePlayer);
            this.AIListTxt.getChildren().add(playerHBox);
        }
    }

}
