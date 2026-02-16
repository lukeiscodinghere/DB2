package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Kunde;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LoginKundeRepository {

    private final JdbcTemplate jdbc;

    public LoginKundeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Kunde> findByEmailAndPasswort(String email, String passwort) {
        List<Kunde> list = jdbc.query(
                "SELECT kunde_id, email, vorname, nachname, passwort FROM kunde WHERE email = ? AND passwort = ?",
                (rs, rowNum) -> {
                    Kunde k = new Kunde();
                    k.setKundeId(rs.getLong("kunde_id"));
                    k.setEmail(rs.getString("email"));
                    k.setVorname(rs.getString("vorname"));
                    k.setNachname(rs.getString("nachname"));
                    k.setPasswort(rs.getString("passwort"));
                    return k;
                },
                email, passwort
        );
        return list.stream().findFirst();
    }
}
