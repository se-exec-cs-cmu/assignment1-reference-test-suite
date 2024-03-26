package edu.cmu.f23qa.loveletter;

import java.util.ArrayList;
import java.util.List;

/**
 * The main game class. Contains methods for running the game.
 */
public class Game {
    private PlayerList players;
    private Deck deck;
    private GameActions gameActions;

    // Required for Syncophant operations
    private boolean SyncophantFlag;
    private Player SyncophantChosenPlayer;

    /**
     * Public constructor for a Game object.
     * @param players    the player list
     * @param deck      the deck of cards
     *          
     */
    public Game(PlayerList players, Deck deck, GameActions gameActions) {
        this.players = players;
        this.deck = deck;
        this.gameActions = gameActions;

        // Required for Syncophant operations
        this.SyncophantFlag = false;
        this.SyncophantChosenPlayer = null;
    }

    /**
     * Sets up the players that make up the player list.
     */
    public void setPlayers(GameUI gameUI) {
        List<String> players = gameUI.getPlayers();
        for(String player : players) {
            this.players.addPlayer(player);
        }
    }

    /**
     * Gets the players that make up the player list.
     */
    public PlayerList getPlayers()
    {
        return players;
    }

    /**
     * Get the deck of cards at any point of time during the game
     */
    public Deck getDeck()
    {
        return deck;
    }


    /**
     * The main game loop.
     */
    public void start(GameUI gameUI) {
        // initialize affection points based on number of players
        players.initializeTargetAffection();
        List<Player> roundWinners = new ArrayList<>();

        while (players.isZeroGameWinner() || players.isTwoOrMoreGameWinners()) {
            // If there is tie between winners then
            // round is played to resolve the tie
            if (players.isTwoOrMoreGameWinners()){
                players.removeLosers();
            }

            startRound(players, deck, gameUI);
            
            // set the game starter
            players.setRoundStarter(roundWinners, gameUI);
            while (players.moreThanSinglePlayerLeft() && deck.hasMoreCards()) {
                Player turn = players.getCurrentPlayer();
                startTurn(turn, gameUI);

                // It's possible that the game ends early without a round winner.
                // check if only one round winner.
                if (!(players.isZeroGameWinner() || players.isTwoOrMoreGameWinners())){
                    break;
                }
                    
            }

            // It's possible that the game ends early without a round winner.
            // check if only one round winner.
            if (!(players.isZeroGameWinner() || players.isTwoOrMoreGameWinners())){
                break;
            }

            roundWinners = declareRoundWinner(players);
            gameUI.showRoundWinners(roundWinners);

            List<Player> playersList = players.getPlayers();
            gameUI.printPlayers(playersList);
        }
        // the loop will terminate only if there is one winner
        Player gameWinner = players.getGameWinners().get(0);
        String winner = gameWinner.getName();
        gameUI.showGameWinner(winner);
    }

    /**
     * Starts the player turn, it shows game status, draws a card
     * and plays the card
     * @param turn
     *          the player who has to player
     * @param gameUI
     *          gameUI to facilitate user input
     */
    public void startTurn(Player turn, GameUI gameUI) {
        if (turn.getHand().hasCards()) {
            // show all players used piles
            List<Player> playersList = players.getPlayers();
            gameUI.printUsedPiles(playersList);

            // show player info
            String playerName = turn.getName();
            gameUI.showPlayerTurn(playerName);

            // player draws a card
            Card card = deck.draw();

            // player plays his turn
            playTurn(turn, card, gameUI);
        }
    }

    /**
     * Sets up the player to play the card.
     * 1. Resets protection
     * 2. Adds drawn card to hand
     * 3. Handles the card to use
     * 4. Plays the card
     * @param turn
     *          the player who is playing
     * @param cardDrawn
     *          the card drawn from the deck
     * @param gameUI
     *          gameUI to facilitate user input
     */
    public void playTurn(Player turn, Card cardDrawn, GameUI gameUI) {
        // reset previous protection and add drawn card to hand
        turn.turnOffProtection();
        turn.getHand().add(cardDrawn);

        Card useCard;
        int royaltyPos = turn.getHand().getRoyaltyPos();
        int countessPos = turn.getHand().getCardPos(Card.COUNTESS);

        // If one card is royal card and other is countess
        // then use countess, else ask the user
        if (royaltyPos != -1 && countessPos != -1) {
            useCard = turn.getHand().remove(countessPos);
        } else {
            useCard = gameUI.getCard(turn);
        }

        // If condition added to accomodate Syncophant card logic
        if (this.SyncophantFlag) {
            Player localSyncophantChosenPlayer = this.SyncophantChosenPlayer;

            // Reset of global Syncophant variables is done before calling playCard to accomodate corner case 
            // Corner case - If 2 Syncophant cards are played in subsequent turns, this is essential
            this.SyncophantChosenPlayer = null;
            this.SyncophantFlag = false;
            playCard(useCard, turn, gameUI, true, localSyncophantChosenPlayer);
        } 
        else {
            playCard(useCard, turn, gameUI, false, null);
        }
        
    }

