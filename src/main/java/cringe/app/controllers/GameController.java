package cringe.app.controllers;

import cringe.app.db.Game;
import cringe.app.db.GameRepository;
import cringe.app.db.User;
import cringe.app.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/game")
public class GameController {
    @Autowired
    public GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    private boolean checkOwnsGame(Principal principal, Game game) {
        if (principal == null) {
            return false;
        }

        User user = userRepository.findByUsername(principal.getName());
        return user.getGames().contains(game);
    }

    @GetMapping("/{id}")
    public String viewGame(Principal principal, @PathVariable int id, Model model) {
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("game", game);
        model.addAttribute("owns_game", checkOwnsGame(principal, game));
        return "view_game";
    }

    @GetMapping("/{id}/buy")
    public String buyGame(@PathVariable int id, Model model) {
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("game", game);
        return "buy_game";
    }

    @GetMapping("/{id}/play")
    public String playGame(@PathVariable int id, Principal principal, Model model) {
        Game game = gameRepository.findGameById(id);
        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!checkOwnsGame(principal, game)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        model.addAttribute("game", game);
        return "jsdos";
    }
}
