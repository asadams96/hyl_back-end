CREATE SCHEMA loan
    AUTHORIZATION admin_loan;


CREATE SEQUENCE loan.loan_id_loan_seq;

CREATE TABLE loan.loan (
                id_loan BIGINT NOT NULL DEFAULT nextval('loan.loan_id_loan_seq'),
                id_usager BIGINT NOT NULL,
                start_date TIMESTAMP NOT NULL,
                end_date TIMESTAMP,
                reference VARCHAR(15) NOT NULL,
                beneficiary VARCHAR(15) NOT NULL,
                information VARCHAR(100),
                reminder TIMESTAMP,
                CONSTRAINT loan_pk PRIMARY KEY (id_loan)
);


ALTER SEQUENCE loan.loan_id_loan_seq OWNED BY loan.loan.id_loan;


INSERT INTO 
	loan.loan (id_usager, start_date, end_date, reference, beneficiary, information, reminder)
VALUES 
	('1', NOW() - INTERVAL '10 day', NOW() - INTERVAL '5 day', 'Gladiator#1', 'Louis', 'Prêté au café de la Vilette', NOW() - INTERVAL '1 day'),
	('1', NOW() - INTERVAL '10 day', NOW() - INTERVAL '5 day', 'Gladiator#2', 'Pierre', 'Prêté sur le Champs de Mars', NOW()),
	('1', NOW() - INTERVAL '10 day', NOW() - INTERVAL '5 day', 'LaPrisonnière#1', 'Napoleon', 'Prété à deux pas de l''Arc de Triomphe', NOW()),
	('1', NOW() - INTERVAL '10 day', NOW() - INTERVAL '5 day', 'Lamiel#1', 'Jacques', 'Prêté à la cafétéria du bureau', NOW() + INTERVAL '1 day'),
	('1', NOW() - INTERVAL '3 day', null, 'Gladiator#1', 'Arthur', 'Prêté dans le bois de Vincennes', NOW() - INTERVAL '1 day'),
	('1', NOW() - INTERVAL '3 day', null, 'Gladiator#2', 'Phillipe', 'Prêté devant le Louvre', NOW()),
	('1', NOW() - INTERVAL '3 day', null, 'LaPrisonnière#1', 'Jeanne', 'Prété dans le Jardin du Luxembourg', NOW()),
	('1', NOW() - INTERVAL '3 day', null, 'Lamiel#1', 'Marie', 'Prêté au pied du musée Rodin', NOW() + INTERVAL '1 day');