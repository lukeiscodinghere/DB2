package com.dbw.spring_boot.controller;

import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.model.Mitarbeiter;
import com.dbw.spring_boot.repositories.LoginKundeRepository;
import com.dbw.spring_boot.repositories.LoginMitarbeiterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginMitarbeiterRepository mitarbeiterRepo;
    private final LoginKundeRepository kundeRepo;

    public LoginController(LoginMitarbeiterRepository mitarbeiterRepo, LoginKundeRepository kundeRepo) {
        this.mitarbeiterRepo = mitarbeiterRepo;
        this.kundeRepo = kundeRepo;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<Mitarbeiter> loginMitarbeiter(@RequestBody Map<String, Object> body) {
        Object pnObj = body.get("personalNr");
        Object pwObj = body.get("passwort");

        Long personalNr = (pnObj instanceof Number) ? ((Number) pnObj).longValue() : null;
        String passwort = (pwObj instanceof String) ? (String) pwObj : null;

        return mitarbeiterRepo.findByPersonalNrAndPasswort(personalNr == null ? -1 : personalNr, passwort)
                .map(m -> {
                    m.setPasswort(null); // Vorgabe: bei Erfolg passwort null
                    return ResponseEntity.ok(m);
                })
                .orElseGet(() -> {
                    Mitarbeiter fail = new Mitarbeiter();
                    fail.setPersonalNr(null);
                    fail.setPasswort(null);
                    fail.setEmail(null);
                    fail.setVorname(null);
                    fail.setNachname(null);
                    return ResponseEntity.status(401).body(fail);
                });
    }

    @PostMapping("/kunde")
    public ResponseEntity<Kunde> loginKunde(@RequestBody Map<String, Object> body) {
        Object emObj = body.get("email");
        Object pwObj = body.get("passwort");

        String email = (emObj instanceof String) ? (String) emObj : null;
        String passwort = (pwObj instanceof String) ? (String) pwObj : null;

        return kundeRepo.findByEmailAndPasswort(email, passwort)
            .map(k -> {
                k.setPasswort(null);              
                k.setAdressen(List.of());         
                return ResponseEntity.ok(k);
            })
            .orElseGet(() -> {
                Kunde fail = new Kunde();
                fail.setKundeId(null);
                fail.setEmail(null);
                fail.setVorname(null);
                fail.setNachname(null);
                fail.setPasswort(null);
                fail.setAdressen(List.of());      //  []
                return ResponseEntity.status(401).body(fail);
            });
    }

}