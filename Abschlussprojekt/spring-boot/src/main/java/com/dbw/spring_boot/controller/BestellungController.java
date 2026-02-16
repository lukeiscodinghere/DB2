package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellung;
import com.dbw.spring_boot.repositories.BestellungRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dbw.spring_boot.model.BestellungDetails;


import java.util.List;

@RestController
@RequestMapping("/bestellungen")
public class BestellungController {

    private final BestellungRepository repo;

    public BestellungController(BestellungRepository repo) {
        this.repo = repo;
    }

    // GET /bestellungen oder GET /bestellungen?id=...
@GetMapping
public ResponseEntity<?> get(@RequestParam(value = "id", required = false) Long id) {
    if (id == null) {
        List<BestellungDetails> all = repo.findAllDetails();
        return ResponseEntity.ok(all);
    }
    return repo.findDetailsById(id)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}


    // POST /bestellungen (ohne bestellungId)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Bestellung b) {
        b.setBestellungId(null);

        if (b.getDatum() == null || b.getStatus() == null || b.getMitarbeiterzuweis() == null || b.getKundeId() == null) {
            return ResponseEntity.badRequest().body("Für POST /bestellungen müssen datum, status, personalnr und kundeId gesetzt sein");
        }

        Bestellung created = repo.insert(b);
        return ResponseEntity.ok(created);
    }

    // PUT /bestellungen (alle Attribute anhand bestellungId ändern)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Bestellung b) {
        if (b.getBestellungId() == null) {
            return ResponseEntity.badRequest().body("bestellungId muss für PUT gesetzt sein");
        }
        if (b.getDatum() == null || b.getStatus() == null || b.getMitarbeiterzuweis() == null || b.getKundeId() == null) {
            return ResponseEntity.badRequest().body("Für PUT /bestellungen müssen datum, status, personalnr und kundeId gesetzt sein");
        }

        long id = b.getBestellungId();
        return repo.updateAllById(id, b)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping
public ResponseEntity<Void> delete(@RequestParam("id") Long id) {
    return repo.deleteById(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
}

}
