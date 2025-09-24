package br.com.fiap.ultimateteam.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceUpdateDTO {

    @NotNull
    private Long playerId;

    @NotNull
    private Long trainingId;

    @NotNull
    private AttendanceStatus newStatus;

    private String justification;
}