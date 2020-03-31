/* --------------------------------------------------- USER_SCHEMA ---------------------------------------------------*/
INSERT INTO usager.usager (pseudo, email, password, surname, name, civility, cellphone) /*password not encrypted = 'password' */
VALUES ('asadams', 'asadams89@gmail.com', 'hEwlcpqmhfzKNihzo8+hi1OQ/Ws3DZkdQfieG/rrDBRJ0zoFR2u4C/DMyIBLC2CrVycvtJGFjaVr9RHI0dj3Xg==', 'De Abreu', 'Ayrton', 'M', '+33602160808');


/* --------------------------------------------------- LOAN_SCHEMA ---------------------------------------------------*/
INSERT INTO 
	loan.loan (id_usager, start_date, end_date, reference, beneficiary, information, reminder)
VALUES 
	('1', NOW() - INTERVAL '10 day', NOW() - INTERVAL '5 day', 'Gladiator#1', 'Louis', 'Prêté au café de la Vilette', NOW() - INTERVAL '1 day'),
	('1', NOW() - INTERVAL '10 day', NOW() - INTERVAL '5 day', 'Gladiator#2', 'Pierre', 'Prêté sur le Champs de Mars', NOW()),
	('1', NOW() - INTERVAL '10 day', NOW() - INTERVAL '5 day', 'LaPrisonnière#1', 'Napoleon', 'Prété à deux pas de l''Arc de Triomphe', NOW()),
	('1', NOW() - INTERVAL '10 day', NOW() - INTERVAL '5 day', 'Lamiel#1', 'Jacques', 'Prêté à la cafétéria du bureau', NOW() + INTERVAL '1 day'),
	('1', NOW() - INTERVAL '3 day', null, 'Gladiator#2', 'Phillipe', 'Prêté devant le Louvre', NOW()),
	('1', NOW() - INTERVAL '3 day', null, 'LaPrisonnière#1', 'Jeanne', 'Prété dans le Jardin du Luxembourg', NOW()),
	('1', NOW() - INTERVAL '3 day', null, 'Lamiel#1', 'Marie', 'Prêté au pied du musée Rodin', NOW() + INTERVAL '1 day');


/* --------------------------------------------------- ITEM_SCHEMA ---------------------------------------------------*/	
INSERT INTO 
	item.category (id_category_parent, id_usager, name)
VALUES 
	(null, 1, 'DVD'),
	(1, 1, 'Péplum'),
	(null, 1, 'Livre'),
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