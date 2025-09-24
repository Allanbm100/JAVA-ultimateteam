package br.com.fiap.ultimateteam.player;

import br.com.fiap.ultimateteam.team.Team;
import jakarta.persistence.*;
import lombok.Data;
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

    private String name;
    private Integer uniformNumber;
    private LocalDate birthDate;
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private OffensiveFunction offensiveFunction;

    @Enumerated(EnumType.STRING)
    private ZoneFunction zoneFunction;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String nicknames;

    @ManyToMany(mappedBy = "players")
    private Set<Team> teams = new HashSet<>();
}
