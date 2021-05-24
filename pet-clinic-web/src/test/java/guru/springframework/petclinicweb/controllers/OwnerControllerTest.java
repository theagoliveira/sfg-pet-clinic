package guru.springframework.petclinicweb.controllers;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.sfgpetclinic.controllers.OwnerController;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService ownerService;

    @Mock
    Model model;

    @InjectMocks
    OwnerController ownerController;

    MockMvc mockMvc;

    private static final Long OWNER_ID1 = 1L;
    private static final Long OWNER_ID2 = 2L;
    private static final Owner OWNER = Owner.builder().id(OWNER_ID1).build();
    private static final List<Owner> OWNERS_LIST_SINGLE = Arrays.asList(
        Owner.builder().id(OWNER_ID1).build()
    );
    private static final List<Owner> OWNERS_LIST = Arrays.asList(
        Owner.builder().id(OWNER_ID1).build(), Owner.builder().id(OWNER_ID2).build()
    );
    private static final Set<Owner> OWNERS_SET = new HashSet<>(
        (List<Owner>) Arrays.asList(
            Owner.builder().id(OWNER_ID1).build(), Owner.builder().id(OWNER_ID2).build()
        )
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/owners/index", "/owners/index.html"})
    void index(String path) throws Exception {
        when(ownerService.findAll()).thenReturn(OWNERS_SET);

        mockMvc.perform(get(path))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/index"))
               .andExpect(model().attribute("owners", hasSize(2)));
    }

    @Test
    void showOwner() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(OWNER);

        mockMvc.perform(get("/owners/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/show"))
               .andExpect(model().attribute("owner", hasProperty("id", is(OWNER_ID1))));

        verify(ownerService).findById(anyLong());
    }

    @Test
    void initFindForm() throws Exception {
        mockMvc.perform(get("/owners/find"))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/find"))
               .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processFindFormReturnMany() throws Exception {
        when(ownerService.findAllByLastNameLikeIgnoreCase(anyString())).thenReturn(OWNERS_LIST);

        mockMvc.perform(get("/owners"))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/index"))
               .andExpect(model().attribute("owners", hasSize(2)));
    }

    @Test
    void processFindFormReturnAll() throws Exception {
        when(ownerService.findAllByLastNameLikeIgnoreCase(anyString())).thenReturn(OWNERS_LIST);

        mockMvc.perform(get("/owners").param("lastName", ""))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/index"))
               .andExpect(model().attribute("owners", hasSize(2)));
    }

    @Test
    void processFindFormReturnOne() throws Exception {
        when(ownerService.findAllByLastNameLikeIgnoreCase(anyString())).thenReturn(
            OWNERS_LIST_SINGLE
        );

        mockMvc.perform(get("/owners"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/owners/1"));
    }

    @Test
    void processFindFormReturnEmpty() throws Exception {
        when(ownerService.findAllByLastNameLikeIgnoreCase(anyString())).thenReturn(
            Collections.emptyList()
        );

        mockMvc.perform(get("/owners"))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/find"))
               .andExpect(model().hasErrors())
               .andExpect(model().attributeHasFieldErrors("owner", "lastName"))
               .andExpect(model().attributeHasFieldErrorCode("owner", "lastName", "notFound"));
    }

    @Test
    void newOwner() throws Exception {
        mockMvc.perform(get("/owners/new"))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/form"))
               .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    void createOwner() throws Exception {
        when(ownerService.save(any(Owner.class))).thenReturn(OWNER);

        mockMvc.perform(post("/owners"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/owners/1"))
               .andExpect(model().attributeExists("owner"));

        verify(ownerService).save(any(Owner.class));
    }

    @Test
    void editOwner() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(OWNER);

        mockMvc.perform(get("/owners/1/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("owners/form"))
               .andExpect(model().attributeExists("owner"));

        verify(ownerService).findById(anyLong());
    }

    @Test
    void updateOwner() throws Exception {
        when(ownerService.save(any(Owner.class))).thenReturn(OWNER);

        mockMvc.perform(post("/owners/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/owners/1"))
               .andExpect(model().attributeExists("owner"));

        verify(ownerService).save(any(Owner.class));
    }

}
