package es.uji.ei1027.ei102725gbgs.validation;

import es.uji.ei1027.ei102725gbgs.dao.UsuariOVIDaoImpl;
import es.uji.ei1027.ei102725gbgs.model.APRequest;
import es.uji.ei1027.ei102725gbgs.model.UsuariOVI;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class APRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return APRequest.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        APRequest apRequest = (APRequest) obj;

        //Tipo asistencia
        if (apRequest.getTipoAsistencia() == null || apRequest.getTipoAsistencia().trim().isEmpty()) {
            errors.rejectValue("tipoAsistencia", "required", "Assistance type is required");
        }

        if (apRequest.getTipoAsistencia() != null &&
                !apRequest.getTipoAsistencia().equals("PAP") &&
            !apRequest.getTipoAsistencia().equals("PATI")) {
            errors.rejectValue("tipoAsistencia", "invalid", "Tipo no valido");
        }

        //Proximidad
        if (apRequest.getProximidad()== null || apRequest.getProximidad().trim().isEmpty()) {
            errors.rejectValue("proximidad", "required", "Proximity is required");
        }

        if (apRequest.getPreferencias() == null || apRequest.getPreferencias().trim().isEmpty()) {
            errors.rejectValue("preferencias", "required", "Las preferencias son obligatorias");
        } else if (apRequest.getPreferencias().length() <5) {
            errors.rejectValue("preferencias", "invalid", "Debe tener al menos 5 caracters");
        }
    }
}
