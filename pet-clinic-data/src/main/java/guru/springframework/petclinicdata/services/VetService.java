package guru.springframework.petclinicdata.services;

import java.util.Set;
import guru.springframework.petclinicdata.model.Vet;

public interface VetService {

    Vet findyById(Long id);

    Vet save(Vet vet);

    Set<Vet> findAll();

}
