package guru.springframework.petclinicdata.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.map.VisitMapService;

class VisitMapServiceTest {

    VisitMapService visitMapService;

    private static final Long visitId = 1L;
    private static final Long petId = 1L;
    private static final Long ownerId = 1L;

    @BeforeEach
    void setUp() {
        visitMapService = new VisitMapService();
        visitMapService.save(
            Visit.builder()
                 .id(visitId)
                 .pet(Pet.builder().id(petId).owner(Owner.builder().id(ownerId).build()).build())
                 .build()
        );
    }

    @Test
    void findAll() {
        Set<Visit> visitSet = visitMapService.findAll();

        assertEquals(1, visitSet.size());
    }

    @Test
    void findById() {
        Visit visit = visitMapService.findById(visitId);

        assertEquals(visitId, visit.getId());
    }

    @Test
    void saveWithId() {
        Long newVisitId = 2L;

        Visit newVisit = Visit.builder()
                              .id(newVisitId)
                              .pet(
                                  Pet.builder()
                                     .id(petId)
                                     .owner(Owner.builder().id(ownerId).build())
                                     .build()
                              )
                              .build();
        Visit savedVisit = visitMapService.save(newVisit);

        assertEquals(newVisitId, savedVisit.getId());
    }

    @Test
    void saveWithoutId() {
        Visit savedVisit = visitMapService.save(
            Visit.builder()
                 .pet(Pet.builder().id(petId).owner(Owner.builder().id(ownerId).build()).build())
                 .build()
        );

        assertNotNull(savedVisit);
        assertNotNull(savedVisit.getId());
    }

    @Test
    void deleteById() {
        visitMapService.deleteById(visitId);

        assertEquals(0, visitMapService.findAll().size());
    }

    @Test
    void delete() {
        visitMapService.delete(visitMapService.findById(visitId));

        assertEquals(0, visitMapService.findAll().size());
    }

}
