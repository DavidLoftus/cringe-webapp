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

import java.sql.SQLException;
import java.util.List;

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

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        List<Game> games = gameRepository.search(String.format("%%%s%%", query));
        model.addAttribute("games", games);
        return "search";
    }
}
