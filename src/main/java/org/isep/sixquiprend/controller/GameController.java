package org.isep.sixquiprend.controller;

import javafx.application.Platform;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.Game;
import org.isep.sixquiprend.model.player.AIPlayer;
import org.isep.sixquiprend.model.player.HumanPlayer;
import org.isep.sixquiprend.model.player.Player;
import org.isep.sixquiprend.view.GUI.SceneManager;
import org.isep.sixquiprend.view.GUI.scenes.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameController {
    private final SceneManager sceneManager;
    private final WelcomeView welcomeView;
    private final GameView gameView;
    private final EndGameView endGameView;
    private final LobbyView lobbyView;
    private final LoadingView loadingView;
    private final Game game;
    private int numberOfAIPlayer = 0;
    private Client client = null;
    private String playerName;
    private List<List<Object>> onlineRoundInfo = new ArrayList<>();
    private final CardController cardController;
    private final PlayerController playerController;

    public GameController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.welcomeView = new WelcomeView();
        this.gameView = new GameView();
        this.endGameView = new EndGameView();
        this.lobbyView = new LobbyView();
        this.loadingView = new LoadingView();
        this.game = new Game();
        this.cardController = new CardController();
        this.playerController = new PlayerController();
        eventListener();
    }

    private void eventListener() {
        sceneManager.addScene("welcome", welcomeView.getScene());
        sceneManager.addScene("game", gameView.getScene());
        sceneManager.addScene("endGame", endGameView.getScene());
        sceneManager.addScene("lobby", lobbyView.getScene());
        sceneManager.addScene("loading", loadingView.getScene());

        welcomeView.getButtonPlay().setOnAction(event -> {
            if (game.getPlayers().size() > 1) {
                startGame();
            }
        });
        welcomeView.getButtonAjouter().setOnAction(event -> addPlayer());
        welcomeView.getButtonOnline().setOnAction(event -> {
                    sceneManager.switchToScene("lobby");
                    playOnline();
                });
        welcomeView.getButtonAjouterAIEasy().setOnAction(event -> addAIPlayerEasy());
        welcomeView.getButtonAjouterAIMedium().setOnAction(event -> addAIPlayerMedium());
        welcomeView.getButtonAjouterAIHard().setOnAction(event -> addAIPlayerHard());
        gameView.getPlayButton().setOnAction(event -> playCard());
        endGameView.getRestartButton().setOnAction(event -> {
            this.numberOfAIPlayer = 0;
            game.getPlayers().clear();
            welcomeView.resetPlayerList();
            sceneManager.switchToScene("welcome");
        });
        endGameView.getQuitButton().setOnAction(event -> {
            if (null != client) {
                client.closeConnection();
            }
            quitGame();
        });

        lobbyView.getQuitButton().setOnAction(event -> {
            sceneManager.switchToScene("welcome");
            client.closeConnection();
        });
        lobbyView.getPlayButton().setOnAction(event ->  {
            if (game.getPlayers().size() > 1 ){
                client.sendMessageToServer("START_GAME");
                startGame();
            }
        });
        loadingView.getContinueButton().setOnAction(event -> {
            sceneManager.switchToScene("game");
            playCard();
        });
    }


    private void startGame() {

        List<Player> provisionalPlayers = game.getPlayers();
        if (null == client){
            List<String> realPlayers = welcomeView.getPlayerList();
            provisionalPlayers.removeIf(player -> !realPlayers.contains(player.getName()));
        }

        game.setPlayers(provisionalPlayers);
        this.setup();

        cardController.shuffle();
        this.boardSetUp(cardController);
        dealCards();

        if (null != client){
            this.sendGameInfo(false);
        }
        else {
            gameView.updatePlayers(game.getPlayers());
            gameView.updateBoard(game.getBoard());
            gameView.updateRound(game.getRound());
            Player currentPlayer = getCurrentPlayer();
            gameView.setPlayerTurn(currentPlayer);

            sceneManager.switchToScene("game");
            nextPlayer();
        }
    }

    public void boardSetUp(CardController cardController) {
        for (int i = 0; i < 4; i++) {
            game.getBoard().add(new ArrayList<>());
            Card card = cardController.draw();
            game.getBoard().get(i).add(card);
        }
    }

    private void resetCardsPlayed(){
        game.setCardsPlayed(new ArrayList<>());
    }

    private void nextPlayer(){
        HumanPlayer humanPlayer = getCurrentPlayer() instanceof HumanPlayer ? (HumanPlayer) getCurrentPlayer() : null;
        AIPlayer aiPlayer = getCurrentPlayer() instanceof AIPlayer ? (AIPlayer) getCurrentPlayer() : null;
        if (aiPlayer != null) {
            String diff = aiPlayer.getDiff();
            switch (diff){
                case "easy" -> aiPlayerPlayCardEasy(aiPlayer);
                case "medium" -> aiPlayerPlayCardMedium(aiPlayer);
                case "hard" -> aiPlayerPlayCardHard(aiPlayer);
            }
        } else if (humanPlayer != null) {
            int countHuman = 0;
            for (Object e : game.getPlayers())
            {
                if (e instanceof HumanPlayer)
                {
                    countHuman++;
                }
            }
            if (countHuman < 2){
                playCard();
            } else {
                loadingView.setConcernedPlayer(humanPlayer.getName());
                sceneManager.switchToScene("loading");
            }
        }
    }

    private void dealCards() {
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            player.setHand(cardController.drawHand());
        }
    }

    private void playCard() {
        if (null != client){
            Card playedCard = gameView.getSelectedCard();
            if (playedCard != null) {
                List<Object> cardPlayedBuilder =  new ArrayList<>();
                cardPlayedBuilder.add("_CARDPLAYED_");
                cardPlayedBuilder.add(this.playerName);
                cardPlayedBuilder.add(playedCard.getNumber());
                client.sendMessageToServer(cardPlayedBuilder);
            }
        }
        else {
            Player currentPlayer = getCurrentPlayer();
            List<Card> currentHand = getCurrentPlayer().getHand();
            Card playedCard = gameView.getSelectedCard();

            endSelectedCardProcess(currentPlayer, currentHand, playedCard);
        }
    }

    private void aiPlayerPlayCardEasy(AIPlayer aiPlayer) {
        List<Card> aiPlayerHand = aiPlayer.getHand();
        if (aiPlayerHand.size() > 0) {
            fetchSelectedCardProcess(aiPlayer, aiPlayerHand, cardController.AICardCalculation(game.getBoard(), aiPlayerHand));
        }
    }

    private void fetchSelectedCardProcess(AIPlayer aiPlayer, List<Card> aiPlayerHand, int selectedCardIndex) {
        Card selectedCard;
        selectedCard = aiPlayerHand.get(selectedCardIndex);
        endSelectedCardProcess(aiPlayer, aiPlayerHand, selectedCard);
    }

    private void endSelectedCardProcess(Player Player, List<Card> PlayerHand, Card selectedCard) {
        if (selectedCard != null) {
            Player.setLastCardPlayed(selectedCard);
            PlayerHand.remove(selectedCard);
            game.getCardsPlayed().add(selectedCard);


            if (checkEndTurn()) {
                return;
            }

            moveToNextPlayer();
            gameView.updatePlayers(game.getPlayers());
            gameView.updateRound(game.getRound());
            gameView.setPlayerTurn(getCurrentPlayer());

            nextPlayer();
        }
    }


    private void aiPlayerPlayCardMedium(AIPlayer aiPlayer) {
        List<Card> aiPlayerHand = aiPlayer.getHand();
        if (aiPlayerHand.size() > 0) {
            Card selectedCard;
            List<List<Card>> board = game.getBoard();
            int selectedCardIndex = -1;


            // Does lowest value card to rows that are "playable" and without any penalties based on rows
            List<Integer> eligibleRows = getEligibleRow(board);

            if (!eligibleRows.isEmpty()){

                List<List<Card>> temp_board = new ArrayList<>();
                for (Integer eligibleRow : eligibleRows) {
                    List<Card> row = board.get(eligibleRow);
                    temp_board.add(row);
                }

                selectedCardIndex = cardController.AICardCalculation(temp_board, aiPlayerHand);

            } else {
                selectedCardIndex = cardController.extremeCaseNoPlayableRows(aiPlayerHand, selectedCardIndex);
            }

            selectedCard = aiPlayerHand.get(selectedCardIndex);
            endSelectedCardProcess(aiPlayer, aiPlayerHand, selectedCard);
        }
    }

    private List<Integer> getEligibleRow(List<List<Card>> board) {
        List<Integer> eligibleRows = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            List<Card> row = board.get(i);
            if (row.size() < 5){
                eligibleRows.add(i);
            }
        }
        return eligibleRows;
    }

    private void aiPlayerPlayCardHard(AIPlayer aiPlayer) {
        List<Card> aiPlayerHand = aiPlayer.getHand();
        if (aiPlayerHand.size() > 0) {
            List<List<Card>> board = game.getBoard();
            int selectedCardIndex = -1;

            List<Integer> eligibleRows = getEligibleRow(board);

            if (!eligibleRows.isEmpty()){

                List<List<Card>> temp_board = new ArrayList<>();
                for (Integer eligibleRow : eligibleRows) {
                    List<Card> row = board.get(eligibleRow);
                    temp_board.add(row);
                }

                selectedCardIndex = cardController.AICardCalculationHard(temp_board, aiPlayerHand);

                // in the EXTREME case where all 4 rows have all 5 cards and there's sadly no eligible rows without
            } else {
                selectedCardIndex =  cardController.extremeCaseNoPlayableRows(aiPlayerHand, selectedCardIndex);
            }

            fetchSelectedCardProcess(aiPlayer, aiPlayerHand, selectedCardIndex);
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
        cardController.newDeck();
        game.getCardsPlayed().clear();
        game.setBoard(new ArrayList<>());
        game.setRound(1);
        game.setGameEnded(false);
        game.setCurrentPlayerIndex(0);
    }

    public void updateBoard(List<Card> cardList) {
        cardList = cardController.sortCardList(cardList);

        for (Card card : cardList) {
            int score = 0;
            int selectedRowIndex = cardController.findSelectedRowIndex(card, game.getBoard());

            if (selectedRowIndex != -1) {
                List<Card> selectedRow = game.getBoard().get(selectedRowIndex);

                if (selectedRow.size() >= 5) {
                    score = cardController.calculateScore(selectedRow);
                    selectedRow.clear();
                }
            } else {
                selectedRowIndex = cardController.findSelectedRowIndexForNonPlayableCard(game.getBoard());

                if (selectedRowIndex != -1) {
                    List<Card> selectedRow = game.getBoard().get(selectedRowIndex);
                    score = cardController.calculateScore(selectedRow);
                    selectedRow.clear();
                }
            }

            addToSelectedRow(card, selectedRowIndex);

            if (score > 0) {
                updatePlayerScores(card, score);
            }
        }
    }


    private void addToSelectedRow(Card card, int selectedRowIndex) {
        List<Card> selectedRow = game.getBoard().get(selectedRowIndex);
        selectedRow.add(card);
        game.getBoard().set(selectedRowIndex, selectedRow);
    }

    private void updatePlayerScores(Card card, int score) {
        if (null != client) {
            for (List<Object> info : this.onlineRoundInfo) {
                if (info.get(1).equals(card)) {
                    Player player = (Player) info.get(0);
                    if (player != null) {
                        player.setScore(player.getScore() + score);
                    } else {
                        System.out.println("Player not found");
                    }
                }
            }
        }
        else {
            for (Player player : game.getPlayers()){
                if (player.getLastCardPlayed().getNumber() == card.getNumber()){
                    player.setScore(player.getScore() + score);
                }
            }
        }
    }


    private boolean checkEndTurn(){
        if (game.getCardsPlayed().size() == game.getPlayers().size()) {
            incrementRound();
            this.updateBoard(game.getCardsPlayed());
            this.resetCardsPlayed();

            if (client != null){
                //Find link and remove card in player hand
                for (List<Object> info : this.onlineRoundInfo) {
                    Player player = (Player) info.get(0);
                    Card card = (Card) info.get(1);
                    if (player != null && card != null) {
                        player.getHand().remove(card);
                    } else {
                        System.out.println("Hand not found");
                    }
                }

                onlineRoundInfo.clear();
                this.sendGameInfo(false);

                if (game.getRound() == cardController.getNumCardsPerPlayer() + 1) {
                    this.sendGameInfo(true);
                }
            }

            else {
                gameView.updateBoard(game.getBoard());
                gameView.updatePlayers(game.getPlayers());

                if (game.getRound() == cardController.getNumCardsPerPlayer() + 1) {
                    endGame();
                    return true;
                }
            }
        }
        return false;
    }

    private void addPlayer(){
        if (game.getPlayers().size() < 10) {
            String playerName = welcomeView.getPlayerName();
            if (!playerName.equalsIgnoreCase("") && !checkAlreadyUsedName(playerName)) {
                HumanPlayer humanPlayer = new HumanPlayer(playerName);
                game.getPlayers().add(humanPlayer);
                welcomeView.addNameToPlayerList(playerName);
                welcomeView.setPlayerNameTextField("");
            }
        }
    }

    private void addAIPlayerEasy() {
        if (game.getPlayers().size() < 10) {
            this.numberOfAIPlayer++;
            AIPlayer aiPlayer = new AIPlayer("AI " + numberOfAIPlayer + ": Facile", "easy");
            game.getPlayers().add(aiPlayer);
            welcomeView.addNameToPlayerList(aiPlayer.getName());
        }
    }

    private void addAIPlayerMedium() {
        if (game.getPlayers().size() < 10) {
            this.numberOfAIPlayer++;
            AIPlayer aiPlayer = new AIPlayer("AI " + numberOfAIPlayer + ": Moyen", "medium");
            game.getPlayers().add(aiPlayer);
            welcomeView.addNameToPlayerList(aiPlayer.getName());
        }
    }

    private void addAIPlayerHard() {
        if (game.getPlayers().size() < 10){
            this.numberOfAIPlayer ++;
            AIPlayer aiPlayer = new AIPlayer("AI " + numberOfAIPlayer + ": Dure", "hard");
            game.getPlayers().add(aiPlayer);
            welcomeView.addNameToPlayerList(aiPlayer.getName());
        }
    }

    private boolean checkAlreadyUsedName(String name){
        return playerController.isNameUsed(name,  game.getPlayers());
    }

    private void playOnline() {
        this.client = new Client(this);
        client.connectToServer();
        client.sendMessageToServer("SET_PLAYERNAME");
        client.sendMessageToServer(welcomeView.getPlayerName());
    }

    public void updateOnlinePlayerList(List<String> list) {
        List<Player> players = playerController.createPlayerListFromString(list);
        game.setPlayers(players);
        lobbyView.setPlayers(players);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        System.out.println("Je suis le joueur " + playerName);
    }

    public void setGameHost(String host){
        boolean gameHost = Objects.equals(this.playerName, host);
        lobbyView.setHost(gameHost);
    }

    public void onlineChangeView(String viewName) {
        sceneManager.switchToScene(viewName);
    }



    public void onlineUpdatePlayerCard(List<Integer> playerCard) {
        Platform.runLater(() -> {
            gameView.updateCards(cardController.findCardByNumberInList(playerCard));
        });
    }

    public void onlineUpdateBoard(List<List<Integer>> boardInfo) {
        List<List<Card>> cardList = new ArrayList<>();
        List<Card> deckCards = cardController.fillDeck();
        for (List<Integer> row : boardInfo) {
            List<Card> cardsRow = new ArrayList<>();
            for (Integer cardNumber : row) {
                for (Card card : deckCards) {
                    if (card.getNumber() == cardNumber) {
                        cardsRow.add(card);
                        break;
                    }
                }
            }
            cardList.add(cardsRow);
        }

        Platform.runLater(() -> {
            gameView.updateBoard(cardList);
        });
    }

    public void onlineUpdateRound(int round) {
        Platform.runLater(() -> {
            gameView.updateRound(round);
        });
    }

    public void onlineHandlePlayerInfo(List<List<?>> playerInfo) {
        StringBuilder playerNames = new StringBuilder();
        for (List<?> player : playerInfo) {
            playerNames.append(player.get(0)).append(" | score : ").append(player.get(1)).append("\n");
        }
        gameView.setPlayerText(playerNames.toString());
    }

    public void setGameCartPlayed(List<List<Object>> roundInfo) {
        boolean isPlayed = false;
        for (int i = 0; i < roundInfo.size(); i++) {
            List<Object> tempInfo = new ArrayList<>();
            String playerName = (String) roundInfo.get(i).get(0);
            Player player = playerController.findPlayerByName(playerName, game.getPlayers());
            if (player != null) {
                tempInfo.add(player);
            } else {
                System.out.println("player not found");
                // Handle the case where the player is not found
                // You can throw an exception or handle it as per your requirements
            }
            Card card = cardController.findCardByNumber((int) roundInfo.get(i).get(1));
            for (Card cardPlayed : game.getCardsPlayed()) {
                assert card != null;
                if (cardPlayed.getNumber() == card.getNumber()){
                    isPlayed = true;
                    break;
                }
            }
            if (!isPlayed){
                game.getCardsPlayed().add(card);
                tempInfo.add(card);
                roundInfo.set(i, tempInfo);
            }
        }
        if (!isPlayed){
            this.onlineRoundInfo = roundInfo;
            checkEndTurn();
        }
    }


    private void sendGameInfo(boolean endGame) {
        List<Object> gameInfo = new ArrayList<>();

        if (!endGame) {
            gameInfo.add("_GAMEINFO_");
        } else {
            gameInfo.add("_ENDGAME_");
        }

        List<List<Integer>> boardOnline = game.getBoard().stream()
                .map(row -> row.stream().map(Card::getNumber).collect(Collectors.toList()))
                .toList();
        gameInfo.add("_BOARD_");
        gameInfo.addAll(boardOnline);

        gameInfo.add("_ROUND_");
        gameInfo.add(game.getRound());

        gameInfo.add("_PLAYERS_");
        List<List<?>> playerList = game.getPlayers().stream()
                .map(player -> {
                    List<Integer> playerHand = player.getHand().stream()
                            .map(Card::getNumber)
                            .collect(Collectors.toList());

                    List<Object> playerInfo = new ArrayList<>();
                    playerInfo.add(player.getName());
                    playerInfo.add(playerHand);
                    playerInfo.add(player.getScore());
                    playerInfo.add(player.getLastCardPlayed() != null ? player.getLastCardPlayed().getNumber() : 0);

                    return playerInfo;
                })
                .collect(Collectors.toList());
        gameInfo.addAll(playerList);

        client.sendMessageToServer(gameInfo);
    }

    public void setOnlineEndGameView(List<List<Object>> playerInfo){
        Platform.runLater(() -> {
            StringBuilder playerNames = new StringBuilder();

            playerInfo.sort(Comparator.comparing(player -> (int) player.get(1)));

            List<Object> winner = playerInfo.get(0);

            for (List<?> player : playerInfo) {
                playerNames.append(player.get(0)).append(" | score : ").append(player.get(1)).append("\n");
            }

            endGameView.setPlayerText(playerNames.toString());
            endGameView.setWinnerText((String) winner.get(0));
            sceneManager.switchToScene("endGame");
            client.closeConnection();
        });
    }
}