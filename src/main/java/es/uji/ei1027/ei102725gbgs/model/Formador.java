package es.uji.ei1027.ei102725gbgs.model;

/**
 * Represents a trainer in the OVI system.
 */
public class Formador {
    /** Unique trainer identifier. */
    private int idFormador;

    /** Trainer first name. */
    private String nombre;

    /** Trainer last name(s). */
    private String apellidos;

    /** Trainer specialization area. */
    private String especialidad;

    /**
     * Builds an empty trainer instance.
     */
    public Formador() {
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
     * Returns the trainer first name.
     *
     * @return first name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the trainer first name.
     *
     * @param nombre first name to assign
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Returns the trainer last name(s).
     *
     * @return last name(s)
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Sets the trainer last name(s).
     *
     * @param apellidos last name(s) to assign
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Returns the trainer specialization.
     *
     * @return specialization area
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Sets the trainer specialization.
     *
     * @param especialidad specialization area to assign
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Builds a text representation of the trainer.
     *
     * @return trainer description string
     */
    @Override
    public String toString() {
        return """
                Formador {%n\
                    idFormador=%d,%n\
                    nombre='%s',%n\
                    apellidos='%s',%n\
                    especialidad='%s'%n\
                }""".formatted(idFormador, nombre, apellidos, especialidad);
    }
}
