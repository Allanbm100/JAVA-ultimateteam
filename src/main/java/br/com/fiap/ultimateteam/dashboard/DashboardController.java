package br.com.fiap.ultimateteam.dashboard;

import br.com.fiap.ultimateteam.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DashboardController {

    private final TeamService teamService;

    @GetMapping
    public String showDashboard(Model model) {
        var teamOptional = teamService.findTeamById(1L);

        if (teamOptional.isPresent()) {
            var team = teamOptional.get();
            model.addAttribute("team", team);


            model.addAttribute("totalPlayers", team.getPlayers().size());
            model.addAttribute("totalAttendanceAvg", 78.5);
            model.addAttribute("nextTrainingDate", "25/09/2025");
            model.addAttribute("nextTrainingTime", "19:30");

            model.addAttribute("recentTrainings", java.util.List.of(
                    new java.util.AbstractMap.SimpleEntry<>("18/09 - 85%", 0.85),
                    new java.util.AbstractMap.SimpleEntry<>("11/09 - 70%", 0.70)
            ));

            model.addAttribute("topPlayers", java.util.List.of(
                    new java.util.AbstractMap.SimpleEntry<>("Ana Silva", 95.0),
                    new java.util.AbstractMap.SimpleEntry<>("Pedro Costa", 88.0)
            ));

            return "dashboard";
        }
        return "redirect:/team/profile";
    }
}