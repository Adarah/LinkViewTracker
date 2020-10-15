package com.mag.UrlShortener.infrastructure.validator;

import com.mag.UrlShortener.model.redirect.RedirectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueKeyValidator implements ConstraintValidator<UniqueKey, String> {
  @Autowired RedirectRepository redirectRepository;

  KeyType keyType;

  @Override
  public void initialize(UniqueKey constraintAnnotation) {
    this.keyType = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    // note: This is technically not perfect since you might still get a DB error since
    // by the time you finish checking, another transaction might have just inserted the
    // key you just checked for availability
    if (keyType == KeyType.ALIAS) {
      return redirectRepository.isAliasAvailable(s);
    }
    return false;
  }
}
