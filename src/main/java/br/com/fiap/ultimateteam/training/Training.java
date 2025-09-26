package br.com.fiap.ultimateteam.training;

import br.com.fiap.ultimateteam.team.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "training")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "{training.date.notnull}")
    @FutureOrPresent(message = "{training.date.futureOrPresent}")
    private LocalDate date;

    @NotNull(message = "{training.startTime.notnull}")
    private LocalTime startTime;

    @NotNull(message = "{training.duration.notnull}")
    @Min(value = 10, message = "{training.duration.min}")
    private Integer durationMinutes;

    @NotBlank(message = "{training.location.notblank}")
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    @NotNull
    private Team team;
}