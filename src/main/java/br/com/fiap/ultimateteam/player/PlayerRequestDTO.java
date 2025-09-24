package br.com.fiap.ultimateteam.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlayerRequestDTO {
    @NotBlank(message = "O nome é obrigatório.")
    private String name;

    @NotNull(message = "O número da camisa é obrigatório.")
    private Integer uniformNumber;

    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate birthDate;

    @NotNull(message = "A seleção do time é obrigatória.")
    private Long teamId;

    private OffensiveFunction offensiveFunction;
    private ZoneFunction zoneFunction;
    private Gender gender;
    private String nicknames;
}
