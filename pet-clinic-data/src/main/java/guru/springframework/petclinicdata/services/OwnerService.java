package guru.springframework.petclinicdata.services;

import java.util.Set;
import guru.springframework.petclinicdata.model.Owner;

public interface OwnerService {

    Owner findByLastName(String lastName);

    Owner findyById(Long id);

    Owner save(Owner owner);

    Set<Owner> findAll();

}
