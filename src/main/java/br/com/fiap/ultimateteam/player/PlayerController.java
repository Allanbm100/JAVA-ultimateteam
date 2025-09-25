package br.com.fiap.ultimateteam.player;

import br.com.fiap.ultimateteam.team.Team;
import br.com.fiap.ultimateteam.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final TeamService teamService;

    private Team getSingleTeam() {
        return teamService.findTeamById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Time não encontrado"));
    }

    @GetMapping
    public String listPlayers(Model model) {
        model.addAttribute("players", playerService.findAll());
        model.addAttribute("team", getSingleTeam());
        return "player-list";
    }

    @GetMapping("/new")
    public String newPlayer(Model model) {
        model.addAttribute("playerRequestDTO", new PlayerRequestDTO());
        model.addAttribute("allTeams", teamService.findAll());
        teamService.findTeamById(1L).ifPresent(t -> model.addAttribute("team", t));
        return "player-new";
    }

    @PostMapping("/save")
    public String savePlayer(
            @ModelAttribute("playerRequestDTO") @Valid PlayerRequestDTO dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allTeams", teamService.findAll());
            model.addAttribute("team", getSingleTeam());
            return "player-new";
        }

        try {
            dto.setTeamId(getSingleTeam().getId());
            playerService.saveNewPlayerFromDTO(dto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("allTeams", teamService.findAll());
            model.addAttribute("errorMessage", "Erro ao salvar o jogador: " + e.getMessage());
            model.addAttribute("team", getSingleTeam());
            return "player-new";
        }

        return "redirect:/player";
    }

    @GetMapping("/edit/{id}")
    public String editPlayer(@PathVariable Long id, Model model) {
        playerService.findPlayerById(id)
                .ifPresent(p -> model.addAttribute("playerRequestDTO", PlayerRequestDTO.fromEntity(p)));
        model.addAttribute("allTeams", teamService.findAll());
        model.addAttribute("team", getSingleTeam());
        return "player-edit";
    }

    @PutMapping("/edit/{id}")
    public String updatePlayer(
            @PathVariable Long id,
            @ModelAttribute("playerRequestDTO") @Valid PlayerRequestDTO dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("playerRequestDTO", dto);
            model.addAttribute("allTeams", teamService.findAll());
            model.addAttribute("team", getSingleTeam());
            return "player-edit";
        }

        try {
            dto.setId(id);
            dto.setTeamId(getSingleTeam().getId());
            playerService.updatePlayerFromDTO(dto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("playerRequestDTO", dto);
            model.addAttribute("allTeams", teamService.findAll());
            model.addAttribute("errorMessage", "Erro ao atualizar o jogador: " + e.getMessage());
            model.addAttribute("team", getSingleTeam());
            return "player-edit";
        }

        return "redirect:/player";
    }

    @DeleteMapping("/{id}")
    public String deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
        return "redirect:/player";
    }
}