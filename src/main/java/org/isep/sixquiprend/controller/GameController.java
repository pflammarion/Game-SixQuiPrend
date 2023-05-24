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
import org.isep.sixquiprend.view.GUI.scenes.WelcomeView;

import java.lang.reflect.Array;
import java.util.*;

public class GameController {
    private final SceneManager sceneManager;
    private WelcomeView welcomeView;
    private GameView gameView;
    private EndGameView endGameView;
    private Game game;
    private Deck deck;
    private int numCardsPerPlayer = 10;

    public GameController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.welcomeView = new WelcomeView();
        this.gameView = new GameView();
        this.endGameView = new EndGameView();
        this.game = new Game();
        eventListener();
        this.deck = new Deck(fillDeck());
    }

    private void eventListener() {
        sceneManager.addScene("welcome", welcomeView.getScene());
        sceneManager.addScene("game", gameView.getScene());
        sceneManager.addScene("endGame", endGameView.getScene());

        welcomeView.getButtonPlay().setOnAction(event -> startGame());
        gameView.getPlayButton().setOnAction(event -> playCard());
        gameView.getSkipButton().setOnAction(event -> skipTurn());
        endGameView.getRestartButton().setOnAction(event -> restartGame());
        endGameView.getQuitButton().setOnAction(event -> quitGame());
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
        String playerName = welcomeView.getPlayerName();
        HumanPlayer humanPlayer = new HumanPlayer(playerName);

        List<AIPlayer> aiPlayers = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            AIPlayer aiPlayer = new AIPlayer("AI "+ i);
            aiPlayers.add(aiPlayer);
        }

        game.getPlayers().add(humanPlayer);
        game.getPlayers().addAll(aiPlayers);
        deck.shuffle();
        game.boardSetUp(deck);

        Player currentPlayer = getCurrentPlayer();
        dealCards();

        gameView.updatePlayers(game.getPlayers());
        gameView.updateBoard(game.getBoard());
        gameView.updateRound(game.getRound());
        gameView.updateTotalBullHeads(game.getTotalBullHeads());
        gameView.setPlayerTurn(currentPlayer);

        sceneManager.switchToScene("game");
    }

    private void dealCards() {
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            List<Card> cards = new ArrayList<>();
            for (int j = 0; j < numCardsPerPlayer; j++) {
                Card card = deck.draw();
                cards.add(card);
            }
            sortDeck(cards);
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
            Card selectedCard = null;
            List<Integer> tempStore = new ArrayList<>();
            List<Card> listCard = new ArrayList<>();
            List<List<Card>> board = game.getBoard();

            int smallestDiff = Integer.MAX_VALUE;
            int selectedCardIndex = -1;
            int lowestRowValue = Integer.MAX_VALUE;

            for (List<Card> row : board) {
                // Do diff to row that are "playable" and without any penalties
                if (row.size() < 5){
                    int lastCardNumber = row.get(row.size() - 1).getNumber();
                    for (Card card : aiPlayerHand) {
                        int cardNumber = card.getNumber();
                        int diff = cardNumber - lastCardNumber;
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
                        selectedCard = new Card(-1, 100);
                    } else {
                        selectedCard = aiPlayerHand.get(selectedCardIndex);
                    }

                    listCard.add(selectedCard);

                // If row is not "playable", do not consider it at all, add 104 to tempStore
                } else {
                   listCard.add(new Card(-1, 100));
                }
            }

            int smallestVal = Integer.MAX_VALUE;
            selectedCardIndex = -1;

            for (int i = 0; i < listCard.size(); i++){
                int num = listCard.get(i).getNumber();
                if (num < smallestVal && num > 0){
                    smallestVal = num;
                    selectedCardIndex = i;
                }
            }

            // VERY EXTREME CASE where if the 4 rows have 5 cards and that he doesn't have a good play
            if (selectedCardIndex == -1){
                for (int i = 0; i < aiPlayerHand.size(); i++){
                    int num = listCard.get(i).getNumber();
                    if (num < smallestVal && num > 0){
                        smallestVal = num;
                        selectedCardIndex = i;
                    }
                }
            }

            selectedCard = listCard.get(selectedCardIndex);

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

    private List<Card> sortDeck(List<Card> cardList){
        Collections.sort(cardList, Comparator.comparingInt(Card::getNumber));
        return cardList;
    }

    private void playGameProcess(){
        List<Card> boardPlayed = sortDeck(game.getCardsPlayed());
        List<Player> players = game.getPlayers();

        for (Card card : boardPlayed){
            //int score = updateBoard(card);
            for (Player player : players){
                if (player.getLastCardPlayed().getNumber() == card.getNumber()){
                    //player.setScore(player.getScore() + score);
                }
            }
        }
        game.getCardsPlayed().clear();
    }

    private void skipTurn() {
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.getHand().add(deck.draw());

        moveToNextPlayer();
        gameView.updatePlayers(game.getPlayers());
        gameView.updateRound(game.getRound());
        gameView.setPlayerTurn(getCurrentPlayer());

        AIPlayer aiPlayer = getCurrentPlayer() instanceof AIPlayer ? (AIPlayer) getCurrentPlayer() : null;
        if (aiPlayer != null) {
            aiPlayerPlayCard(aiPlayer);
        }
    }

    private void endGame() {
        System.out.println("Le jeu est fini");
        endGameView.setScores(game.getPlayers());
        endGameView.setWinner(findWinner());
        sceneManager.switchToScene("endGame");
    }

    private Player findWinner() {
        Player winner = null;
        int minScore = Integer.MAX_VALUE;

        for (Player player : game.getPlayers()) {
            if (player.getScore() < minScore) {
                minScore = player.getScore();
                winner = player;
            }
        }

        return winner;
    }

    private void restartGame() {
        reset();
        deck.shuffle();
        dealCards();

        gameView.updatePlayers(game.getPlayers());
        gameView.updateBoard(game.getBoard());
        gameView.updateRound(game.getRound());
        gameView.updateTotalBullHeads(game.getTotalBullHeads());
        gameView.setPlayerTurn(getCurrentPlayer());

        sceneManager.switchToScene("game");
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
    public void reset() {
        game.getPlayers().clear(); // To check because of get method, not sure if it modifies
        game.getCardsPlayed().clear(); // To check because of get method, not sure if it modifies
        game.getBoard().clear(); // To check because of get method, not sure if it modifies
        game.boardSetUp(deck);
        game.setRound(1);
        game.setTotalBullHeads(0);
        game.setGameEnded(false);
    }

    public void updateBoard(List<Card> cardList) {
        int score = 0;
        cardList = sortDeck(cardList);

        for (Card card: cardList) {
            ArrayList<List<Card>> board = game.getBoard();
            int lastCardDiff = Integer.MAX_VALUE;
            int selectedRowIndex = -1;
            List<Card> selectedRow;
            for(int i = 0; i < board.size(); i ++){
                List<Card> row = board.get(i);
                int lastCardNumber = row.get(row.size() - 1).getNumber();
                int cardDiff = card.getNumber() - lastCardNumber;
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
                for (List<Card> row : board) {
                    int numberOfHead = 0;
                    for (Card cardInRow : row) {
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

    // --------------- Algo de tri ---------------

    // Algo pour trouver l'index de la valeur la plus petite d'une liste, positive. Ajout excep qd pas de jeu possible.
    protected int indexOfSmallest(List<Integer> array){
        if (array.size() == 0)
            return -1;
        // Condition initial
        int index = -1;
        int min = 104;
        for (int i = 0; i < array.size(); i++){
            if (array.get(i) <= min && array.get(i) > 0){
                min = array.get(i);
                index = i;
            }
        }
        return index;
    }

    // Algo pour trouver l'index de la valeur la plus grosse d'une liste, pour AI
    protected int indexOfBiggest(List<Integer> array){
        if (array.size() == 0)
            return -1;
        // Condition initial
        int index = 0;
        int max = 0;
        for (int i = 0; i < array.size(); i++){
            if (array.get(i) >= max){
                max = array.get(i);
                index = i;
            }
        }
        return index;
    }

    // Algo pour trouver la valeur la plus petite d'une liste, positive. Ajout excep qd pas de jeu possible.
    protected int smallestPos(List<Integer> array){
        if (array.size() == 0)
            return -1;
        // Condition initial
        int min = 104;
        for (int i = 0; i < array.size(); i++){
            if (array.get(i) <= min && array.get(i) > 0){
                min = array.get(i);
            }
        }
        return min;
    }

    private boolean checkEndTurn(){
        if (game.getCardsPlayed().size() == game.getPlayers().size()) {
            incrementRound();
            this.updateBoard(game.getCardsPlayed());
            game.resetCardsPlayed();

            gameView.updateBoard(game.getBoard());
            gameView.updatePlayers(game.getPlayers());
            gameView.updateTotalBullHeads(game.getTotalBullHeads());

            if (game.getRound() == numCardsPerPlayer + 1) {
                endGame();
                return true;
            }
        }
        return false;
    }



}
