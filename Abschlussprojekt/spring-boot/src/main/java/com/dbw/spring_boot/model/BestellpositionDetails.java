package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class BestellpositionDetails {

    @JsonProperty("positionsId")
    private Long positionsId;

    @JsonProperty("bestellungId")
    private Long bestellungId;

    @JsonProperty("produkt")
    private Produkt produkt;

    @JsonProperty("menge")
    private Integer menge;

    @JsonProperty("gesamtpreis")
    private BigDecimal gesamtpreis;

    public BestellpositionDetails() {}

    public BestellpositionDetails(Long positionsId, Long bestellungId, Produkt produkt, Integer menge, BigDecimal gesamtpreis) {
        this.positionsId = positionsId;
        this.bestellungId = bestellungId;
        this.produkt = produkt;
        this.menge = menge;
        this.gesamtpreis = gesamtpreis;
    }

    public Long getPositionsId() { return positionsId; }
    public void setPositionsId(Long positionsId) { this.positionsId = positionsId; }

    public Long getBestellungId() { return bestellungId; }
    public void setBestellungId(Long bestellungId) { this.bestellungId = bestellungId; }

    public Produkt getProdukt() { return produkt; }
    public void setProdukt(Produkt produkt) { this.produkt = produkt; }

    public Integer getMenge() { return menge; }
    public void setMenge(Integer menge) { this.menge = menge; }

    public BigDecimal getGesamtpreis() { return gesamtpreis; }
    public void setGesamtpreis(BigDecimal gesamtpreis) { this.gesamtpreis = gesamtpreis; }
}
