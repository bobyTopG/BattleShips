package be.kdg.integration1.battleships_solitaire.view;

import be.kdg.integration1.battleships_solitaire.entities.Difficulty;
import be.kdg.integration1.battleships_solitaire.entities.Player;
import be.kdg.integration1.battleships_solitaire.logic.Utility;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TerminalUIHandler implements UIHandler {

    private final Scanner scanner;
    private String response;

    public TerminalUIHandler() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void showStartScreen() {
        System.out.println("""
                    ____        __  __  __          __    _               _____       ___ __        _        \s
                   / __ )____ _/ /_/ /_/ /__  _____/ /_  (_)___  _____   / ___/____  / (_) /_____ _(_)_______\s
                  / __  / __ `/ __/ __/ / _ \\/ ___/ __ \\/ / __ \\/ ___/   \\__ \\/ __ \\/ / / __/ __ `/ / ___/ _ \\
                 / /_/ / /_/ / /_/ /_/ /  __(__  ) / / / / /_/ (__  )   ___/ / /_/ / / / /_/ /_/ / / /  /  __/
                /_____/\\__,_/\\__/\\__/_/\\___/____/_/ /_/_/ .___/____/   /____/\\____/_/_/\\__/\\__,_/_/_/   \\___/\s
                                                       /_/                                                   \s
                                                      \s""");
        System.out.println("Welcome to Battleships Solitaire!");
    }

    public void prompt(String prompt) {
        System.out.print(prompt + " > ");
        response = scanner.nextLine().trim().toUpperCase();
    }

    @Override
    public String getResponse() {
        return response;
    }

    @Override
    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public void showMainMenu() {
        System.out.println("""
                ┌───────────────────────────────────────┐
                │          ┌─────────────────┐          │
                │          │     Play [P]    │          │
                │          └─────────────────┘          │
                │          ┌─────────────────┐          │
                │          │     Help [H]    │          │
                │          └─────────────────┘          │
                │          ┌─────────────────┐          │
                │          │ Leaderboard [L] │          │
                │          └─────────────────┘          │
                │          ┌─────────────────┐          │
                │          │  Exit/Quit [E]  │          │
                │          └─────────────────┘          │
                └───────────────────────────────────────┘""");
        prompt("Make a choice");
        switch (response) {
            case "P", "PLAY" -> {
                System.out.println("""
                ┌───────────────────────────────────────┐
                │          ┌─────────────────┐          │
                │        > │     Play [P]    │ <        │
                │          └─────────────────┘          │
                └───────────────────────────────────────┘""");
            }
            case "H", "HELP" -> {
                System.out.println("""
                ┌───────────────────────────────────────┐
                │          ┌─────────────────┐          │
                │        > │     Help [H]    │ <        │
                │          └─────────────────┘          │
                └───────────────────────────────────────┘""");
            }
            case "L", "LEADERBOARD" -> {
                System.out.println("""
                ┌───────────────────────────────────────┐
                │          ┌─────────────────┐          │
                │        > │ Leaderboard [L] │ <        │
                │          └─────────────────┘          │
                └───────────────────────────────────────┘""");
            }
            case "E", "EXIT", "Q", "QUIT" -> {
                System.out.println("""
                ┌───────────────────────────────────────┐
                │          ┌─────────────────┐          │
                │        > │  Exit/Quit [E]  │ <        │
                │          └─────────────────┘          │
                └───────────────────────────────────────┘
                Game quit!""");
            }
        }
    }

    @Override
    public void showPlayingOptions() {
        System.out.println("Mark [W]ater | [M]ark Ship | [U]nmark tile | [R]eveal tile | [S]ave game | [E]xit game");
        prompt("Your move ");
    }

    @Override
    public String askForPlayerName() {
        String name;
        // validate the name by checking if it's either empty or contains characters that aren't letters
        // the regex is matching all lowercase and uppercase letters with at least 1 occurrence
        do {
            System.out.print("Please enter your name: ");
            name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("Name can't be empty. Please try again!");
            } else if (!name.matches("[a-zA-Z]+")) {
                System.out.println("Your name may only contain letters. Please try again!");
            }
        } while (name.isEmpty() || !name.matches("[a-zA-Z]+"));
        return name.toUpperCase();
    }

    @Override
    public boolean startNewGame() {
        prompt("Start a new game or load a saved game? [new/load]");
        return response.equals("NEW") || response.equals("N");
    }

    @Override
    public Difficulty chooseDifficulty() {
        System.out.println("Choose your difficulty:");
        // this loops over all difficulties and prints them
        for (Difficulty difficulty : Difficulty.values()) {
            System.out.println(difficulty);
        }

        // difficulty selection, checks if option is valid
        // if player enters anything other than an int, we catch the exception
        int option = 0;
        final int LOWEST_DIFFICULTY = Difficulty.values()[0].getNumericValue();
        final int HIGHEST_DIFFICULTY = Difficulty.values()[Difficulty.values().length - 1].getNumericValue();

        do {
            try {
                System.out.print("Option number: ");
                option = Integer.parseInt(scanner.nextLine());
                if (option < LOWEST_DIFFICULTY || option > HIGHEST_DIFFICULTY) {
                    System.out.printf("Invalid choice. Please select a difficulty between %d and %d.%n", LOWEST_DIFFICULTY, HIGHEST_DIFFICULTY);
                }
            } catch (InputMismatchException e) {
                System.out.printf("Invalid input. Please enter the difficulty using a number between %d and %d.%n", LOWEST_DIFFICULTY, HIGHEST_DIFFICULTY);
                // remove the invalid input from the scanner, otherwise it keeps going on forever
                scanner.nextLine();
            }
        } while (option < LOWEST_DIFFICULTY || option > HIGHEST_DIFFICULTY);

        // uses selected option to create new board
        // board size is determined by the board size defined in the Difficulty enum
        return switch (option) {
            case 1 -> Difficulty.EASY;
            case 2 -> Difficulty.MEDIUM;
            case 3 -> Difficulty.HARD;
            default -> null;
        };
    }

    @Override
    public LocalDate askForBirthdate() {
        LocalDate birthdate = LocalDate.MAX;
        do {
            try {
                prompt("What is your birthday? [YYYY-MM-DD]");
                birthdate = LocalDate.parse(response);
                if (birthdate.isAfter(LocalDate.now())) {
                    throw new Exception("Future date");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please try again.");
            } catch (Exception e) {
                System.out.println("Birthdate cannot be in the future!");
            }
        }  while (birthdate.isAfter(LocalDate.now()));
        return birthdate;
    }

    @Override
    public void showHelpScreen() {
        System.out.println("""
                
                How to play the game? - The game is simple. You have to guess where the ships are starting with a handful of revealed tiles (hints). Each tile is either water or a ship part. Per column and row you will find how many ship pieces are found. Underneath the board you will find a list of the ships and the progress of revealing them. Above the board you will see your score and time passed since the game started.
                
                How to use the controls? - The game is made to be as intuitive as possible so just follow the menus. When you have made a choice just type in the corresponding letter and press ENTER.
                
                What is what on the board? - The letters correspond to a column meanwhile the numbers on the left correspond to a row. Squares are ship pieces that are revealed to you by the game, same goes for the double squiggly line. The singular squiggly line represents water, while an X represents where you have marked a tile as a ship piece.
                
                If you have any more questions about the game, just contact its authors.
                And don't forget to have fun!
                """);
    }

    @Override
    public void welcomePlayer(Player player) {
        System.out.println("Hello, " + player.getName() + "!");
    }

    @Override
    public void awaitEnter() {
        System.out.print("(Press ENTER to continue...)");
        scanner.nextLine();
    }

    @Override
    public int chooseX(int limit) {
        int x = -1;
        do {
            prompt("Choose column [1..]");
            try {
                x = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                System.out.println("You have to give a number!");
            }
        } while (Utility.isXNotInBounds(x, limit));
        return x;
    }

    @Override
    public char chooseY(int limit) {
        char y = '\0';
        do {
            prompt("Choose row [A..]");
            if (getResponse().length() != 1) {
                System.out.println("Enter only one character!");
                continue;
            }
            y = getResponse().charAt(0);
        } while (Utility.isYNotInBounds(y, limit));
        return y;
    }

    @Override
    public void endOfGame() {
        System.out.println("Game finished!");
    }

}