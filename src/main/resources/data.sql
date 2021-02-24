INSERT INTO USERS(USERNAME, PASSWORD, FULL_NAME, EMAIL) VALUES( 'sa', '$2y$12$GVtpMA.Cr7k20Xq9fEbwAO6BimfbxRJ/Y3yz3A0EoCxuOlX.XJltq', 's a', 's@a.sa' );

INSERT INTO "PUBLIC"."ARTIFACTS" VALUES(1, 'application/x-zip-compressed', FILE_READ('./games/doom.jsdos'), 'doom.jsdos');
INSERT INTO "PUBLIC"."ARTIFACTS" VALUES(2, 'image/jpeg', FILE_READ('./games/doom.jpg'), 'doom.jpg');
INSERT INTO GAMES(TITLE, ARTIFACT_ID, ICON_ID, PRICE) VALUES ('Doom', 1, 2, 30);

INSERT INTO "PUBLIC"."ARTIFACTS" VALUES(3, 'application/x-zip-compressed', FILE_READ('./games/mspacman.jsdos'), 'mspacman.jsdos');
INSERT INTO "PUBLIC"."ARTIFACTS" VALUES(4, 'image/png', FILE_READ('./games/mspacman.png'), 'mspacman.png');
INSERT INTO GAMES(TITLE, ARTIFACT_ID, ICON_ID, PRICE) VALUES ('Ms Pacman', 3, 4, 4.99);

INSERT INTO "PUBLIC"."ARTIFACTS" VALUES(5, 'application/x-zip-compressed', FILE_READ('./games/gta.jsdos'), 'gta.jsdos');
INSERT INTO "PUBLIC"."ARTIFACTS" VALUES(6, 'image/png', FILE_READ('./games/gta.png'), 'gta.png');
INSERT INTO GAMES(TITLE, ARTIFACT_ID, ICON_ID, PRICE) VALUES ('Grand Theft Auto', 5, 6, 59.99);

INSERT INTO "PUBLIC"."ARTIFACTS" VALUES(7, 'application/x-zip-compressed', '', 'mspacpc.zip');
INSERT INTO "PUBLIC"."ARTIFACTS" VALUES(8, 'image/jpeg', FILE_READ('./games/cod.jpg'), 'cod.jpg');
INSERT INTO GAMES(TITLE, ARTIFACT_ID, ICON_ID, PRICE) VALUES ('Call of Duty', 7, 8, 50);