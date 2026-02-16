package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Produkt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProduktRepository {

    private final JdbcTemplate jdbc;

    public ProduktRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Produkt> ROW_MAPPER = (rs, rowNum) ->
            new Produkt(
                    rs.getString("sku"),
                    rs.getString("name"),
                    rs.getBigDecimal("preis"),
                    rs.getInt("lagerbestand"),
                    rs.getLong("angelegt_von")
            );

    public List<Produkt> findAll() {
        return jdbc.query(
                "SELECT sku, name, preis, lagerbestand, angelegt_von FROM produkt ORDER BY sku",
                ROW_MAPPER
        );
    }

    public Optional<Produkt> findBySku(String sku) {
        List<Produkt> list = jdbc.query(
                "SELECT sku, name, preis, lagerbestand, angelegt_von FROM produkt WHERE sku = ?",
                ROW_MAPPER,
                sku
        );
        return list.stream().findFirst();
    }

    public Optional<Produkt> insert(Produkt p) {
        int affected = jdbc.update(
                "INSERT INTO produkt (sku, name, preis, lagerbestand, angelegt_von) VALUES (?, ?, ?, ?, ?)",
                p.getSku(), p.getName(), p.getPreis(), p.getLagerbestand(), p.getAngelegtVon()
        );
        if (affected == 0) return Optional.empty();
        return findBySku(p.getSku());
    }

    public boolean deleteBySku(String sku) {
        int affected = jdbc.update("DELETE FROM produkt WHERE sku = ?", sku);
        return affected > 0;
    }

    public Optional<Produkt> updateLagerbestandOnly(String sku, int lagerbestand) {
        int affected = jdbc.update(
                "UPDATE produkt SET lagerbestand = ? WHERE sku = ?",
                lagerbestand, sku
        );
        if (affected == 0) return Optional.empty();
        return findBySku(sku);
    }
}
