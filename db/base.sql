
CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    motdepasse TEXT NOT NULL,
    datecreation  TIMESTAMP  DEFAULT NULL,
    codecreation TEXT UNIQUE NOT NULL 
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
