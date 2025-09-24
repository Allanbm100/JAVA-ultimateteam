package br.com.fiap.ultimateteam.player;

import br.com.fiap.ultimateteam.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeams(Team team);
}
