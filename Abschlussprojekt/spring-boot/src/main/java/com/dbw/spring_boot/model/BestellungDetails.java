package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class BestellungDetails {

    @JsonProperty("bestellungId")
    private Long bestellungId;

    @JsonProperty("kundeId")
    private Long kundeId;

    @JsonProperty("personalnr")
    private Long personalnr;

    @JsonProperty("datum")
    private OffsetDateTime datum;

    @JsonProperty("status")
    private String status;

    @JsonProperty("positionen")
    private List<BestellpositionDetails> positionen = new ArrayList<>();

    public BestellungDetails() {}

    public BestellungDetails(Long bestellungId, Long kundeId, Long personalnr, OffsetDateTime datum, String status) {
        this.bestellungId = bestellungId;
        this.kundeId = kundeId;
        this.personalnr = personalnr;
        this.datum = datum;
        this.status = status;
    }

    public Long getBestellungId() { return bestellungId; }
    public void setBestellungId(Long bestellungId) { this.bestellungId = bestellungId; }

    public Long getKundeId() { return kundeId; }
    public void setKundeId(Long kundeId) { this.kundeId = kundeId; }

    public Long getPersonalnr() { return personalnr; }
    public void setPersonalnr(Long personalnr) { this.personalnr = personalnr; }

    public OffsetDateTime getDatum() { return datum; }
    public void setDatum(OffsetDateTime datum) { this.datum = datum; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<BestellpositionDetails> getPositionen() { return positionen; }
    public void setPositionen(List<BestellpositionDetails> positionen) { this.positionen = positionen; }
}
