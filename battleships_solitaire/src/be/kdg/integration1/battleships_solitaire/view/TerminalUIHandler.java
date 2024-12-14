package be.kdg.integration1.battleships_solitaire.view;

import be.kdg.integration1.battleships_solitaire.entities.Difficulty;

import java.time.LocalDate;
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
                option = scanner.nextInt();
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

//        StringBuilder sb = new StringBuilder("Choose a difficulty [");
//        Difficulty[] difficulties = Difficulty.values();
//        // this loops over all difficulties and prints them
//        for (int i = 0; i < difficulties.length - 1; i++) {
//            sb.append(difficulties[i].getNumericValue())
//                    .append(" - ").append(difficulties[i].name())
//                    .append(", ");
//        }
//        sb.append(difficulties[difficulties.length - 1].getNumericValue())
//                .append(" - ")
//                .append(difficulties[difficulties.length - 1].name());
//        sb.append("]");
//        prompt(sb.toString());
//        return switch (response) {
//            case "1" -> Difficulty.EASY;
//            case "2" -> Difficulty.MEDIUM;
//            case "3" -> Difficulty.HARD;
//            default -> Difficulty.valueOf(response);
//        };
    }

    @Override
    public LocalDate askForBirthday() {
        String birthday;
        do {
            prompt("What is your birthday? [YYYY-MM-DD]");
        } while (LocalDate.parse(response).isAfter(LocalDate.now()));
        return LocalDate.parse(response);
    }
}