package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class Bestellung {

    @JsonProperty("bestellungId")
    private Long bestellungId;

    @JsonProperty("datum")
    private OffsetDateTime datum;

    @JsonProperty("status")
    private String status;

    @JsonProperty("personalnr")
    private Long mitarbeiterzuweis;

    @JsonProperty("kundeId")
    private Long kundeId;


    public Bestellung() {}

    public Bestellung(Long bestellungId, OffsetDateTime datum, String status, Long mitarbeiterzuweis, Long kundeId) {
        this.bestellungId = bestellungId;
        this.datum = datum;
        this.status = status;
        this.mitarbeiterzuweis = mitarbeiterzuweis;
        this.kundeId = kundeId;
    }

    public Long getBestellungId() { return bestellungId; }
    public void setBestellungId(Long bestellungId) { this.bestellungId = bestellungId; }

    public OffsetDateTime getDatum() { return datum; }
    public void setDatum(OffsetDateTime datum) { this.datum = datum; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getMitarbeiterzuweis() { return mitarbeiterzuweis; }
    public void setMitarbeiterzuweis(Long mitarbeiterzuweis) { this.mitarbeiterzuweis = mitarbeiterzuweis; }

    public Long getKundeId() { return kundeId; }
    public void setKundeId(Long kundeId) { this.kundeId = kundeId; }
}
