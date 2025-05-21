package br.com.eduardo.novalumecatalogservice.infra.exception;

import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityAlreadyExistsException;
import br.com.eduardo.novalumecatalogservice.infra.exception.custom.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String ALREADY_EXISTS_MESSAGE = "Entity already exists";
    private static final String NOT_FOUND_MESSAGE = "Entity not found";
    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Invalid parameter";

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TestExceptionController testController() {
            return new TestExceptionController();
        }
    }

    @RestController
    @RequestMapping("/api/test")
    static class TestExceptionController {
        @GetMapping("/already-exists")
        public void throwEntityAlreadyExistsException() {
            throw new EntityAlreadyExistsException(ALREADY_EXISTS_MESSAGE);
        }

        @GetMapping("/not-found")
        public void throwEntityNotFoundException() {
            throw new EntityNotFoundException(NOT_FOUND_MESSAGE);
        }

        @GetMapping("/illegal-argument")
        public void throwIllegalArgumentException() {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
        }
    }

    @Test
    @DisplayName("Should handle EntityAlreadyExistsException with 409 status")
    void shouldHandleEntityAlreadyExistsExceptionWith409Status() throws Exception {
        mockMvc.perform(get("/api/test/already-exists"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.code", is(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath("$.reason", is(ALREADY_EXISTS_MESSAGE)));
    }

    @Test
    @DisplayName("Should handle EntityNotFoundException with 404 status")
    void shouldHandleEntityNotFoundExceptionWith404Status() throws Exception {
        mockMvc.perform(get("/api/test/not-found"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.reason", is(NOT_FOUND_MESSAGE)));
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException with 400 status")
    void shouldHandleIllegalArgumentExceptionWith400Status() throws Exception {
        mockMvc.perform(get("/api/test/illegal-argument"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.reason", is(ILLEGAL_ARGUMENT_MESSAGE)));
    }

    @Test
    @DisplayName("Should include current timestamp in exception response")
    void shouldIncludeCurrentTimestampInExceptionResponse() throws Exception {
        mockMvc.perform(get("/api/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }
}