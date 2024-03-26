package edu.cmu.f23qa.loveletter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;

public class GameTest {
    private Game gameObj;
    private PlayerList playerListObj;
    private Deck deckOfCardsObj;
    private GameActions gameActionsObj;
    private GameUI mockUI;
    private PlayerList mockPlayerList;
    private Deck mockDeck;
    private GameActions mockGameActions;
    private Player mockPlayer;
    private Card mockCard;
    private Hand mockHand;

    @Before
    public void setUp() {
        playerListObj = new PlayerList();
        deckOfCardsObj = new Deck();
        gameActionsObj = new GameActions();


        mockUI = mock(GameUI.class);
        gameObj = new Game(playerListObj, deckOfCardsObj, gameActionsObj);

        mockPlayerList = mock(PlayerList.class);
        mockDeck = mock(Deck.class);
        mockGameActions = mock(GameActions.class);
        mockPlayer = mock(Player.class);
        mockCard = mock(Card.class);
        mockHand = mock(Hand.class);
    }

    /**
     * TEST-1: Test To Check Player List Existence
     * @brief  Test if players are getting added to the list. Idea of this test is to make sure that players are
     *         getting added to the list. Number of players or the correct players are not of importance here. Hence,
     *         the only test condition used here is to check if list is null or not null after calling setPlayers()
     *         method.
     *
     * @param[in]  None
     *
     * @mocks  A mock GameUI object is used.
     *
     * @setup  The mock GameUI is configured to return a list of player names
     *
     * @execution  The setPlayers method of the game object is called with the mock GameUI.
     *
     * @verify  Asserts that the player list obtained from the game is not null, ensuring players are correctly
     *          initialized and added to the game.
     */
    @Test
    public void testPlayerListExistence() {
        List<String> mockPlayerNames = Arrays.asList("Kay","Jay","May", "Lay");
        when(mockUI.getPlayers()).thenReturn(mockPlayerNames);

        gameObj.setPlayers(mockUI);

        List<Player> actualListOfPlayers = playerListObj.getPlayers();

        Assert.assertNotNull("Player list should exist", actualListOfPlayers);
    }

    /**
     * TEST-2: Test Adding Correct Number Of Players To The Game
     * @brief  Test if correct number of players are added to the game. Standard edition allows 2-4 people to play
     *         while the Premium edition allows 5-8 people to play. This test is a 'Happy Path' test where 4 players
     *         are added to check if they are indeed getting added to the list of players. Test condition is to
     *         compare the sizes of expected list and actual list of players. They should match; if not, then test
     *         fails.
     *
     * @param[in]  None
     *
     * @mocks  A mock GameUI object is used.
     *
     * @setup  The mock GameUI is configured to return a list of player names.
     *
     * @execution  setPlayers on the game object is invoked with the mock GameUI.
     *
     * @verify  Asserts that the size of the actual player list matches the expected size, ensuring the correct
     *          number of players are added to the game.
     */
    @Test
    public void testAddingCorrectNumberOfPlayersToGame() {
        List<String> mockPlayerNames = Arrays.asList("Kay","Jay","May", "Lay");
        when(mockUI.getPlayers()).thenReturn(mockPlayerNames);

        gameObj.setPlayers(mockUI);

        int expectedSizeOfList = mockPlayerNames.size();
        List<Player> actualListOfPlayers = playerListObj.getPlayers();
        int actualSizeofPlayerList = actualListOfPlayers.size();

        Assert.assertEquals("Number of players should match", expectedSizeOfList, actualSizeofPlayerList);
    }


    /**
     * TEST-3: Test To Check If Correct Players Have Been Added To The Game
     * @brief  Test if correct players are added to the game. This test ensures that the players entered by the user
     *         are correctly inserted into the list. There should be no bogus entry in the list of players. Each name
     *         is checked for its entry in the list. If not found, the test fails.
     *
     * @param[in]  None
     *
     * @mocks  A mock GameUI object is used.
     *
     * @setup  The mock GameUI is configured to return a list of player names.
     *
     * @execution  setPlayers on the game object is invoked with the mock GameUI.
     *
     * @verify  Iterates through each name in mockPlayerNames and asserts that each player is found in the game,
     *          confirming that all specified players are correctly added.
     */
    @Test
    public void testIfCorrectPlayersHaveBeenAdded()
    {
        List<String> mockPlayerNames = Arrays.asList("Kay", "Jay", "May", "Lay", "Roshni");
        when(mockUI.getPlayers()).thenReturn(mockPlayerNames);

        gameObj.setPlayers(mockUI);

        for(String name: mockPlayerNames){
            Assert.assertNotNull("Player should be in the game", playerListObj.getPlayer(name));
        }
    }

    /**
     * TEST-4: Test Getting Correct List Of Players
     * @brief  This test ensures that the getPlayers() method correctly retrieves the PlayerList associated with
     *         the Game instance.
     *
     * @param[in]  None
     *
     * @mocks  None
     *
     * @setup  None
     *
     * @execution  The getPlayers method is called on the gameObj.
     *
     * @verify  Asserts that the PlayerList returned by getPlayers is the same as the expected playerListObj,
     *          confirming that the method retrieves the correct list of players.
     *
     */
    @Test
    public void testGetPlayers() {

        PlayerList actualPlayers = gameObj.getPlayers();
        Assert.assertEquals("getPlayers should return the correct PlayerList", playerListObj, actualPlayers);
    }

