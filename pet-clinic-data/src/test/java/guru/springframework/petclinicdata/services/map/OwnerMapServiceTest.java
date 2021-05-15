package guru.springframework.petclinicdata.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.map.OwnerMapService;
import guru.springframework.sfgpetclinic.services.map.PetMapService;
import guru.springframework.sfgpetclinic.services.map.PetTypeMapService;

class OwnerMapServiceTest {

    OwnerMapService ownerService;

    private static final Long ownerId = 1L;
    private static final String ownerLastName = "Cavalcante";

    @BeforeEach
    void setUp() {
        ownerService = new OwnerMapService(new PetTypeMapService(), new PetMapService());
        ownerService.save(Owner.builder().id(ownerId).lastName(ownerLastName).build());
    }

    @Test
    void findAll() {
        Set<Owner> ownerSet = ownerService.findAll();

        assertEquals(1, ownerSet.size());
    }

    @Test
    void findById() {
        Owner owner = ownerService.findById(ownerId);

        assertEquals(ownerId, owner.getId());
    }

    @Test
    void saveWithId() {
        Long newOwnerId = 2L;

        Owner newOwner = Owner.builder().id(newOwnerId).build();
        Owner savedOwner = ownerService.save(newOwner);

        assertEquals(newOwnerId, savedOwner.getId());
    }

    @Test
    void saveWithoutId() {
        Owner savedOwner = ownerService.save(Owner.builder().build());

        assertNotNull(savedOwner);
        assertNotNull(savedOwner.getId());
    }

    @Test
    void deleteById() {
        ownerService.deleteById(ownerId);

        assertEquals(0, ownerService.findAll().size());
    }

    @Test
    void delete() {
        ownerService.delete(ownerService.findById(ownerId));

        assertEquals(0, ownerService.findAll().size());
    }

    @Test
    void findByLastName() {
        Owner owner = ownerService.findByLastName(ownerLastName);

        assertNotNull(owner);
        assertEquals(1, owner.getId());
        assertEquals(ownerLastName, owner.getLastName());
    }

    @Test
    void findByLastNameNotFound() {
        Owner owner = ownerService.findByLastName("ownerLastName");

        assertNull(owner);
    }

}
