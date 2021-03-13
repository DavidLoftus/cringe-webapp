package cringe.app.controllers;

import cringe.app.db.*;
import cringe.app.util.UploadType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ArtifactRepository artifactRepository;

    @Test
    void newGame() {
        final Game game = new Game();
        game.setId(1);
        game.setTitle("Doom");

        when(
                gameRepository.save(
                        argThat(x -> x.getTitle().equals(game.getTitle())))
        ).thenReturn(game);

        var ret = adminController.newGame(game.getTitle());
        assertEquals("/admin/game/1", ret.getUrl());

        verify(gameRepository, times(1)).save(any());
    }

    @Test
    void getEditGame() {
        final Game game = new Game();
        game.setId(1);
        game.setTitle("Doom");

        when(gameRepository.findGameById(1)).thenReturn(game);

        var model = new ExtendedModelMap();
        var template = adminController.editGame(1, model);
        assertEquals("admin/edit_game", template);
        assertEquals(game, model.getAttribute("game"));

        verify(gameRepository, times(1)).findGameById(1);
    }

    @Test
    void postEditGame() {
        final Game game = new Game();
        game.setId(1);
        game.setTitle("Doom");

        final Game updatedGame = new Game();
        updatedGame.setId(game.getId());
        updatedGame.setTitle("Doom 2");
        updatedGame.setDescription("Its doom yo");
        updatedGame.setPrice(29.99f);

        when(gameRepository.findGameById(1)).thenReturn(game);

        var model = new ExtendedModelMap();
        var redirect = adminController.editGame(1, updatedGame.getTitle(), updatedGame.getDescription(), updatedGame.getPrice(), model);
        assertEquals("/admin/game/1", redirect.getUrl());

        verify(gameRepository, times(1)).findGameById(1);
        verify(gameRepository, times(1)).save(argThat(g ->
                g.getTitle().equals(updatedGame.getTitle()) && g.getDescription().equals(updatedGame.getDescription()) && g.getPrice() == updatedGame.getPrice()
        ));
    }

    @Test
    void uploadFile() throws IOException, SQLException {
        final Game game = new Game();
        game.setId(1);
        game.setTitle("Doom");

        byte[] data = "random data".getBytes(StandardCharsets.UTF_8);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(data);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getOriginalFilename()).thenReturn("test.png");

        Artifact artifact = new Artifact();
        artifact.setData(new SerialBlob(data));
        artifact.setFileName("test.png");
        artifact.setContentType("image/png");

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId(1);
        updatedArtifact.setData(new SerialBlob(data));
        updatedArtifact.setFileName("test.png");
        updatedArtifact.setContentType("image/png");

        final Game updatedGame = new Game();
        updatedGame.setId(game.getId());
        updatedGame.setTitle("Doom");
        updatedGame.setLogo(artifact);

        when(gameRepository.findGameById(1)).thenReturn(game);

        AtomicReference<Artifact> storedArtifact = new AtomicReference<>();
        when(artifactRepository.save(any()))
                .thenReturn(updatedArtifact);

        adminController.uploadFile(1, file, UploadType.LOGO);

        verify(artifactRepository).save(argThat(x ->
                x.getContentType().equals(artifact.getContentType()) && x.getFileName().equals(artifact.getFileName())
        ));

        verify(gameRepository).save(argThat(x -> x.getLogo().getId() == updatedArtifact.getId()));
    }
}