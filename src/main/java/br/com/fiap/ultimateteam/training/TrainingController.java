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

    private void addTeamToModel(Model model) {
        teamService.findTeamById(1L).ifPresent(t -> model.addAttribute("team", t));
    }

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

    @GetMapping("/new")
    public String newTrainingForm(Model model) {
        model.addAttribute("training", new Training());
        addTeamToModel(model);
        return "training-new"; // Nova View
    }

    @PostMapping("/save")
    public String saveTraining(@ModelAttribute Training training) {
        teamService.findTeamById(1L).ifPresent(training::setTeam);
        trainingService.saveTraining(training);
        return "redirect:/training";
    }

    @GetMapping("/{id}")
    public String editTrainingForm(@PathVariable Long id, Model model) {
        Training training = trainingService.findTrainingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado com ID: " + id));

        model.addAttribute("training", training);
        addTeamToModel(model);

        return "training-edit";
    }

    @PutMapping("/{id}")
    public String updateTraining(@PathVariable Long id, @ModelAttribute Training training) {
        training.setId(id);
        teamService.findTeamById(1L).ifPresent(training::setTeam);

        trainingService.saveTraining(training);
        return "redirect:/training";
    }

    @DeleteMapping("/{id}")
    public String deleteTraining(@PathVariable Long id) {
        trainingService.deleteById(id);
        return "redirect:/training";
    }
}