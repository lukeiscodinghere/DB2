package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Kunde {

    @JsonProperty("kundeId")
    private Long kundeId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("vorname")
    private String vorname;

    @JsonProperty("nachname")
    private String nachname;

    @JsonProperty("passwort")
    private String passwort;

    @JsonProperty("adressen")
    private List<KundeAdresse> adressen = new ArrayList<>();

    public Kunde() {}

    public Kunde(Long kundeId, String email, String vorname, String nachname, String passwort) {
        this.kundeId = kundeId;
        this.email = email;
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
    }

    public Long getKundeId() { return kundeId; }
    public void setKundeId(Long kundeId) { this.kundeId = kundeId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }

    public String getPasswort() { return passwort; }
    public void setPasswort(String passwort) { this.passwort = passwort; }

    public List<KundeAdresse> getAdressen() { return adressen; }
    public void setAdressen(List<KundeAdresse> adressen) { this.adressen = adressen; }
}
