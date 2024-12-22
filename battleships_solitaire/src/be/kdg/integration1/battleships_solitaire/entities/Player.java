package be.kdg.integration1.battleships_solitaire.entities;

import be.kdg.integration1.battleships_solitaire.logic.Utility;
import be.kdg.integration1.battleships_solitaire.view.TerminalUIHandler;
import be.kdg.integration1.battleships_solitaire.view.UIHandler;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Player {

    private int id;
    private String name;
    private LocalDate birthdate;
    private LocalDate joindate;
    private String password;

    public Player(String name, String password) {
        setName(name);
        this.id = -1;
        this.password = password;
    }

    public Player(String name, LocalDate birthdate, String password) {
        this(name, password);
        setBirthdate(birthdate);
        setJoindate(LocalDate.now());
    }

    public Player(int id, String name, LocalDate birthdate, LocalDate joindate, String password) {
        this(name, birthdate, password);
        this.id = id;
        setJoindate(joindate);
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        if (birthdate.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Birthdate cannot be after today!");
        }
            this.birthdate = birthdate;

    }

    public LocalDate getJoindate() {
        return joindate;
    }

    public void setJoindate(LocalDate joindate) {
        this.joindate = joindate;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public void authorize() {
        if (password == null) {
            System.out.println("You have successfully logged in.");
        } else {
            UIHandler uiHandler = new TerminalUIHandler();
            do {
                uiHandler.askForPassword();
            } while (!Utility.validatePassword(uiHandler.getResponse(), password));
            System.out.println("Password is correct, logged in.");
        }
    }

    @Override
    public String toString() {
        return String.format("'%s' joined on %s", name, joindate);
    }

}
