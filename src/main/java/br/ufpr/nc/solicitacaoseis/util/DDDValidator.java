package br.ufpr.nc.solicitacaoseis.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DDDValidator implements ConstraintValidator<ValidDDD, String> {

    @Override
    public boolean isValid(String ddd, ConstraintValidatorContext context) {
        if (ddd == null || !ddd.matches("\\d{2}")) {
            return false; // Deve ter exatamente 2 dígitos numéricos
        }
        int numeroDDD = Integer.parseInt(ddd);
        return numeroDDD >= 11 && numeroDDD <= 99; // DDDs válidos no Brasil
    }
}
