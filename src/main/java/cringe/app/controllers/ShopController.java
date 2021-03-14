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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ShopController {

    @Autowired
    public ArtifactRepository artifactRepository;

    @Autowired
    public GameRepository gameRepository;

    @Autowired
    public UserRepository userRepository;

    private boolean isGameVisible(Principal principal, Game game) {
        if (game.getVisibility() != Game.Visibility.PRIVATE) {
            return true;
        } else if (principal != null) {
            User user = userRepository.findByUsername(principal.getName());
            return user.getGames().contains(game);
        }
        return false;
    }

    private List<Game> filterVisibleGames(Principal principal, List<Game> games) {
        return games.stream().filter(game -> isGameVisible(principal, game)).collect(Collectors.toList());
    }

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        List<Game> games = gameRepository.findAll();
        model.addAttribute("games", filterVisibleGames(principal, games));
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

    @GetMapping("/search")
    public String search(@RequestParam String query, Principal principal, Model model) {
        List<Game> games = gameRepository.search(query);
        model.addAttribute("games", filterVisibleGames(principal, games));
        return "search";
    }
}
