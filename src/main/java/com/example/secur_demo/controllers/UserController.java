package com.example.secur_demo.controllers;

import com.example.secur_demo.model.User;
import com.example.secur_demo.services.SecurityService;
import com.example.secur_demo.services.UserService;
import com.example.secur_demo.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final SecurityService securityService;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator, SecurityService securityService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.securityService = securityService;
    }

    // Передача браузеру страницы с формой
    @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    // Обработка данных формы
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult result, Model model) {

        // Валидация
        userValidator.validate(userForm, result);

        if (result.hasErrors()) {
            return "registration";
        }

        // Запись нового пользователя в базу
        userService.save(userForm);

        // ручная аутентификация
        securityService.manualLogin(userForm.getUsername(),userForm.getConfirmPassword());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {

        if (error != null && !error.isEmpty()) {
            model.addAttribute("error", "username or password is incorrect.");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
        }

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String s = authentication.getName();

        model.addAttribute("username", s);
        return "welcome";
    }
}
