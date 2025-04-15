package br.ufpr.nc.solicitacaoseis.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CPFValidator implements ConstraintValidator<CPF, Object> {

    @Override
    public void initialize(CPF cpf) {
        // Inicialização, se necessário
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // ou false, dependendo da necessidade
        }

        String cpf;

        if (value instanceof Long) {
            cpf = String.format("%011d", (Long) value); // Converte Long para String com 11 dígitos
        } else if (value instanceof String) {
            cpf = ((String) value).replaceAll("[^0-9]", ""); // Remove caracteres não numéricos
        } else {
            return false; // Se não for String nem Long, considera inválido
        }

        if (cpf.length() != 11) {
            return false; // CPF deve ter exatamente 11 números
        }

        return isValidCPF(cpf);
    }

    private boolean isValidCPF(String cpf) {
        // Verifica se o CPF é uma sequência inválida (ex: 00000000000, 11111111111, etc.)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Algoritmo de validação do CPF
        int soma = 0;
        int peso = 10;

        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * peso--;
        }

        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) primeiroDigito = 0;

        soma = 0;
        peso = 11;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * peso--;
        }

        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) segundoDigito = 0;

        return cpf.charAt(9) - '0' == primeiroDigito && cpf.charAt(10) - '0' == segundoDigito;
    }
}
