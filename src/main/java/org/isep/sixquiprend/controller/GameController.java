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

import java.util.ArrayList;
import java.util.List;

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
        fillDeck();
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

    private void fillDeck() {
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
        this.deck = new Deck(cards);
    }

    private void startGame() {
        String playerName = welcomeView.getPlayerName();
        HumanPlayer humanPlayer = new HumanPlayer(playerName);

        List<AIPlayer> aiPlayers = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            AIPlayer aiPlayer = new AIPlayer("AI");
            aiPlayers.add(aiPlayer);
        }

        game.getPlayers().add(humanPlayer);
        game.getPlayers().addAll(aiPlayers);
        game.boardSetUp();

        Player currentPlayer = getCurrentPlayer();

        deck.shuffle();
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
            player.setHand(cards);
        }
    }

    private void playCard() {
        Player currentPlayer = getCurrentPlayer();
        List<Object> playedCard = gameView.getSelectedCard();

        if (playedCard != null && !playedCard.isEmpty() && playedCard.get(0) != null) {
            currentPlayer.getHand().remove((Card) playedCard.get(0));
            game.getCardsPlayed().add((Card) playedCard.get(0));

            if (game.getCardsPlayed().size() / game.getRound() == game.getPlayers().size()) {
                incrementRound();
                gameView.updateBoard(game.getBoard());
                gameView.updateTotalBullHeads(game.getTotalBullHeads());

                if (game.getRound() == numCardsPerPlayer) {
                    endGame();
                    return;
                }
            }
            currentPlayer.setScore(updateBoard(playedCard));

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
        List<Card> cardsPlayed = game.getCardsPlayed();

        int minDifference = Integer.MAX_VALUE;
        Card selectedCard = null;

        // TODO à faire en fonction du board et non des cartes jouées

        for (Card card : aiPlayerHand) {
            int cardNumber = card.getNumber();
            int lastCardNumber = cardsPlayed.get(cardsPlayed.size() - 1).getNumber();
            int difference = Math.abs(cardNumber - lastCardNumber);

            if (difference < minDifference) {
                minDifference = difference;
                selectedCard = card;
            }
        }

        if (selectedCard != null) {
            aiPlayerHand.remove(selectedCard);
            game.getCardsPlayed().add(selectedCard);

            if (game.getCardsPlayed().size() / game.getRound() == game.getPlayers().size()) {
                incrementRound();
                gameView.updateBoard(game.getBoard());
                gameView.updateTotalBullHeads(game.getTotalBullHeads());

                if (game.getRound() == numCardsPerPlayer) {
                    endGame();
                    return;
                }
            }

            int chosenIndex = 0;
            for (List<Card> lists : game.getBoard()) {
                if (lists.isEmpty() || (lists.get(lists.size() - 1).getNumber() < selectedCard.getNumber())) {
                    break;
                }
                chosenIndex++;
                if (chosenIndex > 3) {
                    // TODO faire le fait de choisir la ligne avec le moins de point
                    chosenIndex = 0;
                    break;
                }
            }

            List<Object> playedCard = new ArrayList<>();
            playedCard.add(selectedCard);
            playedCard.add(chosenIndex);

            aiPlayer.setScore(updateBoard(playedCard));

            moveToNextPlayer();
            gameView.updatePlayers(game.getPlayers());
            gameView.updateRound(game.getRound());
            gameView.setPlayerTurn(getCurrentPlayer());

            AIPlayer aiPlayerNext = getCurrentPlayer() instanceof AIPlayer ? (AIPlayer) getCurrentPlayer() : null;
            if (aiPlayerNext != null) {
                aiPlayerPlayCard(aiPlayer);
            }
        }
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
        sceneManager.switchToScene("endGame");
        endGameView.setTotalBullHeads(game.getTotalBullHeads());
        endGameView.setWinner(findWinner());
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
        game.boardSetUp();
        game.setRound(1);
        game.setTotalBullHeads(0);
        game.setGameEnded(false);
    }
    public int updateBoard(List<Object> played) {
        int score = 0;

        ArrayList<List<Card>> board = game.getBoard();
        Card playedCard = ((Card) played.get(0));
        List<Integer> diff = null;
        List<Card> selectedRow = null;

        for (List<Card> row : board) {
            diff.add(playedCard.getNumber() - row.get(row.size() - 1).getNumber());
        }

        int indexRow = indexOfSmallest(diff);

        if (indexRow != -1) {
            selectedRow = game.getBoard().get(indexRow);
        } else {
            // Using diff as the sum of the points on the whole row
            diff.clear();
            for (List<Card> row : board) {
                int sumPointsRow = 0;
                for (int i = 0; i < row.size(); i++){
                    sumPointsRow += row.get(i).getBullHeads();
                }
                diff.add(sumPointsRow);
            }
            // This should not return -1, considering the code.
            indexRow = indexOfSmallest(diff);
            selectedRow = game.getBoard().get(indexRow);
            score = diff.get(indexRow);
            selectedRow.clear();
        }

        if (selectedRow.size() < 6) {
            selectedRow.add((Card) played.get(0));
        } else {
            for (Card card : selectedRow) {
                score += card.getBullHeads();
            }
            selectedRow.clear();
        }
        board.set((Integer) played.get(1), selectedRow); // To check because of get method, not sure if it modifies

        return score;
    }

    // --------------- Algo de tri ---------------

    // Algo pour trouver l'index de la valeur la plus petite d'une liste, positive. Ajout excep qd pas de jeu possible.
    protected static int indexOfSmallest(List<Integer> array){
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
}
