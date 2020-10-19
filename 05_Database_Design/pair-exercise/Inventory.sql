BEGIN TRANSACTION;

CREATE TABLE pet (
    pet_id serial,
    pet_name varchar NOT NULL,
    pet_age varchar NOT NULL,
    pet_type varchar NOT NULL,
    owner_name varchar NOT NULL,
    CONSTRAINT pk_pet_id PRIMARY KEY (pet_id)
);

CREATE TABLE vet(
    vet_id serial,
    pet_id int,
    procedure varchar NOT NULL,
    visit_date date NOT NULL,
    CONSTRAINT pk_vet_id PRIMARY KEY (vet_id),
    CONSTRAINT fk_pet_id FOREIGN KEY (pet_id) REFERENCES pet (pet_id)
);

COMMIT TRANSACTION;