    /**
     * TEST-5: Test Getting Correct Deck Of Cards
     * @brief  This test ensures that the getDeck() method correctly retrieves the Deck associated with
     *         the Game instance.
     *
     * @param[in]  None
     *
     * @mocks  None
     *
     * @setup  None
     *
     * @execution  The getDeck method is called on the gameObj.
     *
     * @verify  Asserts that the Deck returned by getDeck is the same as the deckOfCardsObj, ensuring that the
     *          method retrieves the correct deck of cards used in the game.
     *
     */
    @Test
    public void testGetDeck() {

        Deck actualDeck = gameObj.getDeck();
        Assert.assertEquals("getDeck should return the correct deck", deckOfCardsObj, actualDeck);
    }

    /**
     * TEST-6: Test Start Round Functionality
     * @brief  This test is designed to verify the functionality of the startRound method in the Game class.
     *         It ensures that when a new round starts in the game, the following actions are correctly executed:
     *         players are reset to their initial state, the deck of cards is properly set up for the new round,
     *         and cards are dealt to the players from the deck. This test is critical for validating that the game
     *         correctly initializes the conditions for each new round, ensuring the game progresses as expected.
     *
     * @param[in] None
     *
     * @mocks  Pre-existing mocks for PlayerList (mockPlayerList), Deck (mockDeck), and GameActions (mockGameActions)
     *         are used.
     *
     * @setup  An instance of Game is created using these mocks.
     *
     * @execution  The startRound method of the Game object is called with mockPlayerList and mockDeck.
     *
     * @verify  Checks that reset is called once on mockPlayerList.
     *          Verifies setDeck is invoked once on mockDeck.
     *          Ensures dealCards is called once with mockDeck on mockPlayerList.
     *
     */
    @Test
    public void testStartRoundUnit(){

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.startRound(mockPlayerList, mockDeck, mockUI);

        verify(mockPlayerList, times(1)).reset();

        verify(mockDeck, times(1)).setDeck(0, mockUI);

        verify(mockPlayerList, times(1)).dealCards(mockDeck);

    }

    /**
     * TEST-7.a: Integration Test For Start Round with Standard Setting
     * @brief  This is an integration test for the startRound method in Standard. It ensures that the game's initial setup for a
     *         new round is correct, particularly in terms of deck preparation and card distribution to players.
     *
     * @param[in] None
     *
     * @mocks  Utilizes mockUI for setting up player names.
     *
     * @setup  Players are set up in the game using mockUI, and startRound is then called with playerListObj and
     *         deckOfCardsObj.
     *
     * @execution  The startRound method of the Game object is invoked.
     *
     * @verify  Checks the size of the deck (deckOfCardsObj) to ensure the correct number of cards are left after setup.
     *          Verifies that each player in playerListObj has the expected number of cards in their hand, confirming
     *          proper card distribution.
     * */
    @Test
    public void testStartRoundIntegrationStandard(){

        List<String> mockPlayerNames = Arrays.asList("Kay","Jay","May", "Lay");
        when(mockUI.getPlayers()).thenReturn(mockPlayerNames);

        gameObj.setPlayers(mockUI);

        gameObj.startRound(playerListObj, deckOfCardsObj, mockUI);

        int expectedSizeOfDeck = 16 - mockPlayerNames.size() - 1;

        int actualSizeOfDeck = deckOfCardsObj.returnNumberOfCardsRemaining();

        Assert.assertEquals("There should be 11 cards in the deck", expectedSizeOfDeck, actualSizeOfDeck);

        int expectedHandSize = 1;
        for (Player player : playerListObj.getPlayers()) {
            Assert.assertEquals("Each player should have the expected number of cards in hand", expectedHandSize, player.getHand().getCards().size());
        }
    }

    /**
     * TEST-7.b: Integration Test For Start Round with Premium Setting
     * @brief  This is an integration test for the startRound method in Premium. It ensures that the game's initial setup for a
     *         new round is correct, particularly in terms of deck preparation and card distribution to players.
     *
     * @param[in] None
     *
     * @mocks  Utilizes mockUI for setting up player names.
     *
     * @setup  Players are set up in the game using mockUI, and startRound is then called with playerListObj and
     *         deckOfCardsObj.
     *
     * @execution  The startRound method of the Game object is invoked.
     *
     * @verify  Checks the size of the deck (deckOfCardsObj) to ensure the correct number of cards are left after setup.
     *          Verifies that each player in playerListObj has the expected number of cards in their hand, confirming
     *          proper card distribution.
     * */
    @Test
    public void testStartRoundIntegrationPremium(){

        List<String> mockPlayerNames = Arrays.asList("1","2","3", "4", "5", "6");
        when(mockUI.getPlayers()).thenReturn(mockPlayerNames);

        gameObj.setPlayers(mockUI);

        gameObj.startRound(playerListObj, deckOfCardsObj, mockUI);

        int expectedSizeOfDeck = 25; // 32 - 6 - 1

        int actualSizeOfDeck = deckOfCardsObj.returnNumberOfCardsRemaining();

        Assert.assertEquals("There should be 25 cards in the deck", expectedSizeOfDeck, actualSizeOfDeck);

        int expectedHandSize = 1;
        for (Player player : playerListObj.getPlayers()) {
            Assert.assertEquals("Each player should have the expected number of cards in hand", expectedHandSize, player.getHand().getCards().size());
        }
    }

