package guru.springframework.petclinicdata.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.map.PetMapService;

class PetMapServiceTest {

    PetMapService petService;

    private static final Long petId = 1L;

    @BeforeEach
    void setUp() {
        petService = new PetMapService();
        petService.save(Pet.builder().id(petId).build());
    }

    @Test
    void findAll() {
        Set<Pet> petSet = petService.findAll();

        assertEquals(1, petSet.size());
    }

    @Test
    void findById() {
        Pet pet = petService.findById(petId);

        assertEquals(petId, pet.getId());
    }

    @Test
    void saveWithId() {
        Long newPetId = 2L;

        Pet newPet = Pet.builder().id(newPetId).build();
        Pet savedPet = petService.save(newPet);

        assertEquals(newPetId, savedPet.getId());
    }

    @Test
    void saveWithoutId() {
        Pet savedPet = petService.save(Pet.builder().build());

        assertNotNull(savedPet);
        assertNotNull(savedPet.getId());
    }

    @Test
    void deleteById() {
        petService.deleteById(petId);

        assertEquals(0, petService.findAll().size());
    }

    @Test
    void delete() {
        petService.delete(petService.findById(petId));

        assertEquals(0, petService.findAll().size());
    }

    @Test
    void findByNameIgnoreCaseAndOwnerId() {
        // TODO: implement test
    }

}
