package com.saep.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class AutenticacaoTests {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void bloqueiaApiSemSessao() throws Exception {
        mvc.perform(get("/api/clientes"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Faça login para acessar o sistema."));
        mvc.perform(get("/api/agendamentos")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/macas")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/tatuadores")).andExpect(status().isUnauthorized());
    }

    @Test
    void rejeitaSenhaIncorretaComMotivo() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content("{\"usuario\":\"admin\",\"senha\":\"errada\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Usuário ou senha inválidos."));
    }

    @Test
    void loginAutorizaELogoutInvalidaSessao() throws Exception {
        var resultado = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content("{\"usuario\":\"admin\",\"senha\":\"admin123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Administrador")).andReturn();
        var sessao = (MockHttpSession) resultado.getRequest().getSession(false);
        assertNotNull(sessao);
        mvc.perform(get("/api/clientes").session(sessao)).andExpect(status().isOk());
        mvc.perform(post("/api/auth/logout").session(sessao)).andExpect(status().isNoContent());
        assertTrue(sessao.isInvalid());
        mvc.perform(get("/api/clientes")).andExpect(status().isUnauthorized());
    }

    @Test
    void origemDoAngularPodeUsarCookie() throws Exception {
        mvc.perform(options("/api/clientes")
            .header("Origin", "http://localhost:4200")
            .header("Access-Control-Request-Method", "POST")
            .header("Access-Control-Request-Headers", "content-type"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }
}
