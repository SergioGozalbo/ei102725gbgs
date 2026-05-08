package es.uji.ei1027.ei102725gbgs.model;

import java.io.Serializable;

/**
 * Represents an OVI user profile.
 */
public class UsuariOVI implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Unique user identifier. */
    private String idUsuario;

    /** User first name. */
    private String nombre;

    /** User last name(s). */
    private String apellidos;

    /** User email address. */
    private String email;

    /** User password. */
    private String password;

    /** User phone number. */
    private String telefono;

    /** Indicates whether GDPR consent has been granted. */
    private boolean consentimientoRgpd;


    /**
     * Builds an empty user profile.
     */
    public UsuariOVI() {
    }

    /**
     * Returns the user identifier.
     *
     * @return user identifier
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Sets the user identifier.
     *
     * @param idUsuario user identifier to assign
     */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Returns the user first name.
     *
     * @return first name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the user first name.
     *
     * @param nombre first name to assign
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Returns the user last name(s).
     *
     * @return last name(s)
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Sets the user last name(s).
     *
     * @param apellidos last name(s) to assign
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Returns the user email address.
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user email address.
     *
     * @param email email address to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user password.
     *
     * @return password value
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user password.
     *
     * @param password password value to assign
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the user phone number.
     *
     * @return phone number
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Sets the user phone number.
     *
     * @param telefono phone number to assign
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Indicates whether GDPR consent is granted.
     *
     * @return true if GDPR consent is granted
     */
    public boolean isConsentimientoRgpd() {
        return consentimientoRgpd;
    }

    /**
     * Sets the GDPR consent flag.
     *
     * @param consentimientoRgpd GDPR consent flag to assign
     */
    public void setConsentimientoRgpd(boolean consentimientoRgpd) {
        this.consentimientoRgpd = consentimientoRgpd;
    }

    /**
     * Builds a text representation of the user profile.
     *
     * @return user profile description string
     */
    @Override
    public String toString() {
        return ("UsuariOVI {%n"
            + "idUsuario='%s',%n"
            + "nombre='%s',%n"
            + "apellidos='%s',%n"
            + "email='%s',%n"
            + "}").formatted(idUsuario, nombre, apellidos, email);
    }
}
