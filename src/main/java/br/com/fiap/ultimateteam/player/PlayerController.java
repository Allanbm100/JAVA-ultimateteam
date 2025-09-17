package br.com.fiap.ultimateteam.player;

import br.com.fiap.ultimateteam.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final TeamService teamService;

    @GetMapping
    public String listPlayers(Model model) {
        model.addAttribute("players", playerService.findAll());
        teamService.findTeamById(1L).ifPresent(t -> model.addAttribute("team", t));
        return "player-list";
    }

    @GetMapping("/new")
    public String newPlayer(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("teams", teamService.findAll());
        return "player-form";
    }

    @PostMapping("/save")
    public String savePlayer(@ModelAttribute Player player) {
        playerService.savePlayer(player);
        return "redirect:/player";
    }

    @GetMapping("/edit/{id}")
    public String editPlayer(@PathVariable Long id, Model model) {
        playerService.findPlayerById(id).ifPresent(p -> model.addAttribute("player", p));
        model.addAttribute("teams", teamService.findAll());
        return "player-form";
    }

    @GetMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
        return "redirect:/player";
    }
}