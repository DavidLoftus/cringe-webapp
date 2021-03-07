package cringe.app.controllers;

import cringe.app.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.sql.SQLException;

@Controller
public class ShopController {

    @Autowired
    public ArtifactRepository artifactRepository;

    @Autowired
    public GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", gameRepository.findAll());
        return "index";
    }

    @GetMapping("/artifact/{id}/**")
    @ResponseBody
    public ResponseEntity<Resource> getArtifact(@PathVariable int id) throws SQLException {
        final Artifact artifact = artifactRepository.findArtifactById(id);
        if (artifact == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Resource resource = new InputStreamResource(artifact.getData().getBinaryStream());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, artifact.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + artifact.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/game/{id}")
    public String viewGame(Principal principal, @PathVariable int id, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("game", game);
        if(user.getGames().contains(game)) {
            model.addAttribute("owns_game", true);
        } else {
            model.addAttribute("owns_game", false);
        }
        return "view_game";
    }

    @GetMapping("/game/{id}/buy")
    public String buyGame(@PathVariable int id, Model model) {
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("game", game);
        return "buy_game";
    }

    @GetMapping("/game/{id}/play")
    public String playGame(@PathVariable int id, Model model) {
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("game", game);
        return "jsdos";
    }
}
