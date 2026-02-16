package com.dbw.spring_boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class Produkt {

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("name")
    private String name;

    @JsonProperty("preis")
    private BigDecimal preis;

    @JsonProperty("lagerbestand")
    private Integer lagerbestand;

    @JsonProperty("angelegtVon")
    private Long angelegtVon;

    public Produkt() {}

    public Produkt(String sku, String name, BigDecimal preis, Integer lagerbestand, Long angelegtVon) {
        this.sku = sku;
        this.name = name;
        this.preis = preis;
        this.lagerbestand = lagerbestand;
        this.angelegtVon = angelegtVon;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPreis() { return preis; }
    public void setPreis(BigDecimal preis) { this.preis = preis; }

    public Integer getLagerbestand() { return lagerbestand; }
    public void setLagerbestand(Integer lagerbestand) { this.lagerbestand = lagerbestand; }

    public Long getAngelegtVon() { return angelegtVon; }
    public void setAngelegtVon(Long angelegtVon) { this.angelegtVon = angelegtVon; }
}