    /**
     * Perform card action based on card
     * @param card
     *      the card to play
     * @param user
     *      the player who is using the card
     * @param gameUI
     *      gameUI to facilitate user input
     */
    public void playCard(Card card, Player user, GameUI gameUI, boolean localSyncophantFlag, Player localSyncophantChosenPlayer) {
        user.getDiscarded().add(card);

        Player opponent;
        switch (card) {
            case GUARD:
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, false);
                if(opponent != null){
                    String cardName = gameUI.getGuardGuess();
                    if (gameActions.useAssassin(user, opponent, deck)) {
                        gameUI.assassinResult(user, opponent);
                    } else {
                        boolean correctGuess = gameActions.useGuard(cardName, opponent);
                            gameUI.showGuardGuess(correctGuess);
                    }
                }
                break;

            case PRIEST:
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, false);
                if(opponent != null) {
                    Card opponetCard = gameActions.usePriest(opponent);
                    gameUI.showCard(opponent.getName(), opponetCard);
                }
                break;

            case BARON:
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, false);
                if(opponent != null) {
                    gameActions.useBaron(user, opponent, gameUI);
                }
                break;

            case HANDMAIDEN:
                gameActions.useHandmaiden(user);
                gameUI.showProtection();
                break;

            case PRINCE:
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, true);
                if(opponent != null) {
                    gameActions.usePrince(opponent, deck);
                }
                break;

            case KING:
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, false);
                if(opponent != null) {
                    gameActions.useKing(user, opponent);
                }
                break;

            case COUNTESS:
                break;

            case PRINCESS:
                gameActions.usePrincess(user);
                break;

            case DOWAGERQUEEN: 
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, false);
                String DowagerQueenResult = gameActions.useDowagerQueen(user, opponent);
                gameUI.dowagerQueenResult(DowagerQueenResult, user, opponent);
                break;

            case BISHOP:
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, false);
                // It is always preferred to avoid passing gameUI to GameActions calls, but this case is an exception
                // since there is a lot of gameUI logic associated with bishop card action
                gameActions.useBishop(user, opponent, deck, players, gameUI);
                break;

            case SYNCOPHANT:
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, true);
                this.SyncophantChosenPlayer = opponent;
                this.SyncophantFlag = true;
                break;

            case CONSTABLE:
                // No Action
                break;

            case COUNT:
                // No Action
                break;

            case JESTER:
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, true);
                gameActions.useJester(user, opponent);
                break;

            case BARONESS:
                int numOfPlayers = gameUI.getNumOfPlayerForBaroness(players);
                if (numOfPlayers == 0) {
                    gameUI.printWhenNoPlayerCanBeTarget();
                    break;
                }
                gameActions.useBaroness(numOfPlayers, players, user, gameUI, localSyncophantFlag, localSyncophantChosenPlayer);
                break;
            
            case ASSASSIN:
                // No Action
                break;
            
            case CARDINAL:
                List<Player> targetablePlayers = players.getTargetablePlayers();
                // If less than two targetale players in the round, this card does nothing.
                if (targetablePlayers.size() >= 2) {
                    gameUI.printTargetablePlayers(targetablePlayers);
                    Player opponentOne = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, true);
                    // The second opponent is for sure not a Sycophant marked player.
                    Player opponentTwo = gameUI.getOpponent(players, user, false, null, true);
                    while (opponentOne.getName().equals(opponentTwo.getName())) {
                        gameUI.printNotSamePlayers();
                        opponentTwo = gameUI.getOpponent(players, user, false, null, true);
                    }
                    gameActions.useCardinal(opponentOne, opponentTwo, gameUI);
                }
                break;
        }
    }

    /**
     * All methods below are private methods for the game.
     */

    public void startRound(PlayerList players, Deck deck, GameUI gameUI) {
        players.reset();
        deck.setDeck(players.getNumberOfPlayers(), gameUI);
        players.dealCards(deck);
        this.SyncophantChosenPlayer = null;
        this.SyncophantFlag = false;
    }

    /**
     * Takes in a list of players and declares winners, there can be multiple
     * winners for a round.
     * A winner gets an affection point
     * @param players
     *      a players list
     * @return
     *      a list of winners
     */
    public List<Player> declareRoundWinner(PlayerList players) {
        List<Player> winners = players.getRoundWinners();
        for(Player winner: winners){
            winner.addToken();

            // If the winner has Jester Token from another player,
            // that player should get another token
            if (winner.getJesterToken() != null) {
                winner.getJesterToken().addToken();
            }
        }
        return winners;
    }

    public void setSyncophantFlag(boolean value){
        this.SyncophantFlag = value;
    }

    public void setSyncophantChosenPlayer(Player p){
        this.SyncophantChosenPlayer = p;
    }
}
