package edu.cmu.f23qa.loveletter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayerList {

    private LinkedList<Player> players;
    int targetAffectionPoints;


    public PlayerList() {
        this.players = new LinkedList<>();
    }

    /**
     * Adds a new Player object with the given name to the PlayerList.
     *
     * @param name the given player name
     * @return true if the player is not already in the list and can be added, false if not
     */
    public boolean addPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        players.addLast(new Player(name));
        return true;
    }

    /**
     * Gets the first player in the list and adds them to end of the list.
     *
     * @return the first player in the list
     */
    public Player getCurrentPlayer() {
        Player current = players.removeFirst();
        players.addLast(current);
        return current;
    }

    /**
     * Resets all players within the list.
     */
    public void reset() {
        for (Player p : players) {
            p.getHand().clear();
            p.getDiscarded().clear();
            p.turnOffProtection();
            p.clearJesterToken();
        }
    }

    public List<Player> getPlayers() {
        List<Player> playerList = new ArrayList<>();
        for (Player p : players) {
            playerList.add(p);
        }
        return playerList;
    }

    /**
     * @return the size of the total players to decide standard vs premium version.
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * Checks the list is players is more than one
     *
     * @return true if there are more players
     */
    public boolean moreThanSinglePlayerLeft() {
        int count = 0;
        for (Player p : players) {
            if (p.getHand().hasCards()) {
                count++;
            }
        }
        return count > 1;
    }

    /**
     * Get the list of players that can be targetted during selection process.
     * @return 
     *      a list of players
     */
    public List<Player> getTargetablePlayers() {
        List<Player> playerList = new ArrayList<>();

        for (Player player : players) {
            if (player.getHand().hasCards() && !player.isProtected()) {
                playerList.add(player);
            }
        }

        return playerList;
    }

    /**
     * Returns the rounds winners
     * 1. If one only one player is left he is the winner
     * 2. If multiple players are left, then their card value is compared
     * 3. If there is conflict, use discarded pile. A match in discarded pile implies multiple winners.
     * @return
     *      a list of winners
     */
    public List<Player> getRoundWinners() {
        // copy of all players
        List<Player> playerList = new ArrayList<>(players);

        // filter players with hand
        playerList = filterPlayerWithHand(playerList);
        // First case, one winner left
        if (playerList.size() == 1) {
            return playerList;
        }

        // filter players with max hand value
        playerList = filterPlayerByMaxHandValue(playerList);
        // Second case, one winner after hand value is compared
        if (playerList.size() == 1) {
            return playerList;
        }

        // filter players with max discarded pile value
        playerList = filterPlayerByMaxDiscardedCardValue(playerList);
        // final case, there can be multiple winners if discarded pile matches
        return playerList;
    }

    /**
     * Takes in a list of players and return a list of players with hand
     * i.e a card in hand or these players did not lose the round
     * @param playerList
     *       a list of initial players
     * @return
     *       a list of filtered out players
     */
    public List<Player> filterPlayerWithHand(List<Player> playerList) {
        List<Player> playerWithHandList = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getHand().hasCards()) {
                playerWithHandList.add(player);
            }
        }
        return playerWithHandList;
    }

    /**
     * Takes in a list of players and return a list of players who has max hand card,
     * there can be multiple players with same max hand card
     * @param playerList
     *           a list of initial players
     * @return
     *          a list of filtered out players
     */
    public List<Player> filterPlayerByMaxHandValue(List<Player> playerList) {
        // Whenever there's a comparision btw Bishop vs Princess, the Princess wins regardless of Counts.
        int bishopIndex = getCardContainedIndex(playerList, Card.BISHOP);
        int princessIndex = getCardContainedIndex(playerList, Card.PRINCESS);

        List<Player> removedBishopList = new ArrayList<>();
        if (bishopIndex != -1 && princessIndex != -1) {
            for (int i = 0; i < playerList.size(); i++) {
                if (i != bishopIndex) {
                    removedBishopList.add(playerList.get(i));
                }
            }
        } else {
            removedBishopList = playerList;
        }

        // find max value
        int maxHandValue = -1;
        for (Player player : removedBishopList) {
            int cardValue = player.getPlayerHandValue();
            if (cardValue > maxHandValue) {
                maxHandValue = cardValue;
            }
        }

        // filter out players with max value
        List<Player> filteredPlayerList = new ArrayList<>();
        for (Player player : removedBishopList) {
            int cardValue = player.getPlayerHandValue();
            if (cardValue == maxHandValue) {
                filteredPlayerList.add(player);
            }
        }
        return filteredPlayerList;
    }

    /**
     * Check whether the card type is included in the list and return the index of it.
     * @param playerList
     *              the player list that contains all remaining players
     * @param card
     *              the card type to check for
     * @return
     *              return a valid index if the card exists in the table; -1 if not
     */
    public int getCardContainedIndex(List<Player> playerList, Card card) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getHand().getCard().equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Takes in a list of players and return a list of players who has max discarded pile,
     * there can be multiple players with same max discarded pile
     * @param playerList
     *           a list of initial players
     * @return
     *          a list of filtered out players
     */
    public List<Player> filterPlayerByMaxDiscardedCardValue(List<Player> playerList) {
        // find max value
        int maxDiscardedPileValue = 0;
        for (Player player : playerList) {
            int discardedValue = player.getDiscarded().value();
            if (discardedValue > maxDiscardedPileValue) {
                maxDiscardedPileValue = discardedValue;
            }
        }

        // filter out players with max discarded pile value
        List<Player> filteredPlayerList = new ArrayList<>();
        for (Player player : playerList) {
            int discardedValue = player.getDiscarded().value();
            if (discardedValue == maxDiscardedPileValue) {
                filteredPlayerList.add(player);
            }
        }
        return filteredPlayerList;
    }

    /**
     * Checks if there are zero game winners
     * @return
     *      returns true if there are no game winners
     */
    public boolean isZeroGameWinner() {
        return getGameWinners().isEmpty();
    }

    /**
     * Checks if there are two are more game winners
     * @return
     *      returns true if there are two or more game winners
     */
    public boolean isTwoOrMoreGameWinners() {
        return getGameWinners().size() > 1;
    }

    /**
     * Returns a list of game winners.
     * There can be multiple winners at a certain point.
     * The winners are filtered out in two steps.
     * 1) All players without target token/affection points are filtered out.
     * 2) Then winners token is compared to determine the winner.
     * @return
     *      a list of game winners at a certain point
     */
    public List<Player> getGameWinners() {
        List<Player> playerList = new ArrayList<>(players);
        playerList = filterPlayerByWinners(playerList);
        playerList = filterPlayerByMaxToken(playerList);
        return playerList;
    }

    /**
     * The methods remove all the players who are not winners for player list
     * If multiple winners are present then remove other non-winning players, this is based on rulebook
     * Rule book extract: It’s also possible that two (or more) players could “tie” in
     * terms of winning the game, again because of some of the cards used in the 5-8 player version of the game.
     * The Princess has been smitten with several suitors, who must now vie for her affection.
     * In such a case, all of the tied players will play another round to break the tie,
     * and determine who finally wins the hand of the Princess and the game!
     */
    public void removeLosers() {
        int playerCount = players.size();
        List<Player> winners = getGameWinners();
        for(int i=0; i<playerCount; i++) {
            Player current = players.removeFirst();
            // if current is a winner, add back to winner list
            if (winners.contains(current)){
                players.addLast(current);
            }
        }
    }

    /**
     * filters out players who do not have target affection points
     * @param playerList
     * @return
     *      a list of player who have more than target affectin points
     */
    public List<Player> filterPlayerByWinners(List<Player> playerList) {
        // filter out players who have less than required affection points
        List<Player> filteredPlayerList = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getTokens() >= targetAffectionPoints) {
                filteredPlayerList.add(player);
            }
        }
        return filteredPlayerList;
    }

    /**
     * filters out players based on max tokens, the steps to filter are as follows
     * 1) The maximum token is determined
     * 2) players with token matching maximum token remain
     * @param playerList
     * @return
     *      a list of players with maximum token, multiple players can have maximum token
     */
    public List<Player> filterPlayerByMaxToken(List<Player> playerList) {
        // find max value
        int maxToken = 0;
        for (Player player : playerList) {
            if (player.getTokens() > maxToken) {
                maxToken = player.getTokens();
            }
        }

        // filter out players with max value
        List<Player> filteredPlayerList = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getTokens() == maxToken) {
                filteredPlayerList.add(player);
            }
        }
        return filteredPlayerList;
    }

    /**
     * The function returns the affection points based on the number
     * of players.
     * @param playerCount
     * @return
     */
    public int getTargetAffectionPoints(int playerCount) {
        if (playerCount == 2) {
            return 7;
        }
        if (playerCount == 3) {
            return 5;
        }
        // for all other four affection points
        return 4;
    }

    /**
     * Deals a card to each Player in the list.
     *
     * @param deck the deck of cards
     */
    public void dealCards(Deck deck) {
        for (Player p : players) {
            p.getHand().add(deck.draw());
        }
    }

    /**
     * Gets the player with the given name.
     *
     * @param name the name of the desired player
     * @return the player with the given name or null if there is no such player
     */
    public Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Checks if there are opponents available
     * 1) Ignore self, as a self is not an opponent
     * 2) A player with card but has no protection is an opponent
     * @param player
     * @return
     */
    public boolean isOpponentsAvailable(Player player) {
        for (Player p : players) {
            // skip the player as he not an opponent
            if (p.equals(player)){
                continue;
            }

            // an opponent has hand with no protection, then
            // there is an unprotected opponent
            if (p.getHand().hasCards() && !p.isProtected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initialize target affection points based on player size
     */
    public void initializeTargetAffection() {
        targetAffectionPoints = getTargetAffectionPoints(players.size());
    }

    /**
     * The function determines who starts the round
     * 1) At the start of the game, it follows natural order
     * 2) If there is only one round winner, then they will start the round
     * 3) If there are multiple winners, user is asked 
     */
    public void setRoundStarter(List<Player> winners, GameUI gameUI) {
        Player starter;
        // start of the round
        if (winners.isEmpty()){
            return;
        // one winner
        } else if (winners.size() == 1) {
            starter = winners.get(0);
        // multiple winner
        } else{
            String name =  gameUI.getStartingPlayerName(players);
            starter = getPlayer(name);
        }

        // rotate players until we reach the target player
        while(!players.getFirst().equals(starter)){
            getCurrentPlayer();
        }
    }
}
