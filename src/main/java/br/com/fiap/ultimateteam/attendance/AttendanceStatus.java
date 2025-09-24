package br.com.fiap.ultimateteam.attendance;

public enum AttendanceStatus {
    PRESENTE("Presente"),
    AUSENTE("Ausente"),
    NAO_ERA("Não Era"),
    INDEFINIDO("Indefinido");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}