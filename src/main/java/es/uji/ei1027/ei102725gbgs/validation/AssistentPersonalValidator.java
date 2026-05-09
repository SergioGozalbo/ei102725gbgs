package es.uji.ei1027.ei102725gbgs.validation;

import es.uji.ei1027.ei102725gbgs.model.AssistentPersonal;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AssistentPersonalValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return AssistentPersonal.class.equals(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        AssistentPersonal assistentPersonal = (AssistentPersonal) obj;

        if (assistentPersonal.getNombre() == null || assistentPersonal.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "required", "El nombre es obligatorio"
            );
        }

        if (assistentPersonal.getTelefono()== null || assistentPersonal.getTelefono().trim().isEmpty()) {
            errors.rejectValue("telefono", "required", "El telefono es obligatorio");
        } else if (!assistentPersonal.getTelefono().matches("\\d{9}")) {
            errors.rejectValue(
                    "telefono",
                    "format",
                    "El telefono ha de tenir 9 digits"
            );
        }

        if (assistentPersonal.getEmail()== null || assistentPersonal.getEmail().trim().isEmpty()) {
            errors.rejectValue("email", "required", "El correo es obligatorio");
        } else if (!assistentPersonal.getEmail()
                .matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        }

        if (assistentPersonal.getFormacionAcademica()== null || assistentPersonal.getFormacionAcademica().trim().isEmpty()) {
            errors.rejectValue("formacionAcademica", "required", "La formacion es obligatoria");
        }

        if (assistentPersonal.getExperiencia()== null || assistentPersonal.getExperiencia().trim().isEmpty()) {
            errors.rejectValue("experiencia", "required", "La experiencia es obligatoria");
        }
    }
}
