package com.dbw.spring_boot.model;

import java.math.BigDecimal;

public class ProduktVerkaufszahlenReport {
    private String sku;
    private String name;
    private Long gesamtVerkaufteMenge;
    private BigDecimal umsatz;
    private Long anzahlBestellungen;

    public ProduktVerkaufszahlenReport() {}

    public ProduktVerkaufszahlenReport(String sku, String name, Long gesamtVerkaufteMenge, BigDecimal umsatz, Long anzahlBestellungen) {
        this.sku = sku;
        this.name = name;
        this.gesamtVerkaufteMenge = gesamtVerkaufteMenge;
        this.umsatz = umsatz;
        this.anzahlBestellungen = anzahlBestellungen;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getGesamtVerkaufteMenge() { return gesamtVerkaufteMenge; }
    public void setGesamtVerkaufteMenge(Long gesamtVerkaufteMenge) { this.gesamtVerkaufteMenge = gesamtVerkaufteMenge; }

    public BigDecimal getUmsatz() { return umsatz; }
    public void setUmsatz(BigDecimal umsatz) { this.umsatz = umsatz; }

    public Long getAnzahlBestellungen() { return anzahlBestellungen; }
    public void setAnzahlBestellungen(Long anzahlBestellungen) { this.anzahlBestellungen = anzahlBestellungen; }
}
