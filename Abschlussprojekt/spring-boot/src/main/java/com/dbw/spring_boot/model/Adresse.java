package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Adresse {

    @JsonProperty("adresseId")
    private Long adresseId;

    @JsonProperty("aktiv")
    private Boolean aktiv;

    @JsonProperty("strasse")
    private String strasse;

    @JsonProperty("hausnummer")
    private String hausnummer;

    @JsonProperty("plz")
    private Long plz;

    @JsonProperty("ort")
    private String ort;

    @JsonProperty("land")
    private String land;

    public Adresse() {}

    public Adresse(Long adresseId, Boolean aktiv, String strasse, String hausnummer, Long plz, String ort, String land) {
        this.adresseId = adresseId;
        this.aktiv = aktiv;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.land = land;
    }

    public Long getAdresseId() { return adresseId; }
    public void setAdresseId(Long adresseId) { this.adresseId = adresseId; }

    public Boolean getAktiv() { return aktiv; }
    public void setAktiv(Boolean aktiv) { this.aktiv = aktiv; }

    public String getStrasse() { return strasse; }
    public void setStrasse(String strasse) { this.strasse = strasse; }

    public String getHausnummer() { return hausnummer; }
    public void setHausnummer(String hausnummer) { this.hausnummer = hausnummer; }

    public Long getPlz() { return plz; }
    public void setPlz(Long plz) { this.plz = plz; }

    public String getOrt() { return ort; }
    public void setOrt(String ort) { this.ort = ort; }

    public String getLand() { return land; }
    public void setLand(String land) { this.land = land; }
}
