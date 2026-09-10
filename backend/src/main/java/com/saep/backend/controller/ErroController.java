package com.saep.backend.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErroController {

    private static final Logger logger = LoggerFactory.getLogger(ErroController.class);

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> tratarBanco(DataAccessException erro) {
        logger.error("Falha ao acessar o banco de dados", erro);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message",
                "Não foi possível acessar os dados. Tente novamente ou contate o administrador."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> tratarFormato(HttpMessageNotReadableException erro) {
        return ResponseEntity.badRequest()
            .body(Map.of("message", "Confira o formato dos dados, da data e da hora."));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> tratarStatus(ResponseStatusException erro) {
        String mensagem = erro.getReason();
        if (mensagem == null) {
            mensagem = "Não foi possível concluir a operação.";
        }

        return ResponseEntity.status(erro.getStatusCode())
            .body(Map.of("message", mensagem));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(MethodArgumentNotValidException erro) {
        return ResponseEntity.badRequest()
            .body(Map.of("message", "Confira os campos obrigatórios e o formato do e-mail."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> tratarIntegridade(DataIntegrityViolationException erro) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("message",
                "Operação bloqueada: documento ou horário duplicado, ou registro vinculado a agendamentos."));
    }
}
