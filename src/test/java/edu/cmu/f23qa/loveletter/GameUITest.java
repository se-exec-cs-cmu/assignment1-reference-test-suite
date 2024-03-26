package edu.cmu.f23qa.loveletter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameUITest {
    private GameUI gameUI;
    private Hand mockHand;
    private Player mockPlayer;
    private Player mockOpponent;
    private PlayerList mockPlayerList;
    private Scanner mockScanner;

    @Before
    public void init() {
        mockScanner = Mockito.mock(Scanner.class);
        mockHand = mock(Hand.class);
        gameUI = new GameUI(mockScanner);
        mockPlayer = mock(Player.class);
        mockOpponent = mock(Player.class);
        mockPlayerList = mock(PlayerList.class);

    }

    /**
     * TEST-1: Test Game With One Player Should Not Start The Game
     * @brief  This test ensures that the game does not start if there's only one player. The game requires a minimum
     *         of two players to proceed.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner and GameUI.
     *
     * @setup  Configures the mocked Scanner to simulate user input entering only one valid player name ("testUserA")
     *         and empty inputs thereafter, mimicking a scenario with insufficient players.
     *
     * @execution  Invokes the getPlayers method on the GameUI instance to simulate player entry.
     *
     * @verify  Asserts that the size of the player list is greater than 1, ensuring the game enforces the minimum
     *          requirement of two players.
     * */
    @Test
    public void testGameWithOnePlayerShouldNotStartTheGame() {
        Mockito.when(mockScanner.nextLine()).thenReturn("testUserA", "", "testUserB", "");
        List<String> players = gameUI.getPlayers();

        Assertions.assertTrue(players.size() > 1, "There should be at least 2 players to play the game");
    }

    /**
     * TEST-2: Test Adding More Than Allowed Players To Game
     * @brief  This test checks the GameUI's handling of scenarios where the number of players entered exceeds the
     *         allowed limit. It's designed to ensure that the game enforces player limits correctly.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner and GameUI.
     *
     * @setup  Configures the mocked Scanner to simulate user input entering nine player names, 
     *         which exceeds the standard player limit.
     *
     * @execution  Calls the getPlayers method on the GameUI instance to retrieve the list of players based on the
     *             simulated inputs.
     *
     * @verify  Asserts that the size of the retrieved player list will stays maximum on 8, ensuring the game
     *          correctly handles scenarios with more players than allowed.
     */
    @Test
    public void testAddingMoreThanAllowedPlayersToGame()
    {
        Mockito.when(mockScanner.nextLine()).thenReturn("1", "2", "3", "4", "5", "6", "7", "8", "9");
        List<String> players = gameUI.getPlayers();

        Assertions.assertTrue(players.size() == 8, "Number of players should not exceed 8");
    }

    /**
     * TEST-3: testGetOpponentValid
     * @brief  This test evaluates the getOpponent method in the GameUI class under normal conditions where a valid
     *         opponent is selected.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner, Player, Hand, PlayerList, and GameUI.
     *
     * @setup  Configures the mocked Scanner to simulate user input selecting "Kay" as the opponent. Sets up Player
     *         and PlayerList mocks to represent a game scenario where "Kay" is a valid target (has cards and is not
     *         protected).
     *
     * @execution  Invokes the getOpponent method, simulating a scenario where the user chooses a valid opponent.
     *
     * @verify  Asserts that the returned opponent is not null, indicating a valid selection was made.
     *          Confirms that the opponent's name matches the expected target name "Kay", verifying correct opponent
     *          selection.
     * */
    @Test
    public void testGetOpponentValid() {

        when(mockScanner.nextLine()).thenReturn("Kay");
        when(mockOpponent.getHand()).thenReturn(mockHand);
        when(mockHand.hasCards()).thenReturn(true);
        when(mockPlayer.getName()).thenReturn("May");
        when(mockOpponent.getName()).thenReturn("Kay");
        when(mockPlayerList.getPlayer("Kay")).thenReturn(mockOpponent);
        when(mockPlayerList.isOpponentsAvailable(any())).thenReturn(true);

        GameUI gameUI = new GameUI(mockScanner);

        Player opponent = gameUI.getOpponent(mockPlayerList, mockPlayer, false, null, false);

        Assert.assertNotNull("Opponent should not be null",opponent);
        Assert.assertEquals("Kay is the opponent", opponent.getName(), "Kay");
    }

    /**
     * TEST-4: Test To See If Any Opponent Is Returned If All Are Protected
     * @brief  This test evaluates the getOpponent method in the GameUI class when all potential target players are
     *         protected by a handmaiden, making them invalid targets.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner, Player, Hand, PlayerList, and GameUI.
     *
     * @setup  Configures the mocked Scanner to simulate user input for targeting players. Sets up the Player and
     *         PlayerList mocks to simulate a game state where all players, except the user, are protected.
     *
     * @execution  Invokes the getOpponent method, simulating a scenario where the user attempts to target multiple
     *             players, all of whom are protected.
     *
     * @verify  Asserts that the returned opponent is null because all players are protected.
     * */
    @Test
    public void testGetOpponentIfAllPlayersProtected() {

        when(mockScanner.nextLine()).thenReturn("Kay", "Jay");
        when(mockHand.hasCards()).thenReturn(true);
        when(mockOpponent.getHand()).thenReturn(mockHand);
        when(mockPlayer.getName()).thenReturn("May");
        when(mockOpponent.getName()).thenReturn("Kay");
        when(mockPlayerList.getPlayer("Kay")).thenReturn(mockOpponent);
        when(mockOpponent.isProtected()).thenReturn(true,true);
        when(mockPlayerList.isOpponentsAvailable(any())).thenReturn(false);

        GameUI gameUI = new GameUI(mockScanner);

        Player opponent = gameUI.getOpponent(mockPlayerList, mockPlayer, false, null, false);

        Assert.assertNull("Opponent should be null",opponent);
    }

    /**
     * TEST-5: Test If Handles User Input Correctly
     * @brief  This test checks if the user input can be trimmed properly.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner and GameUI.
     *
     * @setup  Configures the mocked Scanner to simulate user input entering player names with spaces, 
     *         mimicking a scenario with to handle player names with trimming.
     *
     * @execution  Invokes the getPlayers method on the GameUI instance to simulate player entry.
     *
     * @verify  Asserts that the player names are set without spaces.
     * */
    @Test
    public void testGameWithPlayersWithSpaces() {
        Mockito.when(mockScanner.nextLine()).thenReturn("testUserA ", " testUserB   ", "");
        List<String> players = gameUI.getPlayers();

        Assertions.assertTrue(players.get(0).equals("testUserA"));
        Assertions.assertTrue(players.get(1).equals("testUserB"));
    }

    /**
     * TEST-6: Test GetOpponent Can Allow Player as Opponent
     * @brief  This test evaluates the getOpponent method in the GameUI class if it can take the current player as opponent once needed.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner, Player, Hand, PlayerList, and GameUI.
     *
     * @setup  Configures the mocked Scanner to simulate user input selecting "Kay" as the opponent. Sets up Player
     *         and PlayerList mocks to represent a game scenario where "Kay" is a also the current player.
     *
     * @execution  Invokes the getOpponent method, simulating a scenario where the user chooses himself as the opponent.
     *
     * @verify  Asserts that the returned opponent is not null, indicating a valid selection was made.
     *          Confirms that the opponent's name matches the expected target name "Kay", verifying correct opponent
     *          selection.
     * */
    @Test
    public void testGetOpponentSameAsPlayer() {

        when(mockScanner.nextLine()).thenReturn("Kay");
        when(mockOpponent.getHand()).thenReturn(mockHand);
        when(mockHand.hasCards()).thenReturn(true);
        when(mockPlayer.getName()).thenReturn("Kay");
        when(mockOpponent.getName()).thenReturn("Kay");
        when(mockPlayerList.getPlayer("Kay")).thenReturn(mockOpponent);
        when(mockPlayerList.isOpponentsAvailable(any())).thenReturn(true);

        GameUI gameUI = new GameUI(mockScanner);

        Player opponent = gameUI.getOpponent(mockPlayerList, mockPlayer, false, null, true);

        Assert.assertNotNull("Opponent should not be null", opponent);
        Assert.assertEquals("Kay is the opponent", opponent.getName(), "Kay");
    }

    /**
     * TEST-7: Test getNumOfPlayerForBaroness early returns when no targetable players
     * @brief  This test evaluates the getNumOfPlayerForBaroness method in the GameUI class 
     *         if it can early return when all other players are not targetable.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner and GameUI.
     *
     * @setup  Configures the mocked Scanner to initialize the gameUI.
     *
     * @execution  Invokes the getNumOfPlayerForBaroness method, simulating a scenario where no other player can be targetted.
     *
     * @verify  Asserts that the return should be 0 from the getNumOfPlayerForBaroness function.
     * */
    @Test
    public void testGetNumOfPlayerForBaronessNone() {
        Player player1 = new Player("one");
        when(mockPlayerList.getTargetablePlayers()).thenReturn(Arrays.asList(player1));

        GameUI gameUI = new GameUI(mockScanner);

        Assert.assertEquals(gameUI.getNumOfPlayerForBaroness(mockPlayerList), 0);
    }

    /**
     * TEST-8: Test getNumOfPlayerForBaroness early returns only one other targetable player
     * @brief  This test evaluates the getNumOfPlayerForBaroness method in the GameUI class 
     *         if it can notify the user when there's only one other targetable player.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner and GameUI.
     *
     * @setup  Configures the mocked Scanner to initialize the gameUI.
     *
     * @execution  Invokes the getNumOfPlayerForBaroness method, simulating a scenario where one other player can be targetted.
     *
     * @verify  Asserts that the return should be 1 from the getNumOfPlayerForBaroness function.
     * */
    @Test
    public void testGetNumOfPlayerForBaronessTwo() {
        Player player1 = new Player("one");
        Player player2 = new Player("two");
        when(mockPlayerList.getTargetablePlayers()).thenReturn(Arrays.asList(player1, player2));

        GameUI gameUI = new GameUI(mockScanner);

        Assert.assertEquals(gameUI.getNumOfPlayerForBaroness(mockPlayerList), 1);
    }

    /**
     * TEST-8: Test getNumOfPlayerForBaroness early returns multiple targetable players.
     * @brief  This test evaluates the getNumOfPlayerForBaroness method in the GameUI class 
     *         if it can let the user choose number of players to target.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner and GameUI.
     *
     * @setup  Configures the mocked Scanner to initialize the gameUI and take inputs.
     *
     * @execution  Invokes the getNumOfPlayerForBaroness method, simulating a scenario when a player can choose number of players to check cards.
     *
     * @verify  Asserts that the return should be 1 from the getNumOfPlayerForBaroness function.
     * */
    @Test
    public void testGetNumOfPlayerForBaronessMultipleChooseOne() {
        Player player1 = new Player("one");
        Player player2 = new Player("two");
        Player player3 = new Player("three");
        Player player4 = new Player("four");
        when(mockPlayerList.getTargetablePlayers()).thenReturn(Arrays.asList(player1, player2, player3, player4));

        GameUI gameUI = new GameUI(mockScanner);
        when(mockScanner.nextLine()).thenReturn("1");

        Assert.assertEquals(gameUI.getNumOfPlayerForBaroness(mockPlayerList), 1);
    }

    /**
     * TEST-9: Test getNumOfPlayerForBaroness early returns multiple targetable players.
     * @brief  This test evaluates the getNumOfPlayerForBaroness method in the GameUI class 
     *         if it can let the user choose number of players to target.
     *
     * @param[in] None
     *
     * @mocks  Mocks Scanner and GameUI.
     *
     * @setup  Configures the mocked Scanner to initialize the gameUI and take inputs.
     *
     * @execution  Invokes the getNumOfPlayerForBaroness method, simulating a scenario when a player can choose number of players to check cards.
     *
     * @verify  Asserts that the return should be 2 from the getNumOfPlayerForBaroness function.
     * */
    @Test
    public void testGetNumOfPlayerForBaronessMultipleChooseTwo() {
        Player player1 = new Player("one");
        Player player2 = new Player("two");
        Player player3 = new Player("three");
        Player player4 = new Player("four");
        when(mockPlayerList.getTargetablePlayers()).thenReturn(Arrays.asList(player1, player2, player3, player4));

        GameUI gameUI = new GameUI(mockScanner);
        when(mockScanner.nextLine()).thenReturn("2");

        Assert.assertEquals(gameUI.getNumOfPlayerForBaroness(mockPlayerList), 2);
    }
}
