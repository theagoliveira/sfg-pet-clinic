package guru.springframework.sfgpetclinic.controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/owners/{ownerId}/pets")
public class PetController {

    private static final String PETS_FORM = "owners/pets/form";
    private static final String PET = "pet";
    private final PetService petService;
    private final PetTypeService petTypeService;
    private final OwnerService ownerService;

    public PetController(PetService petService, PetTypeService petTypeService,
                         OwnerService ownerService) {
        this.petService = petService;
        this.petTypeService = petTypeService;
        this.ownerService = ownerService;
    }

    @ModelAttribute("petTypes")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/new")
    public String newPet(Owner owner, Model model) {
        var pet = Pet.builder().build();
        owner.getPets().add(pet);
        pet.setOwner(owner);
        model.addAttribute(PET, pet);

        return PETS_FORM;
    }

    @PostMapping
    public String createPet(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
        log.info("Inside createPet.");
        if (StringUtils.hasLength(pet.getName()) && pet.isNew()
                && (petService.findByNameIgnoreCaseAndOwnerId(
                    pet.getName(), owner.getId()
                ) != null)) {
            result.rejectValue("name", "duplicate");
        }

        log.info("Not a duplicate.");
        owner.getPets().add(pet);
        pet.setOwner(owner);
        if (result.hasErrors()) {
            model.addAttribute(PET, pet);

            return PETS_FORM;
        } else {
            log.info("Creating pet.");
            petService.save(pet);

            return "redirect:/owners/" + owner.getId();
        }
    }

    @GetMapping("/{id}/edit")
    public String editPet(@PathVariable Long id, Model model) {
        model.addAttribute(PET, petService.findById(id));

        return PETS_FORM;
    }

    @PostMapping("/{id}")
    public String updatePet(Owner owner, @Valid Pet pet, BindingResult result,
                            @PathVariable Long id, Model model) {
        log.info("Inside updatePet.");
        if (result.hasErrors()) {
            pet.setOwner(owner);
            model.addAttribute(PET, pet);

            return PETS_FORM;
        } else {
            log.info("Updating pet.");
            pet.setOwner(owner);
            petService.save(pet);

            return "redirect:/owners/" + owner.getId();
        }
    }

}
