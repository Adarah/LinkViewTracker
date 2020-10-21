package com.mag.UrlShortener.infrastructure.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueKeyValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueKey {
  String message() default "Resource already taken";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  KeyType value();
}
