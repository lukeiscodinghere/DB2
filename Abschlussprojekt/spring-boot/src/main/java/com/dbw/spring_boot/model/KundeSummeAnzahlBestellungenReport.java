package com.dbw.spring_boot.model;

import java.math.BigDecimal;

public class KundeSummeAnzahlBestellungenReport {
    private Long kundeId;
    private String email;
    private Long anzahlBestellungen;
    private BigDecimal gesamtsumme;

    public KundeSummeAnzahlBestellungenReport() {}

    public KundeSummeAnzahlBestellungenReport(Long kundeId, String email, Long anzahlBestellungen, BigDecimal gesamtsumme) {
        this.kundeId = kundeId;
        this.email = email;
        this.anzahlBestellungen = anzahlBestellungen;
        this.gesamtsumme = gesamtsumme;
    }

    public Long getKundeId() { return kundeId; }
    public void setKundeId(Long kundeId) { this.kundeId = kundeId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getAnzahlBestellungen() { return anzahlBestellungen; }
    public void setAnzahlBestellungen(Long anzahlBestellungen) { this.anzahlBestellungen = anzahlBestellungen; }

    public BigDecimal getGesamtsumme() { return gesamtsumme; }
    public void setGesamtsumme(BigDecimal gesamtsumme) { this.gesamtsumme = gesamtsumme; }
}
