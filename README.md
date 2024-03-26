# About The Repository

This repository is built as a reference for a sample test suite built for 3 selected classes in the code base. The test suite contains 3 Test classes - 

1. GameTest.java - A test class for the main game logic. This class handles the overall flow of the game.
2. GameActionsTest.java - A test class built for testing the individual card actions in the game. 
3. GameUITest.java - A test class for the UI related functionalities (Command Line Interactions in this case) for the game.

The test class covers key unit tests as well as integration tests, where there is an involvement of multiple entities or modules. Refer to the comments in the test classes, for descriptions about individual tests.

## The Game - Love Letter

This repository provides a full implementation of the popular board game, Love Letter, with 16 unique cards and is built for 1-8 players. 


## Installation

### GitHub Codespaces

Since the game is played via a command-line interface, GitHub Codespaces is the easiest way to work with this project.
To get up and running:

1. In the current repository, go to `<> Code > Codespaces > Create codespace on main`.
2. Wait until the container finishes building.
3. After the Codespaces open, wait until you receive the message that the setup is Done and the terminal closes.
4. After the setup is done, **close the Terminal and open a new window to use any of the pre-installed tools**.

![Building the final setup](.devcontainer/build-finish.png)

**Note: Do not forget to push your changes inside Codespaces so that your group partners can access them!**

### Local Setup

This project uses the following languages, tools, and frameworks:

* Java 17
* Maven
* [JUnit 5](https://junit.org/junit5)
* [PITest](https://pitest.org) (pitest-junit5-plugin)
* [Mockito](https://site.mockito.org) (v5.6.0)
* [EasyMock](https://easymock.org) (v5.2.0)
* [Jacoco](https://www.eclemma.org/jacoco/trunk/index.html) (v0.8.11)


## How to run

In Visual Studio Code (VS Code), open the `Main.java` file and press the run button in the top right corner. 
The application will launch and you can interact with it through the command-line.
![Execute the program](.devcontainer/execute-system.png)


## How to test

### Running the tests

Select the class you intend to test and press the run botton next to the class. 
Optionally, if you want to execute a single test, you can press the run button next to the test case in the class.
![Test the program](.devcontainer/execute-test.png)

You can also run all tests by running the following command in the command-line.

```
mvn clean compile test
```

### Checking JaCoCo Output

Executing the `mvn clean compile test` will create the JaCoCo report. You can access the report in the `target/site/jacoco/index.html` folder.
In GitHub Codespaces, you can open the report by opening the `index.html` and clicking the Preview icon in the top right (![Test the program](.devcontainer/jacoco-preview.png)).

If it does not work, press the white square for more browser options (1), select `Open in browser` (2), and press `Open`, when the confirmation window pops-up.

![Check in Browser the report](.devcontainer/jacoco-check-report.png)


### Running Pitest

To execute pitest, execute the following command in the command-line. The command will generate an html report to `target/pit-reports/YYYMMDDHHMI`. You can access these html reports similarly to the procedure described for checking the JaCoCo output.

```
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```
