package com.ogulcanonder.investment_tracking_app.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String password;
    private String passwordConfirm;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.passwordConfirm = constraintAnnotation.passwordConfirm();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(password);
        Object passwordConfirmValue = new BeanWrapperImpl(value).getPropertyValue(passwordConfirm);
        return passwordValue != null && passwordValue.equals(passwordConfirmValue);
    }
}
