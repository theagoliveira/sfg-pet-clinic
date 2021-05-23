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
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.services.springdatajpa.OwnerSDJpaService;

@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {

    @Mock
    OwnerRepository ownerRepository;

    @InjectMocks
    OwnerSDJpaService ownerService;

    private static final Long OWNER_ID = 1L;
    private static final String OWNER_LAST_NAME = "Cavalcante";
    private static final Owner OWNER = Owner.builder()
                                            .id(OWNER_ID)
                                            .lastName(OWNER_LAST_NAME)
                                            .build();

    private static final List<Owner> OWNER_LIST = Arrays.asList(OWNER);

    @Test
    void findAll() {
        Set<Owner> returnOwnersSet = new HashSet<>((List<Owner>) Arrays.asList(OWNER));

        when(ownerRepository.findAll()).thenReturn(returnOwnersSet);
        Set<Owner> owners = ownerService.findAll();

        assertNotNull(owners);
        assertEquals(1, owners.size());
    }

    @Test
    void findById() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.of(OWNER));
        Owner owner = ownerService.findById(OWNER_ID);

        assertEquals(OWNER_ID, owner.getId());
    }

    @Test
    void findByIdNotFound() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());
        Owner owner = ownerService.findById(OWNER_ID);

        assertNull(owner);
    }

    @Test
    void saveWithId() {
        when(ownerRepository.save(any(Owner.class))).thenReturn(OWNER);
        Owner savedOwner = ownerService.save(OWNER);

        assertNotNull(savedOwner);
        assertEquals(OWNER_ID, savedOwner.getId());
        verify(ownerRepository).save(any(Owner.class));
    }

    @Test
    void saveWithoutId() {
        when(ownerRepository.save(any(Owner.class))).thenReturn(OWNER);
        Owner savedOwner = ownerService.save(Owner.builder().build());

        assertNotNull(savedOwner);
        assertEquals(OWNER_ID, savedOwner.getId());
        verify(ownerRepository).save(any(Owner.class));
    }

    @Test
    void deleteById() {
        ownerService.deleteById(OWNER_ID);
        when(ownerRepository.findAll()).thenReturn(new HashSet<Owner>());

        assertEquals(0, ownerService.findAll().size());
        verify(ownerRepository).deleteById(anyLong());
    }

    @Test
    void delete() {
        ownerService.delete(OWNER);
        when(ownerRepository.findAll()).thenReturn(new HashSet<Owner>());

        assertEquals(0, ownerService.findAll().size());
        verify(ownerRepository).delete(any(Owner.class));
    }

    @Test
    void findByLastName() {
        when(ownerRepository.findByLastName(anyString())).thenReturn(OWNER);
        Owner owner = ownerService.findByLastName(OWNER_LAST_NAME);

        assertNotNull(owner);
        assertEquals(OWNER_ID, owner.getId());
        assertEquals(OWNER_LAST_NAME, owner.getLastName());
        verify(ownerRepository).findByLastName(anyString());
    }

    @Test
    void findByLastNameNotFound() {
        when(ownerRepository.findByLastName(anyString())).thenReturn(null);
        Owner owner = ownerService.findByLastName("OWNER_LAST_NAME");

        assertNull(owner);
    }

    @Test
    void findAllByLastNameLikeIgnoreCase() {
        when(ownerRepository.findAllByLastNameLikeIgnoreCase(anyString())).thenReturn(OWNER_LIST);
        List<Owner> owners = ownerService.findAllByLastNameLikeIgnoreCase(OWNER_LAST_NAME);

        assertNotNull(owners);
        assertEquals(1, owners.size());
        assertEquals(OWNER_LAST_NAME, owners.get(0).getLastName());
        verify(ownerRepository).findAllByLastNameLikeIgnoreCase(anyString());
    }

}
