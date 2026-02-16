package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.repositories.KundeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kunden")
public class KundeController {

    private final KundeRepository repo;

    public KundeController(KundeRepository repo) {
        this.repo = repo;
    }

    // GET /kunden
    // GET /kunden?id=1
    // GET /kunden?email=...
    @GetMapping
    public ResponseEntity<?> get(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "email", required = false) String email
    ) {
        // wenn beide gesetzt: nicht erlaubt (Swagger kann das eh schlecht)
        if (id != null) {
            return repo.findById(id)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
        }

        if (email != null) {
            return repo.findByEmail(email)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
        }

        List<Kunde> all = repo.findAll();
        return ResponseEntity.ok(all);
 
    }

    // POST /kunden (nur email, vorname, nachname, passwort)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Kunde k) {
        // KundeId + adressen werden ignoriert
        k.setKundeId(null);
        k.setAdressen(List.of());

        if (k.getEmail() == null || k.getVorname() == null || k.getNachname() == null || k.getPasswort() == null) {
            return ResponseEntity.badRequest().body("Für POST /kunden müssen email, vorname, nachname, passwort gesetzt sein");
        }

        Kunde created = repo.insert(k);
        return ResponseEntity.ok(created);
    }

    // PUT /kunden (alle Attribute anhand kundeId ändern)
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Kunde k) {
        if (k.getKundeId() == null) {
            return ResponseEntity.badRequest().body("kundeId muss für PUT gesetzt sein");
        }
        if (k.getEmail() == null || k.getVorname() == null || k.getNachname() == null || k.getPasswort() == null) {
            return ResponseEntity.badRequest().body("Für PUT /kunden müssen email, vorname, nachname, passwort gesetzt sein");
        }

        long id = k.getKundeId();
        return repo.updateAllById(id, k)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
