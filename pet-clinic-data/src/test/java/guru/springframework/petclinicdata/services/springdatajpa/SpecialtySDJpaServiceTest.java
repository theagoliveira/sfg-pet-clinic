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

import guru.springframework.sfgpetclinic.model.Specialty;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import guru.springframework.sfgpetclinic.services.springdatajpa.SpecialtySDJpaService;

@ExtendWith(MockitoExtension.class)
class SpecialtySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialtySDJpaService specialtyService;

    private static final Long specialtyId = 1L;
    private static final Specialty returnSpecialty = Specialty.builder().id(specialtyId).build();

    @Test
    void findAll() {
        Set<Specialty> returnSpecialtysSet = new HashSet<>(
            (List<Specialty>) Arrays.asList(returnSpecialty)
        );

        when(specialtyRepository.findAll()).thenReturn(returnSpecialtysSet);
        Set<Specialty> specialtys = specialtyService.findAll();

        assertNotNull(specialtys);
        assertEquals(1, specialtys.size());
    }

    @Test
    void findById() {
        when(specialtyRepository.findById(anyLong())).thenReturn(Optional.of(returnSpecialty));
        Specialty specialty = specialtyService.findById(specialtyId);

        assertEquals(specialtyId, specialty.getId());
    }

    @Test
    void findByIdNotFound() {
        when(specialtyRepository.findById(anyLong())).thenReturn(Optional.empty());
        Specialty specialty = specialtyService.findById(specialtyId);

        assertNull(specialty);
    }

    @Test
    void saveWithId() {
        when(specialtyRepository.save(any(Specialty.class))).thenReturn(returnSpecialty);
        Specialty savedSpecialty = specialtyService.save(returnSpecialty);

        assertNotNull(savedSpecialty);
        assertEquals(specialtyId, savedSpecialty.getId());
        verify(specialtyRepository).save(any(Specialty.class));
    }

    @Test
    void saveWithoutId() {
        when(specialtyRepository.save(any(Specialty.class))).thenReturn(returnSpecialty);
        Specialty savedSpecialty = specialtyService.save(Specialty.builder().build());

        assertNotNull(savedSpecialty);
        assertEquals(specialtyId, savedSpecialty.getId());
        verify(specialtyRepository).save(any(Specialty.class));
    }

    @Test
    void deleteById() {
        specialtyService.deleteById(specialtyId);
        when(specialtyRepository.findAll()).thenReturn(new HashSet<Specialty>());

        assertEquals(0, specialtyService.findAll().size());
        verify(specialtyRepository).deleteById(anyLong());
    }

    @Test
    void delete() {
        specialtyService.delete(returnSpecialty);
        when(specialtyRepository.findAll()).thenReturn(new HashSet<Specialty>());

        assertEquals(0, specialtyService.findAll().size());
        verify(specialtyRepository).delete(any(Specialty.class));
    }

}
