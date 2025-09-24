package br.com.fiap.ultimateteam.attendance;

import lombok.Data;

@Data
public class AttendanceResponseDTO {

    private Long attendanceId;
    private Long playerId;
    private Long trainingId;
    private AttendanceStatus status;

    public AttendanceResponseDTO(Attendance attendance) {
        this.attendanceId = attendance.getId();
        this.playerId = attendance.getPlayer().getId();
        this.trainingId = attendance.getTraining().getId();
        this.status = attendance.getStatus();
    }
}