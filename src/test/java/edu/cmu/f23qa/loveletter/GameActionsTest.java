package edu.cmu.f23qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

public class GameActionsTest {
    private static GameUI gameUI;
    private static GameActions gameActions;
    private Player player;
    private Player opponent;
    private DiscardPile discardPile1;
    private DiscardPile discardPile2;


    @BeforeEach
    public void setUpBeforeEach() {
        gameUI = mock(GameUI.class);
        gameActions = new GameActions();

        player = new Player("one");
        opponent = new Player("two");

        discardPile1 = new DiscardPile();
        discardPile2 = new DiscardPile();
    }

    /**
     * Test useGuard function when the player's guess is correct.
     */
    @Test
    public void useGuardTestCorrectGuess() {
        opponent.getHand().add(Card.PRIEST);
        boolean result = gameActions.useGuard("priest", opponent);
        assertEquals(opponent.getHand().hasCards(), false);
        assertEquals(result, true);
    }

    /**
     * Test useGuard function when the player's guess is incorrect.
     */
    @Test
    public void useGuardTestWrongGuess() {
        opponent.getHand().add(Card.PRIEST);
        Boolean result = gameActions.useGuard("guard", opponent);
        assertEquals(result, false);
    }

    /**
     * Test usePriest function to peak a player's hand card.
     */
    @Test
    public void usePriestTest() {
        opponent.getHand().add(Card.BARON);
        assertEquals(gameActions.usePriest(opponent), Card.BARON);
    }

    /**
     * Test useBaron function when player and opponent has different hand card.
     * It should compare the hand card and eliminate based on comparison.
     */
    @Test
    public void useBaronTestDifferentCards() {
        player.getHand().add(Card.GUARD);
        opponent.getHand().add(Card.PRINCESS);

        gameActions.useBaron(player, opponent, gameUI);
        assertEquals(player.getHand().hasCards(), false);
    }

    /**
     * Test useBaron function when player and opponent has the same hand card.
     * In this case no action is taken.
     * Two players have same card in hand. So the comparison should have no
     * action, that is no player should be eliminated.
     */
    @Test
    public void useBaronTestSameHandCard() {
        player.getHand().add(Card.GUARD);
        opponent.getHand().add(Card.GUARD);

        discardPile1.add(Card.COUNTESS);
        discardPile2.add(Card.PRINCE);
        player.setDiscardPile(discardPile1);
        opponent.setDiscardPile(discardPile2);

        gameActions.useBaron(player, opponent, gameUI);
        assertEquals(opponent.getHand().hasCards(), true);
        assertEquals(player.getHand().hasCards(), true);
    }

    /**
     * Test useHandmaiden function to make sure player is protected unde the current round.
     */
    @Test
    public void useHandmaidenTest() {
        player.getHand().add(Card.HANDMAIDEN);
        gameActions.useHandmaiden(player);
        assertEquals(player.isProtected(), true);
    }

    /**
     * Test usePrince function that the opponent has switched the hand card.
     */
    @Test
    public void usePrinceTestDrawOneCard() {
        Deck deck = new Deck();
        deck.setDeck(4, null);
        Card currentTop = deck.viewNextCard();

        opponent.getHand().add(Card.BARON);
        gameActions.usePrince(opponent, deck);
        assertEquals(opponent.getHand().peek(0), currentTop);
    }

    /**
     * Test usePrince when there are no cards in the deck
     * we test this by setting a fresh deck and draw out all the
     * cards until the deck is empty.
     * Now, when we use usePrince, the set aside card should be used
     * as there are no cards in the deck
     */
    @Test
    public void usePrinceTestDrawOneCardDeckEmpty() {
        Deck deck = new Deck();
        deck.setDeck(4, null);

        // Empty the deck
        while(deck.hasMoreCards()){
            deck.draw();
        }
        opponent.getHand().add(Card.BARON);

        Card ExpectedCard = deck.getSetAsideCard();

        gameActions.usePrince(opponent, deck);

        assertNotNull(opponent.getHand());
        assertEquals(opponent.getHand().peek(0), ExpectedCard);
    }

    /**
     * Test usePrince on opponent with Princess eliminates the opponent
     */
    @Test
    public void usePrinceOnOpponentWithPrincessEliminatesOpponent() {
        Deck deck = new Deck();
        deck.setDeck(4, null);
        opponent.getHand().add(Card.PRINCESS);

        gameActions.usePrince(opponent, deck);

        // the opponent is eliminated
        assertFalse(opponent.getHand().hasCards());
    }

    /**
     * Test useKing function that player and opponent has switched their hand card.
     */
    @Test
    public void useKingTest() {
        player.getHand().add(Card.BARON);
        opponent.getHand().add(Card.COUNTESS);

        gameActions.useKing(player, opponent);

        assertEquals(player.getHand().peek(0), Card.COUNTESS);
        assertEquals(opponent.getHand().peek(0), Card.BARON);
    }

    /**
     * Test usePrincess function that the player is eliminated.
     */
    @Test
    public void usePrincessTest() {
        player.getHand().add(Card.PRINCESS);
        gameActions.usePrincess(player);
        assertEquals(player.getHand().hasCards(), false);
    }

