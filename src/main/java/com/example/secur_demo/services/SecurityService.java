package com.example.secur_demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager manager;

    @Autowired
    public SecurityService(UserDetailsService userDetailsService, AuthenticationManager manager) {
        this.userDetailsService = userDetailsService;
        this.manager = manager;
    }

    public void manualLogin(String username, String password) {

        // Мы получим объект стандарный User из UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Сгенерируем специальный токен аутентификации
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        // Обратимся к менеджеру аутентификации и аутентифицируем токен
        manager.authenticate(token);

        // Аутентифицируем пользователя с помощью токен
        if (token.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

}
