//package impl.utils;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Constraint(validatedBy = PasswordValidator.class)
//@Target({ ElementType.FIELD, ElementType.PARAMETER })
//@Retention(RetentionPolicy.RUNTIME)
//public @interface PasswordConstraint {
//    String message() default "La contraseña no cumple con los estándares de seguridad: mínimo 6 caracteres, una mayúscula, una minúscula y un número.";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
//}
