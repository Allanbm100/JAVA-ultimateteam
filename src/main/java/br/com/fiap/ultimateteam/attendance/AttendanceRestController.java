package br.com.fiap.ultimateteam.attendance;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Slf4j
public class AttendanceRestController {

    private final AttendanceService attendanceService;

    @PutMapping
    public ResponseEntity<?> updateAttendanceStatus(@RequestBody @Valid AttendanceUpdateDTO dto) {
        try {
            Attendance updatedAttendance = attendanceService.saveOrUpdateAttendance(
                    dto.getPlayerId(),
                    dto.getTrainingId(),
                    dto.getNewStatus()
            );
            AttendanceResponseDTO responseDto = new AttendanceResponseDTO(updatedAttendance);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            log.error("Error updating attendance: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}