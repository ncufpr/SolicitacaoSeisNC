package br.ufpr.nc.solicitacaoseis.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TelefoneValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTelefone {
    String message() default "Número de telefone inválido. Deve ter 8 ou 9 dígitos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}