package br.com.fiap.ultimateteam.attendance;

import br.com.fiap.ultimateteam.player.Player;
import br.com.fiap.ultimateteam.training.Training;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    private LocalDateTime registrationDate;

    @Column(columnDefinition = "TEXT")
    private String justification;
}