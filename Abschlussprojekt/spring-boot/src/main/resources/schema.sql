
CREATE TABLE adresse (
    adresse_id BIGSERIAL PRIMARY KEY,

    aktiv BOOLEAN NOT NULL,

    strasse VARCHAR(60) NOT NULL,
    hausnummer VARCHAR(10) NOT NULL,
    plz BIGINT NOT NULL,
    ort VARCHAR(60) NOT NULL,
    land VARCHAR(60) NOT NULL,


    CONSTRAINT chk_adresse_strasse_format
        CHECK (
            char_length(strasse) <= 60
            AND strasse ~ '^[A-ZÄÖÜ][a-zäöüß]{0,59}$'
        ),

    
    CONSTRAINT chk_adresse_hausnummer_format
        CHECK (
            hausnummer ~ '^[0-9]+[a-z]?$'
        ),

    
    CONSTRAINT chk_adresse_plz_format
        CHECK (
            plz >= 0
            AND plz <= 999999999999
        )
);


CREATE TABLE kunde (
    kunde_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(256) NOT NULL UNIQUE,
    vorname VARCHAR(32) NOT NULL,
    nachname VARCHAR(32) NOT NULL,
    passwort VARCHAR(20) NOT NULL,

    CONSTRAINT chk_kunde_vorname_format
        CHECK (vorname ~ '^[A-Za-zÄÖÜäöüß]{1,32}$'),

    CONSTRAINT chk_kunde_nachname_format
        CHECK (nachname ~ '^[A-Za-zÄÖÜäöüß]{1,32}$'),

    CONSTRAINT chk_kunde_email_format
        CHECK (
            position('@' in email) > 1
            AND position('.' in email) > position('@' in email)
        ),

    CONSTRAINT chk_kunde_passwort_laenge
        CHECK (char_length(passwort) BETWEEN 5 AND 20),

    CONSTRAINT chk_kunde_passwort_buchstabe
        CHECK (passwort ~ '[A-Za-z]'),

    CONSTRAINT chk_kunde_passwort_zahl
        CHECK (passwort ~ '[0-9]'),

    CONSTRAINT chk_kunde_passwort_sonderzeichen
        CHECK (passwort ~ '[^A-Za-z0-9]')
);

CREATE TABLE kunde_hat_adressen (
    adresse_id BIGINT NOT NULL,
    kunde_id BIGINT NOT NULL,
    typ VARCHAR(20) NOT NULL,

    CONSTRAINT pk_kunde_hat_adressen PRIMARY KEY (adresse_id, kunde_id),

    CONSTRAINT fk_kha_adresse
        FOREIGN KEY (adresse_id) REFERENCES adresse(adresse_id),

    CONSTRAINT fk_kha_kunde
        FOREIGN KEY (kunde_id) REFERENCES kunde(kunde_id),

    CONSTRAINT chk_kha_typ
        CHECK (typ IN ('Lieferadresse', 'Rechnungsadresse'))
);

CREATE TABLE mitarbeiter (
    personal_nr BIGSERIAL PRIMARY KEY,
    vorname VARCHAR(32) NOT NULL,
    nachname VARCHAR(32) NOT NULL,
    email VARCHAR(256) NOT NULL,
    passwort VARCHAR(20) NOT NULL,

    CONSTRAINT chk_mitarbeiter_passwort_laenge
        CHECK (char_length(passwort) BETWEEN 5 AND 20),

    CONSTRAINT chk_mitarbeiter_passwort_buchstabe
        CHECK (passwort ~ '[A-Za-z]'),

    CONSTRAINT chk_mitarbeiter_passwort_zahl
        CHECK (passwort ~ '[0-9]'),

    CONSTRAINT chk_mitarbeiter_passwort_sonderzeichen
        CHECK (passwort ~ '[^A-Za-z0-9]')
);

CREATE TABLE produkt (
    sku VARCHAR(64) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    preis NUMERIC(10,2) NOT NULL,
    lagerbestand INTEGER NOT NULL,
    angelegt_von BIGINT NOT NULL,

    CONSTRAINT chk_produkt_lagerbestand_nonneg
        CHECK (lagerbestand >= 0),

    
    CONSTRAINT chk_produkt_preis_format
        CHECK (preis >= 0 AND preis < 100000000),

    CONSTRAINT fk_produkt_angelegt_von
        FOREIGN KEY (angelegt_von) REFERENCES mitarbeiter(personal_nr)
    ON DELETE CASCADE
);

