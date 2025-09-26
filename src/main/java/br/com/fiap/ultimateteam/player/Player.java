package br.com.fiap.ultimateteam.player;

import br.com.fiap.ultimateteam.team.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{player.name.notblank}")
    @Size(min = 2, max = 100, message = "{player.name.size}")
    private String name;

    @NotNull(message = "{player.uniformNumber.notnull}")
    @Min(value = 0, message = "{player.uniformNumber.min}")
    private Integer uniformNumber;

    @NotNull(message = "{player.birthDate.notnull}")
    @PastOrPresent(message = "{player.birthDate.pastOrPresent}")
    private LocalDate birthDate;

    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{player.function.notnull}")
    private OffensiveFunction offensiveFunction;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{player.function.notnull}")
    private ZoneFunction zoneFunction;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{player.gender.notnull}")
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String nicknames;

    @ManyToMany(mappedBy = "players")
    private Set<Team> teams = new HashSet<>();
}
