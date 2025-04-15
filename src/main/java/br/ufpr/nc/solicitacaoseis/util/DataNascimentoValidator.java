package br.ufpr.nc.solicitacaoseis.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataNascimentoValidator implements ConstraintValidator<ValidDataNascimento, String> {
    private static final int IDADE_MINIMA = 0;   // Não pode ser negativa
    private static final int IDADE_MAXIMA = 100; // Máximo 100 anos
    private static final DateTimeFormatter FORMATTER_INPUT = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formato vindo do input
    private static final DateTimeFormatter FORMATTER_STORAGE = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formato esperado pelo sistema

    @Override
    public boolean isValid(String dataNascimento, ConstraintValidatorContext context) {
        if (dataNascimento == null || dataNascimento.trim().isEmpty()) {
            return false; // Data não pode ser nula ou vazia
        }

        try {
            // Converte a String para LocalDate no formato correto
            LocalDate data = LocalDate.parse(dataNascimento, FORMATTER_INPUT);
            LocalDate hoje = LocalDate.now();
            int idade = Period.between(data, hoje).getYears();

            return !data.isAfter(hoje) && idade >= IDADE_MINIMA && idade <= IDADE_MAXIMA;
        } catch (DateTimeParseException e) {
            return false; // Retorna falso se a String não for uma data válida
        }
    }
}
