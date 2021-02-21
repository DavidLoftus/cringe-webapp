INSERT INTO USERS(USERNAME, PASSWORD, FULL_NAME, EMAIL) VALUES( 'sa', '$2y$12$GVtpMA.Cr7k20Xq9fEbwAO6BimfbxRJ/Y3yz3A0EoCxuOlX.XJltq', 's a', 's@a.sa' );

INSERT INTO "PUBLIC"."RELEASES" VALUES(1, 'application/x-zip-compressed', FILE_READ('./games/DOOM-@evilution.zip'), 'DOOM-@evilution.zip');
INSERT INTO "PUBLIC"."RELEASES" VALUES(2, 'image/jpeg', FILE_READ('./games/doom.jpg'), 'doom.jpg');
INSERT INTO GAMES(TITLE, ARTIFACT_ID, ICON_ID, START_PATH) VALUES ('Doom', 1, 2, './DOOM/DOOM.EXE');

INSERT INTO "PUBLIC"."RELEASES" VALUES(3, 'application/x-zip-compressed', FILE_READ('./games/mspacpc.zip'), 'mspacpc.zip');
INSERT INTO "PUBLIC"."RELEASES" VALUES(4, 'image/png', FILE_READ('./games/mspacpc.png'), 'mspacpc.png');
INSERT INTO GAMES(TITLE, ARTIFACT_ID, ICON_ID, START_PATH) VALUES ('Ms Pacman', 3, 4, './MSPAC.EXE');

INSERT INTO "PUBLIC"."RELEASES" VALUES(5, 'application/x-zip-compressed', '', 'mspacpc.zip');
INSERT INTO "PUBLIC"."RELEASES" VALUES(6, 'image/jpeg', FILE_READ('./games/cod.jpg'), 'cod.jpg');
INSERT INTO GAMES(TITLE, ARTIFACT_ID, ICON_ID, START_PATH) VALUES ('Call of Duty', 5, 6, '');