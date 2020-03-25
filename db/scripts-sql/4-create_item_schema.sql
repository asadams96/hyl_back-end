CREATE SCHEMA item
    AUTHORIZATION admin_item;



CREATE SEQUENCE item.category_id_category_seq;

CREATE TABLE item.category (
                id_category BIGINT NOT NULL DEFAULT nextval('item.category_id_category_seq'),
                id_category_parent BIGINT,
                id_usager BIGINT NOT NULL,
                name VARCHAR(15) NOT NULL,
                CONSTRAINT category_pk PRIMARY KEY (id_category)
);


ALTER SEQUENCE item.category_id_category_seq OWNED BY item.category.id_category;

CREATE SEQUENCE item.item_id_item_seq;

CREATE TABLE item.item (
                id_item BIGINT NOT NULL DEFAULT nextval('item.item_id_item_seq'),
                description VARCHAR(50),
                name VARCHAR(15) NOT NULL,
                id_usager BIGINT NOT NULL,
                id_category BIGINT,
                CONSTRAINT item_pk PRIMARY KEY (id_item)
);


ALTER SEQUENCE item.item_id_item_seq OWNED BY item.item.id_item;

CREATE SEQUENCE item.subitem_id_subitem_seq;

CREATE TABLE item.subitem (
                id_subitem BIGINT NOT NULL DEFAULT nextval('item.subitem_id_subitem_seq'),
                reference VARCHAR(15) NOT NULL,
                id_item BIGINT NOT NULL,
                CONSTRAINT subitem_pk PRIMARY KEY (id_subitem)
);


ALTER SEQUENCE item.subitem_id_subitem_seq OWNED BY item.subitem.id_subitem;

CREATE SEQUENCE item.picture_id_picture_seq;

CREATE TABLE item.picture (
                id_picture BIGINT NOT NULL DEFAULT nextval('item.picture_id_picture_seq'),
                url VARCHAR,
                name VARCHAR(15) NOT NULL,
                id_subitem BIGINT NOT NULL,
                CONSTRAINT picture_pk PRIMARY KEY (id_picture)
);


ALTER SEQUENCE item.picture_id_picture_seq OWNED BY item.picture.id_picture;

CREATE SEQUENCE item.tracking_sheet_id_tracking_sheet_seq;

CREATE TABLE item.tracking_sheet (
                id_tracking_sheet BIGINT NOT NULL DEFAULT nextval('item.tracking_sheet_id_tracking_sheet_seq'),
                date TIMESTAMP NOT NULL,
                comment VARCHAR(150) NOT NULL,
                id_subitem BIGINT NOT NULL,
                id_loan BIGINT,
                CONSTRAINT tracking_sheet_pk PRIMARY KEY (id_tracking_sheet)
);


ALTER SEQUENCE item.tracking_sheet_id_tracking_sheet_seq OWNED BY item.tracking_sheet.id_tracking_sheet;

ALTER TABLE item.item ADD CONSTRAINT category_item_fk
FOREIGN KEY (id_category)
REFERENCES item.category (id_category)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE item.subitem ADD CONSTRAINT item_subitem_fk
FOREIGN KEY (id_item)
REFERENCES item.item (id_item)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE item.tracking_sheet ADD CONSTRAINT subitem_tracking_sheet_fk
FOREIGN KEY (id_subitem)
REFERENCES item.subitem (id_subitem)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE item.picture ADD CONSTRAINT subitem_picture_fk
FOREIGN KEY (id_subitem)
REFERENCES item.subitem (id_subitem)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;



INSERT INTO 
	item.category (id_category_parent, id_usager, name)
VALUES 
	(null, 1, 'DVD'),
	(1, 1, '‎Péplum'),
	(null, 1, '‎Livre'),
	(3, 1, 'Proust');
	

INSERT INTO 
	item.item (description, name, id_usager, id_category)
VALUES
	('Version collector', 'Gladiator', '1', '2'),
	('Livre de poche', 'La prisonnière', '1', '4'),
	('Livre broché', 'Lamiel', '1', '4');


INSERT INTO 
	item.subitem (reference, id_item)
VALUES 
	('Gladiator#1', '1'),
	('Gladiator#2', '1'),
	('LaPrisonnière#1', '2'),
	('Lamiel#1', '3');


INSERT INTO 
	item.picture (url, name, id_subitem)
VALUES 
	('user1/item1/sub1/img-1.jpg', 'face-avant', '1'),
	('user1/item1/sub1/img-2.jpg', 'face-arrière', '1'),
	('user1/item1/sub2/img-3.jpg', 'face-avant', '2'),
	('user1/item1/sub2/img-4.jpg', 'face-arrière', '2'),
	('user1/item2/sub3/img-5.jpg', 'face-avant', '3'),
	('user1/item2/sub3/img-6.jpg', 'face-arrière', '3'),
	('user1/item3/sub4/img-7.jpg', 'face-avant', '4'),
	('user1/item3/sub4/img-8.jpg', 'face-arrière', '4');


INSERT INTO 
	item.tracking_sheet (date, comment, id_subitem, id_loan)
VALUES
	(NOW() - INTERVAL '5 day', 'Tâche sur le dvd (heuresement pas du coté de la lecture...) -> partie en nettoyant mais quand même !', '1', '1'),
	(NOW() - INTERVAL '5 day', 'Manque le livret du dvd qui à priori n''y était pas ...,', '2', '2'),
	(NOW() - INTERVAL '5 day', 'Trace de stylo bille sur le dos du livre -> merci les enfants !', '3', '3'),
	(NOW() - INTERVAL '5 day', 'A corné les pages à la place d''utiliser un marque-page ! Hallucinant !', '4', '4'),
	(NOW() - INTERVAL '4 day', 'Déchirure sur face avant de la jacquette en bas a gauche (ma faute).', '1', null),
	(NOW() - INTERVAL '4 day', 'Rayure de 1.2cm après que j''ai fais tomber le DVD.', '2', null),
	(NOW() - INTERVAL '4 day', 'Page 19 avec une trace de café de la taille d''une pièce de 2€ (ma faute).', '3', null),
	(NOW() - INTERVAL '4 day', 'Manque la page 5 (préface)', '4', null);
	
	
/* Correction bug imcompréhensible et inédit (insert le nom mais impossible de le retrouvé s'il n'est pas remis a jour.)*/
	UPDATE item.category
	SET name = 'Péplum'
	WHERE id_category = 2;
	
	UPDATE item.category
	SET name = 'Livre'
	WHERE id_category = 3;