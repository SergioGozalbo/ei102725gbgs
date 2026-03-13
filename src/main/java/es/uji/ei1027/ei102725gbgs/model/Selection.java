package es.uji.ei1027.ei102725gbgs.model;

/**
 * Represents an assistant selection linked to a request.
 */
public class Selection {
    /** Unique selection identifier. */
    private int idSeleccion;

    /** Identifier of the associated request. */
    private int idSolicitud;

    /** Identifier of the selected assistant. */
    private String idAsistente;

    /**
     * Builds an empty selection instance.
     */
    public Selection() {
    }

    /**
     * Returns the selection identifier.
     *
     * @return selection identifier
     */
    public int getIdSeleccion() {
        return idSeleccion;
    }

    /**
     * Sets the selection identifier.
     *
     * @param idSeleccion selection identifier to assign
     */
    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
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
     * Returns the selected assistant identifier.
     *
     * @return selected assistant identifier
     */
    public String getIdAsistente() {
        return idAsistente;
    }

    /**
     * Sets the selected assistant identifier.
     *
     * @param idAsistente selected assistant identifier to assign
     */
    public void setIdAsistente(String idAsistente) {
        this.idAsistente = idAsistente;
    }

    /**
     * Builds a text representation of the selection.
     *
     * @return selection description string
     */
    @Override
    public String toString() {
        return """
                Selection {
                idSeleccion=%d,
                idSolicitud=%d,
                idAsistente='%s'
                }""".formatted(idSeleccion, idSolicitud, idAsistente);
    }
}
