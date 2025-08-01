package br.ufpr.nc.solicitacaoseis.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NomeValidator implements ConstraintValidator<ValidNome, String> {

    @Override
    public void initialize(ValidNome constraintAnnotation) {
        // Inicialização, se necessário
    }

    @Override
    public boolean isValid(String nome, ConstraintValidatorContext context) {
        if (nome == null || nome.trim().isEmpty()) {
            return false; // O nome não pode ser nulo ou vazio
        }

        // Regex para validar que o nome contém apenas letras, espaços e tem no máximo 100 caracteres
        return nome.matches("^[A-Za-zÀ-ÿ0-9]+(?:\\s[A-Za-zÀ-ÿ0-9]+)+$");
    }
}
