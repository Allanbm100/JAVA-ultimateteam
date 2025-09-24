package br.com.fiap.ultimateteam.attendance;

import br.com.fiap.ultimateteam.player.Player;
import br.com.fiap.ultimateteam.training.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByTraining(Training training);

    List<Attendance> findByTrainingIn(List<Training> trainings);

    Optional<Attendance> findByPlayerAndTraining(Player player, Training training);
}
