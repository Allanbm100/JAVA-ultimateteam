package br.com.fiap.ultimateteam.player;

import br.com.fiap.ultimateteam.team.Team;
import br.com.fiap.ultimateteam.player.Player;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class PlayerRequestDTO {

    private Long id;
    private String name;
    private String nicknames;
    private Integer uniformNumber;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;
    private OffensiveFunction offensiveFunction;
    private ZoneFunction zoneFunction;
    private Gender gender;
    private Long teamId;

    public static PlayerRequestDTO fromEntity(Player player) {
        PlayerRequestDTO dto = new PlayerRequestDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setNicknames(player.getNicknames());
        dto.setUniformNumber(player.getUniformNumber());
        dto.setBirthDate(player.getBirthDate());
        dto.setOffensiveFunction(player.getOffensiveFunction());
        dto.setZoneFunction(player.getZoneFunction());
        dto.setGender(player.getGender());

        if (!player.getTeams().isEmpty()) {
            Team team = player.getTeams().iterator().next();
            dto.setTeamId(team.getId());
        }

        return dto;
    }
}
