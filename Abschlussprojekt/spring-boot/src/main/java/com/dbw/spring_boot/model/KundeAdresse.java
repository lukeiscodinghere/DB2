package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KundeAdresse {

    @JsonProperty("adresse")
    private Adresse adresse;

    @JsonProperty("typ")
    private String typ;

    public KundeAdresse() {}

    public KundeAdresse(Adresse adresse, String typ) {
        this.adresse = adresse;
        this.typ = typ;
    }

    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }

    public String getTyp() { return typ; }
    public void setTyp(String typ) { this.typ = typ; }
}
