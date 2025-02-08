-- fournisseur d'identite

CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    motdepasse TEXT NOT NULL,
    datecreation  TIMESTAMP  DEFAULT NULL,
    codecreation TEXT UNIQUE NOT NULL,
    photo BYTEA
);

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE utilisateurrole (
    id SERIAL PRIMARY KEY,
    utilisateurid INT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    roleid INT NOT NULL REFERENCES role(id) ON DELETE CASCADE
);

CREATE TABLE statut (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE utilisateurstatut (
    id SERIAL PRIMARY KEY,
    utilisateurid INT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    statutid INT NOT NULL REFERENCES statut(id) ON DELETE CASCADE,
    datecreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tentativesconnexion (
    id SERIAL PRIMARY KEY,
    utilisateurid INT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    email VARCHAR(255) UNIQUE NOT NULL REFERENCES utilisateur(email) ON DELETE CASCADE,
    compteurtentative INT NOT NULL,
    datetentative TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE typesession (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE session (
    id SERIAL PRIMARY KEY,
    utilisateurid INT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    typesessionid INT DEFAULT 1 REFERENCES typesession(id) ON DELETE CASCADE,
    token TEXT NOT NULL,
    expireat TIMESTAMP NOT NULL,
    datecreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE authentification (
    id SERIAL PRIMARY KEY,
    utilisateurid INT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL REFERENCES utilisateur(email) ON DELETE CASCADE,
    pin TEXT NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    expireat TIMESTAMP NOT NULL,
    datecreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_email_pin UNIQUE (email, pin)
);

CREATE TABLE reinitialisation (
    id SERIAL PRIMARY KEY,
    utilisateurid INT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL REFERENCES utilisateur(email) ON DELETE CASCADE,
    codereinitialisation TEXT UNIQUE NOT NULL,
    expireat TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    datecreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_email_code UNIQUE (email, codereinitialisation)
);

INSERT INTO statut (id,nom, description) VALUES 
    (1,'normal', 'Utilisateur lambda'),
    (2,'bloque', 'Utilisateur bloque');

INSERT INTO typesession (id,nom, description) VALUES 
    (1,'action', 'session pour action');


-- crypto
CREATE TABLE cryptomonnaie (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    abrev VARCHAR(50) NOT NULL
);

CREATE TABLE changecrypto (
    id SERIAL PRIMARY KEY,
    cryptomonnaieid INT REFERENCES cryptomonnaie(id) ON DELETE CASCADE,
    valeur NUMERIC(20, 8) NOT NULL,
    datechangement TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE portemonnaiefiat (
    id SERIAL PRIMARY KEY,
    utilisateurid INT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    quantite NUMERIC(20, 8) DEFAULT 0
);

CREATE TABLE portemonnaiecrypto (
    id SERIAL PRIMARY KEY,
    utilisateurid INT NOT NULL REFERENCES utilisateur(id) ON DELETE CASCADE,
    cryptomonnaieid INT REFERENCES cryptomonnaie(id) ON DELETE CASCADE,
    quantite NUMERIC(20, 8) DEFAULT 0
);

CREATE TABLE typefond (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    description TEXT
);

INSERT INTO typefond (type, description) 
VALUES ('depot', 'Depot de fonds'), 
       ('retrait', 'Retrait de fonds');

CREATE TABLE fond (
    id SERIAL PRIMARY KEY,
    utilisateurid INT REFERENCES utilisateur(id) ON DELETE CASCADE,
    valeur NUMERIC(20, 8) NOT NULL,
    typefond INT REFERENCES typefond(id) ON DELETE SET NULL,
    istransaction BOOLEAN DEFAULT FALSE,
    isvalid BOOLEAN DEFAULT FALSE,
    codefond TEXT,
    expireat TIMESTAMP,
    datefond TIMESTAMP DEFAULT NOW()
);

CREATE TABLE annoncevente (
    id SERIAL PRIMARY KEY,
    vendeurid INT REFERENCES utilisateur(id) ON DELETE CASCADE,
    cryptomonnaieid INT REFERENCES cryptomonnaie(id) ON DELETE CASCADE,
    quantite NUMERIC(20, 8) NOT NULL,
    prix NUMERIC(20, 8) NOT NULL,
    isconfirmed BOOLEAN DEFAULT FALSE,
    date_annonce TIMESTAMP DEFAULT NOW()
);

CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    typeTransaction VARCHAR(10) DEFAULT 'vente',
    utilisateurid INT REFERENCES utilisateur(id) ON DELETE CASCADE,
    retraitid INT REFERENCES fond(id) ON DELETE SET NULL,
    depotid INT REFERENCES fond(id) ON DELETE SET NULL,
    cryptomonnaieid INT REFERENCES cryptomonnaie(id) ON DELETE CASCADE,
    quantitecrypto NUMERIC(20, 8) NOT NULL,
    datetransaction TIMESTAMP DEFAULT NOW()
);

CREATE OR REPLACE FUNCTION update_portefeuille_fiat()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.typefond = (SELECT id FROM typefond WHERE type = 'depot') THEN
        UPDATE portemonnaiefiat
        SET quantite = quantite + NEW.valeur
        WHERE utilisateurid = NEW.utilisateurid;
    ELSIF NEW.typefond = (SELECT id FROM typefond WHERE type = 'retrait') THEN
        UPDATE portemonnaiefiat
        SET quantite = quantite - NEW.valeur
        WHERE utilisateurid = NEW.utilisateurid;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_portefeuille_fiat
AFTER UPDATE ON transaction
FOR EACH ROW
WHEN (NEW.isconfirmed = TRUE AND OLD.isconfirmed = FALSE)  -- Déclenche uniquement quand `isconfirmed` passe de FALSE à TRUE
EXECUTE PROCEDURE update_portefeuille_fiat();


CREATE OR REPLACE FUNCTION update_portefeuille_crypto()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE portemonnaiecrypto
    SET quantite = quantite + NEW.quantitecrypto
    WHERE utilisateurid = NEW.acheteurid AND cryptomonnaieid = NEW.cryptomonnaieid;

    UPDATE portemonnaiecrypto
    SET quantite = quantite - NEW.quantitecrypto
    WHERE utilisateurid = NEW.vendeurid AND cryptomonnaieid = NEW.cryptomonnaieid;

    UPDATE annoncevente
    SET isvendue = TRUE
    WHERE id = NEW.annonceventeid;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_portefeuille_crypto
AFTER INSERT ON transaction
FOR EACH ROW
EXECUTE PROCEDURE update_portefeuille_crypto();
