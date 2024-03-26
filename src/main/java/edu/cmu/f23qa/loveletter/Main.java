package edu.cmu.f23qa.loveletter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
       
        PlayerList players = new PlayerList();
        Deck deck = new Deck();
        Scanner in = new Scanner(System.in);
        GameActions gameActions = new GameActions();

        Game game = new Game(players, deck, gameActions);
        GameUI gameUI= new GameUI(in);

        game.setPlayers(gameUI);
        game.start(gameUI);
    }

}
