-- fournisseur d'identite

CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    motdepasse TEXT NOT NULL,
    datecreation  TIMESTAMP  DEFAULT NULL,
    codecreation TEXT UNIQUE NOT NULL,
    photo VARCHAR(255)
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

CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    typetransaction VARCHAR NOT NULL,
    utilisateurid INT REFERENCES utilisateur(id) ON DELETE CASCADE,
    retraitid INT REFERENCES fond(id) ON DELETE SET NULL,
    depotid INT REFERENCES fond(id) ON DELETE SET NULL,
    cryptomonnaieid INT REFERENCES cryptomonnaie(id) ON DELETE CASCADE,
    quantitecrypto NUMERIC(20, 8) NOT NULL,
    isconfirmed BOOLEAN DEFAULT FALSE,
    datetransaction TIMESTAMP DEFAULT NOW()
);

CREATE TABLE Commission (
    id BIGSERIAL PRIMARY KEY,
    achat_commission DECIMAL(19, 4) DEFAULT 0.0,
    vente_commission DECIMAL(19, 4) DEFAULT 0.0,
    crypto_id BIGINT,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (crypto_id) REFERENCES Cryptomonnaie(id)
);

CREATE TABLE pourcentage_commission (
    id SERIAL PRIMARY KEY,
    achat_commission DECIMAL(5, 4) NOT NULL,
    vente_commission DECIMAL(5, 4) NOT NULL,
    date TIMESTAMP NOT NULL
);

-- Insert initial commission percentages
INSERT INTO pourcentage_commission (achat_commission, vente_commission, date) VALUES
(0.005, 0.005, NOW());

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
AFTER INSERT ON fond
FOR EACH ROW
WHEN (NEW.isvalid = TRUE)
EXECUTE PROCEDURE update_portefeuille_fiat();

CREATE OR REPLACE FUNCTION update_portefeuille_crypto()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.isconfirmed = TRUE AND OLD.isconfirmed = FALSE THEN
        IF NEW.typetransactionid = (SELECT id FROM typetransaction WHERE type = 'achat') THEN
            -- Achat: Ajouter crypto
            UPDATE portemonnaiecrypto
            SET quantite = quantite + NEW.quantitecrypto
            WHERE utilisateurid = NEW.utilisateurid AND cryptomonnaieid = NEW.cryptomonnaieid;
        ELSIF NEW.typetransactionid = (SELECT id FROM typetransaction WHERE type = 'vente') THEN
            -- Vente: Enlever crypto
            UPDATE portemonnaiecrypto
            SET quantite = quantite - NEW.quantitecrypto
            WHERE utilisateurid = NEW.utilisateurid AND cryptomonnaieid = NEW.cryptomonnaieid;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_portefeuille_crypto
AFTER INSERT ON transaction
FOR EACH ROW
EXECUTE PROCEDURE update_portefeuille_crypto();

CREATE OR REPLACE FUNCTION insert_commission()
RETURNS TRIGGER AS $$
DECLARE
    commission_record RECORD;
    achat_commission_value DECIMAL(19, 4);
    vente_commission_value DECIMAL(19, 4);
BEGIN
    IF NEW.isconfirmed = TRUE AND OLD.isconfirmed = FALSE THEN
        -- Fetch the current commission percentages
        SELECT achat_commission, vente_commission INTO commission_record
        FROM pourcentage_commission
        WHERE date <= NOW()
        ORDER BY date DESC
        LIMIT 1;

        -- Calculate the commission based on the transaction type
        IF NEW.typetransaction = 'achat' THEN
            achat_commission_value := NEW.quantitecrypto * commission_record.achat_commission;
            INSERT INTO Commission (achat_commission, crypto_id, date)
            VALUES (achat_commission_value, NEW.cryptomonnaieid, NOW());
        ELSIF NEW.typetransaction = 'vente' THEN
            vente_commission_value := NEW.quantitecrypto * commission_record.vente_commission;
            INSERT INTO Commission (vente_commission, crypto_id, date)
            VALUES (vente_commission_value, NEW.cryptomonnaieid, NOW());
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_insert_commission
AFTER UPDATE ON transaction
FOR EACH ROW
EXECUTE PROCEDURE insert_commission();

INSERT INTO cryptomonnaie (nom, abrev) VALUES
('Bitcoin', 'BTC'),
('Ethereum', 'ETH'),
('Ripple', 'XRP'),
('Litecoin', 'LTC'),
('Bitcoin Cash', 'BCH'),
('Cardano', 'ADA'),
('Polkadot', 'DOT'),
('Binance Coin', 'BNB'),
('Stellar', 'XLM'),
('Dogecoin', 'DOGE');


CREATE OR REPLACE FUNCTION update_crypto_values()
RETURNS void AS $$
DECLARE
    crypto_rec RECORD;
    last_val NUMERIC(20,8);
    new_val NUMERIC(20,8);
    adjustment NUMERIC(20,8);
BEGIN
    FOR crypto_rec IN SELECT id FROM cryptomonnaie LOOP
        -- Récupérer la dernière valeur enregistrée pour la crypto
        SELECT valeur INTO last_val
        FROM changecrypto
        WHERE cryptomonnaieid = crypto_rec.id
        ORDER BY datechangement DESC
        LIMIT 1;
        
        -- Si aucune valeur n'existe, on utilise 10000
        IF last_val IS NULL THEN
            last_val := 10000;
        END IF;
        
        -- Calcul d'une variation aléatoire entre -1% et +1%
        adjustment := random() * 0.02 - 0.01;  -- random() donne une valeur entre 0 et 1
        new_val := last_val * (1 + adjustment);
        new_val := round(new_val, 8);
        
        -- Insertion du nouveau changement de valeur
        INSERT INTO changecrypto (cryptomonnaieid, valeur, datechangement)
        VALUES (crypto_rec.id, new_val, NOW());
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Insert admin user
INSERT INTO utilisateur (nom, email, motdepasse, datecreation, codecreation)
VALUES ('Admin', 'admin@admin.com', 'password123', NOW(), 'admin_code');

-- Get the ID of the admin user
DO $$
DECLARE
    admin_id INT;
BEGIN
    SELECT id INTO admin_id FROM utilisateur WHERE email = 'admin@admin.com';

    -- Insert admin role if not exists
    IF NOT EXISTS (SELECT 1 FROM role WHERE nom = 'admin') THEN
        INSERT INTO role (nom) VALUES ('admin');
    END IF;

    -- Get the ID of the admin role
    PERFORM id FROM role WHERE nom = 'admin';

    -- Assign admin role to the admin user
    INSERT INTO utilisateurrole (utilisateurid, roleid)
    VALUES (admin_id, (SELECT id FROM role WHERE nom = 'admin'));
END $$;
