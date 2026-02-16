package com.dbw.spring_boot.model;

public class MitarbeiterUebersichtReport {
    private Long personalNr;
    private Long anzahlVerwalteterBestellungen;
    private Long anzahlAngelegterProdukte;

    public MitarbeiterUebersichtReport() {}

    public MitarbeiterUebersichtReport(Long personalNr, Long anzahlVerwalteterBestellungen, Long anzahlAngelegterProdukte) {
        this.personalNr = personalNr;
        this.anzahlVerwalteterBestellungen = anzahlVerwalteterBestellungen;
        this.anzahlAngelegterProdukte = anzahlAngelegterProdukte;
    }

    public Long getPersonalNr() { return personalNr; }
    public void setPersonalNr(Long personalNr) { this.personalNr = personalNr; }

    public Long getAnzahlVerwalteterBestellungen() { return anzahlVerwalteterBestellungen; }
    public void setAnzahlVerwalteterBestellungen(Long anzahlVerwalteterBestellungen) { this.anzahlVerwalteterBestellungen = anzahlVerwalteterBestellungen; }

    public Long getAnzahlAngelegterProdukte() { return anzahlAngelegterProdukte; }
    public void setAnzahlAngelegterProdukte(Long anzahlAngelegterProdukte) { this.anzahlAngelegterProdukte = anzahlAngelegterProdukte; }
}
