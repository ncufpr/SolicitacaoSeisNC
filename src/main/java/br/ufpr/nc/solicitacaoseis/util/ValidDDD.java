package br.ufpr.nc.solicitacaoseis.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DDDValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDDD {
    String message() default "DDD inválido. Deve ter 2 dígitos entre 11 e 99";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
