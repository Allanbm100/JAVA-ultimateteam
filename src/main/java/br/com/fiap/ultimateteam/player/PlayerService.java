package br.com.fiap.ultimateteam.player;

import br.com.fiap.ultimateteam.team.Team;
import br.com.fiap.ultimateteam.team.TeamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamService teamService;

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Optional<Player> findPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    @Transactional
    public Player saveNewPlayerFromDTO(PlayerRequestDTO dto) {

        Team team = teamService.findTeamById(dto.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Time não encontrado com ID: " + dto.getTeamId()));

        Player newPlayer = new Player();
        newPlayer.setName(dto.getName());
        newPlayer.setUniformNumber(dto.getUniformNumber());
        newPlayer.setBirthDate(dto.getBirthDate());
        newPlayer.setJoinDate(LocalDate.now());
        newPlayer.setOffensiveFunction(dto.getOffensiveFunction());
        newPlayer.setZoneFunction(dto.getZoneFunction());
        newPlayer.setGender(dto.getGender());
        newPlayer.setNicknames(dto.getNicknames());

        Set<Team> playerTeams = new HashSet<>();
        playerTeams.add(team);
        newPlayer.setTeams(playerTeams);

        Player savedPlayer = playerRepository.save(newPlayer);

        team.getPlayers().add(savedPlayer);
        teamService.saveTeam(team);

        return savedPlayer;
    }
}