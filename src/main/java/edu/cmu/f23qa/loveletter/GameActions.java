package edu.cmu.f23qa.loveletter;

/**
 * The possible player actions to be taken during the game.
 */
public class GameActions {

    /**
     * Allows the user to guess a card that a player's hand contains (excluding another guard).
     * If the user is correct, the opponent loses the round and must lay down their card.
     * If the user is incorrect, the opponent is not affected.
     * @param cardName
     *          the input stream
     * @param opponent
     *          the targeted player
     * @return
     *          true if opponent is eliminated
     */
    public boolean useGuard(String cardName, Player opponent) {
        Card opponentCard = opponent.getHand().peek(0);
        if (opponentCard.getName().equalsIgnoreCase(cardName)) {
            opponent.eliminate();
            return true;
        }
        return false;
    }

    /**
     * Allows the user to peek at the card of an opposing player.
     * @param opponent
     *          the targeted player
     */
    public Card usePriest(Player opponent) {
        return opponent.getHand().peek(0);
    }

    /**
     * Allows the user to compare cards with an opponent.
     * If the user's card is of higher value, the opposing player loses the round and their card.
     * If the user's card is of lower value, the user loses the round and their card.
     * If the two players have the same card, their used pile values are compared in the same manner.
     * @param user
     *          the initiator of the comparison
     * @param opponent
     *          the targeted player
     * @param gameUI
     *          get user input and show output
     */
    public void useBaron(Player user, Player opponent, GameUI gameUI) {
        Player looser = getLooser(user, opponent);
        if(looser != null) {
            looser.eliminate();
            gameUI.showGuardWinner(user != looser);
        }
    }

