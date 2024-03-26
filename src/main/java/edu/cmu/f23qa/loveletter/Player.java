package edu.cmu.f23qa.loveletter;

public class Player {
    private String name;
    private Hand hand;
    private GameUI gameUI;
    private Player jesterToken;

    private DiscardPile discarded;

    /**
     * True if the player is protected by a handmaiden, false if not.
     */
    private boolean isProtected;

    /**
     * The number of blocks the player has won.
     */
    private int tokens;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
        this.discarded = new DiscardPile();
        this.isProtected = false;
        this.tokens = 0;
        this.jesterToken = null;
    }

    public void addToken() {
        this.tokens++;
    }

    /**
     * Eliminates the player from the round by discarding their hand.
     */
    public void eliminate() {
        if (this.discarded.containsConstable()){
            gameUI.printConstable(this.name);
            this.addToken();
        }

        this.discarded.add(this.hand.remove(0));
    }

    /**
     * The card is dropped and new card is given to player
     */
    public void replaceHandCard(Card card) {
        this.discarded.add(this.hand.remove(0));
        getHand().add(card);
    }

    /**
     * Switches the user's level of protection.
     */
    public void switchProtection() {
        this.isProtected = !this.isProtected;
    }

    /**
     * Turns off player protection
     */
    public void turnOffProtection() {
        if (isProtected()) {
            switchProtection();
        }
    }

    /**
     * Clear jesterToken after each round
     */
    public void clearJesterToken() {
        this.jesterToken = null;
    }

    public void setJesterToken(Player player) {
        this.jesterToken = player;
    }

    public Player getJesterToken() {
        return this.jesterToken;
    }

    public Hand getHand() {
        return this.hand;
    }

    public DiscardPile getDiscarded() {
        return this.discarded;
    }

    /**
     * Checks to see if the user is protected by a handmaiden.
     *
     * @return true, if the player is protected, false if not
     */
    public boolean isProtected() {
        return this.isProtected;
    }

    public int getTokens() {
        return this.tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.tokens + " tokens)";
    }

    public void setProtected(Boolean isProtected) {
        this.isProtected = isProtected;
    }

    public void setDiscardPile(DiscardPile discardPile) {
        this.discarded = discardPile;
    }

    public boolean SwapCard (Player player, Deck deck) {
        if (deck.hasMoreCards()){
            Card card = deck.draw();
            player.getHand().remove(0);
            player.getHand().add(card);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates and returns the number of COUNT cards in the player's discarded pile.
     *
     * @return The number of COUNT cards in the player's discarded pile.
     */
    public int countOfCountCards(Player p){
        int count = 0;
        for (Card c : p.getDiscarded().getCards()){
            if (c.equals(Card.COUNT))
                count ++;
        }
        return count;
    }
    
    /**
     * Calculates the returns the value of hand at the last of each round for a player.
     * This is considering the effect of Count card as well.
     * @return
     */
    public int getPlayerHandValue() {
        if (this.hand.getCards().size() != 1) {
            gameUI.printException(
                "At the end of each round, the player should only have one hand card"
            );
        }
        return this.hand.peek(0).value() + countOfCountCards(this);
    }

    /**
     * Utility method to set the game UI.
     */
    void setGameUI(GameUI gameUI) {
        this.gameUI = gameUI;
    }

}
