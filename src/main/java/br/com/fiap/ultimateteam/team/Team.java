package br.com.fiap.ultimateteam.team;

import br.com.fiap.ultimateteam.player.Player;
import br.com.fiap.ultimateteam.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{team.name.notblank}")
    @Size(min = 3, max = 100, message = "{team.name.size}")
    private String name;

    @URL(message = "{team.logoUrl.url}")
    @NotBlank(message = "{team.logoUrl.notblank}")
    private String logoUrl;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "{team.creationDate.notnull}")
    @PastOrPresent(message = "{team.creationDate.pastOrPresent}")
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{team.trainingDay.notnull}")
    private TrainingDay trainingDay;

    @NotBlank(message = "{team.trainingTime.notblank}")
    private String trainingTime;

    @NotBlank(message = "{team.trainingLocation.notblank}")
    private String trainingLocation;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{team.category.notnull}")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "team_player",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private Set<Player> players;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}