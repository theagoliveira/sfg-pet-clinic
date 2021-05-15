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

    VetMapService vetMapService;

    private static final Long vetId = 1L;

    @BeforeEach
    void setUp() {
        vetMapService = new VetMapService(new SpecialtyMapService());
        vetMapService.save(Vet.builder().id(vetId).build());
    }

    @Test
    void findAll() {
        Set<Vet> vetSet = vetMapService.findAll();

        assertEquals(1, vetSet.size());
    }

    @Test
    void findById() {
        Vet vet = vetMapService.findById(vetId);

        assertEquals(vetId, vet.getId());
    }

    @Test
    void saveWithId() {
        Long newVetId = 2L;

        Vet newVet = Vet.builder().id(newVetId).build();
        Vet savedVet = vetMapService.save(newVet);

        assertEquals(newVetId, savedVet.getId());
    }

    @Test
    void saveWithoutId() {
        Vet savedVet = vetMapService.save(Vet.builder().build());

        assertNotNull(savedVet);
        assertNotNull(savedVet.getId());
    }

    @Test
    void deleteById() {
        vetMapService.deleteById(vetId);

        assertEquals(0, vetMapService.findAll().size());
    }

    @Test
    void delete() {
        vetMapService.delete(vetMapService.findById(vetId));

        assertEquals(0, vetMapService.findAll().size());
    }

}
