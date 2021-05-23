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

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import guru.springframework.sfgpetclinic.services.springdatajpa.VisitSDJpaService;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService visitService;

    private static final Long VISIT_ID = 1L;
    private static final Visit RETURN_VISIT = Visit.builder().id(VISIT_ID).build();

    @Test
    void findAll() {
        Set<Visit> returnVisitsSet = new HashSet<>((List<Visit>) Arrays.asList(RETURN_VISIT));

        when(visitRepository.findAll()).thenReturn(returnVisitsSet);
        Set<Visit> visits = visitService.findAll();

        assertNotNull(visits);
        assertEquals(1, visits.size());
    }

    @Test
    void findById() {
        when(visitRepository.findById(anyLong())).thenReturn(Optional.of(RETURN_VISIT));
        Visit visit = visitService.findById(VISIT_ID);

        assertEquals(VISIT_ID, visit.getId());
    }

    @Test
    void findByIdNotFound() {
        when(visitRepository.findById(anyLong())).thenReturn(Optional.empty());
        Visit visit = visitService.findById(VISIT_ID);

        assertNull(visit);
    }

    @Test
    void saveWithId() {
        when(visitRepository.save(any(Visit.class))).thenReturn(RETURN_VISIT);
        Visit savedVisit = visitService.save(RETURN_VISIT);

        assertNotNull(savedVisit);
        assertEquals(VISIT_ID, savedVisit.getId());
        verify(visitRepository).save(any(Visit.class));
    }

    @Test
    void saveWithoutId() {
        when(visitRepository.save(any(Visit.class))).thenReturn(RETURN_VISIT);
        Visit savedVisit = visitService.save(Visit.builder().build());

        assertNotNull(savedVisit);
        assertEquals(VISIT_ID, savedVisit.getId());
        verify(visitRepository).save(any(Visit.class));
    }

    @Test
    void deleteById() {
        visitService.deleteById(VISIT_ID);
        when(visitRepository.findAll()).thenReturn(new HashSet<Visit>());

        assertEquals(0, visitService.findAll().size());
        verify(visitRepository).deleteById(anyLong());
    }

    @Test
    void delete() {
        visitService.delete(RETURN_VISIT);
        when(visitRepository.findAll()).thenReturn(new HashSet<Visit>());

        assertEquals(0, visitService.findAll().size());
        verify(visitRepository).delete(any(Visit.class));
    }

}
