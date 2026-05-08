package es.uji.ei1027.ei102725gbgs.model;

/**
 * Represents an assistance request in the OVI system.
 */
public class APRequest {
    /** Unique request identifier. */
    private int idSolicitud;

    /** Identifier of the OVI user that created the request. */
    private String idUsuarioOvi;

    /** Current status of the request. */
    private String estado;

    /** Requested assistance type (for example, PAP or PATI). */
    private String tipoAsistencia;

    /** User preferences associated with the request. */
    private String preferencias;

    /** Proximity or location preference. */
    private String proximidad;

    /**
     * Builds an empty assistance request.
     */
    public APRequest() {
    }

    /**
     * Returns the request identifier.
     *
     * @return request identifier
     */
    public int getIdSolicitud() {
        return idSolicitud;
    }

    /**
     * Sets the request identifier.
     *
     * @param idSolicitud request identifier to assign
     */
    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    /**
     * Returns the OVI user identifier.
     *
     * @return OVI user identifier
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
     * Returns the request status.
     *
     * @return request status
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Sets the request status.
     *
     * @param estado request status to assign
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Returns the assistance type.
     *
     * @return assistance type
     */
    public String getTipoAsistencia() {
        return tipoAsistencia;
    }

    /**
     * Sets the assistance type.
     *
     * @param tipoAsistencia assistance type to assign
     */
    public void setTipoAsistencia(String tipoAsistencia) {
        this.tipoAsistencia = tipoAsistencia;
    }

    /**
     * Returns the request preferences.
     *
     * @return request preferences
     */
    public String getPreferencias() {
        return preferencias;
    }

    /**
     * Sets the request preferences.
     *
     * @param preferencias preferences to assign
     */
    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

    /**
     * Returns the proximity preference.
     *
     * @return proximity preference
     */
    public String getProximidad() {
        return proximidad;
    }

    /**
     * Sets the proximity preference.
     *
     * @param proximidad proximity preference to assign
     */
    public void setProximidad(String proximidad) {
        this.proximidad = proximidad;
    }

    /**
     * Builds a text representation of the request.
     *
     * @return request description string
     */
@Override
public String toString() {
    return ("APRequest {%n"
            + "idSolicitud=%d,%n"
            + "idUsuarioOvi='%s',%n"
            + "estado='%s',%n"
            + "tipoAsistencia='%s'%n"
            + "}").formatted(idSolicitud, idUsuarioOvi, estado, tipoAsistencia);
    }
}