    /**
     * Test useBishop when the player has guessed the correct value
     * while the opponent doesn't want to switch card.
     * The player's token of affection should added by one.
     */
    @Test
    public void useBishopTestUserGuessCorrect() {
        Mockito.when(gameUI.getBishopGuess()).thenReturn(1);
        Mockito.when(gameUI.getUserSwapConfirmation()).thenReturn(false);

        PlayerList players = new PlayerList();
        players.addPlayer("aPlayer");
        players.addPlayer("bPlayer");
        players.initializeTargetAffection();

        Player player = players.getPlayer("aPlayer");
        Player opponent = players.getPlayer("bPlayer");

        player.getHand().add(Card.CARDINAL);
        int initialToken = player.getTokens();
        opponent.getHand().add(Card.GUARD);
        Deck deck = new Deck();

        gameActions.useBishop(player, opponent, deck, players, gameUI);
        assertEquals(player.getTokens(), initialToken + 1);
        assertEquals(opponent.getHand().getCard(), Card.GUARD);
    }

    /**
     * Test useBishop when the player has guessed the correct value
     * and by winning one more token affection, the directly wins the game.
     */
    @Test
    public void useBishopTestUserGuessCorrectWinGame() {
        Mockito.when(gameUI.getBishopGuess()).thenReturn(1);
        Mockito.when(gameUI.getUserSwapConfirmation()).thenReturn(false);

        PlayerList players = new PlayerList();
        players.addPlayer("aPlayer");
        players.addPlayer("bPlayer");
        players.initializeTargetAffection();

        Player player = players.getPlayer("aPlayer");
        Player opponent = players.getPlayer("bPlayer");

        player.setTokens(6); // Two players need 7 tokens to win

        player.getHand().add(Card.CARDINAL);
        opponent.getHand().add(Card.GUARD);
        Deck deck = new Deck();

        gameActions.useBishop(player, opponent, deck, players, gameUI);
        verify(gameUI, times(0)).showBishopResults("UserWin");
    }


    /**
     * Test useBishop when the player has guessed the correct value
     * and the opponent wants to switch card to a new card.
     * The player's token of affection should added by one.
     */
    @Test
    public void useBishopTestUserGuessCorrectWithCardChange() {
        Mockito.when(gameUI.getBishopGuess()).thenReturn(1);
        Mockito.when(gameUI.getUserSwapConfirmation()).thenReturn(true);

        PlayerList players = new PlayerList();
        players.addPlayer("aPlayer");
        players.addPlayer("bPlayer");
        players.initializeTargetAffection();

        Player player = players.getPlayer("aPlayer");
        Player opponent = players.getPlayer("bPlayer");

        player.getHand().add(Card.CARDINAL);
        int initialToken = player.getTokens();
        opponent.getHand().add(Card.GUARD);
        Deck deck = new Deck();
        deck.build(2);

        gameActions.useBishop(player, opponent, deck, players, gameUI);
        assertEquals(player.getTokens(), initialToken + 1);
        assertNotEquals(opponent.getHand().getCard(), Card.GUARD);
    }

    /**
     * Test useBishop when the player has guessed an incorrect value
     * so the token of affection should not change.
     */
    @Test
    public void useBishopTestUserGuessInCorrect() {
        Mockito.when(gameUI.getBishopGuess()).thenReturn(1);
        Mockito.when(gameUI.getUserSwapConfirmation()).thenReturn(false);

        PlayerList players = new PlayerList();
        players.addPlayer("aPlayer");
        players.addPlayer("bPlayer");
        players.initializeTargetAffection();

        Player player = players.getPlayer("aPlayer");
        Player opponent = players.getPlayer("bPlayer");

        player.getHand().add(Card.CARDINAL);
        int initialToken = player.getTokens();
        opponent.getHand().add(Card.HANDMAIDEN);
        Deck deck = new Deck();

        gameActions.useBishop(player, opponent, deck, players, gameUI);
        assertEquals(player.getTokens(), initialToken);
        assertEquals(opponent.getHand().getCard(), Card.HANDMAIDEN);
    }

    /**
     * Test useDowagerQueen function when the user has a higher hand value than the opponent.
     * In this case, the user should be eliminated, and the result should be "UserLose".
     */
    @Test
    public void useDowagerQueenTestUserLoses() {
        // Mock the cards for user and opponent
        player.getHand().add(Card.COUNTESS);
        opponent.getHand().add(Card.BARON); 

        // Pass the parameters to the useDowagerQueen method
        String result = gameActions.useDowagerQueen(player, opponent);

        // Assert results
        assertEquals("UserLose", result);
        // Verify that the eliminate method is called on the opponent
        assertEquals(player.getHand().hasCards(), false);
    }   