    /**
     * TEST-8: Unit Test For Start Turn
     * @brief  This test, testStartTurnUnit, is designed to validate the startTurn method in the Game class
     *
     * @param[in]  None
     *
     * @mocks  Utilizes a spy on a new Game instance, along with mocks for Player, Hand, Deck, and Card.
     *
     * @setup  Configures the mocks to simulate a player ready to take a turn, including drawing a card from the deck.
     *
     * @execution  Calls startTurn on the Game instance.
     *
     * @verify  Confirms hasCards is called on the player's hand.
     *          Verifies the player's name retrieval and UI interaction to show the player's turn.
     *          Checks that a card is drawn from the deck.
     *          Ensures playTurn is invoked with the correct arguments.
     * */
    @Test
    public void testStartTurnUnit() {

        Game game = spy(new Game(mockPlayerList, mockDeck, mockGameActions));

        when(mockPlayer.getHand()).thenReturn(mockHand);
        when(mockHand.hasCards()).thenReturn(true);
        when(mockDeck.draw()).thenReturn(mockCard);
        doNothing().when(game).playTurn(any(Player.class), eq(mockCard), any(GameUI.class));
        when(mockPlayer.getName()).thenReturn("Kay");

        game.startTurn(mockPlayer, mockUI);

        verify(mockPlayer.getHand(), times(1)).hasCards();
        verify(mockPlayer).getName();
        verify(mockUI).showPlayerTurn("Kay");
        verify(mockDeck).draw();
        verify(game).playTurn(mockPlayer, mockCard, mockUI);

    }

    /**
     * TEST-9: Test Empty Player List for Round Winner Declaration
     * @brief  This test checks the declareRoundWinner method's handling of an empty player list, ensuring no
     *         winners are declared.
     *
     * @param[in] None
     *
     * @mocks  Mocks PlayerList.
     *
     * @setup  Configures PlayerList to return an empty list of round winners.
     *
     * @execution  Executes declareRoundWinner on a Game instance with the mocked PlayerList.
     *
     * @verify  Verifies that getRoundWinners is called on PlayerList.
     *          Asserts that the winners list is empty, aligning with an empty player list scenario.
     * */
    @Test
    public void testEmptyPlayerList() {

        when(mockPlayerList.getRoundWinners()).thenReturn(Arrays.asList());

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);
        List<Player> winners = game.declareRoundWinner(mockPlayerList);

        verify(mockPlayerList, times(1)).getRoundWinners();

