package com.dbw.spring_boot.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Bestellposition {

    @JsonProperty("positionsId")   // <- wie Aufgabenstellung
    private Long positionId;

    @JsonProperty("menge")
    private Integer menge;

    @JsonProperty("produktSku")    // <- wie Aufgabenstellung
    private String sku;

    @JsonProperty("bestellungId")
    private Long bestellungId;

    @JsonProperty("gesamtpreis")
    private BigDecimal gesamtpreis;

    public Bestellposition() {}

    public Bestellposition(Long positionId, Integer menge, String sku, Long bestellungId, BigDecimal gesamtpreis) {
        this.positionId = positionId;
        this.menge = menge;
        this.sku = sku;
        this.bestellungId = bestellungId;
        this.gesamtpreis = gesamtpreis;
    }

    public Long getPositionId() { return positionId; }
    public void setPositionId(Long positionId) { this.positionId = positionId; }

    public Integer getMenge() { return menge; }
    public void setMenge(Integer menge) { this.menge = menge; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Long getBestellungId() { return bestellungId; }
    public void setBestellungId(Long bestellungId) { this.bestellungId = bestellungId; }

    public BigDecimal getGesamtpreis() { return gesamtpreis; }
    public void setGesamtpreis(BigDecimal gesamtpreis) { this.gesamtpreis = gesamtpreis; }
}
