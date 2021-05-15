package guru.springframework.petclinicdata.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.map.PetTypeMapService;

class PetTypeMapServiceTest {

    PetTypeMapService petTypeService;

    private static final Long petTypeId = 1L;

    @BeforeEach
    void setUp() {
        petTypeService = new PetTypeMapService();
        petTypeService.save(PetType.builder().id(petTypeId).build());
    }

    @Test
    void findAll() {
        Set<PetType> petTypeSet = petTypeService.findAll();

        assertEquals(1, petTypeSet.size());
    }

    @Test
    void findById() {
        PetType petType = petTypeService.findById(petTypeId);

        assertEquals(petTypeId, petType.getId());
    }

    @Test
    void saveWithId() {
        Long newPetTypeId = 2L;

        PetType newPetType = PetType.builder().id(newPetTypeId).build();
        PetType savedPetType = petTypeService.save(newPetType);

        assertEquals(newPetTypeId, savedPetType.getId());
    }

    @Test
    void saveWithoutId() {
        PetType savedPetType = petTypeService.save(PetType.builder().build());

        assertNotNull(savedPetType);
        assertNotNull(savedPetType.getId());
    }

    @Test
    void deleteById() {
        petTypeService.deleteById(petTypeId);

        assertEquals(0, petTypeService.findAll().size());
    }

    @Test
    void delete() {
        petTypeService.delete(petTypeService.findById(petTypeId));

        assertEquals(0, petTypeService.findAll().size());
    }

}
