-- INSERT INTO USERS(USERNAME, PASSWORD, FULL_NAME, EMAIL) VALUES( 'sa', '$2y$12$GVtpMA.Cr7k20Xq9fEbwAO6BimfbxRJ/Y3yz3A0EoCxuOlX.XJltq', 's a', 's@a.sa' );

INSERT INTO ARTIFACTS VALUES(1, 'application/x-zip-compressed', FILE_READ('./games/doom.jsdos'), 'doom.jsdos');
INSERT INTO ARTIFACTS VALUES(2, 'image/jpeg', FILE_READ('./games/doom.jpg'), 'doom.jpg');
INSERT INTO ARTIFACTS VALUES(13, 'image/jpeg', FILE_READ('./games/doom-logo.jpg'), 'doom-logo.jpg');
INSERT INTO ARTIFACTS VALUES(14, 'image/jpeg', FILE_READ('./games/doom-banner.jpg'), 'doom-banner.jpg');
INSERT INTO GAMES(TITLE, DESCRIPTION, RELEASE_ID, ICON_ID, LOGO_ID, BANNER_ID, PRICE, VISIBILITY) VALUES ('Doom', 'DOOM returns as a brutally fun and challenging modern-day shooter experience. Relentless demons, impossibly destructive guns, and fast, fluid movement provide the foundation for intense, first-person combat – whether you’re obliterating demon hordes through the depths of Hell in the single-player campaign, or competing against your friends in numerous multiplayer modes. Expand your gameplay experience using DOOM SnapMap game editor to easily create, play, and share your content with the world.', 1, 2, 13, 14, 30, 0);

INSERT INTO ARTIFACTS VALUES(3, 'application/x-zip-compressed', FILE_READ('./games/mspacman.jsdos'), 'mspacman.jsdos');
INSERT INTO ARTIFACTS VALUES(4, 'image/png', FILE_READ('./games/mspacman.png'), 'mspacman.png');
INSERT INTO ARTIFACTS VALUES(15, 'image/jpeg', FILE_READ('./games/mspacman-banner.jpg'), 'mspacman-banner.jpg');
INSERT INTO ARTIFACTS VALUES(16, 'image/jpeg', FILE_READ('./games/mspacman-logo.jpg'), 'mspacman-logo.jpg');
INSERT INTO GAMES(TITLE, DESCRIPTION, RELEASE_ID, ICON_ID, BANNER_ID, LOGO_ID, PRICE, VISIBILITY) VALUES ('Ms Pacman', 'Woah they even got my boi pacman.', 3, 4, 15, 16, 4.99, 0);

INSERT INTO ARTIFACTS VALUES(5, 'application/x-zip-compressed', FILE_READ('./games/gta.jsdos'), 'gta.jsdos');
INSERT INTO ARTIFACTS VALUES(6, 'image/png', FILE_READ('./games/gta.png'), 'gta.png');
INSERT INTO ARTIFACTS VALUES(9, 'image/jpeg', FILE_READ('./games/gta-banner.jpg'), 'gta-banner.jpg');
INSERT INTO ARTIFACTS VALUES(10, 'image/jpeg', FILE_READ('./games/gta-logo.jpg'), 'gta-logo.jpg');
INSERT INTO ARTIFACTS VALUES(11, 'image/jpeg', FILE_READ('./games/gta-background.jpg'), 'gta-background.jpg');
INSERT INTO GAMES(TITLE, DESCRIPTION, RELEASE_ID, ICON_ID, BANNER_ID, LOGO_ID, BACKGROUND_ID, PRICE, VISIBILITY)
    VALUES (
            'Grand Theft Auto',
            'Grand Theft Auto V for PC offers players the option to explore the award-winning world of Los Santos and Blaine County in resolutions of up to 4k and beyond, as well as the chance to experience the game running at 60 frames per second.',
            5, 6, 9, 10, 11,
            59.99, 0);

INSERT INTO ARTIFACTS VALUES(7, 'application/x-zip-compressed', '', 'mspacpc.zip');
INSERT INTO ARTIFACTS VALUES(8, 'image/jpeg', FILE_READ('./games/cod.jpg'), 'cod.jpg');
INSERT INTO ARTIFACTS VALUES(12, 'image/png', FILE_READ('./games/cod-banner.png'), 'cod-banner.png');
INSERT INTO GAMES(TITLE, DESCRIPTION, RELEASE_ID, ICON_ID, BANNER_ID, PRICE, VISIBILITY) VALUES ('Call of Duty', 'Welcome to 2065. A new breed of Black Ops soldier emerges and the lines are blurred between our own humanity and the cutting-edge military robotics that define the future of combat.', 7, 8, 12, 50, 1);

-- INSERT INTO CARTS VALUES(1);
-- INSERT INTO CARTS_GAMES VALUES(1, 1);
-- INSERT INTO CARTS_GAMES VALUES(1, 3);
--
-- UPDATE USERS SET cart_id = 1 WHERE USERNAME = 'sa';
