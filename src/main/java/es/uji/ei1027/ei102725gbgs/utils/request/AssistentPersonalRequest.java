package es.uji.ei1027.ei102725gbgs.utils.request;

public class AssistentPersonalRequest {

    /** Assistant first name. */
    private String nombre;

    /** Assistant last name(s). */
    private String apellidos;

    /** Assistant email address. */
    private String email;

    /** Assistant password. */
    private String password;

    /** Assistant phone number. */
    private String telefono;

    /** Academic education details. */
    private String formacionAcademica;

    /** Professional experience summary. */
    private String experiencia;

    /** Approval status of the assistant profile. */
    private String estadoAceptado;

    /**
     * Builds an empty assistant instance.
     */
    public AssistentPersonalRequest() {
    }

    /**
     * Returns the assistant first name.
     *
     * @return first name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the assistant first name.
     *
     * @param nombre first name to assign
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Returns the assistant last name(s).
     *
     * @return last name(s)
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Sets the assistant last name(s).
     *
     * @param apellidos last name(s) to assign
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Returns the assistant email.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the assistant email.
     *
     * @param email email address to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the assistant password.
     *
     * @return password value
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the assistant password.
     *
     * @param password password value to assign
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the assistant phone number.
     *
     * @return phone number
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Sets the assistant phone number.
     *
     * @param telefono phone number to assign
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Returns the academic education details.
     *
     * @return academic education details
     */
    public String getFormacionAcademica() {
        return formacionAcademica;
    }

    /**
     * Sets the academic education details.
     *
     * @param formacionAcademica academic education details to assign
     */
    public void setFormacionAcademica(String formacionAcademica) {
        this.formacionAcademica = formacionAcademica;
    }

    /**
     * Returns professional experience.
     *
     * @return professional experience summary
     */
    public String getExperiencia() {
        return experiencia;
    }

    /**
     * Sets professional experience.
     *
     * @param experiencia professional experience to assign
     */
    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    /**
     * Returns the assistant approval status.
     *
     * @return approval status
     */
    public String getEstadoAceptado() {
        return estadoAceptado;
    }

    /**
     * Sets the assistant approval status.
     *
     * @param estadoAceptado approval status to assign
     */
    public void setEstadoAceptado(String estadoAceptado) {
        this.estadoAceptado = estadoAceptado;
    }

    /**
     * Builds a text representation of the assistant.
     *
     * @return assistant description string
     */
    @Override
    public String toString() {
        return """
                AssistentPersonal {
                    nombre='%s',
                    apellidos='%s',
                    email='%s',
                    estadoAceptado='%s'
                }""".formatted(nombre, apellidos, email, estadoAceptado);
    }
}
