package be.kdg.integration1.battleships_solitaire.entities;

import java.sql.Date;
import java.time.LocalDate;

public class Player {

    private int id;
    private String name;
    private LocalDate birthdate;
    private LocalDate joindate;

    public Player(String name) {
        setName(name);
        this.id = -1;
    }

    public Player(String name, LocalDate birthdate) {
        this(name);
        setBirthdate(birthdate);
        setJoindate(LocalDate.now());
    }

    public Player(int id, String name, LocalDate birthdate, LocalDate joindate) {
        this(name, birthdate);
        this.id = id;
        setJoindate(joindate);
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        if (birthdate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Birthdate cannot be after today!");
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

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("'%s' joined on %s", name, joindate);
    }

}
