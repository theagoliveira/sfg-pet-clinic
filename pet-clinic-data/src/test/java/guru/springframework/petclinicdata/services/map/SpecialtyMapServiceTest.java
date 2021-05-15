package guru.springframework.petclinicdata.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.sfgpetclinic.model.Specialty;
import guru.springframework.sfgpetclinic.services.map.SpecialtyMapService;

class SpecialtyMapServiceTest {

    SpecialtyMapService specialtyMapService;

    private static final Long specialtyId = 1L;

    @BeforeEach
    void setUp() {
        specialtyMapService = new SpecialtyMapService();
        specialtyMapService.save(Specialty.builder().id(specialtyId).build());
    }

    @Test
    void findAll() {
        Set<Specialty> specialtySet = specialtyMapService.findAll();

        assertEquals(1, specialtySet.size());
    }

    @Test
    void findById() {
        Specialty specialty = specialtyMapService.findById(specialtyId);

        assertEquals(specialtyId, specialty.getId());
    }

    @Test
    void saveWithId() {
        Long newSpecialtyId = 2L;

        Specialty newSpecialty = Specialty.builder().id(newSpecialtyId).build();
        Specialty savedSpecialty = specialtyMapService.save(newSpecialty);

        assertEquals(newSpecialtyId, savedSpecialty.getId());
    }

    @Test
    void saveWithoutId() {
        Specialty savedSpecialty = specialtyMapService.save(Specialty.builder().build());

        assertNotNull(savedSpecialty);
        assertNotNull(savedSpecialty.getId());
    }

    @Test
    void deleteById() {
        specialtyMapService.deleteById(specialtyId);

        assertEquals(0, specialtyMapService.findAll().size());
    }

    @Test
    void delete() {
        specialtyMapService.delete(specialtyMapService.findById(specialtyId));

        assertEquals(0, specialtyMapService.findAll().size());
    }

}