    /**
     * Test useDowagerQueen function when the user has a lower hand value than the opponent.
     * In this case, the opponent should be eliminated, and the result should be "UserWin".
     */
    @Test
    public void useDowagerQueenTestUserWins() {
        // Mock the cards for user and opponent
        player.getHand().add(Card.GUARD);
        opponent.getHand().add(Card.BARON); 

        // Pass the parameters to the useDowagerQueen method
        String result = gameActions.useDowagerQueen(player, opponent);

        // Assert results
        assertEquals("UserWin", result);
        // Verify that the eliminate method is called on the opponent
        assertEquals(opponent.getHand().hasCards(), false);
    }
    
    /**
     * Test useDowagerQueen function when the user and opponents have same hand value.
     * In this case, no one should be eliminated, and the result should be "Tie".
     */
    @Test
    public void useDowagerQueenTestTie() {
        // Mock the cards for user and opponent
        player.getHand().add(Card.GUARD);
        opponent.getHand().add(Card.GUARD); 

        // Pass the parameters to the useDowagerQueen method
        String result = gameActions.useDowagerQueen(player, opponent);

        // Assert results
        assertEquals("Tie", result);
        // Verify that the eliminate method is called on the opponent
        assertEquals(opponent.getHand().hasCards(), true);
        assertEquals(player.getHand().hasCards(), true);
    }

    /**
     * Test useBaroness function to peak one other player's hand card.
     */
    @Test
    public void useBaronessTestOnePlayer() {
        PlayerList players = new PlayerList();
        players.addPlayer("aPlayer");
        players.addPlayer("bPlayer");

        Player player = players.getPlayer("aPlayer");
        Player opponent = players.getPlayer("bPlayer");
        when(gameUI.getOpponent(players, player, false, null, false)).thenReturn(opponent);

        player.getHand().add(Card.ASSASSIN);
        opponent.getHand().add(Card.GUARD);

        gameActions.useBaroness(1, players, player, gameUI, false, null);
        verify(gameUI, times(1)).showCard(opponent.getName(), Card.GUARD);
    }

    /**
     * Test useBaroness function to peak two other player's hand cards.
     */
    @Test
    public void useBaronessTestTwo() {
        PlayerList players = new PlayerList();
        players.addPlayer("aPlayer");
        players.addPlayer("bPlayer");
        players.addPlayer("cPlayer");

        Player player = players.getPlayer("aPlayer");
        Player opponent1 = players.getPlayer("bPlayer");
        Player opponent2 = players.getPlayer("cPlayer");
        when(gameUI.getOpponent(players, player, false, null, false)).thenReturn(opponent1, opponent2);

        player.getHand().add(Card.ASSASSIN);
        opponent1.getHand().add(Card.GUARD);
        opponent2.getHand().add(Card.HANDMAIDEN);

        gameActions.useBaroness(2, players, player, gameUI, false, null);
        verify(gameUI, times(1)).showCard(opponent1.getName(), Card.GUARD);
        verify(gameUI, times(1)).showCard(opponent2.getName(), Card.HANDMAIDEN);
    }

    /**
     * Cardinal will swap two players and check their cards
     * The test checks if the cards of two players are swapped
     */
    @Test
    public void useCardinalTest() {
        Player a = new Player("a");
        a.getHand().add(Card.GUARD);

        Player b = new Player("b");
        b.getHand().add(Card.KING);

        gameActions.useCardinal(a, b, gameUI);

        // check if the cards are swapped
        assertEquals(Card.KING, a.getHand().getCard());
        assertEquals(Card.GUARD, b.getHand().getCard());
    }

    /**
     * When a user targets an opponent, if the opponent has assignn then
     * the user losses and the opponent gets a new card
     */
    @Test
    public void useAssasinTest() {
        Player opponent = new Player("a");
        opponent.getHand().add(Card.ASSASSIN);
        Player user = new Player("b");
        user.getHand().add(Card.GUARD);

        Deck deck = Mockito.mock(Deck.class);
        when(deck.draw()).thenReturn(Card.BARON);

        boolean eliminated = gameActions.useAssassin(user, opponent, deck);

        // user eliminated if opponent has assasin
        assertTrue(eliminated);
        // user is eliminated and has no cards
        assertFalse(user.getHand().hasCards());

        // opponent gets a new card
        assertEquals(Card.BARON, opponent.getHand().peek(0));

    }

    /**
     * Test constable card functionality using useGuard action. 
     * When the opponent has a constable card in discard pile, the opponent should earn a token of affection.
     * This can be tested when a user plays a Guard card and guesses the opponent's card correctly.
     */
    @Test
    public void constableCardTestUsingGuardAction() {

        // Add constable card to opponents discard pile and tokens
        discardPile2.add(Card.CONSTABLE);
        opponent.setDiscardPile(discardPile2);
        opponent.setTokens(2);

        // Inject mock gameUI to simulate the printConstable() action in eliminate()
        opponent.setGameUI(gameUI);
        doNothing().when(gameUI).printConstable(eq(opponent.getName()));

        // Simulate successful guess of opponent card
        opponent.getHand().add(Card.PRIEST);
        boolean result = gameActions.useGuard("priest", opponent);

        // Asssert that the opponent receives a token of affection and is then eliminated
        assertEquals(opponent.getTokens(), 3);
        assertEquals(opponent.getHand().hasCards(), false);
        assertEquals(result, true);
    }

}
