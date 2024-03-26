package edu.cmu.f23qa.loveletter;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile {
    private ArrayList<Card> cards;

    public DiscardPile() {
        this.cards = new ArrayList<>();
    }

    public void add(Card card) {
        this.cards.add(card);
    }

    public int value() {
        int value = 0;
        for (Card c : this.cards) {
            value += c.value();
        }
        return value;
    }

    public void clear() {
        this.cards.clear();
    }

    public List<Card> getCards() { return this.cards; }

    public boolean containsConstable (){
        return this.cards.contains(Card.CONSTABLE);
    }
}
