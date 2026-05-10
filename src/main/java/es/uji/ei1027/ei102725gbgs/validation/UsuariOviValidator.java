package es.uji.ei1027.ei102725gbgs.validation;

import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator class for validating UsuariOVI entities.
 */
public class UsuariOviValidator implements Validator {

    /**
     * Minimum password length requirement for validation.
     */
    private static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * DAO for accessing UsuariOVI data, used to validate unique email addresses.
     */
    private UsuariOVIDaoImpl usuariDao;

    /**
     * Constructor for UsuariOviValidator.
     * @param usuariDao the UsuariOVIDaoImpl instance to be used for validating unique email addresses
     */
    public UsuariOviValidator(UsuariOVIDaoImpl usuariDao) {
        this.usuariDao = usuariDao;
    }

    /**
     * Checks if the given class is supported by this validator.
     * @param cls the class to check for support
     * @return true if the class is UsuariOVI, false otherwise
     */
    @Override
    public boolean supports(Class<?> cls) {
        return UsuariOVI.class.equals(cls);
    }

    /**
     * Validates the given object.
     * @param obj the object to validate
     * @param errors the errors object to store validation errors
     */
    @Override
    public void validate(Object obj, Errors errors) {
        UsuariOVI usuari = (UsuariOVI) obj;

        if (usuari.getNombre() == null || usuari.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "required",
                    "El nombre es obligatorio");
        }

        if (usuari.getNombre() != null && !usuari.getNombre().isEmpty()) {
            char first = usuari.getNombre().charAt(0);
            if (!Character.isUpperCase(first)) {
                errors.rejectValue("nombre", "invalid",
                    "El nombre debe comenzar con mayúscula");
            }
        }

        if (usuari.getApellidos() == null || usuari.getApellidos().isEmpty()) {
            errors.rejectValue("apellidos", "required",
                    "El apellidos es obligatorio");
        }

        if (usuari.getApellidos() != null && !usuari.getApellidos().isEmpty()) {
            char first = usuari.getApellidos().charAt(0);
            if (!Character.isUpperCase(first)) {
                errors.rejectValue("apellidos", "inválido",
                    "El apellidos debe comenzar con mayúscula");
            }
        }

        if (usuari.getEmail() == null || usuari.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "required", "El email es obligatorio");
        } else if (!usuari.getEmail().contains("@")) {
                errors.rejectValue("email", "invalid",
                    "formato de email incorrecto");
        }

        if (usuariDao.getUsuariOVIByEmail(usuari.getEmail()) != null) {
            errors.rejectValue("email", "duplicate",
                    "Este email ya està registrado");
        }

        if (usuari.getPassword() == null
            || usuari.getPassword().trim().isEmpty()) {
            errors.rejectValue("password",
            "required",
            "La contraseña es obligatoria");
        } else {
            String password = usuari.getPassword();
            if (password.length() < MIN_PASSWORD_LENGTH) {
                errors.rejectValue("password",
                "invalid",
                "Minimo 8 caracters");
            }

            if (!password.matches(".*[A-Z.]*")) {
                errors.rejectValue("password",
                "invalid",
                "Debe contener una mayúscula");
            }

            if (!password.matches(".*\\d.*")) {
                errors.rejectValue("password",
                "invalid",
                "Debe contener un número");
            }

            if (!password.matches(".*[!@#$%^&*()].*")) {
                errors.rejectValue("password",
                "invalid",
                "Debe contener un carácter especial");
            }
        }

        if (usuari.getTelefono() != null
            && !usuari.getTelefono().matches("\\d+")) {
            errors.rejectValue("telefono",
            "invalid",
            "El teléfono solo debe contener números");
        }

        if (!usuari.isConsentimientoRgpd()) {
            errors.rejectValue("consentimientoRgpd",
            "required",
            "Debe aceptar el RGPD");
        }
    }
}
