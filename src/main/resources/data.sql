INSERT INTO "PUBLIC"."RELEASES" VALUES(1, 'application/x-zip-compressed', FILE_READ('./games/DOOM-@evilution.zip'), 'DOOM-@evilution.zip');
INSERT INTO "PUBLIC"."RELEASES" VALUES(2, 'image/jpeg', FILE_READ('./games/doom.jpg'), 'doom.jpg');
INSERT INTO GAMES(TITLE, ARTIFACT_ID, ICON_ID) VALUES ('Doom', 1, 2);
INSERT INTO USERS(USERNAME, PASSWORD, FULL_NAME, EMAIL) VALUES( 'sa', '$2y$12$GVtpMA.Cr7k20Xq9fEbwAO6BimfbxRJ/Y3yz3A0EoCxuOlX.XJltq', 's a', 's@a.sa' );