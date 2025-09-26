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

    @Transactional
    public void deleteById(Long id) {

        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Jogador não encontrado com ID: " + id));

        for (Team team : player.getTeams()) {
            team.getPlayers().remove(player);
            teamService.saveTeam(team);
        }

        player.getTeams().clear();
        playerRepository.save(player);

        playerRepository.delete(player);
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

        return savedPlayer;
    }

    @Transactional
    public Player updatePlayerFromDTO(PlayerRequestDTO dto) {

        Player existing = playerRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Jogador não encontrado com ID: " + dto.getId()));

        Team team = teamService.findTeamById(dto.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Time não encontrado com ID: " + dto.getTeamId()));

        existing.setName(dto.getName());
        existing.setUniformNumber(dto.getUniformNumber());
        existing.setBirthDate(dto.getBirthDate());
        existing.setOffensiveFunction(dto.getOffensiveFunction());
        existing.setZoneFunction(dto.getZoneFunction());
        existing.setGender(dto.getGender());
        existing.setNicknames(dto.getNicknames());

        existing.getTeams().clear();
        existing.getTeams().add(team);

        Player savedPlayer = playerRepository.save(existing);

        team.getPlayers().add(savedPlayer);

        return savedPlayer;
    }

}