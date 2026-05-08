package es.uji.ei1027.ei102725gbgs.model;

/**
 * Represents an attendance record for a training activity.
 */
public class AssistenciaFormacio {
    /** Unique attendance identifier. */
    private int idAsistencia;

    /** Identifier of the related activity. */
    private int idActividad;

    /** OVI user identifier attending the activity (nullable). */
    private String idUsuarioOvi;

    /** Assistant identifier attending the activity (nullable). */
    private String idAsistente;

    /** True when attendance has been confirmed. */
    private boolean asistenciaConfirmada;

    /**
     * Builds an empty attendance record.
     */
    public AssistenciaFormacio() {
    }

    /**
     * Returns the attendance identifier.
     *
     * @return attendance identifier
     */
    public int getIdAsistencia() {
        return idAsistencia;
    }

    /**
     * Sets the attendance identifier.
     *
     * @param idAsistencia attendance identifier to assign
     */
    public void setIdAsistencia(int idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    /**
     * Returns the activity identifier.
     *
     * @return activity identifier
     */
    public int getIdActividad() {
        return idActividad;
    }

    /**
     * Sets the activity identifier.
     *
     * @param idActividad activity identifier to assign
     */
    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    /**
     * Returns the OVI user identifier.
     *
     * @return OVI user identifier, or null
     */
    public String getIdUsuarioOvi() {
        return idUsuarioOvi;
    }

    /**
     * Sets the OVI user identifier.
     *
     * @param idUsuarioOvi OVI user identifier to assign
     */
    public void setIdUsuarioOvi(String idUsuarioOvi) {
        this.idUsuarioOvi = idUsuarioOvi;
    }

    /**
     * Returns the assistant identifier.
     *
     * @return assistant identifier, or null
     */
    public String getIdAsistente() {
        return idAsistente;
    }

    /**
     * Sets the assistant identifier.
     *
     * @param idAsistente assistant identifier to assign
     */
    public void setIdAsistente(String idAsistente) {
        this.idAsistente = idAsistente;
    }

    /**
     * Returns whether attendance is confirmed.
     *
     * @return true if attendance is confirmed
     */
    public boolean isAsistenciaConfirmada() {
        return asistenciaConfirmada;
    }

    /**
     * Sets the attendance confirmation state.
     *
     * @param asistenciaConfirmada confirmation flag to assign
     */
    public void setAsistenciaConfirmada(boolean asistenciaConfirmada) {
        this.asistenciaConfirmada = asistenciaConfirmada;
    }

    /**
     * Builds a text representation of the attendance record.
     *
     * @return attendance description string
     */
    @Override
    public String toString() {
        return """
                AssistenciaFormacio {%n\
                    idAsistencia=%d,%n\
                    idActividad=%d,%n\
                    idUsuarioOvi='%s',%n\
                    idAsistente='%s',%n\
                    asistenciaConfirmada=%s%n\
                    }""".formatted(
                idAsistencia,
                idActividad,
                idUsuarioOvi,
                idAsistente,
                asistenciaConfirmada
        );
    }
}
