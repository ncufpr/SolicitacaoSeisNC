package br.ufpr.nc.solicitacaoseis.util;

import org.springframework.stereotype.Component;

@Component
public class Util {
    public static String formatarCPF(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo.");
        }

        // Preencher com zeros à esquerda até ter 11 dígitos
        cpf = String.format("%011d", Long.parseLong(cpf.replaceAll("[^0-9]", "")));

        // Formatar diretamente como String
        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11));
    }

}
