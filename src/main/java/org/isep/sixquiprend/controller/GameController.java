package org.isep.sixquiprend.controller;

import javafx.application.Platform;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.Deck;
import org.isep.sixquiprend.model.Game;
import org.isep.sixquiprend.model.player.AIPlayer;
import org.isep.sixquiprend.model.player.HumanPlayer;
import org.isep.sixquiprend.model.player.Player;
import org.isep.sixquiprend.view.GUI.SceneManager;
import org.isep.sixquiprend.view.GUI.scenes.EndGameView;
import org.isep.sixquiprend.view.GUI.scenes.GameView;
import org.isep.sixquiprend.view.GUI.scenes.LobbyView;
import org.isep.sixquiprend.view.GUI.scenes.WelcomeView;

import java.util.*;

public class GameController {
    private final SceneManager sceneManager;
    private final WelcomeView welcomeView;
    private final GameView gameView;
    private final EndGameView endGameView;
    private final LobbyView lobbyView;
    private final Game game;
    private Deck deck;
    private final int numCardsPerPlayer = 10;
    private int numberOfAIPlayer = 0;
    private Client client = null;
    private String playerName;

    public GameController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.welcomeView = new WelcomeView();
        this.gameView = new GameView();
        this.endGameView = new EndGameView();
        this.lobbyView = new LobbyView();
        this.game = new Game();
        eventListener();
    }

    private void eventListener() {
        sceneManager.addScene("welcome", welcomeView.getScene());
        sceneManager.addScene("game", gameView.getScene());
        sceneManager.addScene("endGame", endGameView.getScene());
        sceneManager.addScene("lobby", lobbyView.getScene());

        welcomeView.getButtonPlay().setOnAction(event -> startGame());
        welcomeView.getButtonAjouter().setOnAction(event -> addPlayer());
        welcomeView.getButtonAjouterAI().setOnAction(event -> addAIPlayer());
        welcomeView.getButtonOnline().setOnAction(event -> {
                    sceneManager.switchToScene("lobby");
                    playOnline();
                });
        gameView.getPlayButton().setOnAction(event -> playCard());
        endGameView.getRestartButton().setOnAction(event -> {
            this.numberOfAIPlayer = 0;
            game.getPlayers().clear();
            welcomeView.resetPlayerList();
            sceneManager.switchToScene("welcome");
        });
        endGameView.getQuitButton().setOnAction(event -> quitGame());

        lobbyView.getQuitButton().setOnAction(event -> {
            sceneManager.switchToScene("welcome");
            client.closeConnection();
        });
        lobbyView.getPlayButton().setOnAction(event -> startOnlineGame());
    }

    private List<Card> fillDeck() {
        List<Card> cards = new ArrayList<>();
        for (int i = 1; i <= 104; i++) {
            int bullHeads;
            if (i % 11 == 0) {
                bullHeads = 5;
            } else if (i % 10 == 0) {
                bullHeads = 3;
            } else if (i % 5 == 0) {
                bullHeads = 2;
            } else {
                bullHeads = 1;
            }
            Card card = new Card(i, bullHeads);
            cards.add(card);
        }
        return cards;
    }

    private void startGame() {
        if (game.getPlayers().size() >= 2){

            this.setup();

            deck.shuffle();
            game.boardSetUp(deck);

            Player currentPlayer = getCurrentPlayer();
            dealCards();

            gameView.updatePlayers(game.getPlayers());
            gameView.updateBoard(game.getBoard());
            gameView.updateRound(game.getRound());
            gameView.setPlayerTurn(currentPlayer);

            sceneManager.switchToScene("game");
            AIPlayer aiPlayer = getCurrentPlayer() instanceof AIPlayer ? (AIPlayer) getCurrentPlayer() : null;
            if (aiPlayer != null) {
                aiPlayerPlayCard(aiPlayer);
            }

        }
        else if (null != client){
            client.sendMessageToServer("GET_PLAYERS");
            if (client.waitForResponse().equals("playerlist")){
                System.out.println("i have the player list");
            }
        }
        else {
            System.out.println("Il faut au moins deux joueur pour commencer à jouer");
        }
    }

    private void dealCards() {
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            List<Card> cards = new ArrayList<>();
            for (int j = 0; j < numCardsPerPlayer; j++) {
                Card card = deck.draw();
                cards.add(card);
            }

            cards.sort(Comparator.comparingInt(Card::getNumber));
            player.setHand(cards);
        }
    }

    private void playCard() {
        Player currentPlayer = getCurrentPlayer();

        Card playedCard = gameView.getSelectedCard();

        if (playedCard != null) {
            currentPlayer.setLastCardPlayed(playedCard);
            currentPlayer.getHand().remove(playedCard);
            game.getCardsPlayed().add(playedCard);

            if (checkEndTurn()) {
                return;
            }

            moveToNextPlayer();
            gameView.updatePlayers(game.getPlayers());
            gameView.updateRound(game.getRound());
            gameView.setPlayerTurn(getCurrentPlayer());

            AIPlayer aiPlayer = getCurrentPlayer() instanceof AIPlayer ? (AIPlayer) getCurrentPlayer() : null;
            if (aiPlayer != null) {
                aiPlayerPlayCard(aiPlayer);
            }
        }
    }

    private void aiPlayerPlayCard(AIPlayer aiPlayer) {
        List<Card> aiPlayerHand = aiPlayer.getHand();
        if (aiPlayerHand.size() > 0) {

            Card selectedCard;
            List<Integer> tempStore = new ArrayList<>();
            List<List<Card>> board = game.getBoard();
            int smallestDiff = Integer.MAX_VALUE;
            int selectedCardIndex = -1;
            int lowestRowValue = Integer.MAX_VALUE;

            for (List<Card> row : board) {
                int lastCardNumber = row.get(row.size() - 1).getNumber();
                if (lastCardNumber < lowestRowValue) {
                    lowestRowValue = lastCardNumber;
                }
            }

            for (Card card : aiPlayerHand) {
                int cardNumber = card.getNumber();
                int diff = cardNumber - lowestRowValue;
                tempStore.add(diff);
            }

            for (int i = 0; i < tempStore.size(); i++) {
                int currentDiff = tempStore.get(i);
                if (currentDiff < smallestDiff && currentDiff > 0) {
                    smallestDiff = currentDiff;
                    selectedCardIndex = i;
                }
            }

            // Take the lowest card of the player hand
            if (selectedCardIndex == -1) {
                int minValue = Integer.MAX_VALUE;
                int currentIndex = 0;
                for (Card card : aiPlayerHand) {
                    if (card.getNumber() < minValue) {
                        minValue = card.getNumber();
                        selectedCardIndex = currentIndex;
                    }
                    currentIndex++;
                }
            }

            selectedCard = aiPlayerHand.get(selectedCardIndex);
            if (selectedCard != null) {
                aiPlayer.setLastCardPlayed(selectedCard);
                aiPlayerHand.remove(selectedCard);
                game.getCardsPlayed().add(selectedCard);

                if (checkEndTurn()) {
                    return;
                }

                moveToNextPlayer();
                gameView.updatePlayers(game.getPlayers());
                gameView.updateRound(game.getRound());
                gameView.setPlayerTurn(getCurrentPlayer());

                AIPlayer aiPlayerNext = getCurrentPlayer() instanceof AIPlayer ? (AIPlayer) getCurrentPlayer() : null;
                if (aiPlayerNext != null) {
                    aiPlayerPlayCard(aiPlayerNext);
                }
            }
        }
    }

    private void endGame() {
        endGameView.setScores(game.getPlayers());
        endGameView.setWinner(findWinner());
        sceneManager.switchToScene("endGame");
    }

    private Player findWinner() {
        Player winner = null;
        int minScore = Integer.MAX_VALUE;

        //TODO faire quand deux joueurs ont le même score
        for (Player player : game.getPlayers()) {
            if (player.getScore() < minScore) {
                minScore = player.getScore();
                winner = player;
            }
        }

        return winner;
    }

    private void quitGame() {
        Platform.exit();
        System.exit(0);
    }

    public void moveToNextPlayer() {
        game.setCurrentPlayerIndex((game.getCurrentPlayerIndex()+ 1) % game.getPlayers().size());
    }
    public Player getCurrentPlayer(){
        return game.getPlayers().get(game.getCurrentPlayerIndex());
    }
    public void incrementRound() {
        game.setRound(game.getRound() + 1);
    }
    public void setup() {
        this.deck = new Deck(fillDeck());
        game.getCardsPlayed().clear();
        game.getBoard().clear();
        game.setRound(1);
        game.setGameEnded(false);
        game.setCurrentPlayerIndex(0);
    }

    public void updateBoard(List<Card> cardList) {
        cardList.sort(Comparator.comparingInt(Card::getNumber));

        for (Card card: cardList) {
            int score = 0;
            ArrayList<List<Card>> board = game.getBoard();
            int lastCardDiff = Integer.MAX_VALUE;
            int selectedRowIndex = -1;
            List<Card> selectedRow;
            for(int i = 0; i < board.size(); i ++){
                List<Card> row = board.get(i);
                int lastCardNumber = row.get(row.size() - 1).getNumber();
                int cardDiff = card.getNumber() - lastCardNumber;
                // A revoir, pas sur de cette méthode
                // On peut reprendre ma méthode pour déterminer la plus petite diff obtenu pour une carte; ca marchait
                if (cardDiff > 0 && lastCardDiff > cardDiff){
                    selectedRowIndex = i;
                }
            }
            if (selectedRowIndex != -1){
                selectedRow = board.get(selectedRowIndex);
                if (selectedRow.size() >= 5) {
                    for (Card cardInRow : selectedRow) {
                        score += cardInRow.getBullHeads();
                    }
                    selectedRow.clear();
                }

            } else {
                // Algo in case card played is not playable then choose the min point line
                int minNumberOfHead = Integer.MAX_VALUE;
                int currentIndex = 0;
                for (List<Card> cards : board) {
                    int numberOfHead = 0;
                    for (Card cardInRow : cards) {
                        numberOfHead += cardInRow.getBullHeads();
                    }
                    if (numberOfHead < minNumberOfHead) {
                        minNumberOfHead = numberOfHead;
                        selectedRowIndex = currentIndex;
                    }
                    // TODO faire quand le nombre est equivalent pour choisir un meilleur ligne en fonction de la derniere carte
                    currentIndex++;
                }

                selectedRow = board.get(selectedRowIndex);

                for (Card cardInRow : selectedRow) {
                    score += cardInRow.getBullHeads();
                }

                selectedRow.clear();
            }

            selectedRow.add(card);
            board.set(selectedRowIndex, selectedRow);

            if (score > 0) {
                for (Player player : game.getPlayers()){
                    if (player.getLastCardPlayed().getNumber() == card.getNumber()){
                        player.setScore(player.getScore() + score);
                    }
                }
            }
        }
    }

    private boolean checkEndTurn(){
        if (game.getCardsPlayed().size() == game.getPlayers().size()) {
            incrementRound();
            this.updateBoard(game.getCardsPlayed());
            game.resetCardsPlayed();

            gameView.updateBoard(game.getBoard());
            gameView.updatePlayers(game.getPlayers());

            if (game.getRound() == numCardsPerPlayer + 1) {
                endGame();
                return true;
            }
        }
        return false;
    }
    private void addPlayer(){
        String playerName = welcomeView.getPlayerName();
        if (!playerName.equalsIgnoreCase("") && !checkAlreadyUsedName(playerName)){
            HumanPlayer humanPlayer = new HumanPlayer(playerName);
            game.getPlayers().add(humanPlayer);
            welcomeView.addNameToPlayerList(playerName);
            welcomeView.setPlayerNameTextField("");
        }
    }

    private void addAIPlayer() {
        this.numberOfAIPlayer ++;
        AIPlayer aiPlayer = new AIPlayer("AI " + numberOfAIPlayer);
        game.getPlayers().add(aiPlayer);
        welcomeView.addNameToPlayerList(aiPlayer.getName());
    }

    private boolean checkAlreadyUsedName(String name){
        for (Player player : game.getPlayers()){
            if (Objects.equals(player.getName(), name)){
                return true;
            }
        }
        return false;
    }

    private void playOnline() {
        this.client = new Client(this);
        client.connectToServer();
        client.sendMessageToServer("SET_PLAYERNAME");
        client.sendMessageToServer(welcomeView.getPlayerName());
    }

    public void updateOnlinePlayerList(List<String> list) {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i < list.size(); i++){
            players.add(new HumanPlayer(list.get(i)));
        }
        game.setPlayers(players);
        lobbyView.setPlayers(players);
        System.out.println(this.playerName);
    }

    private void startOnlineGame() {

    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
