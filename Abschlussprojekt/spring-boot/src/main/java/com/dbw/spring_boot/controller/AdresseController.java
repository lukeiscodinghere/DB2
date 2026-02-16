package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.repositories.AdresseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adressen")
public class AdresseController {

    private final AdresseRepository repo;

    public AdresseController(AdresseRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            List<Adresse> all = repo.findAll();
            return ResponseEntity.ok(all);
        }
        return repo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Adresse> create(@RequestBody Adresse a) {
        // adresseId darf nicht angegeben werden
        a.setAdresseId(null);
        Adresse created = repo.insert(a);
        return ResponseEntity.ok(created);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Adresse a) {
        if (a.getAdresseId() == null) {
            return ResponseEntity.badRequest().body("adresseId muss für PUT gesetzt sein");
        }
        long id = a.getAdresseId();
        return repo.updateAllById(id, a)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
