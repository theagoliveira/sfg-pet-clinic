package guru.springframework.petclinicdata.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.services.map.SpecialtyMapService;
import guru.springframework.sfgpetclinic.services.map.VetMapService;

class VetMapServiceTest {

    VetMapService vetService;

    private static final Long vetId = 1L;

    @BeforeEach
    void setUp() {
        vetService = new VetMapService(new SpecialtyMapService());
        vetService.save(Vet.builder().id(vetId).build());
    }

    @Test
    void findAll() {
        Set<Vet> vetSet = vetService.findAll();

        assertEquals(1, vetSet.size());
    }

    @Test
    void findById() {
        Vet vet = vetService.findById(vetId);

        assertEquals(vetId, vet.getId());
    }

    @Test
    void saveWithId() {
        Long newVetId = 2L;

        Vet newVet = Vet.builder().id(newVetId).build();
        Vet savedVet = vetService.save(newVet);

        assertEquals(newVetId, savedVet.getId());
    }

    @Test
    void saveWithoutId() {
        Vet savedVet = vetService.save(Vet.builder().build());

        assertNotNull(savedVet);
        assertNotNull(savedVet.getId());
    }

    @Test
    void deleteById() {
        vetService.deleteById(vetId);

        assertEquals(0, vetService.findAll().size());
    }

    @Test
    void delete() {
        vetService.delete(vetService.findById(vetId));

        assertEquals(0, vetService.findAll().size());
    }

}
