package guru.springframework.petclinicdata.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.map.OwnerMapService;
import guru.springframework.sfgpetclinic.services.map.PetMapService;
import guru.springframework.sfgpetclinic.services.map.PetTypeMapService;

class OwnerMapServiceTest {

    OwnerMapService ownerService;

    private static final Long OWNER_ID1 = 1L;
    private static final Long OWNER_ID2 = 2L;
    private static final int BEGIN_INDEX = 2;
    private static final int END_INDEX = 8;
    private static final String OWNER_LAST_NAME1 = "Cavalcante";
    private static final String OWNER_LAST_NAME2 = "Xxvalcantx";

    @BeforeEach
    void setUp() {
        ownerService = new OwnerMapService(new PetTypeMapService(), new PetMapService());
        ownerService.save(Owner.builder().id(OWNER_ID1).lastName(OWNER_LAST_NAME1).build());
    }

    @Test
    void findAll() {
        Set<Owner> ownerSet = ownerService.findAll();

        assertEquals(1, ownerSet.size());
    }

    @Test
    void findById() {
        Owner owner = ownerService.findById(OWNER_ID1);

        assertEquals(OWNER_ID1, owner.getId());
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
        ownerService.deleteById(OWNER_ID1);

        assertEquals(0, ownerService.findAll().size());
    }

    @Test
    void delete() {
        ownerService.delete(ownerService.findById(OWNER_ID1));

        assertEquals(0, ownerService.findAll().size());
    }

    @Test
    void findByLastName() {
        Owner owner = ownerService.findByLastName(OWNER_LAST_NAME1);

        assertNotNull(owner);
        assertEquals(1, owner.getId());
        assertEquals(OWNER_LAST_NAME1, owner.getLastName());
    }

    @Test
    void findByLastNameNotFound() {
        Owner owner = ownerService.findByLastName("OWNER_LAST_NAME1");

        assertNull(owner);
    }

    @Test
    void findAllByLastNameLikeIgnoreCase() {
        ownerService.save(Owner.builder().id(OWNER_ID2).lastName(OWNER_LAST_NAME2).build());

        List<Owner> owners = ownerService.findAllByLastNameLikeIgnoreCase(
            OWNER_LAST_NAME1.substring(BEGIN_INDEX, END_INDEX).toUpperCase()
        );

        assertEquals(2, owners.size());
    }

    @Test
    void findAllByLastNameLikeIgnoreCaseReturnEmpty() {
        List<Owner> owners = ownerService.findAllByLastNameLikeIgnoreCase("OWNER_LAST_NAME1");

        assertEquals(0, owners.size());
    }

}
