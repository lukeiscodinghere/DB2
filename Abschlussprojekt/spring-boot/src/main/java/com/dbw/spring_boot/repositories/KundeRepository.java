package com.dbw.spring_boot.repositories;

import com.dbw.spring_boot.model.Adresse;
import com.dbw.spring_boot.model.Kunde;
import com.dbw.spring_boot.model.KundeAdresse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class KundeRepository {

    private final JdbcTemplate jdbc;

    public KundeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    
    private static final String BASE_SQL =
            "SELECT " +
            "  k.kunde_id, k.email, k.vorname, k.nachname, k.passwort, " +
            "  kha.typ, " +
            "  a.adresse_id, a.aktiv, a.strasse, a.hausnummer, a.plz, a.ort, a.land " +
            "FROM kunde k " +
            "LEFT JOIN kunde_hat_adressen kha ON kha.kunde_id = k.kunde_id " +
            "LEFT JOIN adresse a ON a.adresse_id = kha.adresse_id ";

    public List<Kunde> findAll() {
        return fetchAndGroup(BASE_SQL + "ORDER BY k.kunde_id, a.adresse_id");
    }

    public Optional<Kunde> findById(long id) {
        List<Kunde> list = fetchAndGroup(BASE_SQL + "WHERE k.kunde_id = ? ORDER BY k.kunde_id, a.adresse_id", id);
        return list.stream().findFirst();
    }

    public Optional<Kunde> findByEmail(String email) {
        List<Kunde> list = fetchAndGroup(BASE_SQL + "WHERE k.email = ? ORDER BY k.kunde_id, a.adresse_id", email);
        return list.stream().findFirst();
    }

    public Kunde insert(Kunde k) {
        Long newId = jdbc.queryForObject(
                "INSERT INTO kunde (email, vorname, nachname, passwort) VALUES (?, ?, ?, ?) RETURNING kunde_id",
                Long.class,
                k.getEmail(), k.getVorname(), k.getNachname(), k.getPasswort()
        );
        return findById(newId).orElseThrow();
    }

    public Optional<Kunde> updateAllById(long id, Kunde k) {
        int affected = jdbc.update(
                "UPDATE kunde SET email = ?, vorname = ?, nachname = ?, passwort = ? WHERE kunde_id = ?",
                k.getEmail(), k.getVorname(), k.getNachname(), k.getPasswort(), id
        );
        if (affected == 0) return Optional.empty();
        return findById(id);
    }

    
    private List<Kunde> fetchAndGroup(String sql, Object... params) {
        List<Map<String, Object>> rows = (params == null || params.length == 0)
                ? jdbc.queryForList(sql)
                : jdbc.queryForList(sql, params);

        
        Map<Long, Kunde> kunden = new LinkedHashMap<>();

        for (Map<String, Object> r : rows) {
            Long kundeId = ((Number) r.get("kunde_id")).longValue();

            Kunde kunde = kunden.computeIfAbsent(kundeId, id ->
                    new Kunde(
                            id,
                            (String) r.get("email"),
                            (String) r.get("vorname"),
                            (String) r.get("nachname"),
                            (String) r.get("passwort")
                    )
            );

            
            Object addrIdObj = r.get("adresse_id");
            if (addrIdObj != null) {
                Long adresseId = ((Number) addrIdObj).longValue();
                Boolean aktiv = (Boolean) r.get("aktiv");
                String strasse = (String) r.get("strasse");
                String hausnummer = (String) r.get("hausnummer");
                Long plz = r.get("plz") == null ? null : ((Number) r.get("plz")).longValue();
                String ort = (String) r.get("ort");
                String land = (String) r.get("land");
                String typ = (String) r.get("typ");

                Adresse adresse = new Adresse(adresseId, aktiv, strasse, hausnummer, plz, ort, land);
                kunde.getAdressen().add(new KundeAdresse(adresse, typ));
            }
        }

        return new ArrayList<>(kunden.values());
    }
}
