package guru.springframework.petclinicdata.services.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import guru.springframework.sfgpetclinic.services.springdatajpa.PetTypeSDJpaService;

@ExtendWith(MockitoExtension.class)
class PetTypeSDJpaServiceTest {

    @Mock
    PetTypeRepository pettypeRepository;

    @InjectMocks
    PetTypeSDJpaService pettypeService;

    private static final Long pettypeId = 1L;
    private static final PetType returnPetType = PetType.builder().id(pettypeId).build();

    @Test
    void findAll() {
        Set<PetType> returnPetTypesSet = new HashSet<>(
            (List<PetType>) Arrays.asList(returnPetType)
        );

        when(pettypeRepository.findAll()).thenReturn(returnPetTypesSet);
        Set<PetType> pettypes = pettypeService.findAll();

        assertNotNull(pettypes);
        assertEquals(1, pettypes.size());
    }

    @Test
    void findById() {
        when(pettypeRepository.findById(anyLong())).thenReturn(Optional.of(returnPetType));
        PetType pettype = pettypeService.findById(pettypeId);

        assertEquals(pettypeId, pettype.getId());
    }

    @Test
    void findByIdNotFound() {
        when(pettypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        PetType pettype = pettypeService.findById(pettypeId);

        assertNull(pettype);
    }

    @Test
    void saveWithId() {
        when(pettypeRepository.save(any(PetType.class))).thenReturn(returnPetType);
        PetType savedPetType = pettypeService.save(returnPetType);

        assertNotNull(savedPetType);
        assertEquals(pettypeId, savedPetType.getId());
        verify(pettypeRepository).save(any(PetType.class));
    }

    @Test
    void saveWithoutId() {
        when(pettypeRepository.save(any(PetType.class))).thenReturn(returnPetType);
        PetType savedPetType = pettypeService.save(PetType.builder().build());

        assertNotNull(savedPetType);
        assertEquals(pettypeId, savedPetType.getId());
        verify(pettypeRepository).save(any(PetType.class));
    }

    @Test
    void deleteById() {
        pettypeService.deleteById(pettypeId);
        when(pettypeRepository.findAll()).thenReturn(new HashSet<PetType>());

        assertEquals(0, pettypeService.findAll().size());
        verify(pettypeRepository).deleteById(anyLong());
    }

    @Test
    void delete() {
        pettypeService.delete(returnPetType);
        when(pettypeRepository.findAll()).thenReturn(new HashSet<PetType>());

        assertEquals(0, pettypeService.findAll().size());
        verify(pettypeRepository).delete(any(PetType.class));
    }

}
