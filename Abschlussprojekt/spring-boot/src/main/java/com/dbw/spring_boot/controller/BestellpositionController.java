package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Bestellposition;
import com.dbw.spring_boot.repositories.BestellpositionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bestellpositionen")
public class BestellpositionController {

    private final BestellpositionRepository repo;

    public BestellpositionController(BestellpositionRepository repo) {
        this.repo = repo;
    }

    // GET /bestellpositionen oder GET /bestellpositionen?id=...
    @GetMapping
    public ResponseEntity<?> get(@RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            List<Bestellposition> all = repo.findAll();
            return ResponseEntity.ok(all);
        }
        return repo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /bestellpositionen (ohne positionId)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Bestellposition bp) {
        bp.setPositionId(null);

        if (bp.getMenge() == null || bp.getSku() == null || bp.getBestellungId() == null) {
            return ResponseEntity.badRequest().body("Für POST /bestellpositionen müssen menge, sku und bestellungId gesetzt sein");
        }

        Bestellposition created = repo.insert(bp);
        return ResponseEntity.status(201).body(created);
    }

    /*/ PUT /bestellpositionen (alle Attribute anhand positionId ändern)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Bestellposition bp) {
        if (bp.getPositionId() == null) {
            return ResponseEntity.badRequest().body("positionId muss für PUT gesetzt sein");
        }
        if (bp.getMenge() == null || bp.getSku() == null || bp.getBestellungId() == null) {
            return ResponseEntity.badRequest().body("Für PUT /bestellpositionen müssen menge, sku und bestellungId gesetzt sein");
        }

        long id = bp.getPositionId();
        return repo.updateAllById(id, bp)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }*/

    // DELETE /bestellpositionen?id=...
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("id") Long id) {
        return repo.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
