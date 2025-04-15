package br.ufpr.nc.solicitacaoseis.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SecureStringValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureString {
    String message() default "Entrada inválida detectada, verifique os caracteres";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
