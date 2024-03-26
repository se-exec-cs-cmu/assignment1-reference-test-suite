package edu.cmu.f23qa.loveletter;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private ArrayList<Card> hand;

    public Hand() {
        this.hand  = new ArrayList<>();
    }

    /**
     * Peeks the card held by the player.
     *
     * @param idx
     *          the index of the Card to peek
     *
     * @return the card held by the player
     */
    public Card peek(int idx) {
        return this.hand.get(idx);
    }

    public void add(Card card) {
        this.hand.add(card);
    }

    /**
     * Removes the card at the given index from the hand.
     *
     * @param idx
     *          the index of the card
     *
     * @return the card at the given index
     */
    public Card remove(int idx) {
        return this.hand.remove(idx);
    }

    /**
     * Finds the position of a royal card in the hand.
     *
     * @return the position of a royal card, -1 if no royal card is in hand
     */
    public int getRoyaltyPos() {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i) == Card.PRINCE || hand.get(i) == Card.KING) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the position of a countess card in the hand.
     *
     * @return the position of a countess card, -1 if no countess card is in hand
     */
    public int getCardPos(Card card) {
        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i) == card){
                return i;
            }
        }
        return -1;
    }

    public boolean hasCards() {
        return !this.hand.isEmpty();
    }

    public void clear() {
        this.hand.clear();
    }

    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        for (Card c : this.hand) {
            cards.add(c);
        }
        return cards;
    }

    /**
     * If the hand has 1 card only, it returns that card.
     *
     * @return The only card present in a player's hand.
     */
    public Card getCard() {
        if (hand.size() == 1) {
            Card c = hand.get(0);
            return c;
        } else {
            return null;
        }
    }

    public void setHand(Card card) {
        this.hand.clear();
        this.hand.add(card);
    }
}
