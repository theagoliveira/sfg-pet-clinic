package guru.springframework.petclinicweb.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import guru.springframework.sfgpetclinic.controllers.VisitController;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

    private static final Long VISIT_ID = 1L;
    private static final LocalDate VISIT_DATE = LocalDate.now();
    private static final String VISIT_DESCRIPTION = "descritpion";
    private static final Visit VISIT = Visit.builder()
                                            .id(VISIT_ID)
                                            .date(VISIT_DATE)
                                            .description(VISIT_DESCRIPTION)
                                            .build();

    @Mock
    VisitService visitService;

    @Mock
    PetService petService;

    @InjectMocks
    VisitController visitController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();
    }

    @Test
    void newVisit() throws Exception {
        when(petService.findById(anyLong())).thenReturn(Pet.builder().build());

        mockMvc.perform(get("/owners/1/pets/1/visits/new"))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/pets/visits/form"))
               .andExpect(model().attributeExists("visit"))
               .andExpect(model().attributeExists("pet"));

        verify(petService).findById(anyLong());
    }

    @Test
    void createVisit() throws Exception {
        when(petService.findById(anyLong())).thenReturn(Pet.builder().build());
        when(visitService.save(any(Visit.class))).thenReturn(VISIT);

        mockMvc.perform(
            post("/owners/1/pets/1/visits").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                           .param("date", "2021-05-24")
                                           .param("description", VISIT_DESCRIPTION)
        )
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/owners/1"))
               .andExpect(model().attributeExists("visit"))
               .andExpect(model().attributeExists("pet"));

        verify(petService).findById(anyLong());
        verify(visitService).save(any(Visit.class));
    }

    @Test
    void createVisitWithError() throws Exception {
        when(petService.findById(anyLong())).thenReturn(Pet.builder().build());

        mockMvc.perform(
            post("/owners/1/pets/1/visits").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                           .param("date", "123456789")
                                           .param("description", VISIT_DESCRIPTION)
        )
               .andExpect(status().isOk())
               .andExpect(view().name("owners/pets/visits/form"))
               .andExpect(model().attributeExists("visit"))
               .andExpect(model().attributeExists("pet"));

        verify(petService).findById(anyLong());
        verify(visitService, never()).save(any(Visit.class));
    }

}
