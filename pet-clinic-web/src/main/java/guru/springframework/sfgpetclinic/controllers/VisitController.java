package guru.springframework.sfgpetclinic.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;

@Controller
@RequestMapping("/owners/{ownerId}/pets/{petId}/visits")
public class VisitController {

    private final VisitService visitService;
    private final PetService petService;

    public VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable Long petId, Model model) {
        var pet = petService.findById(petId);
        var visit = new Visit();

        pet.getVisits().add(visit);
        visit.setPet(pet);
        model.addAttribute("pet", pet);

        return visit;
    }

    @GetMapping("/new")
    public String newVisit(@PathVariable Long petId, Model model) {
        return "owners/pets/visits/form";
    }

    @PostMapping
    public String createVisit(@Valid Visit visit, BindingResult result,
                              @PathVariable Long ownerId) {
        if (result.hasErrors()) {
            return "owners/pets/visits/form";
        } else {
            visitService.save(visit);
            return "redirect:/owners/" + ownerId;
        }
    }

}
