package guru.springframework.petclinicdata.services.springdatajpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.services.springdatajpa.OwnerSDJpaService;

@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {

    @Mock
    OwnerRepository ownerRepository;

    @InjectMocks
    OwnerSDJpaService ownerService;

    private static final Long ownerId = 1L;
    private static final String ownerLastName = "Cavalcante";
    private static final Owner returnOwner = Owner.builder()
                                                  .id(ownerId)
                                                  .lastName(ownerLastName)
                                                  .build();

    @Test
    void findAll() {
        Set<Owner> returnOwnersSet = new HashSet<>((List<Owner>) Arrays.asList(returnOwner));

        when(ownerRepository.findAll()).thenReturn(returnOwnersSet);
        Set<Owner> owners = ownerService.findAll();

        assertNotNull(owners);
        assertEquals(1, owners.size());
    }

    @Test
    void findById() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.of(returnOwner));
        Owner owner = ownerService.findById(ownerId);

        assertEquals(ownerId, owner.getId());
    }

    @Test
    void findByIdNotFound() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());
        Owner owner = ownerService.findById(ownerId);

        assertNull(owner);
    }

    @Test
    void saveWithId() {
        when(ownerRepository.save(any(Owner.class))).thenReturn(returnOwner);
        Owner savedOwner = ownerService.save(returnOwner);

        assertNotNull(savedOwner);
        assertEquals(ownerId, savedOwner.getId());
        verify(ownerRepository).save(any(Owner.class));
    }

    @Test
    void saveWithoutId() {
        when(ownerRepository.save(any(Owner.class))).thenReturn(returnOwner);
        Owner savedOwner = ownerService.save(Owner.builder().build());

        assertNotNull(savedOwner);
        assertEquals(ownerId, savedOwner.getId());
        verify(ownerRepository).save(any(Owner.class));
    }

    @Test
    void deleteById() {
        ownerService.deleteById(ownerId);
        when(ownerRepository.findAll()).thenReturn(new HashSet<Owner>());

        assertEquals(0, ownerService.findAll().size());
        verify(ownerRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void delete() {
        ownerService.delete(returnOwner);
        when(ownerRepository.findAll()).thenReturn(new HashSet<Owner>());

        assertEquals(0, ownerService.findAll().size());
        verify(ownerRepository, times(1)).delete(any(Owner.class));
    }

    @Test
    void findByLastName() {
        when(ownerRepository.findByLastName(anyString())).thenReturn(returnOwner);
        Owner owner = ownerService.findByLastName(ownerLastName);

        assertNotNull(owner);
        assertEquals(ownerId, owner.getId());
        assertEquals(ownerLastName, owner.getLastName());
        verify(ownerRepository).findByLastName(anyString());
    }

    @Test
    void findByLastNameNotFound() {
        when(ownerRepository.findByLastName(anyString())).thenReturn(null);
        Owner owner = ownerService.findByLastName("ownerLastName");

        assertNull(owner);
    }

}
