package br.com.fiap.ultimateteam.player;

import br.com.fiap.ultimateteam.team.Team;
import br.com.fiap.ultimateteam.player.Player;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class PlayerRequestDTO {

    private Long id;

    @NotBlank(message = "{player.name.notblank}")
    @Size(min = 2, max = 100, message = "{player.name.size}")
    private String name;

    private String nicknames;

    @NotNull(message = "{player.uniformNumber.notnull}")
    @Min(value = 1, message = "{player.uniformNumber.min}")
    private Integer uniformNumber;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "{player.birthDate.notnull}")
    @PastOrPresent(message = "{player.birthDate.pastOrPresent}")
    private LocalDate birthDate;

    @NotNull(message = "{player.function.notnull}")
    private OffensiveFunction offensiveFunction;

    @NotNull(message = "{player.function.notnull}")
    private ZoneFunction zoneFunction;

    @NotNull(message = "{player.gender.notnull}")
    private Gender gender;

    @NotNull(message = "{player.teamId.notnull}")
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
