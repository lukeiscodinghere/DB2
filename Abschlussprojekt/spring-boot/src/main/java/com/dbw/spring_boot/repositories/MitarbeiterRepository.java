package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Mitarbeiter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MitarbeiterRepository {

    private final JdbcTemplate jdbc;

    public MitarbeiterRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Mitarbeiter> ROW_MAPPER = (rs, rowNum) ->
            new Mitarbeiter(
                    rs.getLong("personal_nr"),
                    rs.getString("passwort"),
                    rs.getString("email"),
                    rs.getString("vorname"),
                    rs.getString("nachname")
            );

    public List<Mitarbeiter> findAll() {
        return jdbc.query(
                "SELECT personal_nr, passwort, email, vorname, nachname FROM mitarbeiter ORDER BY personal_nr",
                ROW_MAPPER
        );
    }

    public Optional<Mitarbeiter> findById(long id) {
        List<Mitarbeiter> list = jdbc.query(
                "SELECT personal_nr, passwort, email, vorname, nachname FROM mitarbeiter WHERE personal_nr = ?",
                ROW_MAPPER,
                id
        );
        return list.stream().findFirst();
    }

    public Mitarbeiter insert(Mitarbeiter m) {
        Long newId = jdbc.queryForObject(
                "INSERT INTO mitarbeiter (vorname, nachname, email, passwort) VALUES (?, ?, ?, ?) RETURNING personal_nr",
                Long.class,
                m.getVorname(), m.getNachname(), m.getEmail(), m.getPasswort()
        );
        return findById(newId).orElseThrow();
    }

    public boolean deleteById(long id) {
        int affected = jdbc.update("DELETE FROM mitarbeiter WHERE personal_nr = ?", id);
        return affected > 0;
    }
}
