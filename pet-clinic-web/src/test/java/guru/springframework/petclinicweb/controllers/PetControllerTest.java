package guru.springframework.petclinicweb.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.sfgpetclinic.controllers.PetController;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    PetService petService;

    @Mock
    PetTypeService petTypeService;

    @Mock
    OwnerService ownerService;

    @Mock
    Model model;

    @InjectMocks
    PetController petController;

    MockMvc mockMvc;

    private static final Long ID1 = 1L;
    private static final Long ID2 = 2L;
    private static final String PET_NAME = "name";
    private static final Owner OWNER = Owner.builder().id(ID1).build();
    private static final Pet PET1 = Pet.builder().name(PET_NAME).build();
    private static final Pet PET2 = Pet.builder().id(ID1).name(PET_NAME).build();
    private static final Set<PetType> PET_TYPES_SET = new HashSet<>(
        (List<PetType>) Arrays.asList(
            PetType.builder().id(ID1).name("Dog").build(),
            PetType.builder().id(ID2).name("Cat").build()
        )
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
    }

    @Test
    void newPet() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(OWNER);
        when(petTypeService.findAll()).thenReturn(PET_TYPES_SET);

        mockMvc.perform(get("/owners/1/pets/new"))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/pets/form"))
               .andExpect(model().attributeExists("pet"))
               .andExpect(model().attributeExists("owner"))
               .andExpect(model().attributeExists("petTypes"));

        verifyNoInteractions(petService);
    }

    @Test
    void createPet() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(OWNER);
        when(petTypeService.findAll()).thenReturn(PET_TYPES_SET);
        when(petService.save(any(Pet.class))).thenReturn(PET2);
        when(petService.findByNameIgnoreCaseAndOwnerId(anyString(), anyLong())).thenReturn(null);

        mockMvc.perform(post("/owners/1/pets").flashAttr("pet", PET1))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/owners/1"))
               .andExpect(model().attributeExists("pet"))
               .andExpect(model().attributeExists("owner"))
               .andExpect(model().attributeExists("petTypes"))
               .andExpect(model().hasNoErrors());

        verify(petService).findByNameIgnoreCaseAndOwnerId(anyString(), anyLong());
        verify(petService).save(any(Pet.class));
    }

    @Test
    void createPetDuplicate() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(OWNER);
        when(petTypeService.findAll()).thenReturn(PET_TYPES_SET);
        when(petService.findByNameIgnoreCaseAndOwnerId(anyString(), anyLong())).thenReturn(PET2);

        mockMvc.perform(post("/owners/1/pets").flashAttr("pet", PET1))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/pets/form"))
               .andExpect(model().attributeExists("pet"))
               .andExpect(model().attributeExists("owner"))
               .andExpect(model().attributeExists("petTypes"))
               .andExpect(model().hasErrors())
               .andExpect(model().attributeHasFieldErrors("pet", "name"))
               .andExpect(model().attributeHasFieldErrorCode("pet", "name", "duplicate"));

        verify(petService).findByNameIgnoreCaseAndOwnerId(anyString(), anyLong());
        verify(petService, never()).save(any(Pet.class));
    }

    @Test
    void editPet() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(OWNER);
        when(petTypeService.findAll()).thenReturn(PET_TYPES_SET);
        when(petService.findById(anyLong())).thenReturn(PET2);

        mockMvc.perform(get("/owners/1/pets/1/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/pets/form"))
               .andExpect(model().attributeExists("pet"))
               .andExpect(model().attributeExists("owner"))
               .andExpect(model().attributeExists("petTypes"));

        verify(petService).findById(anyLong());
    }

    @Test
    void updatePet() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(OWNER);
        when(petTypeService.findAll()).thenReturn(PET_TYPES_SET);
        when(petService.save(any(Pet.class))).thenReturn(PET1);

        mockMvc.perform(post("/owners/1/pets/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/owners/1"))
               .andExpect(model().attributeExists("pet"))
               .andExpect(model().attributeExists("owner"))
               .andExpect(model().attributeExists("petTypes"));

        verify(petService).save(any(Pet.class));
    }

}
