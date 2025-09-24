package br.com.fiap.ultimateteam.attendance;

import br.com.fiap.ultimateteam.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final TeamService teamService;

    private static final int NUM_RECENT_TRAININGS = 10;

    @GetMapping("/grid")
    public String showAttendanceGrid(Model model) {

        var teamOptional = teamService.findTeamById(1L);

        if (teamOptional.isPresent()) {
            var team = teamOptional.get();
            model.addAttribute("team", team);

            Map<String, Object> gridData = attendanceService.getAttendanceGridData(team, NUM_RECENT_TRAININGS);

            model.addAttribute("players", gridData.get("players"));
            model.addAttribute("trainings", gridData.get("trainings"));
            model.addAttribute("attendanceMap", gridData.get("attendanceMap"));

            // 💡 CORREÇÃO: Prepara o mapa de status para ser usado pelo JavaScript no template.
            Map<String, String> statusDescriptions = new HashMap<>();
            for (AttendanceStatus status : AttendanceStatus.values()) {
                statusDescriptions.put(status.name(), status.getDescription());
            }
            model.addAttribute("statusDescriptions", statusDescriptions);
            // --------------------------------------------------------------------------

            return "attendance-grid";
        }

        return "redirect:/team/profile";
    }
}
