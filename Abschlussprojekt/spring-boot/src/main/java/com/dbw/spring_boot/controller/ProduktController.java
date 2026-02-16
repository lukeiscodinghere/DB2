package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Produkt;
import com.dbw.spring_boot.repositories.ProduktRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produkte")
public class ProduktController {

    private final ProduktRepository repo;

    public ProduktController(ProduktRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(value = "sku", required = false) String sku) {
        if (sku == null) {
            List<Produkt> all = repo.findAll();
            return ResponseEntity.ok(all);
        }
        return repo.findBySku(sku)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Produkt p) {
        if (p.getSku() == null || p.getName() == null || p.getPreis() == null
                || p.getLagerbestand() == null || p.getAngelegtVon() == null) {
            return ResponseEntity.badRequest().body("Für POST /produkte müssen sku, name, preis, lagerbestand und angelegtVon gesetzt sein");
        }
        return repo.insert(p)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("sku") String sku) {
        return repo.deleteBySku(sku)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<?> updateLagerbestand(@RequestBody Produkt p) {
        if (p.getSku() == null || p.getLagerbestand() == null) {
            return ResponseEntity.badRequest().body("Für PUT /produkte müssen sku und lagerbestand gesetzt sein");
        }
        return repo.updateLagerbestandOnly(p.getSku(), p.getLagerbestand())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
