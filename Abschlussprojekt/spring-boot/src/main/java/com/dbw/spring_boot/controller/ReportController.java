package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.*;
import com.dbw.spring_boot.repositories.ReportRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportRepository repo;

    public ReportController(ReportRepository repo) {
        this.repo = repo;
    }

    // (a)
    @GetMapping("/kunde/summe-anzahl-bestellungen")
    public List<KundeSummeAnzahlBestellungenReport> kundenReport() {
        return repo.getKundenSummeAnzahlBestellungen();
    }

    // (b)
    @GetMapping("/produkt/verkaufszahlen")
    public List<ProduktVerkaufszahlenReport> produktReport() {
        return repo.getProduktVerkaufszahlen();
    }

    // (c)
    @GetMapping("/mitarbeiter/uebersicht")
    public List<MitarbeiterUebersichtReport> mitarbeiterUebersicht() {
        return repo.getMitarbeiterUebersicht();
    }

    // (d)
    @GetMapping("/mitarbeiter/bestellstatus-uebersicht")
    public List<MitarbeiterBestellstatusUebersichtReport> mitarbeiterBestellstatus() {
        return repo.getMitarbeiterBestellstatusUebersicht();
    }
}
