package guru.springframework.petclinicdata.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.sfgpetclinic.model.Specialty;
import guru.springframework.sfgpetclinic.services.map.SpecialtyMapService;

class SpecialtyMapServiceTest {

    SpecialtyMapService specialtyService;

    private static final Long specialtyId = 1L;

    @BeforeEach
    void setUp() {
        specialtyService = new SpecialtyMapService();
        specialtyService.save(Specialty.builder().id(specialtyId).build());
    }

    @Test
    void findAll() {
        Set<Specialty> specialtySet = specialtyService.findAll();

        assertEquals(1, specialtySet.size());
    }

    @Test
    void findById() {
        Specialty specialty = specialtyService.findById(specialtyId);

        assertEquals(specialtyId, specialty.getId());
    }

    @Test
    void saveWithId() {
        Long newSpecialtyId = 2L;

        Specialty newSpecialty = Specialty.builder().id(newSpecialtyId).build();
        Specialty savedSpecialty = specialtyService.save(newSpecialty);

        assertEquals(newSpecialtyId, savedSpecialty.getId());
    }

    @Test
    void saveWithoutId() {
        Specialty savedSpecialty = specialtyService.save(Specialty.builder().build());

        assertNotNull(savedSpecialty);
        assertNotNull(savedSpecialty.getId());
    }

    @Test
    void deleteById() {
        specialtyService.deleteById(specialtyId);

        assertEquals(0, specialtyService.findAll().size());
    }

    @Test
    void delete() {
        specialtyService.delete(specialtyService.findById(specialtyId));

        assertEquals(0, specialtyService.findAll().size());
    }

}
