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

import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.repositories.VetRepository;
import guru.springframework.sfgpetclinic.services.springdatajpa.VetSDJpaService;

@ExtendWith(MockitoExtension.class)
class VetSDJpaServiceTest {

    @Mock
    VetRepository vetRepository;

    @InjectMocks
    VetSDJpaService vetService;

    private static final Long vetId = 1L;
    private static final Vet returnVet = Vet.builder().id(vetId).build();

    @Test
    void findAll() {
        Set<Vet> returnVetsSet = new HashSet<>((List<Vet>) Arrays.asList(returnVet));

        when(vetRepository.findAll()).thenReturn(returnVetsSet);
        Set<Vet> vets = vetService.findAll();

        assertNotNull(vets);
        assertEquals(1, vets.size());
    }

    @Test
    void findById() {
        when(vetRepository.findById(anyLong())).thenReturn(Optional.of(returnVet));
        Vet vet = vetService.findById(vetId);

        assertEquals(vetId, vet.getId());
    }

    @Test
    void findByIdNotFound() {
        when(vetRepository.findById(anyLong())).thenReturn(Optional.empty());
        Vet vet = vetService.findById(vetId);

        assertNull(vet);
    }

    @Test
    void saveWithId() {
        when(vetRepository.save(any(Vet.class))).thenReturn(returnVet);
        Vet savedVet = vetService.save(returnVet);

        assertNotNull(savedVet);
        assertEquals(vetId, savedVet.getId());
        verify(vetRepository).save(any(Vet.class));
    }

    @Test
    void saveWithoutId() {
        when(vetRepository.save(any(Vet.class))).thenReturn(returnVet);
        Vet savedVet = vetService.save(Vet.builder().build());

        assertNotNull(savedVet);
        assertEquals(vetId, savedVet.getId());
        verify(vetRepository).save(any(Vet.class));
    }

    @Test
    void deleteById() {
        vetService.deleteById(vetId);
        when(vetRepository.findAll()).thenReturn(new HashSet<Vet>());

        assertEquals(0, vetService.findAll().size());
        verify(vetRepository).deleteById(anyLong());
    }

    @Test
    void delete() {
        vetService.delete(returnVet);
        when(vetRepository.findAll()).thenReturn(new HashSet<Vet>());

        assertEquals(0, vetService.findAll().size());
        verify(vetRepository).delete(any(Vet.class));
    }

}
