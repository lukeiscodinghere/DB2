package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Bestellposition;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BestellpositionRepository {

    private final JdbcTemplate jdbc;

    public BestellpositionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    

    private static final RowMapper<Bestellposition> ROW_MAPPER = (rs, rowNum) ->
            new Bestellposition(
                    rs.getLong("position_id"),
                    rs.getInt("menge"),
                    rs.getString("sku"),
                    rs.getLong("bestellung_id"),
                    rs.getBigDecimal("gesamtpreis")
            );

    public List<Bestellposition> findAll() {
    return jdbc.query(
            "SELECT bp.position_id, bp.menge, bp.sku, bp.bestellung_id, " +
            "       (bp.menge * p.preis) AS gesamtpreis " +
            "FROM bestellposition bp " +
            "JOIN produkt p ON p.sku = bp.sku " +
            "ORDER BY bp.position_id",
            ROW_MAPPER
    );
}


public Optional<Bestellposition> findById(long id) {
    List<Bestellposition> list = jdbc.query(
            "SELECT bp.position_id, bp.menge, bp.sku, bp.bestellung_id, " +
            "       (bp.menge * p.preis) AS gesamtpreis " +
            "FROM bestellposition bp " +
            "JOIN produkt p ON p.sku = bp.sku " +
            "WHERE bp.position_id = ?",
            ROW_MAPPER,
            id
    );
    return list.stream().findFirst();
}


    public Bestellposition insert(Bestellposition bp) {
        Long newId = jdbc.queryForObject(
                "INSERT INTO bestellposition (menge, sku, bestellung_id) VALUES (?, ?, ?) RETURNING position_id",
                Long.class,
                bp.getMenge(), bp.getSku(), bp.getBestellungId()
        );
        return findById(newId).orElseThrow();
    }
/* 
    public Optional<Bestellposition> updateAllById(long id, Bestellposition bp) {
        int affected = jdbc.update(
                "UPDATE bestellposition SET menge = ?, sku = ?, bestellung_id = ? WHERE position_id = ?",
                bp.getMenge(), bp.getSku(), bp.getBestellungId(), id
        );
        if (affected == 0) return Optional.empty();
        return findById(id);
    }*/

    public boolean deleteById(long id) {
        int affected = jdbc.update("DELETE FROM bestellposition WHERE position_id = ?", id);
        return affected > 0;
    }
}
