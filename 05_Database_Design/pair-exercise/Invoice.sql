BEGIN TRANSACTION;
--Assumptions--
-- Entire address is entered as one string
-- Prefix (Mr., Mrs., etc.) is included in the same
-- Tax rate as entered is already divided by 100 (8% = 0.08, etc.)

CREATE TABLE customer (
  customer_id serial,
  name varchar NOT NULL,
  address varchar NOT NULL,
  CONSTRAINT pk_customer_id PRIMARY KEY (customer_id)
);

CREATE TABLE procedure (
    procedure_id serial,
    pet_name varchar NOT NULL,
    procedure_name varchar NOT NULL,
    amount numeric(6, 2) NOT NULL,
    CONSTRAINT pk_procedure_id PRIMARY KEY (procedure_id)
);

CREATE TABLE transaction (
    invoice_number serial,
    date date NOT NULL,
    hospital_name varchar NOT NULL,
    procedure_id int NOT NULL,
    customer_id int NOT NULL,
    tax_rate numeric(4, 2),
    CONSTRAINT pk_invoice_number PRIMARY KEY (invoice_number),
    CONSTRAINT fk_customer_id FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    CONSTRAINT fk_procedure_id FOREIGN KEY (procedure_id) REFERENCES procedure(procedure_id)
);

COMMIT;