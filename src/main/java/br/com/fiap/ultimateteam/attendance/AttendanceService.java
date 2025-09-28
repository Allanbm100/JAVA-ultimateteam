package br.com.fiap.ultimateteam.attendance;

import br.com.fiap.ultimateteam.player.Player;
import br.com.fiap.ultimateteam.player.PlayerRepository;
import br.com.fiap.ultimateteam.player.PlayerService;
import br.com.fiap.ultimateteam.team.Team;
import br.com.fiap.ultimateteam.training.Training;
import br.com.fiap.ultimateteam.training.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final PlayerRepository playerRepository;
    private final TrainingService trainingService;

    public Map<String, Object> getAttendanceGridData(Team team, int numberOfTrainings) {

        List<Player> players = playerRepository.findByTeams(team);

        List<Training> allTrainings = trainingService.findTrainingsByTeam(team);
        List<Training> recentTrainings = allTrainings.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .limit(numberOfTrainings)
                .toList();

        List<Attendance> attendances = attendanceRepository.findByTrainingIn(recentTrainings);

        Map<Long, Map<Long, Attendance>> attendanceMap = new HashMap<>();

        for (Player player : players) {
            attendanceMap.put(player.getId(), new HashMap<>());
        }

        for (Attendance attendance : attendances) {
            Long playerId = attendance.getPlayer().getId();

            if (attendanceMap.containsKey(playerId)) {
                attendanceMap.get(playerId)
                        .put(attendance.getTraining().getId(), attendance);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("players", players);
        data.put("trainings", recentTrainings);
        data.put("attendanceMap", attendanceMap);

        return data;
    }

    public Attendance saveOrUpdateAttendance(Long playerId, Long trainingId, AttendanceStatus newStatus) {

        Optional<Player> playerOpt = playerRepository.findById(playerId);
        Optional<Training> trainingOpt = trainingService.findTrainingById(trainingId);

        if (playerOpt.isEmpty() || trainingOpt.isEmpty()) {
            throw new IllegalArgumentException("Jogador ou Treino não encontrado.");
        }

        Player player = playerOpt.get();
        Training training = trainingOpt.get();

        Optional<Attendance> existingAttendance = attendanceRepository.findByPlayerAndTraining(player, training);

        Attendance attendance;
        if (existingAttendance.isPresent()) {
            attendance = existingAttendance.get();
            attendance.setStatus(newStatus);
        } else {
            attendance = new Attendance();
            attendance.setPlayer(player);
            attendance.setTraining(training);
            attendance.setStatus(newStatus);
        }

        attendance.setRegistrationDate(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }
}