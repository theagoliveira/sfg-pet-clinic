package guru.springframework.petclinicweb.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import guru.springframework.sfgpetclinic.controllers.IndexController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    IndexController indexController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        indexController = new IndexController();
        mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/index/", "/index.html"})
    void index(String path) throws Exception {
        mockMvc.perform(get(path)).andExpect(status().isOk()).andExpect(view().name("index"));
    }

    @Test
    void oopsHandler() throws Exception {
        mockMvc.perform(get("/oops"))
               .andExpect(status().isOk())
               .andExpect(view().name("notImplemented"));
    }

}
