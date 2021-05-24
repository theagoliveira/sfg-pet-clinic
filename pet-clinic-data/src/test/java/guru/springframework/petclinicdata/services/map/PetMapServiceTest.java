package guru.springframework.petclinicdata.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.map.PetMapService;

class PetMapServiceTest {

    PetMapService petService;

    private static final Long PET_ID1 = 1L;
    private static final Long PET_ID2 = 2L;
    private static final Long OWNER_ID1 = 3L;
    private static final Long OWNER_ID2 = 4L;
    private static final String PET_NAME1 = "name1";
    private static final String PET_NAME2 = "name2";

    @BeforeEach
    void setUp() {
        petService = new PetMapService();
        petService.save(
            Pet.builder()
               .id(PET_ID1)
               .name(PET_NAME1)
               .owner(Owner.builder().id(OWNER_ID1).build())
               .build()
        );
    }

    @Test
    void findAll() {
        Set<Pet> petSet = petService.findAll();

        assertEquals(1, petSet.size());
    }

    @Test
    void findById() {
        Pet pet = petService.findById(PET_ID1);

        assertEquals(PET_ID1, pet.getId());
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
        petService.deleteById(PET_ID1);

        assertEquals(0, petService.findAll().size());
    }

    @Test
    void delete() {
        petService.delete(petService.findById(PET_ID1));

        assertEquals(0, petService.findAll().size());
    }

    @Test
    void findByNameIgnoreCaseAndOwnerId() {
        petService.save(
            Pet.builder()
               .id(PET_ID2)
               .name(PET_NAME2)
               .owner(Owner.builder().id(OWNER_ID2).build())
               .build()
        );

        Pet pet = petService.findByNameIgnoreCaseAndOwnerId(PET_NAME2.toUpperCase(), OWNER_ID2);

        assertNotNull(pet);
        assertEquals(PET_NAME2, pet.getName());
        assertEquals(OWNER_ID2, pet.getOwner().getId());

        pet = petService.findByNameIgnoreCaseAndOwnerId(PET_NAME2.toLowerCase(), OWNER_ID2);

        assertNotNull(pet);
        assertEquals(PET_NAME2, pet.getName());
        assertEquals(OWNER_ID2, pet.getOwner().getId());
    }

    @Test
    void findByNameIgnoreCaseAndOwnerIdReturnNull() {
        Pet pet = petService.findByNameIgnoreCaseAndOwnerId(PET_NAME2, OWNER_ID2);

        assertNull(pet);
    }

}
