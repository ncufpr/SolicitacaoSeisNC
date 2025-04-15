package br.ufpr.nc.solicitacaoseis.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.util.HtmlUtils;

public class SecureStringValidator implements ConstraintValidator<SecureString, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Permite valores nulos ou vazios (caso precise bloquear, retorne false)
        }

        // Remove scripts, tags HTML e tenta prevenir SQL Injection
        String sanitizedValue = sanitizeInput(value);

        // Se a string sanitizada for diferente da original, significa que havia algo suspeito
        return sanitizedValue.equals(value);
    }

    private String sanitizeInput(String input) {
        // Remove comandos de SQL Injection, mas permite caracteres especiais usuais
        String sanitized = input.replaceAll("(?i)(--|\\b(select|insert|update|delete|drop|union|alter|exec|create|grant|revoke|truncate)\\b)", "");

        // Remove apenas `< >` para evitar injeção de scripts
        sanitized = sanitized.replaceAll("[<>]", "");

        return sanitized;
    }
}