    /**
     * Compares the two players hand and return the looser
     * - If user wins, return opponent
     * - If opponent wins, return user
     * - If both are equal, return null, no looser
     * @param user
     *          the current player
     * @param opponent
     *          the opponent player
     * @return
     *          the looser player or null if no looser
     */
    public Player getLooser(Player user, Player opponent) {
        Card userCard = user.getHand().peek(0);
        Card opponentCard = opponent.getHand().peek(0);
        int cardComparison = Integer.compare(userCard.value(), opponentCard.value());
        if (cardComparison > 0) {
            return opponent;
        } else if (cardComparison < 0) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * Switches the user's protection for one turn. This protects them from being targeted.
     * @param user
     *          the current player
     */
    public void useHandmaiden(Player user) {
        user.switchProtection();
    }

    /**
     * Makes an opposing player lay down their card in their used pile and draw another.
     * @param opponent
     *          the targeted player
     * @param d
     *          the deck of cards
     */
    public void usePrince(Player opponent, Deck d) {
        // opponent drops princess, then he is eliminated
        if (opponent.getHand().peek(0) == Card.PRINCESS) {
            opponent.eliminate();
            return;
        }

        // replace hand card for opponent
        Card card;
        if (d.hasMoreCards()) {
            card = d.draw();
        } else {
            card = d.getSetAsideCard();
        }
        opponent.replaceHandCard(card);
    }

    /**
     * Allows the user to switch cards with an opponent.
     * Swaps the user's hand for the opponent's.
     * @param user
     *          the initiator of the swap
     * @param opponent
     *          the targeted player
     */
    public void useKing(Player user, Player opponent) {
        Card userCard = user.getHand().remove(0);
        Card opponentCard = opponent.getHand().remove(0);
        user.getHand().add(opponentCard);
        opponent.getHand().add(userCard);
    }

    /**
     * If the princess is played, the user loses the round and must lay down their hand.
     * @param user
     *          the current player
     */
    public void usePrincess(Player user) {
        user.eliminate();
    }


    /**
     * Uses the Bishop's ability to guess a card in an opponent's hand.
     * If the guess is correct, the user gains a token of affection.
     * The opponent may discard their card (unless it's the Princess) and draw a new one.
     * @param guessedCard
     *          the card guessed by the user
     * @param user
     *          the current player
     * @param opponent
     *          the targeted player
     * @param deck
     *          the deck of cards
     * @param playerList
     *          the list of players
     * @return
     *         UserWin or UserLose or GameOver
     * 
     */
    public void useBishop(Player user, Player opponent, Deck deck, PlayerList players, GameUI gameUI) {
        int guessedValue = gameUI.getBishopGuess();
        Card opponentCard = opponent.getHand().peek(0);
        if (opponentCard.value() == guessedValue) {
            gameUI.printGetAToken();
            user.addToken(); // Add a token to the user's collection

            // Check for immediate win condition
            if (!players.isZeroGameWinner()) {
                // Do nothing, as this logic is taken care in Game.java:start() loop
                return;
            }
            gameUI.showBishopResults("UserWin");
            boolean swapConfirmation = gameUI.getUserSwapConfirmation();
            if (swapConfirmation && deck.hasMoreCards()) {
                // Opponent discards and draws a new card
                if (opponentCard.value() == 8) {
                    usePrincess(opponent);
                } else {
                    opponent.getHand().remove(0);
                    opponent.getDiscarded().add(opponentCard);
                    opponent.getHand().add(deck.draw());
                }
            }
        } else {
            gameUI.showBishopResults("UserLose");
        }
    }

    /**
     * Uses the Dowager Queen's ability to compare hands with another player.
     * The player with the higher hand value is knocked out of the round.
     * In the event of a tie, nothing happens.
     * Assuming that opponents protected by Handmaiden can't be targeted.
     * 
     * @param user      
     *          The player who played the Dowager Queen.
     * @param opponent  
     *          The player chosen to compare hands with.
     * @return     
     *          UserWin or UserLose or Tie    
     */
    public String useDowagerQueen(Player user, Player opponent) {
        Card userCard = user.getHand().peek(0); 
        Card opponentCard = opponent.getHand().peek(0);

        int comparison = Integer.compare(userCard.value(), opponentCard.value());

        // Comparison between Princess and Bishop yet to be considered 
        
        if (comparison > 0) {
            // User's card is higher, user gets eliminated
            user.eliminate();
            return "UserLose";
        } else if (comparison < 0) {
            // Opponent's card is higher, opponent gets eliminated
            opponent.eliminate();
            return "UserWin";
        } else {
            // It's a tie, nothing happens
            return "Tie";
        }
    }
    

    /**
     * Jester will add a jester token to the targetted opponent.
     * @param user
     *          The player who uses the jester card
     * @param opponent
     *          The player who is targetted by the jester card
     */
    public void useJester(Player user, Player opponent) {
        opponent.setJesterToken(user);
    }

    /**
     * Giving the number of players to peak, print out results on others' cards.
     * @param opponent
     *          the targeted player
     */
    public void useBaroness(int numOfPlayers, PlayerList players, Player user, GameUI gameUI, 
        boolean localSyncophantFlag, Player localSyncophantChosenPlayer) {
            
        String prevPlayerToPeak = null;
        for (int i = 0; i < numOfPlayers; i++) {
            Player opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, false);
            if (opponent != null && localSyncophantChosenPlayer != null && opponent.getName().equals(localSyncophantChosenPlayer.getName())) {
                localSyncophantChosenPlayer = null;
                localSyncophantFlag = false;
            }

            while (opponent.getName().equals(prevPlayerToPeak)) {
                gameUI.printWhenBaronessOnSameOpponent();
                opponent = gameUI.getOpponent(players, user, localSyncophantFlag, localSyncophantChosenPlayer, false);
            }

            Card opponentCard = opponent.getHand().peek(0);
            gameUI.showCard(opponent.getName(), opponentCard);
            prevPlayerToPeak = opponent.getName();
        }
    }

    /**
     * useAssassin checks whether the opponent has the Assassin card.
     * If yes, the current player is eliminiated from the round. 
     * And the opponent need to discard the assassin and draw a new one.
     * If no, nothing happens.
     * 
     * @param user
     *          The player who is playing Guard and choose to play against opponent.
     * @param opponent
     *          The player chosen by the user when playing Guard
     * @return
     *          True when opponent has Assassin; otherwise, false.
     */
    boolean useAssassin(Player user, Player opponent, Deck deck) {
        if (opponent.getHand().getCards().contains(Card.ASSASSIN)) {
            user.eliminate();
            int assassinIndex = opponent.getHand().getCardPos(Card.ASSASSIN);

            opponent.getHand().remove(assassinIndex);
            opponent.getDiscarded().add(Card.ASSASSIN);
            opponent.getHand().add(deck.draw());
            return true;
        }

        return false;
    }

    /**
     * useCardinal asks for two players to swap their cards and print out one of their cards based on the user's choice.
     * @param one
     *          the first player
     * @param two
     *          the second player
     * @param gameUI
     *          UI to interact 
     */
    public void useCardinal(Player one, Player two, GameUI gameUI) {
        // Swap Hand Card
        Card temp = one.getHand().getCard();
        one.getHand().setHand(two.getHand().getCard());
        two.getHand().setHand(temp);

        // Peek one of the player's card
        gameUI.cardinalPeekOne(one, two);
    }
}
