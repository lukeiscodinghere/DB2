package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Bestellung;
import com.dbw.spring_boot.model.BestellungDetails;
import com.dbw.spring_boot.model.BestellpositionDetails;
import com.dbw.spring_boot.model.Produkt;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class BestellungRepository {

    private final JdbcTemplate jdbc;

    public BestellungRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Bestellung> ROW_MAPPER = (rs, rowNum) ->
            new Bestellung(
                    rs.getLong("bestellung_id"),
                    rs.getObject("datum", OffsetDateTime.class),
                    rs.getString("status"),
                    rs.getLong("mitarbeiterzuweis"),
                    rs.getLong("kunde_id")
            );

    public List<Bestellung> findAll() {
        return jdbc.query(
                "SELECT bestellung_id, datum, status, mitarbeiterzuweis, kunde_id FROM bestellung ORDER BY bestellung_id",
                ROW_MAPPER
        );
    }

    public Optional<Bestellung> findById(long id) {
        List<Bestellung> list = jdbc.query(
                "SELECT bestellung_id, datum, status, mitarbeiterzuweis, kunde_id FROM bestellung WHERE bestellung_id = ?",
                ROW_MAPPER,
                id
        );
        return list.stream().findFirst();
    }

    public Bestellung insert(Bestellung b) {
        Long newId = jdbc.queryForObject(
                "INSERT INTO bestellung (datum, status, mitarbeiterzuweis, kunde_id) VALUES (?, ?, ?, ?) RETURNING bestellung_id",
                Long.class,
                b.getDatum(), b.getStatus(), b.getMitarbeiterzuweis(), b.getKundeId()
        );
        return findById(newId).orElseThrow();
    }

    public Optional<Bestellung> updateAllById(long id, Bestellung b) {
        int affected = jdbc.update(
                "UPDATE bestellung SET datum = ?, status = ?, mitarbeiterzuweis = ?, kunde_id = ? WHERE bestellung_id = ?",
                b.getDatum(), b.getStatus(), b.getMitarbeiterzuweis(), b.getKundeId(), id
        );
        if (affected == 0) return Optional.empty();
        return findById(id);
    }

    public boolean deleteById(long id) {
    int affected = jdbc.update("DELETE FROM bestellung WHERE bestellung_id = ?", id);
    return affected > 0;
    }

    
    public List<BestellungDetails> findAllDetails() {
    String sql =
        "SELECT " +
        "  b.bestellung_id, b.kunde_id, b.mitarbeiterzuweis, b.datum, b.status, " +
        "  bp.position_id, bp.menge, " +
        "  (bp.menge * p.preis) AS gesamtpreis, " +
        "  p.sku AS p_sku, p.name AS p_name, p.preis AS p_preis, " +
        "  p.lagerbestand AS p_lagerbestand, p.angelegt_von AS p_angelegt_von " +
        "FROM bestellung b " +
        "LEFT JOIN bestellposition bp ON bp.bestellung_id = b.bestellung_id " +
        "LEFT JOIN produkt p ON p.sku = bp.sku " +
        "ORDER BY b.bestellung_id, bp.position_id";

    Map<Long, BestellungDetails> map = new LinkedHashMap<>();

    jdbc.query(sql, rs -> {
        long bestellungId = rs.getLong("bestellung_id");

        BestellungDetails b = map.get(bestellungId);
        if (b == null) {
            b = new BestellungDetails(
                bestellungId,
                rs.getLong("kunde_id"),
                rs.getLong("mitarbeiterzuweis"),
                rs.getObject("datum", OffsetDateTime.class),
                rs.getString("status")
            );
            map.put(bestellungId, b);
        }

        Long positionId = (Long) rs.getObject("position_id");
        if (positionId != null) {
            Produkt prod = new Produkt(
                rs.getString("p_sku"),
                rs.getString("p_name"),
                rs.getBigDecimal("p_preis"),
                rs.getInt("p_lagerbestand"),
                rs.getLong("p_angelegt_von")
            );

            b.getPositionen().add(new BestellpositionDetails(
                positionId,
                bestellungId,
                prod,
                rs.getInt("menge"),
                rs.getBigDecimal("gesamtpreis")
            ));
        }
    });

    return new ArrayList<>(map.values());
}

    
    public Optional<BestellungDetails> findDetailsById(long id) {
    String sql =
        "SELECT " +
        "  b.bestellung_id, b.kunde_id, b.mitarbeiterzuweis, b.datum, b.status, " +
        "  bp.position_id, bp.menge, " +
        "  (bp.menge * p.preis) AS gesamtpreis, " +
        "  p.sku AS p_sku, p.name AS p_name, p.preis AS p_preis, " +
        "  p.lagerbestand AS p_lagerbestand, p.angelegt_von AS p_angelegt_von " +
        "FROM bestellung b " +
        "LEFT JOIN bestellposition bp ON bp.bestellung_id = b.bestellung_id " +
        "LEFT JOIN produkt p ON p.sku = bp.sku " +
        "WHERE b.bestellung_id = ? " +
        "ORDER BY b.bestellung_id, bp.position_id";

    Map<Long, BestellungDetails> map = new LinkedHashMap<>();

    jdbc.query(sql, rs -> {
        long bestellungId = rs.getLong("bestellung_id");

        BestellungDetails b = map.get(bestellungId);
        if (b == null) {
            b = new BestellungDetails(
                bestellungId,
                rs.getLong("kunde_id"),
                rs.getLong("mitarbeiterzuweis"),
                rs.getObject("datum", OffsetDateTime.class),
                rs.getString("status")
            );
            map.put(bestellungId, b);
        }

        Long positionId = (Long) rs.getObject("position_id");
        if (positionId != null) {
            Produkt prod = new Produkt(
                rs.getString("p_sku"),
                rs.getString("p_name"),
                rs.getBigDecimal("p_preis"),
                rs.getInt("p_lagerbestand"),
                rs.getLong("p_angelegt_von")
            );

            b.getPositionen().add(new BestellpositionDetails(
                positionId,
                bestellungId,
                prod,
                rs.getInt("menge"),
                rs.getBigDecimal("gesamtpreis")
            ));
        }
    }, id);

    return map.values().stream().findFirst();
}

}
