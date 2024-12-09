package be.kdg.integration1.battleships_solitaire;

import java.sql.Date;
import java.time.LocalDate;

public class Player {

    private String name;
    private Date birthdate;
    private Date joindate;

    public Player(String name) {
        setName(name);
    }

    public Player(String name, Date birthdate) {
        this(name);
        setBirthdate(birthdate);
        setJoindate(Date.valueOf(LocalDate.now()));
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        if (birthdate.after(Date.valueOf(LocalDate.now())))
            throw new IllegalArgumentException("Birthdate cannot be after today!");
        this.birthdate = birthdate;
    }

    public Date getJoindate() {
        return joindate;
    }

    public void setJoindate(Date joindate) {
        this.joindate = joindate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    @Override
    public String toString() {
        return String.format("'%s' joined on %s", name, joindate);
    }

}
