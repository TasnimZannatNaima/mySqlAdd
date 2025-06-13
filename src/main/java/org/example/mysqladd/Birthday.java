package org.example.mysqladd;

import java.time.LocalDate;

public class Birthday {
    private int id;
    private String name;
    private LocalDate birthdate;

    public int getId() {
        return id;//it22001
    }

    public void setId(int id) {
        this.id = id;//it22001
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;//naima
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
