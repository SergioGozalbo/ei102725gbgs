package es.uji.ei1027.ei102725gbgs.validation;

import es.uji.ei1027.ei102725gbgs.model.Formador;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

/**
 * Validator class for validating Formador entities.
 */
public class FormadorValidator implements Validator {

    /**
     * Checks if the given class is supported by this validator.
     * @param clazz the class to check for support
     * @return true if the class is Formador, false otherwise
     */
    @Override
    public boolean supports(Class<?> clazz) {

        return Formador.class.equals(clazz);
    }

    /**
     * Validates the given object.
     * @param target the object to validate
     * @param errors the errors object to store validation errors
     */
    @Override
    public void validate(Object target, Errors errors) {
        Formador f = (Formador) target;

        if (f.getNombre() == null || f.getNombre().isEmpty()) {
            errors.rejectValue("nombre", "required", "Name is required");
        }

        if (f.getNombre() != null && !f.getNombre().isEmpty()) {
            char first = f.getNombre().charAt(0);
            if (!Character.isUpperCase(first)) {
                errors.rejectValue("nombre", "invalid",
                        "Name must start with capital letter");
            }
        }

        if (f.getApellidos() == null || f.getApellidos().isEmpty()) {
            errors.rejectValue("apellidos", "required", "Surname is required");
        }

        if (f.getApellidos() != null && !f.getApellidos().isEmpty()) {
            char first = f.getApellidos().charAt(0);
            if (!Character.isUpperCase(first)) {
                errors.rejectValue("apellidos", "invalid",
                        "Surname must start with capital letter");
            }
        }

        if (f.getEspecialidad() == null || f.getEspecialidad().isEmpty()) {
            errors.rejectValue("especialidad", "required",
                    "Specialty is required");
        }
    }
}
