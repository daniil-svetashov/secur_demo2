package com.example.secur_demo.validators;

import com.example.secur_demo.model.User;
import com.example.secur_demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace
                (errors, "username", "", "This field is required");

        // Поле username должно быть длиной от 8 до 32 символов
        if (user.getUsername().length() < 8 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "", "Username must be between 8 and 32 characters");
        }

        // Поле username должно быть уникальным в системе
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "", "Username is already exists.");
        }

        // Поле password не должно быть пустым
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "", "This field is required.");
        // Поле password должно быть длиной от 8 до 32 символов
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "", "Password must be between 8 and 32 characters");
        }
        // Поле password должно совпадать с полем confirmPassword
        if (!user.getConfirmPassword().equals(user.getPassword())) {
            errors.rejectValue("password", "", "Passwords don't match!");
        }

    }
}
