package es.uji.ei1027.ei102725gbgs.model;

import java.time.LocalDateTime;

/** Represents a chat message between an OVI user and an assistant. */
public class Mensaje {

    private int idMensaje;
    private int idSolicitud;
    /** 'OVI' or 'ASISTENT' */
    private String remitenteType;
    private String remitenteId;
    private String contenido;
    private LocalDateTime fechaEnvio;

    public Mensaje() {}

    public int getIdMensaje() { return idMensaje; }
    public void setIdMensaje(int idMensaje) { this.idMensaje = idMensaje; }

    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }

    public String getRemitenteType() { return remitenteType; }
    public void setRemitenteType(String remitenteType) {
        this.remitenteType = remitenteType; }

    public String getRemitenteId() { return remitenteId; }
    public void setRemitenteId(String remitenteId) {
        this.remitenteId = remitenteId; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio; }
}