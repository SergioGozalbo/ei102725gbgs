package es.uji.ei1027.ei102725gbgs.model;

import java.time.LocalDateTime;

/**
 * Represents a training or outreach activity in the OVI system.
 */
public class ActivitatFormacio {
    /** Unique activity identifier. */
    private int idActividad;

    /** Identifier of the trainer responsible for the activity. */
    private int idFormador;

    /** Activity name. */
    private String nombre;

    /** Activity description. */
    private String descripcion;

    /** Activity type (e.g., training or outreach). */
    private String tipo;

    /** Date and time when the activity takes place. */
    private LocalDateTime fecha;

    /** Maximum capacity for the activity (nullable for unlimited capacity). */
    private Integer aforoMax;

    /**
     * Builds an empty activity instance.
     */
    public ActivitatFormacio() {
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
     * Returns the trainer identifier.
     *
     * @return trainer identifier
     */
    public int getIdFormador() {
        return idFormador;
    }

    /**
     * Sets the trainer identifier.
     *
     * @param idFormador trainer identifier to assign
     */
    public void setIdFormador(int idFormador) {
        this.idFormador = idFormador;
    }

    /**
     * Returns the activity name.
     *
     * @return activity name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the activity name.
     *
     * @param nombre activity name to assign
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Returns the activity description.
     *
     * @return activity description
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the activity description.
     *
     * @param descripcion activity description to assign
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Returns the activity type.
     *
     * @return activity type
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the activity type.
     *
     * @param tipo activity type to assign
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Returns the activity date and time.
     *
     * @return date and time of the activity
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Sets the activity date and time.
     *
     * @param fecha date and time to assign
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * Returns the maximum capacity.
     *
     * @return maximum capacity, or null when not defined
     */
    public Integer getAforoMax() {
        return aforoMax;
    }

    /**
     * Sets the maximum capacity.
     *
     * @param aforoMax capacity to assign
     */
    public void setAforoMax(Integer aforoMax) {
        this.aforoMax = aforoMax;
    }

    /**
     * Builds a text representation of the activity.
     *
     * @return activity description string
     */
    @Override
    public String toString() {
        return """
                ActivitatFormacio {%n\
                idActividad=%d,%n\
                idFormador=%d,%n\
                nombre='%s',%n\
                tipo='%s',%n\
                fecha=%s%n\
                }""".formatted(idActividad, idFormador, nombre, tipo, fecha).strip();
    }
}
