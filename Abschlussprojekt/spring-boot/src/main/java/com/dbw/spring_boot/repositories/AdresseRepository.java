package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Adresse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdresseRepository {

    private final JdbcTemplate jdbc;

    public AdresseRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Adresse> ROW_MAPPER = (rs, rowNum) ->
            new Adresse(
                    rs.getLong("adresse_id"),
                    rs.getBoolean("aktiv"),
                    rs.getString("strasse"),
                    rs.getString("hausnummer"),
                    rs.getLong("plz"),
                    rs.getString("ort"),
                    rs.getString("land")
            );

    public List<Adresse> findAll() {
        return jdbc.query(
                "SELECT adresse_id, aktiv, strasse, hausnummer, plz, ort, land FROM adresse ORDER BY adresse_id",
                ROW_MAPPER
        );
    }

    public Optional<Adresse> findById(long id) {
        List<Adresse> list = jdbc.query(
                "SELECT adresse_id, aktiv, strasse, hausnummer, plz, ort, land FROM adresse WHERE adresse_id = ?",
                ROW_MAPPER,
                id
        );
        return list.stream().findFirst();
    }

    public Adresse insert(Adresse a) {
        Long newId = jdbc.queryForObject(
                "INSERT INTO adresse (aktiv, strasse, hausnummer, plz, ort, land) VALUES (?, ?, ?, ?, ?, ?) RETURNING adresse_id",
                Long.class,
                a.getAktiv(), a.getStrasse(), a.getHausnummer(), a.getPlz(), a.getOrt(), a.getLand()
        );
        return findById(newId).orElseThrow();
    }

    public Optional<Adresse> updateAllById(long id, Adresse a) {
        int affected = jdbc.update(
                "UPDATE adresse SET aktiv = ?, strasse = ?, hausnummer = ?, plz = ?, ort = ?, land = ? WHERE adresse_id = ?",
                a.getAktiv(), a.getStrasse(), a.getHausnummer(), a.getPlz(), a.getOrt(), a.getLand(), id
        );
        if (affected == 0) return Optional.empty();
        return findById(id);
    }
}
