package br.com.fiap.ultimateteam.training;

import br.com.fiap.ultimateteam.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training,Long> {
    List<Training> findByTeam(Team team);
}
