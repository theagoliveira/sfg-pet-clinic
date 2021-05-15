package guru.springframework.petclinicdata.services.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
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

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.services.springdatajpa.PetSDJpaService;

@ExtendWith(MockitoExtension.class)
class PetSDJpaServiceTest {

    @Mock
    PetRepository petRepository;

    @InjectMocks
    PetSDJpaService petService;

    private static final Long petId = 1L;
    private static final Pet returnPet = Pet.builder().id(petId).build();

    @Test
    void findAll() {
        Set<Pet> returnPetsSet = new HashSet<>((List<Pet>) Arrays.asList(returnPet));

        when(petRepository.findAll()).thenReturn(returnPetsSet);
        Set<Pet> pets = petService.findAll();

        assertNotNull(pets);
        assertEquals(1, pets.size());
    }

    @Test
    void findById() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.of(returnPet));
        Pet pet = petService.findById(petId);

        assertEquals(petId, pet.getId());
    }

    @Test
    void findByIdNotFound() {
        when(petRepository.findById(anyLong())).thenReturn(Optional.empty());
        Pet pet = petService.findById(petId);

        assertNull(pet);
    }

    @Test
    void saveWithId() {
        when(petRepository.save(any(Pet.class))).thenReturn(returnPet);
        Pet savedPet = petService.save(returnPet);

        assertNotNull(savedPet);
        assertEquals(petId, savedPet.getId());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void saveWithoutId() {
        when(petRepository.save(any(Pet.class))).thenReturn(returnPet);
        Pet savedPet = petService.save(Pet.builder().build());

        assertNotNull(savedPet);
        assertEquals(petId, savedPet.getId());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void deleteById() {
        petService.deleteById(petId);
        when(petRepository.findAll()).thenReturn(new HashSet<Pet>());

        assertEquals(0, petService.findAll().size());
        verify(petRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void delete() {
        petService.delete(returnPet);
        when(petRepository.findAll()).thenReturn(new HashSet<Pet>());

        assertEquals(0, petService.findAll().size());
        verify(petRepository, times(1)).delete(any(Pet.class));
    }

}
