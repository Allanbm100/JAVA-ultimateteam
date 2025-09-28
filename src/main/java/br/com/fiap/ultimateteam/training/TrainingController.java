package br.com.fiap.ultimateteam.training;

import br.com.fiap.ultimateteam.team.Team;
import br.com.fiap.ultimateteam.team.TeamService;
import br.com.fiap.ultimateteam.user.User;
import br.com.fiap.ultimateteam.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final TeamService teamService;
    private final UserRepository userRepository;

    private Optional<Team> getAuthenticatedUserTeam(OAuth2User principal) {
        if (principal == null) return Optional.empty();
        Optional<User> userOptional = userRepository.findByEmail(principal.getAttribute("email"));
        return userOptional.map(User::getTeam);
    }

    @GetMapping
    public String listTrainings(Model model, @AuthenticationPrincipal OAuth2User principal) {
        Optional<Team> teamOptional = getAuthenticatedUserTeam(principal);

        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            model.addAttribute("team", team);
            model.addAttribute("trainings", trainingService.findTrainingsByTeam(team));
            return "training-list";
        }
        return "redirect:/team/profile";
    }

    @GetMapping("/new")
    public String newTrainingForm(Model model, @AuthenticationPrincipal OAuth2User principal) {
        getAuthenticatedUserTeam(principal).ifPresent(team -> model.addAttribute("team", team));
        model.addAttribute("training", new Training());
        return "training-new";
    }

    @PostMapping("/save")
    public String saveTraining(@ModelAttribute @Valid Training training, BindingResult result, Model model, @AuthenticationPrincipal OAuth2User principal, RedirectAttributes redirectAttributes) {
        Optional<Team> teamOptional = getAuthenticatedUserTeam(principal);

        if (teamOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuário ou time não encontrado.");
            return "redirect:/training";
        }

        if (result.hasErrors()) {
            model.addAttribute("team", teamOptional.get());
            return "training-new";
        }

        try {
            training.setTeam(teamOptional.get());
            trainingService.saveTraining(training);
            redirectAttributes.addFlashAttribute("successMessage", "Treino salvo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar o treino: " + e.getMessage());
            return "redirect:/training/new";
        }
        return "redirect:/training";
    }

    @GetMapping("/{id}")
    public String editTrainingForm(@PathVariable Long id, Model model, @AuthenticationPrincipal OAuth2User principal) {
        Training training = trainingService.findTrainingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado com ID: " + id));
        model.addAttribute("training", training);
        getAuthenticatedUserTeam(principal).ifPresent(team -> model.addAttribute("team", team));
        return "training-edit";
    }

    @PutMapping("/{id}")
    public String updateTraining(@PathVariable Long id, @ModelAttribute @Valid Training training, BindingResult result, Model model, @AuthenticationPrincipal OAuth2User principal, RedirectAttributes redirectAttributes) {
        Optional<Team> teamOptional = getAuthenticatedUserTeam(principal);

        if (teamOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuário ou time não encontrado.");
            return "redirect:/training";
        }

        if (result.hasErrors()) {
            model.addAttribute("team", teamOptional.get());
            return "training-edit";
        }

        try {
            training.setId(id);
            training.setTeam(teamOptional.get());
            trainingService.saveTraining(training);
            redirectAttributes.addFlashAttribute("successMessage", "Treino atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar o treino.");
            return "redirect:/training/" + id;
        }

        return "redirect:/training";
    }

    @DeleteMapping("/{id}")
    public String deleteTraining(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            trainingService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Treino excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir o treino.");
        }
        return "redirect:/training";
    }
}