CREATE TABLE bestellung (
    bestellung_id BIGSERIAL PRIMARY KEY,
    datum TIMESTAMPTZ NOT NULL,
    status VARCHAR(20) NOT NULL,
    mitarbeiterzuweis BIGINT NOT NULL,
    kunde_id BIGINT NOT NULL,

    CONSTRAINT chk_bestellung_status
        CHECK (status IN ('neu', 'bezahlt', 'versendet', 'abgeschlossen', 'storniert')),

    CONSTRAINT fk_bestellung_mitarbeiter
        FOREIGN KEY (mitarbeiterzuweis) REFERENCES mitarbeiter(personal_nr),

    CONSTRAINT fk_bestellung_kunde
        FOREIGN KEY (kunde_id) REFERENCES kunde(kunde_id)
    ON DELETE CASCADE
);

CREATE TABLE bestellposition (
    position_id BIGSERIAL PRIMARY KEY,
    menge INTEGER NOT NULL,
    sku VARCHAR(64) NOT NULL,
    bestellung_id BIGINT NOT NULL,

    CONSTRAINT chk_bestellposition_menge_pos
        CHECK (menge > 0),

    CONSTRAINT fk_bestellposition_produkt
        FOREIGN KEY (sku) REFERENCES produkt(sku),

    CONSTRAINT fk_bestellposition_bestellung
        FOREIGN KEY (bestellung_id) REFERENCES bestellung(bestellung_id)
    ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION trg_bestellung_status_storno_check_and_restock()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'storniert' AND OLD.status <> 'storniert' THEN
        
        IF OLD.status NOT IN ('neu', 'bezahlt') THEN
            RAISE EXCEPTION
                'Status darf nur von neu oder bezahlt auf storniert geändert werden (alt: %, neu: %)',
                OLD.status, NEW.status;
        END IF;

        
        UPDATE produkt p
        SET lagerbestand = p.lagerbestand + bp.menge
        FROM bestellposition bp
        WHERE bp.bestellung_id = NEW.bestellung_id
          AND bp.sku = p.sku;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER bestellung_status_storno_check_and_restock
BEFORE UPDATE OF status ON bestellung
FOR EACH ROW
EXECUTE FUNCTION trg_bestellung_status_storno_check_and_restock();

CREATE OR REPLACE FUNCTION trg_bestellposition_before_insert()
RETURNS TRIGGER AS $$
DECLARE
    v_status TEXT;
    v_lager INTEGER;
BEGIN
    SELECT status INTO v_status
    FROM bestellung
    WHERE bestellung_id = NEW.bestellung_id;

    IF v_status IS NULL THEN
        RAISE EXCEPTION 'Bestellung % existiert nicht', NEW.bestellung_id;
    END IF;

    IF v_status <> 'storniert' THEN
        SELECT lagerbestand INTO v_lager
        FROM produkt
        WHERE sku = NEW.sku
        FOR UPDATE;

        IF v_lager < NEW.menge THEN
            RAISE EXCEPTION
                'Nicht genug Lagerbestand für Produkt % (verfügbar: %, benötigt: %)',
                NEW.sku, v_lager, NEW.menge;
        END IF;

        UPDATE produkt
        SET lagerbestand = lagerbestand - NEW.menge
        WHERE sku = NEW.sku;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER bestellposition_before_insert
BEFORE INSERT ON bestellposition
FOR EACH ROW
EXECUTE FUNCTION trg_bestellposition_before_insert();

CREATE OR REPLACE FUNCTION trg_bestellposition_before_update()
RETURNS TRIGGER AS $$
DECLARE
    v_status TEXT;
    v_lager INTEGER;
    v_diff INTEGER;
BEGIN
    
    IF NEW.sku <> OLD.sku OR NEW.bestellung_id <> OLD.bestellung_id THEN
        RAISE EXCEPTION 'sku oder bestellung_id dürfen nicht geändert werden';
    END IF;

    SELECT status INTO v_status
    FROM bestellung
    WHERE bestellung_id = NEW.bestellung_id;

    IF v_status <> 'storniert' THEN
        v_diff := NEW.menge - OLD.menge;

        IF v_diff > 0 THEN
            SELECT lagerbestand INTO v_lager
            FROM produkt
            WHERE sku = NEW.sku
            FOR UPDATE;

            IF v_lager < v_diff THEN
                RAISE EXCEPTION
                    'Nicht genug Lagerbestand für Erhöhung bei Produkt % (verfügbar: %, zusätzlich benötigt: %)',
                    NEW.sku, v_lager, v_diff;
            END IF;

            UPDATE produkt
            SET lagerbestand = lagerbestand - v_diff
            WHERE sku = NEW.sku;

        ELSIF v_diff < 0 THEN
            UPDATE produkt
            SET lagerbestand = lagerbestand + (-v_diff)
            WHERE sku = NEW.sku;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER bestellposition_before_update
BEFORE UPDATE ON bestellposition
FOR EACH ROW
EXECUTE FUNCTION trg_bestellposition_before_update();

CREATE OR REPLACE FUNCTION trg_bestellposition_after_delete()
RETURNS TRIGGER AS $$
DECLARE
    v_status TEXT;
BEGIN
    SELECT status INTO v_status
    FROM bestellung
    WHERE bestellung_id = OLD.bestellung_id;

    IF COALESCE(v_status, '') <> 'storniert' THEN
        UPDATE produkt
        SET lagerbestand = lagerbestand + OLD.menge
        WHERE sku = OLD.sku;
    END IF;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER bestellposition_after_delete
AFTER DELETE ON bestellposition
FOR EACH ROW
EXECUTE FUNCTION trg_bestellposition_after_delete();


-- =========================================================
-- Aufgabe 6: Views (Reports)
-- =========================================================

-- (a) v_kunde_summe_anzahl_bestellungen
CREATE OR REPLACE VIEW v_kunde_summe_anzahl_bestellungen AS
SELECT
    k.kunde_id AS kunde_id,
    k.email    AS email,
    COUNT(DISTINCT b.bestellung_id) AS anzahl_bestellungen,
    COALESCE(SUM(p.preis * bp.menge), 0) AS gesamtsumme
FROM kunde k
LEFT JOIN bestellung b
    ON b.kunde_id = k.kunde_id
LEFT JOIN bestellposition bp
    ON bp.bestellung_id = b.bestellung_id
LEFT JOIN produkt p
    ON p.sku = bp.sku
GROUP BY k.kunde_id, k.email
ORDER BY k.kunde_id ASC;


-- (b) v_produkt_verkaufszahlen
CREATE OR REPLACE VIEW v_produkt_verkaufszahlen AS
SELECT
    p.sku  AS sku,
    p.name AS name,

    COALESCE(SUM(
        CASE
            WHEN b.status NOT IN ('neu', 'storniert') THEN bp.menge
            ELSE 0
        END
    ), 0) AS gesamt_verkaufte_menge,

    COALESCE(SUM(
        CASE
            WHEN b.status NOT IN ('neu', 'storniert') THEN (p.preis * bp.menge)
            ELSE 0
        END
    ), 0) AS umsatz,

    COALESCE(COUNT(DISTINCT b.bestellung_id), 0)AS anzahl_bestellungen

FROM produkt p
LEFT JOIN bestellposition bp
    ON bp.sku = p.sku
LEFT JOIN bestellung b
    ON b.bestellung_id = bp.bestellung_id
GROUP BY p.sku, p.name
ORDER BY gesamt_verkaufte_menge DESC;



-- (c) v_mitarbeiter_uebersicht
CREATE OR REPLACE VIEW v_mitarbeiter_uebersicht AS
SELECT
    m.personal_nr AS personal_nr,
    COUNT(DISTINCT b.bestellung_id) AS anzahl_verwalteter_bestellungen,
    COUNT(DISTINCT p.sku) AS anzahl_angelegter_produkte
FROM mitarbeiter m
LEFT JOIN bestellung b
    ON b.mitarbeiterzuweis = m.personal_nr
LEFT JOIN produkt p
    ON p.angelegt_von = m.personal_nr
GROUP BY m.personal_nr
ORDER BY m.personal_nr ASC;


-- (d) v_mitarbeiter_bestellstatus_uebersicht
CREATE OR REPLACE VIEW v_mitarbeiter_bestellstatus_uebersicht AS
SELECT
    m.personal_nr AS personal_nr,
    s.status      AS status,
    COUNT(b.bestellung_id) AS anzahl_bestellungen
FROM mitarbeiter m
CROSS JOIN (
    VALUES
        ('abgeschlossen'::varchar),
        ('bezahlt'::varchar),
        ('neu'::varchar),
        ('storniert'::varchar),
        ('versendet'::varchar)
) AS s(status)
LEFT JOIN bestellung b
    ON b.mitarbeiterzuweis = m.personal_nr
   AND b.status = s.status
GROUP BY m.personal_nr, s.status
ORDER BY m.personal_nr ASC, s.status ASC;
