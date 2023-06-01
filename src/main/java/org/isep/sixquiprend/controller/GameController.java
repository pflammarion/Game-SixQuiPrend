package org.isep.sixquiprend.controller;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.Deck;
import org.isep.sixquiprend.model.Game;
import org.isep.sixquiprend.model.player.AIPlayer;
import org.isep.sixquiprend.model.player.HumanPlayer;
import org.isep.sixquiprend.model.player.Player;
import org.isep.sixquiprend.view.GUI.SceneManager;
import org.isep.sixquiprend.view.GUI.scenes.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameController {
    private final SceneManager sceneManager;
    private final WelcomeView welcomeView;
    private final GameView gameView;
    private final EndGameView endGameView;
    private final LobbyView lobbyView;
    private final LoadingView loadingView;
    private final Game game;
    private Deck deck;
    private final int numCardsPerPlayer = 10;
    private int numberOfAIPlayer = 0;
    private Client client = null;
    private String playerName;
    private boolean gameHost;
    private List<List<Object>> onlineRoundInfo = new ArrayList<>();

    public GameController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.welcomeView = new WelcomeView();
        this.gameView = new GameView();
        this.endGameView = new EndGameView();
        this.lobbyView = new LobbyView();
        this.loadingView = new LoadingView();
        this.game = new Game();
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
        welcomeView.getButtonDelete().setOnAction(event -> deletePlayer());
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

        this.setup();

        deck.shuffle();
        game.boardSetUp(deck);
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
            selectedCardIndex = conditionSelectedCardIndex(aiPlayerHand, selectedCardIndex);

            fetchSelectedCardProcess(aiPlayer, aiPlayerHand, selectedCardIndex);
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

    private int conditionSelectedCardIndex(List<Card> aiPlayerHand, int selectedCardIndex) {
        if (selectedCardIndex == -1) {
            selectedCardIndex = extremeCaseNoPlayableRows(aiPlayerHand, selectedCardIndex);
        }
        return selectedCardIndex;
    }

    private void aiPlayerPlayCardMedium(AIPlayer aiPlayer) {
        List<Card> aiPlayerHand = aiPlayer.getHand();
        if (aiPlayerHand.size() > 0) {
            Card selectedCard;
            List<Integer> tempStore = new ArrayList<>();
            List<List<Card>> board = game.getBoard();
            int smallestDiff = Integer.MAX_VALUE;
            int selectedCardIndex = -1;
            int lowestRowValue = Integer.MAX_VALUE;
            int i;


            // Does lowest value card to rows that are "playable" and without any penalties based on rows
            List<Integer> eligibleRows = new ArrayList<>();
            for (i = 0; i < board.size(); i++) {
                List<Card> row = board.get(i);
                if (row.size() < 5){
                    eligibleRows.add(i);
                }
            }

            if (!eligibleRows.isEmpty()){
                for (i = 0; i < eligibleRows.size(); i++){
                    List<Card> row = board.get(eligibleRows.get(i));
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

                for (i = 0; i < tempStore.size(); i++) {
                    int currentDiff = tempStore.get(i);
                    if (currentDiff < smallestDiff && currentDiff > 0) {
                        smallestDiff = currentDiff;
                        selectedCardIndex = i;
                    }
                }
                // Take the lowest card of the player hand
                selectedCardIndex = conditionSelectedCardIndex(aiPlayerHand, selectedCardIndex);
                // in the EXTREME case where all 4 rows have all 5 cards and there's sadly no eligible rows without
            // penalty
            } else {
                selectedCardIndex = extremeCaseNoPlayableRows(aiPlayerHand, selectedCardIndex);
            }

            selectedCard = aiPlayerHand.get(selectedCardIndex);

            endSelectedCardProcess(aiPlayer, aiPlayerHand, selectedCard);
        }
    }

    private int extremeCaseNoPlayableRows(List<Card> aiPlayerHand, int selectedCardIndex) {
        int minValue = Integer.MAX_VALUE;
        int currentIndex = 0;
        for (Card card : aiPlayerHand) {
            if (card.getNumber() < minValue) {
                minValue = card.getNumber();
                selectedCardIndex = currentIndex;
            }
            currentIndex++;
        }
        return selectedCardIndex;
    }

    private void aiPlayerPlayCardHard(AIPlayer aiPlayer) {
        List<Card> aiPlayerHand = aiPlayer.getHand();
        if (aiPlayerHand.size() > 0) {
            List<List<Card>> board = game.getBoard();
            int smallestDiff;
            int selectedCardIndex = -1;
            int bestRow = -1;
            List<Integer> latestRowValue = new ArrayList<>();
            List<List<Integer>> diffLatestRowValue = new ArrayList<>();
            List<Integer> lowestEachRow = new ArrayList<>();
            int i;

            // Does lowest Diff to rows that are "playable" and without any penalties based on rows
            List<Integer> eligibleRows = new ArrayList<>();
            for (i = 0; i < board.size(); i++) {
                List<Card> row = board.get(i);
                if (row.size() < 5){
                    eligibleRows.add(i);
                }
            }

            if (!eligibleRows.isEmpty()){
                for (i = 0; i < eligibleRows.size(); i++){
                    List<Card> row = board.get(eligibleRows.get(i));
                    int lastCardNumber = row.get(row.size() - 1).getNumber();
                    latestRowValue.add(lastCardNumber);
                }

                for (i = 0; i < latestRowValue.size(); i++){
                    List<Integer> tempStore = new ArrayList<>();
                    int rowValue = latestRowValue.get(i);
                    for (Card card : aiPlayerHand) {
                        int cardNumber = card.getNumber();
                        int diff = cardNumber - rowValue;
                        tempStore.add(diff);
                    }
                    diffLatestRowValue.add(tempStore);
                }

                for (i = 0; i < diffLatestRowValue.size(); i++) {
                    List<Integer> rowDiff = diffLatestRowValue.get(i);
                    smallestDiff = Integer.MAX_VALUE;  // Reset smallestDiff for each row
                    for (int j = 0; j < rowDiff.size(); j++) {
                        int currentDiff = rowDiff.get(j);
                        if (currentDiff < smallestDiff && currentDiff > 0) {
                            smallestDiff = currentDiff;
                        }
                    }
                    lowestEachRow.add(smallestDiff);
                }

                smallestDiff = Integer.MAX_VALUE;  // Reset smallestDiff

                for (i = 0; i < lowestEachRow.size(); i++) {
                    int currentDiff = lowestEachRow.get(i);
                    if (currentDiff < smallestDiff && currentDiff > 0) {
                        smallestDiff = currentDiff;
                        bestRow = i;
                    }
                }

                if (bestRow != -1){
                    List<Integer> chosenRowDiff = diffLatestRowValue.get(bestRow);
                    selectedCardIndex = chosenRowDiff.indexOf(smallestDiff);
                }
                else {
                    // Take the lowest card of the player hand
                    selectedCardIndex = extremeCaseNoPlayableRows(aiPlayerHand, selectedCardIndex);
                }


                // in the EXTREME case where all 4 rows have all 5 cards and there's sadly no eligible rows without
                // penalty
            } else {
                selectedCardIndex = extremeCaseNoPlayableRows(aiPlayerHand, selectedCardIndex);
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
        this.deck = new Deck(fillDeck());
        game.getCardsPlayed().clear();
        game.setBoard(new ArrayList<>());
        game.setRound(1);
        game.setGameEnded(false);
        game.setCurrentPlayerIndex(0);
    }

    public void updateBoard(List<Card> cardList) {
        cardList.sort(Comparator.comparingInt(Card::getNumber));

        for (Card card: cardList) {
            int score = 0;
            int lastCardDiff = Integer.MAX_VALUE;
            int selectedRowIndex = -1;
            ArrayList<List<Card>> board = game.getBoard();
            List<Card> selectedRow;

            for(int i = 0; i < board.size(); i ++){
                List<Card> row = board.get(i);
                int lastCardNumber = row.get(row.size() - 1).getNumber();
                int cardDiff = card.getNumber() - lastCardNumber;
                if (cardDiff > 0 && lastCardDiff > cardDiff){
                    selectedRowIndex = i;
                    lastCardDiff = cardDiff;
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
                List<Integer> tempNumHeads= new ArrayList<>();
                for (List<Card> row : board) {
                    int numberOfHead = 0;
                    for (Card cardInRow : row) {
                        numberOfHead += cardInRow.getBullHeads();
                    }
                    tempNumHeads.add(numberOfHead);
                }
                int minNumberOfHead = Collections.min(tempNumHeads);

                List<Integer> indexesMin = new ArrayList<>();

                for (int i = 0; i<tempNumHeads.size();i++){
                    if (minNumberOfHead == tempNumHeads.get(i)){
                        indexesMin.add(i);
                    }
                }

                if (indexesMin.size() == 1){
                    selectedRowIndex = indexesMin.get(0);
                } else {

                    List<Integer> latestCardRow = new ArrayList<>();
                    for (int i = 0; i < indexesMin.size(); i++){
                        List<Card> row = board.get(indexesMin.get(i));
                        latestCardRow.add(row.get(row.size() - 1).getNumber());
                    }

                    int highestNumber = Integer.MIN_VALUE;
                    int highestIndex = -1;

                    for (int i = 0; i < latestCardRow.size(); i++) {
                        int currentNumber = latestCardRow.get(i);
                        if (currentNumber > highestNumber) {
                            highestNumber = currentNumber;
                            highestIndex = i;
                        }
                    }

                    selectedRowIndex = indexesMin.get(highestIndex);

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

        }
    }

    private boolean checkEndTurn(){
        if (game.getCardsPlayed().size() == game.getPlayers().size()) {
            incrementRound();
            this.updateBoard(game.getCardsPlayed());
            game.resetCardsPlayed();

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

                if (game.getRound() == numCardsPerPlayer + 1) {
                    this.sendGameInfo(true);
                }
            }

            else {
                gameView.updateBoard(game.getBoard());
                gameView.updatePlayers(game.getPlayers());

                if (game.getRound() == numCardsPerPlayer + 1) {
                    endGame();
                    return true;
                }
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

    public void deletePlayer(){
        int index = welcomeView.getPlayerListText().getSelectionModel().getSelectedIndex();
        game.getPlayers().remove(index);
        welcomeView.removeNameToPlayerList(index);
    }

    private void addAIPlayerEasy() {
        this.numberOfAIPlayer ++;
        AIPlayer aiPlayer = new AIPlayer("AI " + numberOfAIPlayer + ": Facile", "easy");
        game.getPlayers().add(aiPlayer);
        welcomeView.addNameToPlayerList(aiPlayer.getName());
    }

    private void addAIPlayerMedium() {
        this.numberOfAIPlayer ++;
        AIPlayer aiPlayer = new AIPlayer("AI " + numberOfAIPlayer + ": Moyen", "medium");
        game.getPlayers().add(aiPlayer);
        welcomeView.addNameToPlayerList(aiPlayer.getName());
    }

    private void addAIPlayerHard() {
        this.numberOfAIPlayer ++;
        AIPlayer aiPlayer = new AIPlayer("AI " + numberOfAIPlayer + ": Dure", "hard");
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
        for (String s : list) {
            players.add(new HumanPlayer(s));
        }
        game.setPlayers(players);
        lobbyView.setPlayers(players);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        System.out.println("Je suis le joueur " + playerName);
    }

    public void setGameHost(String host){
        this.gameHost = Objects.equals(this.playerName, host);
        lobbyView.setHost(this.gameHost);
    }

    public void onlineChangeView(String viewName) {
        sceneManager.switchToScene(viewName);
    }

    private List<Card> findCardByNumberInList(List<Integer> cardListToFind) {
        List<Card> cardList = new ArrayList<>();
        List<Card> deckCards = fillDeck();
        for (int cardNumber : cardListToFind) {
            for (Card card : deckCards){
                if (card.getNumber() == cardNumber) {
                    cardList.add(card);
                    break;
                }
            }
        }
        return cardList;
    }

    private Card findCardByNumber(int number){
        List<Card> deckCards = fillDeck();
        for (Card card : deckCards){
            if (card.getNumber() == number) {
                return card;
            }
        }
        return null;
    }

    private Player findPlayerByName(String name){
        for (Player player : game.getPlayers()){
            if (player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    public void onlineUpdatePlayerCard(List<Integer> playerCard) {
        Platform.runLater(() -> {
            gameView.updateCards(findCardByNumberInList(playerCard));
        });
    }

    public void onlineUpdateBoard(List<List<Integer>> boardInfo) {
        List<List<Card>> cardList = new ArrayList<>();
        List<Card> deckCards = fillDeck();
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
            Player player = findPlayerByName(playerName);
            if (player != null) {
                tempInfo.add(player);
            } else {
                System.out.println("player not found");
                // Handle the case where the player is not found
                // You can throw an exception or handle it as per your requirements
            }
            Card card = findCardByNumber((int) roundInfo.get(i).get(1));
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