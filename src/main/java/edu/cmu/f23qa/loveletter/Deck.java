package edu.cmu.f23qa.loveletter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<Card> deck;
    private static int MAX_STANDARD_PLAYER = 4;
    private Card setAsideCard;

    public Deck() {
        this.deck = new Stack<>();
    }

    /**
     * Builds a new full deck and shuffles it.
     */
    public void setDeck(int numOfPlayer, GameUI gameUI) {
        build(numOfPlayer);
        shuffle();
        setAsideCard = draw();
        // If two players are playing the game, then three
        // cards should be set aside face up.
        if(numOfPlayer == 2) {
            List<Card> cards = new ArrayList<>();
            for (int i=0; i<3; i++){
                cards.add(draw());
            }
            gameUI.showFaceUpSetAsideCards(cards);
        }
    }

    public void build(int numOfPlayer) {
        deck.clear();

        for (int i = 0; i < 5; i++) {
            deck.push(Card.GUARD);
        }

        for (int i = 0; i < 2; i++) {
            deck.push(Card.PRIEST);
            deck.push(Card.BARON);
            deck.push(Card.HANDMAIDEN);
            deck.push(Card.PRINCE);
        }

        deck.push(Card.KING);
        deck.push(Card.COUNTESS);
        deck.push(Card.PRINCESS);

        // Additional cards are added for Premium version
        if (numOfPlayer > MAX_STANDARD_PLAYER) {
            deck.push(Card.BISHOP);
            deck.push(Card.DOWAGERQUEEN);
            deck.push(Card.CONSTABLE);
            deck.push(Card.JESTER);
            deck.push(Card.ASSASSIN);

            for (int i = 0; i < 2; i++) {
                deck.push(Card.COUNT);
                deck.push(Card.SYNCOPHANT);
                deck.push(Card.BARONESS);
                deck.push(Card.CARDINAL);
            }

            for (int i = 0; i < 3; i++) {
                deck.push(Card.GUARD);
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card draw() {
        return deck.pop();
    }

    public boolean hasMoreCards() {
        return !deck.isEmpty();
    }

    public int returnNumberOfCardsRemaining(){
        return deck.size();
    }

    public Card viewNextCard() {
        return deck.peek();
    }

    public Card getSetAsideCard() { return setAsideCard; }
}
