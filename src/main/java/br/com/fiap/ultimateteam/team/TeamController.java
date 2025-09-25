package br.com.fiap.ultimateteam.team;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/profile")
    public String teamProfile(Model model) {
        var teamOptional = teamService.findTeamById(1L);

        teamOptional.ifPresent(team -> model.addAttribute("team", team));

        return "team-profile";
    }

    @GetMapping("/{id}")
    public String teamEditForm(@PathVariable Long id, Model model) {
        var teamOptional = teamService.findTeamById(id);

        if (teamOptional.isPresent()) {
            model.addAttribute("team", teamOptional.get());
            return "team-edit";
        } else {
            return "redirect:/team/profile";
        }
    }

    @PutMapping("/{id}")
    public String teamEditSave(@PathVariable Long id, @ModelAttribute Team team) {
        team.setId(id);
        teamService.saveTeam(team);
        return "redirect:/team/profile";
    }
}