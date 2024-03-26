package edu.cmu.f23qa.loveletter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameUI {
    private static int MIN_PLAYERS = 2;
    private static int MAX_PLAYERS = 8;

    private final Scanner in;

    GameUI(Scanner in) {
        this.in = in;
    }

    /**
     * The method get all the player names, an empty name indicates
     * all the player names are entered
     * @return
     *      a list of player names
     */
    List<String> getPlayers() {
        List<String> playerNames = new ArrayList<>();
        System.out.print("Enter player name (empty when done): ");
        String name = in.nextLine().trim();

        while (!canStart(name.isEmpty(), playerNames.size())) {
            if(name.isEmpty()){
                if (playerNames.size() == 0) {
                    System.out.println("No player to start the game");
                }
                if (playerNames.size() == 1) {
                    System.out.println("One player cannot start the game");
                }
            }
            else if(playerNames.contains(name)){
                System.out.println("Player is already in the game");
            } else {
                playerNames.add(name);
            }

            // Start the game if there are eight players
            // as premium edition can have a max of eight players
            if(playerNames.size() == MAX_PLAYERS) {
                break;
            }

            System.out.print("Enter player name (empty when done): ");
            name = in.nextLine().trim();
        }
        return playerNames;
    }

    /**
     * The game can start only when we get start signal and there
     * are more than one player
     * @param startSignal
     *              a true indicates a start game signal
     * @param playerCount
     *              the number of players who have registered for the game
     * @return
     */
    boolean canStart(boolean startSignal, int playerCount) {
        return startSignal && playerCount >= MIN_PLAYERS && playerCount <= MAX_PLAYERS;
    }

    /**
     * Display the name of the round winner
     * @param name
     *          name of the round winner
     */
    void showRoundWinner(String name) {
        System.out.println(name + " has won this round!");
    }

    /**
     * Display the names of the round winners
     * @param players
     *          round winners
     */
    void showRoundWinners(List<Player> players) {
        for(Player player : players) {
            showRoundWinner(player.getName());
        }
    }

    /**
     * Display the name of the game winner
     * @param name
     *          name of the game winner
     */
    void showGameWinner(String name) {
        System.out.println(name+ " has won the game and the heart of the princess!");
    }

    /**
     * Display the person's turn
     * @param name
     *          name of the person with the turn
     */
    void showPlayerTurn(String name) {
        System.out.println("\n" + name + "'s turn:");
    }

    /**
     * Useful method for obtaining a chosen target from the player list.
     * @param playerList 
     *              the list of players
     * @param user 
     *              the player choosing an opponent
     * @param syncophantFlag 
     *              flag to indicate if a player is marked with Syncophant
     * @param syncophantChosenPlayer 
     *              the player is marked by Sycophant
     * @param includeSelf 
     *              if the target player can be the player himself
     * @return 
     *              the chosen target player
     */
    Player getOpponent(PlayerList playerList, Player user, boolean syncophantFlag, Player syncophantChosenPlayer, boolean includeSelf) {
        Player opponent = null;

        if (syncophantFlag && (syncophantChosenPlayer != null)) {
            // Since Syncophant players is chosen, clear the Syncophant record.
            printForChosenSycophant(syncophantChosenPlayer);
            return syncophantChosenPlayer;
        }
        
        boolean validTarget = false;

        if (!playerList.isOpponentsAvailable(user) && !includeSelf) {
            System.out.println("No opponent available.");
            return null;
        }

        while (!validTarget) {
            System.out.print("Who would you like to target: ");
            String opponentName = in.nextLine().trim();
            opponent = playerList.getPlayer(opponentName);
            if (opponent == null) {
                System.out.println("This player is not in the game.");
            } else if (opponent.isProtected()) {
                System.out.println("This player is protected by a handmaiden.");
            } else if (opponent.getName().equals(user.getName()) && !includeSelf) {
                System.out.println("You cannot target yourself.");
            } else if (!opponent.getHand().hasCards()) {
                System.out.println("This player is out of cards.");
            } else {
                validTarget = true;
            }
        }
        return opponent;
    }

    /**
     * Takes in the number of opponents that current player wants to check when using Baroness.
     * @return 
     *      the number of opponents
     */
    public int getNumOfPlayerForBaroness(PlayerList players) {
        List<Player> targetablePlayers = players.getTargetablePlayers();

        if (targetablePlayers.size() == 1) {
            return 0;
        }

        // Active players will always be more than two players.
        // If there's only two active players in the round, the current player can only choose
        // one opponent to peak.
        if (targetablePlayers.size() == 2) {
            System.out.println("You can only check one other player in the round.");
            return 1;
        }

        System.out.print("How many players do you want to check? Please enter 1 or 2: ");
        String numString = in.nextLine().trim();
        int num = Integer.parseInt(numString);
        while (num != 1 && num != 2) {
            System.out.println("You can only view 1 or 2 other players. Please re-enter: ");
            numString = in.nextLine();
            num = Integer.parseInt(numString);
        }

        return num;
    }

    /**
     * When the player wants to check two other players, the player shouldn't input two same player name.
     */
    public void printWhenBaronessOnSameOpponent() {
        System.out.println("You can't check for the same player. Please reenter with another player.");
    }

    /**
     * 
     * @return
     */
    public void printWhenNoPlayerCanBeTarget() {
        System.out.println("All other players are out or protected. ");
    }

    /**
     * Get the opponent card guess while using Guard
     * @return
     *      the card name guessed
     */
    String getGuardGuess() {
        ArrayList<String> cardNames = new ArrayList<>(Arrays.asList(Card.CARD_NAMES));

        System.out.print("Which card would you like to guess: ");
        String cardName = in.nextLine().trim();

        while (!cardNames.contains(cardName.toLowerCase()) || cardName.equalsIgnoreCase("guard")) {
            System.out.println("Invalid card name");
            System.out.print("Which card would you like to guess: ");
            cardName = in.nextLine().trim();
        }

        return cardName;
    }

    /**
     * Display if the guess is correct
     * @param isCorrect
     *      the correctness of the guess,
     */
    void showGuardGuess(boolean isCorrect) {
        if (isCorrect) {
            System.out.println("You have guessed correctly!");
        } else {
            System.out.println("You have guessed incorrectly.");
        }
    }

    /**
     * Ask user to choose a card to use
     * @return
     *      the index of the chosen card, possible value -- 0, 1
     */
    int chooseCard() {
        System.out.println();
        System.out.print("Which card would you like to play (0 for first, 1 for second): ");
        String cardPosition = in.nextLine().trim();
        while (!cardPosition.equals("0") && !cardPosition.equals("1")) {
            System.out.println("Please enter a valid card position");
            System.out.print("Which card would you like to play (0 for first, 1 for second): ");
            cardPosition = in.nextLine().trim();
        }

        int idx = Integer.parseInt(cardPosition);
        return idx;
    }

    /**
     * Show the opponent card
     * @param opponentName
     *          name of the opponent
     * @param opponentCard
     *          card of the opponent
     */
    void showCard(String opponentName, Card opponentCard) {
        System.out.println(opponentName + " shows you a " + opponentCard);
    }

    /**
     * Display protection message
     */
    void showProtection() {
        System.out.println("You are now protected until your next turn.");
    }

    /**
     * Display guard comparison result message
     * @param isUserWinner
     *          did the user win
     */
    void showGuardWinner(boolean isUserWinner){
        if (isUserWinner) {
            System.out.println("You have won the comparison!");
        } else {
            System.out.println("You have lost the comparison.");
        }
    }

    /**
     * Displays a list of player names with their cards
     * @param players
     *          player that we want to display
     */
    public void printUsedPiles(List<Player> players) {
        for (Player p : players) {
            System.out.println("\n" + p.getName());
            DiscardPile pile = p.getDiscarded();
            List<Card> cards = pile.getCards();
            printCards(cards);
        }
    }

    /**
     * Display a list of cards
     * @param cards
     *           a list of cards to display
     */
    public void printCards(List<Card> cards) {
        for (Card c : cards) {
            System.out.println(c);
        }
    }

    /**
     * Display a list of players
     * @param players
     *          a list of players to display
     */
    public void printPlayers(List<Player> players) {
        System.out.println();
        for (Player p : players) {
            System.out.println(p);
        }
        System.out.println();
    }

    /**
     * Allows for the user to pick a card from their hand to play.
     *
     * @param user
     *      the current player
     *
     * @return the chosen card
     */
    public Card getCard(Player user) {
        Hand hand = user.getHand();

        List<Card> cards = hand.getCards();
        printCards(cards);

        int idx = chooseCard();
        return hand.remove(idx);
    }

    /**
     * Get the card number guess when bishop card is played
     */
    int getBishopGuess() {
        System.out.print("Which card number would you like to guess (0-9): ");
        String inputValue = in.nextLine().trim();
        int guessedValue = Integer.parseInt(inputValue);

        while (guessedValue < 0 || guessedValue > 9) {
            System.out.println("Invalid card number");
            System.out.print("Which card number would you like to guess (0-9): ");
            inputValue = in.nextLine().trim();
            guessedValue = Integer.parseInt(inputValue);
        }

        return guessedValue;
    }

    /**
     * When the player plays bishop card and wins, the opponent 
     * is asked whether he wants to swap a card with deck
     */
    public boolean getUserSwapConfirmation() {
        System.out.print("Does opponent want to swap card? (yes/no): ");
        String response = in.nextLine().trim().toLowerCase();
    
        while (!response.equals("yes") && !response.equals("no")) {
            System.out.println("Invalid input. Please answer with 'yes' or 'no'.");
            System.out.print("Do you want to swap your card? (yes/no): ");
            response = in.nextLine().trim().toLowerCase();
        }
    
        return response.equals("yes");
    }

    /**
     * Display results after Bishop card is played
     * @param players
     *          a list of players to display
     */
    public void showBishopResults(String result) {
        if (result.equals("UserWin")) {
            System.out.println("You have guessed correctly. You win a token of affection!");
        } else if (result.equals("UserLose")) {
            System.out.println("You have guessed incorrectly");
        }
    }

    /**
     * Prints the result of the Dowager Queen's action.
     * 
     * @param result The result of the Dowager Queen action ("UserWin", "UserLose", "Tie").
     */
    public void dowagerQueenResult(String result, Player user, Player opponent) {
        switch (result) {
            case "UserLose":
                System.out.println(user.getName() + " has a higher card. " + user.getName() + " is knocked out of the round.");
                break;
            case "UserWin":
                System.out.println(opponent.getName() + " has a higher card. " + opponent.getName() + " is knocked out of the round.");
                break;
            case "Tie":
                System.out.println("It's a tie! No one is knocked out of the round.");
                break;
            default:
                System.out.println("Invalid result.");
                break;
        }
    }

    /**
     * Print results after current player use Guard on an opponent with Assassin in Hand
     * @param user
     */
    public void assassinResult(Player user, Player opponent) {
        System.out.println(user.getName() + " is eliminated b/c " + opponent.getName() + " has Assassin in hand.");
    }

    /**
     * Ask for the user which player to check and print of the player's card.
     * @param one
     *        the first player to choose
     * @param two
     *        the second player to choose
     */
    public void cardinalPeekOne(Player one, Player two) {
        System.out.println("After swap, what's the player's card you want to peek: ");
        String name = in.nextLine().trim();
        while (!name.equals(one.getName()) && !name.equals(two.getName())) {
            System.out.println("The player name is invalid or not in chosen for swapping. Please enter a valid player to peek: ");
            name = in.nextLine().trim();
        }
        if (name.equals(one.getName())) {
            System.out.println(name + " shows you a " + one.getHand().getCard().getName());
        } else {
            System.out.println(name + " shows you a " + two.getHand().getCard().getName());
        }
    }

    public void printConstable(String name) {
        System.out.println(name +" has a Constable in discarded pile. Hence, he earns an affection token.");
    }

    public void printGetAToken() {
        System.out.println("Correct guess! You receive a Token of Affection.");
    }

    public void printException(String exception) {
        System.out.println("This is an Exception: " + exception);
    }

    public void printTargetablePlayers(List<Player> players) {
        System.out.println("Please choose two players from the following list: ");

        String nameList = players.get(0).getName();
        for (int i = 1; i < players.size(); i++) {
            nameList = nameList + ", " + players.get(i).getName();
        }
        System.out.println(nameList);
    }

    public void printNotSamePlayers() {
        System.out.println("Please enter a different player. ");
    }

    public void printForChosenSycophant(Player player) {
        System.out.println("The player " + player.getName() + " is automatically selected as a Sycophant marked player. ");
    }

    public void showFaceUpSetAsideCards(List<Card> cards) {
        System.out.println("The cards set aside are: "  + cards);
    }

    public String getStartingPlayerName(List<Player> players) {
        List<String> playerNames = new ArrayList<>();
        for (Player player: players) {
            playerNames.add(player.getName());
        }

        System.out.println("There are multiple winners, the round is started by the one who is recently on a date, choose among: " + players);
        String name = in.nextLine().trim();
        while (!playerNames.contains(name)) {
            System.out.println("The player name is invalid, choose from these players: "+players);
            name = in.nextLine().trim();
        }
        return name;
    }
}
