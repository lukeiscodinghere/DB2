package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Mitarbeiter {

    @JsonProperty("personalNr")
    private Long personalNr;

    @JsonProperty("passwort")
    private String passwort;

    @JsonProperty("email")
    private String email;

    @JsonProperty("vorname")
    private String vorname;

    @JsonProperty("nachname")
    private String nachname;

    public Mitarbeiter() {}

    public Mitarbeiter(Long personalNr, String passwort, String email, String vorname, String nachname) {
        this.personalNr = personalNr;
        this.passwort = passwort;
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Long getPersonalNr() { return personalNr; }
    public void setPersonalNr(Long personalNr) { this.personalNr = personalNr; }

    public String getPasswort() { return passwort; }
    public void setPasswort(String passwort) { this.passwort = passwort; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }
}
