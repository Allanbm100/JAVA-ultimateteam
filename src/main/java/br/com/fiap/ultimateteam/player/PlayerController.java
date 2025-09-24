package br.com.fiap.ultimateteam.player;

import br.com.fiap.ultimateteam.team.Team;
import br.com.fiap.ultimateteam.team.TeamService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

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
        model.addAttribute("playerRequestDTO", new PlayerRequestDTO());

        model.addAttribute("allTeams", teamService.findAll());
        teamService.findTeamById(1L).ifPresent(t -> model.addAttribute("team", t));

        return "player-form";
    }

    @PostMapping("/save")
    public String savePlayer(
            @ModelAttribute("playerRequestDTO") @Valid PlayerRequestDTO dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allTeams", teamService.findAll());
            teamService.findTeamById(1L).ifPresent(t -> model.addAttribute("team", t));
            return "player-form";
        }

        try {
            playerService.saveNewPlayerFromDTO(dto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("allTeams", teamService.findAll());
            model.addAttribute("errorMessage", "Erro ao salvar o jogador: " + e.getMessage());
            teamService.findTeamById(1L).ifPresent(t -> model.addAttribute("team", t));
            return "player-form";
        }

        return "redirect:/player";
    }

    @GetMapping("/edit/{id}")
    public String editPlayer(@PathVariable Long id, Model model) {
        playerService.findPlayerById(id).ifPresent(p -> model.addAttribute("player", p));
        model.addAttribute("teams", teamService.findAll());
        teamService.findTeamById(1L).ifPresent(t -> model.addAttribute("team", t));

        return "player-form";
    }

    @GetMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
        return "redirect:/player";
    }
}