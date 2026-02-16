package com.dbw.spring_boot.model;

public class MitarbeiterBestellstatusUebersichtReport {
    private Long personalNr;
    private String status;
    private Long anzahlBestellungen;

    public MitarbeiterBestellstatusUebersichtReport() {}

    public MitarbeiterBestellstatusUebersichtReport(Long personalNr, String status, Long anzahlBestellungen) {
        this.personalNr = personalNr;
        this.status = status;
        this.anzahlBestellungen = anzahlBestellungen;
    }

    public Long getPersonalNr() { return personalNr; }
    public void setPersonalNr(Long personalNr) { this.personalNr = personalNr; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getAnzahlBestellungen() { return anzahlBestellungen; }
    public void setAnzahlBestellungen(Long anzahlBestellungen) { this.anzahlBestellungen = anzahlBestellungen; }
}
