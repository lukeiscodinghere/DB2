package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Mitarbeiter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LoginMitarbeiterRepository {

    private final JdbcTemplate jdbc;

    public LoginMitarbeiterRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Mitarbeiter> findByPersonalNrAndPasswort(long personalNr, String passwort) {
        List<Mitarbeiter> list = jdbc.query(
                "SELECT personal_nr, passwort, email, vorname, nachname FROM mitarbeiter WHERE personal_nr = ? AND passwort = ?",
                (rs, rowNum) -> {
                    Mitarbeiter m = new Mitarbeiter();
                    m.setPersonalNr(rs.getLong("personal_nr"));
                    m.setPasswort(rs.getString("passwort"));
                    m.setEmail(rs.getString("email"));
                    m.setVorname(rs.getString("vorname"));
                    m.setNachname(rs.getString("nachname"));
                    return m;
                },
                personalNr, passwort
        );
        return list.stream().findFirst();
    }
}
