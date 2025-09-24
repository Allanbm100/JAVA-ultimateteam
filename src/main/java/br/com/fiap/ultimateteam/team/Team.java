package br.com.fiap.ultimateteam.team;

import br.com.fiap.ultimateteam.player.Player;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String logoUrl;
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    private TrainingDay trainingDay;

    private String trainingTime;
    private String trainingLocation;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String primaryColor;
    private String secondaryColor;

    @ManyToMany
    @JoinTable(
            name = "team_player",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private Set<Player> players;
}