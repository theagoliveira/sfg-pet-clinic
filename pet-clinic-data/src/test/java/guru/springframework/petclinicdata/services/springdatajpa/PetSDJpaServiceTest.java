package guru.springframework.petclinicdata.services.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.services.springdatajpa.PetSDJpaService;

@ExtendWith(MockitoExtension.class)
class PetSDJpaServiceTest {

    @Mock
    PetRepository petRepository;

    @InjectMocks
    PetSDJpaService petService;

    private static final Long PET_ID = 1L;
    private static final String PET_NAME = "name";
    private static final Long OWNER_ID = 1L;
    private static final Owner OWNER = Owner.builder().id(OWNER_ID).build();
    private static final Pet PET = Pet.builder().id(PET_ID).name(PET_NAME).owner(OWNER).build();

    @Test
    void findAll() {
        Set<Pet> returnPetsSet = new HashSet<>((List<Pet>) Arrays.asList(PET));

        when(petRepository.findAll()).thenReturn(returnPetsSet);
        Set<Pet> pets = petService.findAll();

        assertNotNull(pets);
        assertEquals(1, pets.size());
    }

    @Test
    void findById() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(PET));
        Pet pet = petService.findById(PET_ID);

        assertEquals(PET_ID, pet.getId());
    }

    @Test
    void findByIdNotFound() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.empty());
        Pet pet = petService.findById(PET_ID);

        assertNull(pet);
    }

    @Test
    void saveWithId() {
        when(petRepository.save(any(Pet.class))).thenReturn(PET);
        Pet savedPet = petService.save(PET);

        assertNotNull(savedPet);
        assertEquals(PET_ID, savedPet.getId());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void saveWithoutId() {
        when(petRepository.save(any(Pet.class))).thenReturn(PET);
        Pet savedPet = petService.save(Pet.builder().build());

        assertNotNull(savedPet);
        assertEquals(PET_ID, savedPet.getId());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void deleteById() {
        petService.deleteById(PET_ID);
        when(petRepository.findAll()).thenReturn(new HashSet<Pet>());

        assertEquals(0, petService.findAll().size());
        verify(petRepository).deleteById(anyLong());
    }

    @Test
    void delete() {
        petService.delete(PET);
        when(petRepository.findAll()).thenReturn(new HashSet<Pet>());

        assertEquals(0, petService.findAll().size());
        verify(petRepository).delete(any(Pet.class));
    }

    @Test
    void findByNameIgnoreCaseAndOwnerId() {
        when(petRepository.findByNameIgnoreCaseAndOwnerId(anyString(), anyLong())).thenReturn(PET);
        Pet pet = petService.findByNameIgnoreCaseAndOwnerId(PET_NAME, OWNER_ID);

        assertNotNull(pet);
        assertEquals(PET_ID, pet.getId());
        assertEquals(PET_NAME, pet.getName());
        assertEquals(OWNER_ID, pet.getOwner().getId());
    }

    @Test
    void findByNameIgnoreCaseAndOwnerIdNotFound() {
        when(petRepository.findByNameIgnoreCaseAndOwnerId(anyString(), anyLong())).thenReturn(null);
        Pet pet = petService.findByNameIgnoreCaseAndOwnerId(PET_NAME, OWNER_ID);

        assertNull(pet);
    }

}
