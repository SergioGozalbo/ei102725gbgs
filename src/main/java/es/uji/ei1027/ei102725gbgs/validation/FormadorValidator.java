package es.uji.ei1027.ei102725gbgs.validation;

import es.uji.ei1027.ei102725gbgs.model.Formador;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

public class FormadorValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {

        return Formador.class.equals(clazz);
    }

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
