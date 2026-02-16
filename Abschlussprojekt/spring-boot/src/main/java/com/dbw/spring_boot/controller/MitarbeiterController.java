package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.repositories.MitarbeiterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mitarbeiter")
public class MitarbeiterController {

    private final MitarbeiterRepository repo;

    public MitarbeiterController(MitarbeiterRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            List<Mitarbeiter> all = repo.findAll();
            return ResponseEntity.ok(all);
        }
        return repo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mitarbeiter> create(@RequestBody Mitarbeiter m) {
        m.setPersonalNr(null);
        Mitarbeiter created = repo.insert(m);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("id") Long id) {
        return repo.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