        Assert.assertTrue("Winner list should be empty for an empty player list", winners.isEmpty());
    }

    /**
     * TEST-10: Test Declare Round Winners with Multiple Winners
     * @brief  Tests the declareRoundWinner method for scenarios where there are multiple winners in a round.
     *
     * @param[in] None
     *
     * @mocks  Mocks PlayerList and uses real Player instances.
     *
     * @setup  Configures PlayerList to return a list with two winners, Kay and Jay.
     *
     * @execution  Calls declareRoundWinner on a Game instance, passing the mocked PlayerList.
     *
     * @verify  Verifies that getRoundWinners is called on PlayerList.
     *          Asserts the correct number of winners (two) are returned.
     *          Checks that both Kay and Jay are in the list of winners.
     *          Confirms each winner is awarded a token.
     * */
    @Test
    public void testDeclareRoundWinnersWithMultipleWinners() {

        Player Kay = new Player("Kay");
        Player Jay = new Player("Jay");
        when(mockPlayerList.getRoundWinners()).thenReturn(Arrays.asList(Kay, Jay));

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        List<Player> winners = game.declareRoundWinner(mockPlayerList);

        verify(mockPlayerList, times(1)).getRoundWinners();

        Assert.assertEquals("There should be two winners",2, winners.size());
        Assert.assertTrue("Kay and Jay should be winners", winners.containsAll(Arrays.asList(Kay, Jay)));
        Assert.assertEquals("Kay should have 1 token",1, Kay.getTokens());
        Assert.assertEquals("Jay should have 1 token",1, Jay.getTokens());
    }

    /**
     * TEST-11: Test Play Turn with Non-Royal Card
     * @brief  This is a unit test for the method playTurn(). It examines the playTurn method in the Game class
     *         when a normal card (card other than a royal card and countess card) is played.
     *
     * @param[in] None
     *
     * @mocks  Uses a spy on the Game instance, and mocks for Player, Hand, Card, and GameUI.
     *
     * @setup  Configures the mock Player to have a hand that returns a non-royalty card.
     *
     * @execution  Invokes playTurn on the game object with the mock Player, a normal card, and the mock GameUI.
     *
     * @verify  Confirms turnOffProtection is called on the player.
     *          Checks that a card is added to the player's hand.
     *          Verifies the invocation of getCard from GameUI.
     *          Ensures playCard is called with the correct parameters.
     * */
    @Test
    public void testPlayTurnWithNormalCard() {

        Game game = spy(new Game(mockPlayerList, mockDeck, mockGameActions));

        when(mockPlayer.getHand()).thenReturn(mockHand);
        when(mockUI.getCard(mockPlayer)).thenReturn(mockCard);
        when(mockHand.getRoyaltyPos()).thenReturn(-1);
        doNothing().when(game).playCard(mockCard, mockPlayer, mockUI, false, null);

        game.playTurn(mockPlayer, mockCard, mockUI);

        verify(mockPlayer, times(1)).turnOffProtection();
        verify(mockHand, times(1)).add(mockCard);
        verify(mockUI, times(1)).getCard(mockPlayer);
        verify(game, times(1)).playCard(mockCard, mockPlayer, mockUI, false, null);
    }

    /**
     * TEST-12: Test Play Turn with Royal and Countess Card
     * @brief  This test focuses on the scenario in the playTurn method where a player has both a royal card and a
     *         Countess in their hand. The test ensures that the game logic correctly enforces the rule that if a
     *         player has a royal card and the Countess, they must play the Countess.
     *
     * @param[in] None
     *
     * @mocks  Utilizes a spy on Game and mocks for Card, Hand, and Player.
     *
     * @setup  Configures the player's hand to have a royal card and a Countess, and sets up the removal of the Countess
     *         from the hand.
     *
     * @execution  Executes the playTurn method, simulating a player's turn with these specific cards.
     *
     * @verify  Checks if the player's protection is turned off.
     *          Verifies that a card (not necessarily the Countess) is added to the player's hand.
     *          Ensures GameUI.getCard is not called, as the Countess should be automatically played.
     *          Confirms that playCard is invoked with the Countess, not the player’s choice.
     * */
    @Test
    public void testPlayTurnWithRoyalCardAndCountess(){

        Card mockCountessCard = mock(Card.class);
        Game game = spy(new Game(mockPlayerList, mockDeck, mockGameActions));

        when(mockPlayer.getHand()).thenReturn(mockHand);
        when(mockHand.getRoyaltyPos()).thenReturn(1);
        when(mockHand.getCardPos(Card.COUNTESS)).thenReturn(0);
        when(mockHand.remove(0)).thenReturn(mockCountessCard);
        doNothing().when(game).playCard(mockCountessCard, mockPlayer, mockUI, false, null);

        game.playTurn(mockPlayer, mockCard, mockUI);

        verify(mockPlayer, times(1)).turnOffProtection();
        verify(mockHand, times(1)).add(mockCard);
        verify(mockUI, never()).getCard(mockPlayer);
        verify(game, times(1)).playCard(mockCountessCard, mockPlayer, mockUI, false, null);

    }

    /**
     * TEST-13: Test Play Card With Guard
     * @brief  Tests the playCard method with the Guard card, ensuring correct gameplay mechanics when a Guard card is
     *         used.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, and GameActions.
     *
     * @setup  Configures a scenario where the Guard card is played. Mocks the opponent and discard pile, and sets
     *         up the Guard card's guess action.
     *
     * @execution  Executes playCard on the Game instance with the Guard card, the mocked player, and GameUI.
     *
     * @verify  Checks that the Guard card is added to the discard pile.
     *          Verifies the process of getting an opponent and making a Guard guess.
     *          Confirms the use of the Guard card's action and the display of the guess result.
     * */
    @Test
    public void testPlayCardWithGuard(){

        Player mockOpponent = mock(Player.class);
        Card guardCard = Card.GUARD;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, false)).thenReturn(mockOpponent);
        when(mockUI.getGuardGuess()).thenReturn("Priest");
        when(mockGameActions.useGuard("Priest", mockOpponent)).thenReturn(true);
        doNothing().when(mockUI).showGuardGuess(true);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(guardCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(guardCard);
        verify(mockUI, times(1)).getOpponent(any(), eq(mockPlayer), eq(false), eq(null), eq(false));
        verify(mockUI, times(1)).getGuardGuess();
        verify(mockGameActions, times(1)).useGuard(eq("Priest"), eq(mockOpponent));
        verify(mockUI, times(1)).showGuardGuess(eq(true));

    }

    /**
     * TEST-14: Test Play Card With Priest
     * @brief  This test evaluates the functionality of the playCard method when the Priest card is played. It checks
     *         the card's unique action of revealing an opponent's card.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, GameActions, and an opponent Player.
     *
     * @setup  Sets up the game environment to play the Priest card, simulating the action of revealing an opponent's card.
     *
     * @execution  Executes the playCard method on the Game instance with the Priest card, the mocked player, and the GameUI.
     *
     * @verify  Confirms that the Priest card is added to the discard pile.
     *          Verifies the process of selecting an opponent and executing the Priest card's action.
     *          Ensures the display of the revealed card by the GameUI.
     * */
    @Test
    public void testPlayCardWithPriest(){

        Player mockOpponent = mock(Player.class);
        Card priestCard = Card.PRIEST;
        Card opponentCard = Card.PRINCE;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, false)).thenReturn(mockOpponent);
        when(mockGameActions.usePriest(mockOpponent)).thenReturn(opponentCard);
        when(mockOpponent.getName()).thenReturn("Kay");
        doNothing().when(mockUI).showCard("Kay", opponentCard);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(priestCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(priestCard);
        verify(mockUI, times(1)).getOpponent(any(), eq(mockPlayer), eq(false), eq(null), eq(false));
        verify(mockGameActions, times(1)).usePriest(eq(mockOpponent));
        verify(mockUI, times(1)).showCard(eq("Kay"), eq(opponentCard));

    }

    /**
     * TEST-15: Test Play Card With Baron
     * @brief  This test examines the functionality of the playCard method when a Baron card is played. It focuses
     *         on the card's action of comparing hands with an opponent and determining the outcome.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, GameActions, and an opponent Player.
     *
     * @setup  Sets up the game environment for the Baron card play, including the selection of an opponent.
     *
     * @execution  Executes playCard on the Game instance with the Baron card, the mocked player, and the GameUI.
     *
     * @verify  Ensures the Baron card is added to the discard pile.
     *          Confirms the process of selecting an opponent for the Baron card's comparison action.
     *          Verifies the execution of the Baron's hand comparison action through GameActions.
     * */
    @Test
    public void testPlayCardWithBaron(){

        Player mockOpponent = mock(Player.class);
        Card baronCard = Card.BARON;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, false)).thenReturn(mockOpponent);
        doNothing().when(mockGameActions).useBaron(mockPlayer, mockOpponent, mockUI);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(baronCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(baronCard);
        verify(mockUI, times(1)).getOpponent(any(), eq(mockPlayer), eq(false), eq(null), eq(false));
        verify(mockGameActions, times(1)).useBaron(eq(mockPlayer), eq(mockOpponent), eq(mockUI));

    }

    /**
     * TEST-16: Test Play Card With Handmaiden
     * @brief  This test evaluates the playCard method's functionality when the Handmaiden card is played.
     *         It focuses on the card's action of providing protection to the player.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, and GameActions.
     *
     * @setup  Prepares for the Handmaiden card play, simulating its effect of providing player protection.
     *
     * @execution  Executes playCard on the Game instance with the Handmaiden card, the mocked player, and the GameUI.
     *
     * @verify  Confirms that the Handmaiden card is added to the discard pile.
     *          Verifies the execution of the Handmaiden's protection action through GameActions.
     *          Ensures that the game UI displays the protection status to the player.
     * */
    @Test
    public void testPlayCardWithHandmaiden(){

        Card handmaidenCard = Card.HANDMAIDEN;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        Mockito.when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        Mockito.doNothing().when(mockGameActions).useHandmaiden(mockPlayer);
        doNothing().when(mockUI).showProtection();

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(handmaidenCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(handmaidenCard);
        verify(mockGameActions, times(1)).useHandmaiden(eq(mockPlayer));
        verify(mockUI, times(1)).showProtection();

    }

    /**
     * TEST-17: Test Play Card With Prince
     * @brief  This test assesses the functionality of the playCard method when the Prince card is played.
     *         It checks the card's unique action of forcing an opponent to discard their hand.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, GameActions, and an opponent Player.
     *
     * @setup  Sets up for the play of the Prince card, including the selection of an opponent.
     *
     * @execution  Executes the playCard method on the Game instance with the Prince card, the mocked player, and
     *             the GameUI.
     *
     * @verify  Ensures the Prince card is added to the discard pile.
     *          Verifies the selection of an opponent for the Prince card's action.
     *          Confirms the execution of the Prince's action to make the opponent discard their hand.
     * */
    @Test
    public void testPlayCardWithPrince(){

        Player mockOpponent = mock(Player.class);
        Card princeCard = Card.PRINCE;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, true)).thenReturn(mockOpponent);
        doNothing().when(mockGameActions).usePrince(mockOpponent, mockDeck);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(princeCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(princeCard);
        verify(mockUI, times(1)).getOpponent(any(), eq(mockPlayer), eq(false), eq(null), eq(true));
        verify(mockGameActions, times(1)).usePrince(eq(mockOpponent), eq(mockDeck));

    }

    /**
     * TEST-18: Test Play Card With King
     * @brief  This test checks the functionality of the playCard method when the King card is played, focusing on
     *         the card's action of exchanging hands with an opponent.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, GameActions, and an opponent Player.
     *
     * @setup  Prepares for the play of the King card, including the selection of an opponent for the hand exchange.
     *
     * @execution  Executes the playCard method on the Game instance with the King card, the mocked player, and
     *             the GameUI.
     *
     * @verify  Confirms the King card is added to the discard pile.
     *          Verifies the process of selecting an opponent for the King card's hand exchange action.
     *          Checks the execution of the King's action through GameActions.
     * */
    @Test
    public void testPlayCardWithKing(){

        Player mockOpponent = mock(Player.class);
        Card kingCard = Card.KING;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, false)).thenReturn(mockOpponent);
        doNothing().when(mockGameActions).useKing(mockPlayer, mockOpponent);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(kingCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(kingCard);
        verify(mockUI, times(1)).getOpponent(any(), eq(mockPlayer), eq(false), eq(null), eq(false));
        verify(mockGameActions, times(1)).useKing(eq(mockPlayer), eq(mockOpponent));

    }

    /**
     * TEST-19: Test Play Card With Princess
     * @brief  This test assesses the playCard method's functionality with the Princess card, focusing on the card's
     *         unique action that affects the game's outcome.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player and DiscardPile.
     *
     * @setup  Prepares the environment for playing the Princess card, including setting up the discard pile.
     *
     * @execution  Executes the playCard method on the Game instance with the Princess card and the mocked player.
     *
     * @verify  Ensures the Princess card is added to the discard pile.
     *          Verifies the execution of the Princess card's action through GameActions.
     * */
    @Test
    public void testPlayCardWithPrincess(){

        Card princessCard = Card.PRINCESS;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        doNothing().when(mockGameActions).usePrincess(mockPlayer);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(princessCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(princessCard);
        verify(mockGameActions, times(1)).usePrincess(eq(mockPlayer));

    }

    /**
     * TEST-20: Test Start Normal Game Flow
     * @brief  This test evaluates the normal game flow using the start method of the Game class. It simulates a
     *         typical game scenario, progressing through rounds until a winner is declared.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, PlayerList, Deck, GameActions, and uses a spy on Game.
     *
     * @setup  Configures the game environment to simulate multiple rounds of play with no immediate winner, eventually
     *         leading to a winner's declaration.
     *
     * @execution  Executes the start method on the Game instance, simulating a series of rounds and turns.
     *
     * @verify  Checks for calls to getGameWinner, moreThanSinglePlayerLeft, hasMoreCards, getCurrentPlayer, and
     *          interactions with GameUI for showing round and game winners.
     * */
    @Test
    public void testStartNormalGameFlow(){

        Player mockWinner = mock(Player.class);
        Game game = spy(new Game(mockPlayerList, mockDeck, mockGameActions));
        when(mockPlayerList.isZeroGameWinner()).thenReturn(true, true, false, false);
        when(mockPlayerList.isTwoOrMoreGameWinners()).thenReturn(false);

        doNothing().when(game).startRound(mockPlayerList, mockDeck, mockUI);
        when(mockPlayerList.moreThanSinglePlayerLeft()).thenReturn(true, true, false, true, false);
        when(mockDeck.hasMoreCards()).thenReturn(true);

        when(mockPlayerList.getCurrentPlayer()).thenReturn(mockPlayer);
        doNothing().when(game).startTurn(mockPlayer, mockUI);
        doReturn(Arrays.asList(mockPlayer)).when(game).declareRoundWinner(mockPlayerList);
        doReturn(Arrays.asList(mockWinner)).when(mockPlayerList).getGameWinners();
        when(mockWinner.getName()).thenReturn("Kay");
        when(mockPlayer.getName()).thenReturn("Kay");

        game.start(mockUI);

        verify(mockPlayerList, atLeastOnce()).initializeTargetAffection();
        verify(mockPlayerList, atLeastOnce()).isZeroGameWinner();
        verify(mockPlayerList, atLeastOnce()).moreThanSinglePlayerLeft();
        verify(mockDeck, atLeastOnce()).hasMoreCards();
        verify(mockPlayerList, atLeastOnce()).getCurrentPlayer();
        verify(mockUI, times(1)).showGameWinner(anyString());

    }

    /**
     * TEST-21: Test Start with Immediate Winner
     * @brief  This test examines the start method's handling of a game scenario where an immediate winner is
     *         identified without progressing through multiple rounds.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, PlayerList, and GameActions. Uses a real Game instance.
     *
     * @setup  Configures the PlayerList mock to return an immediate game winner.
     *
     * @execution  Executes the start method on the Game instance, expecting the game to identify a winner immediately.
     *
     * @verify  Confirms that getGameWinner is called and the GameUI shows the winner's name.
     *          Verifies that methods like moreThanSinglePlayerLeft, hasMoreCards, and getCurrentPlayer are never called,
     *          indicating no progression to further rounds.
     * */
    @Test
    public void testStartWithImmediateWinner(){
        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        Player mockWinner = mock(Player.class);

        when(mockPlayerList.isZeroGameWinner()).thenReturn(false);
        when(mockPlayerList.isTwoOrMoreGameWinners()).thenReturn(false);
        when(mockPlayerList.getGameWinners()).thenReturn(Arrays.asList(mockWinner));
        when(mockWinner.getName()).thenReturn("Kay");

        game.start(mockUI);

        verify(mockPlayerList, times(1)).isZeroGameWinner();
        verify(mockPlayerList, times(1)).isTwoOrMoreGameWinners();
        verify(mockPlayerList, times(1)).getGameWinners();
        verify(mockUI, times(1)).showGameWinner(mockWinner.getName());

        verify(mockPlayerList, never()).moreThanSinglePlayerLeft();
        verify(mockDeck, never()).hasMoreCards();
        verify(mockPlayerList, never()).getCurrentPlayer();

    }

    /**
     * TEST-22: Test Start with No Card in Deck
     * @brief  This test assesses the start method's behavior when the deck runs out of cards before a game
     *         winner is declared.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, PlayerList, Deck, and GameActions. Uses a spy on Game.
     *
     * @setup  Configures the game environment to simulate a situation where the deck runs out of cards without a
     *         clear winner.
     *
     * @execution  Executes the start method on the Game instance, simulating game rounds until the deck is empty.
     *
     * @verify  Verifies that getGameWinner is called and the deck's card availability is checked.
     *          Confirms that getCurrentPlayer is never called, indicating no further turns are taken.
     *          Checks that the game UI displays the game winner once declared.
     * */
    @Test
    public void testStartWithNoCardInDeck(){

        Player mockWinner = mock(Player.class);
        Game game = spy(new Game(mockPlayerList, mockDeck, mockGameActions));

        when(mockPlayerList.isZeroGameWinner()).thenReturn(true, false);
        when(mockPlayerList.isTwoOrMoreGameWinners()).thenReturn(false);
        doNothing().when(game).startRound(mockPlayerList, mockDeck, mockUI);
        when(mockPlayerList.moreThanSinglePlayerLeft()).thenReturn(true);
        when(mockDeck.hasMoreCards()).thenReturn(false);

        doReturn(Arrays.asList(mockPlayer)).when(game).declareRoundWinner(mockPlayerList);
        when(mockPlayerList.getGameWinners()).thenReturn(Arrays.asList(mockWinner));
        when(mockWinner.getName()).thenReturn("Kay");
        when(mockPlayer.getName()).thenReturn("Kay");

        game.start(mockUI);

        verify(mockPlayerList, atLeastOnce()).isZeroGameWinner();
        verify(mockDeck, atLeastOnce()).hasMoreCards();
        verify(mockPlayerList, never()).getCurrentPlayer();
        verify(mockUI, times(1)).showGameWinner(anyString());
    }

    /**
     * TEST-23: Test Declare Round Winner with Single Winner
     * @brief  Evaluates the declareRoundWinner method to ensure it correctly identifies and awards a
     *         single round winner.
     *
     * @param[in] None
     *
     * @mocks  Mocks PlayerList and creates a real Player instance.
     *
     * @setup  Configures PlayerList to return a list with a single winner.
     *
     * @execution  Calls declareRoundWinner on a Game instance, passing the mocked PlayerList.
     *
     * @verify  Checks that getRoundWinners is called on PlayerList.
     *          Asserts that only one winner is returned and it's the expected player.
     *          Confirms the winner has been awarded the correct number of tokens.
     * */
    @Test
    public void testDeclareRoundWinnerWithSingleWinner() {

        Player winner = new Player("Kay");

        when(mockPlayerList.getRoundWinners()).thenReturn(Arrays.asList(winner));

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        List<Player> winners = game.declareRoundWinner(mockPlayerList);

        verify(mockPlayerList, times(1)).getRoundWinners();

        Assert.assertEquals("There should be only one winner",1, winners.size());
        Assert.assertTrue("Kay should be the winner", winners.contains(winner));
        Assert.assertEquals("Kay should have 1 token",1, winner.getTokens());
    }

    /**
     * TEST-24: Test Play Card With Dowager Queen
     * @brief  This test examines the functionality of the playCard method when a DowagerQueen card is played. It focuses
     *         on the card's action of comparing hands with an opponent and determining the outcome.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, GameActions, and an opponent Player.
     *
     * @setup  Sets up the game environment for the DowagerQueen card play, including the selection of an opponent.
     *
     * @execution  Executes playCard on the Game instance with the Dowager Queen card, the mocked player, and the GameUI.
     *
     * @verify  Ensures the Dowager Queen card is added to the discard pile.
     *          Confirms the process of selecting an opponent for the Dowager Queen card's comparison action.
     *          Verifies the execution of the Dowager Queens's hand comparison action through GameActions.
     *          Verifies the execution of the GameUI's display of the Dowager Queen's comparison results.
     * */
    @Test
    public void testPlayCardWithDowagerQueen(){

        Player mockOpponent = mock(Player.class);
        Card dowagerQueenCard = Card.DOWAGERQUEEN;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, false)).thenReturn(mockOpponent);
        when(mockGameActions.useDowagerQueen(mockPlayer, mockOpponent)).thenReturn("UserWin");

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(dowagerQueenCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(dowagerQueenCard);
        verify(mockUI, times(1)).getOpponent(any(), eq(mockPlayer), eq(false), eq(null), eq(false));
        verify(mockUI, times(1)).dowagerQueenResult(eq("UserWin"), eq(mockPlayer), eq(mockOpponent));
        verify(mockGameActions, times(1)).useDowagerQueen(eq(mockPlayer), eq(mockOpponent));
    }

    /**
     * TEST-25: Test Play Card With Bishop
     * @brief  This test examines the functionality of the playCard method when a Bishop card is played. It focuses
     *         on the card's action of adding a token when the player has a correct guess on the opponent.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, GameActions, and an opponent Player.
     *
     * @setup  Sets up the game environment for the Bishop card play, including the selection of an opponent.
     *
     * @execution  Executes playCard on the Game instance with the Bishop card, the mocked player, and the GameUI.
     *
     * @verify  Ensures the Bishop card is added to the discard pile.
     *          Confirms the process of selecting an opponent for the Bishop card.
     *          Verifies the execution of the Bishop's hand action through GameActions.
     * */
    @Test
    public void testPlayCardWithBishop() {

        Player mockOpponent = mock(Player.class);
        Card bishopCard = Card.BISHOP;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, false)).thenReturn(mockOpponent);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(bishopCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(bishopCard);
        verify(mockUI, times(1)).getOpponent(any(), eq(mockPlayer), eq(false), eq(null), eq(false));
        verify(mockGameActions, times(1)).useBishop(eq(mockPlayer), eq(mockOpponent), eq(mockDeck), eq(mockPlayerList), eq(mockUI));
    }
  
     /** 
     * TEST-26: Test Play Card With Cardinal
     * @brief  This test examines the functionality of the playCard method when a Cardinal card is played. It focuses
     *         on the card's action of swapping two players card and checking their card
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, GameActions, and mockUI.
     *
     * @setup  Sets up the game environment for the Cardinal card play, including selecting two players.
     *
     * @execution  Executes playCard on the Game instance with the Cardinal card, the mocked player, and the GameUI.
     *
     * @verify Verifies the process of getting list of available players to select
     *          Verifies the process of getting selection from players
     *          Verifies the execution of Cardinal which swaps the two players card
     * */
    @Test
    public void testPlayCardWithCardinal(){
        Card cardinal = Card.CARDINAL;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);
        Player a = new Player("a");
        Player b = new Player("b");
        List<Player> targetablePlayerList = Arrays.asList(a, b);

        // using mock UI, provide first person twice, this results in mock UI asking for the second person again
        // total mock UI interaction first interaction (positive), second interaction (negative, does not like that
        // the same person is given), third interaction (positive).
        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockPlayerList.getTargetablePlayers()).thenReturn(targetablePlayerList);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, true)).thenReturn(a, a, b);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(cardinal, mockPlayer, mockUI, false, null);

        verify(mockPlayerList, times(1)).getTargetablePlayers();
        // three calls are made, first call is getting first player, second player is for getting second player
        // as second player is same as first call, a third call is made so a new second player is given
        verify(mockUI, times(3)).getOpponent(mockPlayerList,  mockPlayer, false, null, true);
        verify(mockGameActions, times(1)).useCardinal(a, b, mockUI);
    }

    /** 
     * TEST-27: Test Play Card With Baroness
     * @brief  This test examines the functionality of the playCard method when a Baroness card is played. It tests that
     *          there has to be either one or two player to peak without protection.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, GameActions, and mockUI.
     *
     * @setup  Sets up the game environment for the Baroness card play.
     *
     * @execution  Executes playCard on the Game instance with the Baroness card, the mocked player, and the GameUI.
     *
     * @verify Verifies that when there's not enough targetable player in the round, the Baroness will do nothing.
     * */
    @Test
    public void testPlayCardWithBaronessNone() {
        DiscardPile mockDiscardPile = mock(DiscardPile.class);
        Player mockOpponent = mock(Player.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getNumOfPlayerForBaroness(mockPlayerList)).thenReturn(0);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, false)).thenReturn(mockOpponent);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(Card.BARONESS, mockPlayer, mockUI, false, null);

        verify(mockUI, times(1)).printWhenNoPlayerCanBeTarget();
    }

    /** 
     * TEST-28: Test Play Card With Baroness
     * @brief  This test examines the functionality of the playCard method when a Baroness card is played. It focuses on the 
     *          functionality that peaks the hand card of one or two other player.
     *
     * @param[in] None
     *
     * @mocks  Mocks Player, DiscardPile, GameUI, GameActions, and mockUI.
     *
     * @setup  Sets up the game environment for the Baroness card play.
     *
     * @execution  Executes playCard on the Game instance with the Baroness card, the mocked player, and the GameUI.
     *
     * @verify Ensures the Baroness card is added to the discard pile.
     *         Confirms the process of selecting an opponent for the Baroness card.
     *         Verifies the execution of the Baroness's hand action through GameActions.
     * */
    @Test
    public void testPlayCardWithBaroness() {
        DiscardPile mockDiscardPile = mock(DiscardPile.class);
        Player mockOpponent = mock(Player.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getNumOfPlayerForBaroness(mockPlayerList)).thenReturn(1);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, false)).thenReturn(mockOpponent);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(Card.BARONESS, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(Card.BARONESS);
        verify(mockUI, times(0)).printWhenNoPlayerCanBeTarget();
        verify(mockGameActions, times(1)).useBaroness(1, mockPlayerList, mockPlayer, mockUI, false, null);
    }

    /**
     * TEST-29: Test Play Card Functionality with Syncophant Card
     * @brief  This test verifies the playCard method in the Game class when playing the Syncophant card.
     *
     * @param[in]  None
     *
     * @mocks  Mocks Player, DiscardPile, PlayerList, Deck, GameActions, and UI instances to simulate the game environment and interactions.
     *
     * @setup  Initializes mocks for Player (mockOpponent), DiscardPile (mockDiscardPile), PlayerList, Deck, and GameActions.
     *         Configures mock behaviors for getting discarded cards and selecting an opponent.
     *
     * @execution  Creates a Game instance with the mocked objects. Plays the Syncophant card using the playCard method.
     *
     * @verify  Verifies that the Syncophant card is added to the discard pile exactly once.
     *          Verifies that the method to get an opponent is called exactly once with specified parameters.
     */
    @Test
    public void testPlayCardWithSyncophant(){

        Player mockOpponent = mock(Player.class);
        Card syncophantCard = Card.SYNCOPHANT;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, true)).thenReturn(mockOpponent);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(syncophantCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(syncophantCard);
        verify(mockUI, times(1)).getOpponent(any(), eq(mockPlayer), eq(false), eq(null), eq(true));

    }

    /**
     * TEST-30: Test Play Card Functionality with Jester Card
     * @brief  This test verifies the playCard method in the Game class when playing the Jester card.
     *
     * @param[in]  None
     *
     * @mocks  Mocks Player, DiscardPile, PlayerList, Deck, GameActions, and UI instances to simulate the game environment and interactions.
     *
     * @setup  Initializes mocks for Player (mockOpponent), DiscardPile (mockDiscardPile), PlayerList, Deck, and GameActions.
     *         Configures mock behaviors for getting discarded cards, selecting an opponent, and the use of the Jester card.
     *
     * @execution  Creates a Game instance with the mocked objects. Plays the Jester card using the playCard method.
     *
     * @verify  Verifies that the Jester card is added to the discard pile exactly once.
     *          Verifies that the method to get an opponent is called exactly once with specified parameters.
     *          Verifies that the useJester method in mockGameActions is invoked exactly once with the specified players.
     */
    @Test
    public void testPlayCardWithJester(){

        Player mockOpponent = mock(Player.class);
        Card jesterCard = Card.JESTER;
        DiscardPile mockDiscardPile = mock(DiscardPile.class);

        when(mockPlayer.getDiscarded()).thenReturn(mockDiscardPile);
        when(mockUI.getOpponent(mockPlayerList, mockPlayer, false, null, true)).thenReturn(mockOpponent);
        //mockGameActions.useJester(mockPlayer, mockOpponent);

        Game game = new Game(mockPlayerList, mockDeck, mockGameActions);

        game.playCard(jesterCard, mockPlayer, mockUI, false, null);

        verify(mockDiscardPile, times(1)).add(jesterCard);
        verify(mockUI, times(1)).getOpponent(any(), eq(mockPlayer), eq(false), eq(null), eq(true));
        verify(mockGameActions, times(1)).useJester(eq(mockPlayer), eq(mockOpponent));

    }

    /**
     * TEST-31: Test Play Turn with True Syncophant Flag in Game
     * @brief  This test verifies the playTurn method in the Game class when the Syncophant flag is true.
     *
     * @param[in]  None
     *
     * @mocks  Mocks Player, Hand, Card, UI, GameActions, Deck, and PlayerList instances to simulate the game environment and interactions.
     *         Uses a spy on the Game instance to monitor interactions.
     *
     * @setup  Initializes mocks for Player (syncophantChosenPlayer), Hand (mockHand), Card (mockCard), UI, GameActions, Deck, and PlayerList.
     *         Sets up the Game instance with necessary configurations for testing the playTurn method under the condition where the Syncophant flag is true.
     *
     * @execution  Sets the Syncophant flag and the Syncophant chosen player in the game instance.
     *             Executes the playTurn method with the mockPlayer and mockCard.
     *
     * @verify  Verifies that the player's protection is turned off once.
     *          Verifies that a card is added to the player's hand once.
     *          Verifies that the getCard method is called once for the player.
     *          Verifies that the playCard method is called once with the specified parameters.
     */
    @Test
    public void testPlayTurnWithTrueSyncophantFlag() {

        Player syncophantChosenPlayer = mock(Player.class);
        Game game = spy(new Game(mockPlayerList, mockDeck, mockGameActions));


        when(mockPlayer.getHand()).thenReturn(mockHand);
        when(mockUI.getCard(mockPlayer)).thenReturn(mockCard);
        when(mockHand.getRoyaltyPos()).thenReturn(-1);
        doNothing().when(game).playCard(mockCard, mockPlayer, mockUI, true, syncophantChosenPlayer);

        game.setSyncophantFlag(true);
        game.setSyncophantChosenPlayer(syncophantChosenPlayer);
        game.playTurn(mockPlayer, mockCard, mockUI);

        verify(mockPlayer, times(1)).turnOffProtection();
        verify(mockHand, times(1)).add(mockCard);
        verify(mockUI, times(1)).getCard(mockPlayer);
        verify(game, times(1)).playCard(mockCard, mockPlayer, mockUI, true, syncophantChosenPlayer);
    }
}