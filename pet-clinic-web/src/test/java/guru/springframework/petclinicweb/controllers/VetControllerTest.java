package guru.springframework.petclinicweb.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.sfgpetclinic.controllers.VetController;
import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.services.VetService;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    VetService vetService;

    @Mock
    Model model;

    @InjectMocks
    VetController vetController;

    MockMvc mockMvc;

    private static final Set<Vet> vets = new HashSet<>(
        (List<Vet>) Arrays.asList(Vet.builder().id(1L).build(), Vet.builder().id(2L).build())
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vetController).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/vets/", "/vets", "/vets/index", "/vets/index.html", "/vets.html"})
    void listVets(String path) throws Exception {
        when(vetService.findAll()).thenReturn(vets);

        mockMvc.perform(get(path))
               .andExpect(status().isOk())
               .andExpect(view().name("vets/index"))
               .andExpect(model().attribute("vets", hasSize(2)));
    }

}
