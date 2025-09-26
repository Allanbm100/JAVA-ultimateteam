package br.com.fiap.ultimateteam.training;

import br.com.fiap.ultimateteam.team.Team;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public List<Training> findAll() {
        return trainingRepository.findAll();
    }


    public List<Training> findTrainingsByTeam(Team team) {
        return trainingRepository.findByTeam(team);
    }

    public Optional<Training> findTrainingById(Long id) {
        return trainingRepository.findById(id);
    }

    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    public void deleteById(Long id) {
        trainingRepository.deleteById(id);
    }
}
