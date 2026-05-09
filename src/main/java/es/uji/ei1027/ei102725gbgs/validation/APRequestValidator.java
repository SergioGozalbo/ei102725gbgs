package es.uji.ei1027.ei102725gbgs.validation;

import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.APRequest;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for APRequest objects.
 */
public class APRequestValidator implements Validator {

    /**
     * Minimum length for preferencias field.
     */
    private static final int MIN_PREFERENCIAS_LENGTH = 5;

    /**
     * Checks if the given class is supported by this validator.
      * @param cls the class to check for support
      * @return true if the class is APRequest, false otherwise
     */
    @Override
    public boolean supports(Class<?> cls) {
        return APRequest.class.equals(cls);
    }

    /**
     * Validates the given APRequest object and populates the Errors object with any validation errors.
     */
    @Override
    public void validate(Object obj, Errors errors) {
        APRequest apRequest = (APRequest) obj;

        //Tipo asistencia
        if (apRequest.getTipoAsistencia() == null || apRequest.getTipoAsistencia().trim().isEmpty()) {
            errors.rejectValue("tipoAsistencia", "required", "Assistance type is required");
        }

        if (apRequest.getTipoAsistencia() != null
            && !apRequest.getTipoAsistencia().equals("PAP")
            && !apRequest.getTipoAsistencia().equals("PATI")) {
            errors.rejectValue("tipoAsistencia",
            "invalid",
            "Tipo no valido");
        }

        //Proximidad
        if (apRequest.getProximidad() == null || apRequest.getProximidad().trim().isEmpty()) {
            errors.rejectValue("proximidad", "required", "Proximity is required");
        }

        if (apRequest.getPreferencias() == null || apRequest.getPreferencias().trim().isEmpty()) {
            errors.rejectValue("preferencias", "required", "Las preferencias son obligatorias");
        } else if (apRequest.getPreferencias().length() < MIN_PREFERENCIAS_LENGTH) {
            errors.rejectValue("preferencias", "invalid", "Debe tener al menos 5 caracters");
        }
    }
}
