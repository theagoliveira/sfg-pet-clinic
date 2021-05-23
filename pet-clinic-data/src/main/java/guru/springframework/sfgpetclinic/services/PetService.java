package guru.springframework.sfgpetclinic.services;

import guru.springframework.sfgpetclinic.model.Pet;

public interface PetService extends CrudService<Pet, Long> {

    Pet findByNameIgnoreCaseAndOwnerId(String name, Long ownerId);

}
