package br.com.fiap.ultimateteam.training;

import br.com.fiap.ultimateteam.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final TeamService teamService;

    @GetMapping
    public String listTrainings(Model model) {
        var teamOptional = teamService.findTeamById(1L);

        if (teamOptional.isPresent()) {
            var team = teamOptional.get();
            model.addAttribute("team", team);
            model.addAttribute("trainings", trainingService.findTrainingsByTeam(team));
            return "training-list";
        }
        return "redirect:/team/profile";
    }

    @GetMapping({"/new", "/edit/{id}"})
    public String showForm(@PathVariable(required = false) Long id, Model model) {
        Training training = id == null ? new Training() :
                trainingService.findTrainingById(id).orElse(new Training());

        model.addAttribute("training", training);
        teamService.findTeamById(1L).ifPresent(t -> model.addAttribute("team", t));

        return "training-form";
    }

    @PostMapping("/save")
    public String saveTraining(@ModelAttribute Training training) {
        teamService.findTeamById(1L).ifPresent(training::setTeam);

        trainingService.saveTraining(training);
        return "redirect:/training";
    }

    @GetMapping("/delete/{id}")
    public String deleteTraining(@PathVariable Long id) {
        trainingService.deleteById(id);
        return "redirect:/training";
    }
}