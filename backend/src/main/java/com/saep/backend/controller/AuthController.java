package com.saep.backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> dados) {
        String usuario = dados.get("usuario");
        String senha = dados.get("senha");

        if ("admin".equals(usuario) && "admin123".equals(senha)) {
            return Map.of(
                "nome", "Administrador",
                "token", "sessao-local"
            );
        }

        throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Usuário ou senha inválidos."
        );
    }
}
