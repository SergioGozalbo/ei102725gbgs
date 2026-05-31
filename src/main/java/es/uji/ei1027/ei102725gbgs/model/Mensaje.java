package es.uji.ei1027.ei102725gbgs.model;

import java.time.LocalDateTime;

/** Represents a chat message between an OVI user and an assistant. */
public class Mensaje {

    /**
     * Unique identifier for the message.
     */
    private int idMensaje;

    /**
     * Identifier of the related solicitud.
     */
    private int idSolicitud;

    /**
     * 'OVI' or 'ASISTENT'.
     */
    private String remitenteType;

    /**
     * Identifier of the sender.
     */
    private String remitenteId;

    /**
     * Message content.
     */
    private String contenido;

    /**
     * Timestamp.
     */
    private LocalDateTime fechaEnvio;

    /**
     * Default constructor.
     */
    public Mensaje() {}

    /**
     * Returns the message id.
     * @return the message id
     */
    public int getIdMensaje() {
        return idMensaje;
    }

    /**
     * Sets the message id.
     * @param idMensaje the message id
     */
    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    /**
     * Returns the related solicitud id.
     * @return the related solicitud id
     */
    public int getIdSolicitud() {
        return idSolicitud;
    }

    /**
     * Sets the related solicitud id.
     * @param idSolicitud the related solicitud id
     */
    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    /**
     * Returns the sender type ('OVI' or 'ASISTENT').
     * @return the sender type
     */
    public String getRemitenteType() {
        return remitenteType;
    }

    /**
     * Sets the sender type ('OVI' or 'ASISTENT').
     * @param remitenteType the sender type
     */
    public void setRemitenteType(String remitenteType) {
        this.remitenteType = remitenteType;
    }

    /**
     * Returns the sender id.
     * @return the sender id
     */
    public String getRemitenteId() {
        return remitenteId;
    }

    /**
     * Sets the sender id.
     * @param remitenteId the sender id
     */
    public void setRemitenteId(String remitenteId) {
        this.remitenteId = remitenteId;
    }

    /**
     * Returns the message content.
     * @return the message content
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Sets the message content.
     * @param contenido the message content
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Returns the message timestamp.
     * @return the message timestamp
     */
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    /**
     * Sets the message timestamp.
     * @param fechaEnvio the message timestamp
     */
    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}