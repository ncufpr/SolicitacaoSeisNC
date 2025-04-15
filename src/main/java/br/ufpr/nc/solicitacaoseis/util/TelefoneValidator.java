package br.ufpr.nc.solicitacaoseis.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<ValidTelefone, String> {

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null) {
            return false;
        }

        // Remove caracteres não numéricos (ex: traços, espaços)
        String telefoneLimpo = telefone.replaceAll("\\D", "");

        // Validação: deve ter 8 ou 9 dígitos
        return telefoneLimpo.matches("\\d{8,9}");
    }
}
