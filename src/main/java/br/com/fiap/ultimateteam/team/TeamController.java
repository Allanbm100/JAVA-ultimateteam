package br.com.fiap.ultimateteam.team;

import br.com.fiap.ultimateteam.user.User;
import br.com.fiap.ultimateteam.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TeamController {

    private final TeamService teamService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/team/profile";
    }

    @GetMapping("/team/new")
    public String showCreateForm(Model model) {
        model.addAttribute("team", new Team());
        return "team-new";
    }

    @PostMapping("/team")
    public String createTeam(@Valid @ModelAttribute("team") Team team, BindingResult result, @AuthenticationPrincipal OAuth2User principal, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "team-new";
        }

        try {
            Optional<User> userOptional = userRepository.findByEmail(principal.getAttribute("email"));
            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Usuário não encontrado.");
                return "redirect:/login?error=true";
            }
            team.setUser(userOptional.get());
            teamService.saveTeam(team);
            redirectAttributes.addFlashAttribute("successMessage", "Time criado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar o time.");
            log.error("Error creating team: {}", e.getMessage());
        }

        return "redirect:/team/profile";
    }

    @GetMapping("/team/profile")
    public String teamProfile(Model model, @AuthenticationPrincipal OAuth2User principal) {
        Optional<User> userOptional = userRepository.findByEmail(principal.getAttribute("email"));
        if (userOptional.isEmpty()) return "redirect:/login";

        User user = userOptional.get();
        if (user.getTeam() == null) return "redirect:/team/new";

        teamService.findTeamById(user.getTeam().getId()).ifPresent(team -> model.addAttribute("team", team));
        return "team-profile";
    }

    @GetMapping("/team/{id}")
    public String teamEditForm(@PathVariable Long id, Model model) {
        teamService.findTeamById(id).ifPresent(team -> model.addAttribute("team", team));
        return "team-edit";
    }

    @PutMapping("/team/{id}")
    public String teamEditSave(
            @PathVariable Long id,
            @ModelAttribute("formTeam") @Valid Team teamFromForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal OAuth2User principal) {

        if (result.hasErrors()) {
            teamFromForm.setId(id);
            model.addAttribute("team", teamFromForm);
            return "team-edit";
        }

        try {
            Team originalTeam = teamService.findTeamById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Time inválido: " + id));

            User currentUser = userRepository.findByEmail(principal.getAttribute("email"))
                    .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado no banco de dados."));
            originalTeam.setUser(currentUser);

            originalTeam.setName(teamFromForm.getName());
            originalTeam.setLogoUrl(teamFromForm.getLogoUrl());
            originalTeam.setCategory(teamFromForm.getCategory());
            originalTeam.setCreationDate(teamFromForm.getCreationDate());
            originalTeam.setTrainingDay(teamFromForm.getTrainingDay());
            originalTeam.setTrainingTime(teamFromForm.getTrainingTime());
            originalTeam.setTrainingLocation(teamFromForm.getTrainingLocation());

            teamService.saveTeam(originalTeam);
            redirectAttributes.addFlashAttribute("successMessage", "Time atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar o time.");
            log.error("Error updating team: {}", e.getMessage());
        }

        return "redirect:/team/profile";
    }
}