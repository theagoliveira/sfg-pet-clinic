package guru.springframework.sfgpetclinic.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/owners")
public class OwnerController {

    private static final String OWNERS = "owners";
    private static final String OWNER = "owner";

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping({"/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute(OWNERS, ownerService.findAll());

        return "owners/index";
    }

    @GetMapping("/find")
    public String initFindForm(Model model) {
        model.addAttribute(OWNER, Owner.builder().build());

        return "owners/find";
    }

    @GetMapping
    public String processFindForm(Owner owner, BindingResult result, Model model) {
        if (owner.getLastName() == null) {
            owner.setLastName("");
        }

        List<Owner> results = ownerService.findAllByLastNameLikeIgnoreCase(owner.getLastName());
        if (results.isEmpty()) {
            result.rejectValue("lastName", "notFound");
            return "owners/find";
        } else if (results.size() == 1) {
            owner = results.get(0);
            return "redirect:/owners/" + owner.getId();
        } else {
            model.addAttribute(OWNERS, results);
            return "owners/index";
        }
    }

    @GetMapping("/{id}")
    public ModelAndView showOwner(@PathVariable Long id) {
        var mav = new ModelAndView("owners/show");
        mav.addObject(ownerService.findById(id));

        return mav;
    }

    @GetMapping("/new")
    public String newOwner(Model model) {
        model.addAttribute(OWNER, Owner.builder().build());

        return "owners/form";
    }

    @PostMapping
    public String createOwner(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return "owners/form";
        } else {
            log.info("Creating owner.");
            var savedOwner = ownerService.save(owner);

            return "redirect:/owners/" + savedOwner.getId();
        }
    }

    @GetMapping("/{id}/edit")
    public String editOwner(@PathVariable Long id, Model model) {
        model.addAttribute(OWNER, ownerService.findById(id));

        return "owners/form";
    }

    @PostMapping("/{id}")
    public String updateOwner(@Valid Owner owner, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return "owners/form";
        } else {
            log.info("Updating owner.");
            owner.setId(id);
            var savedOwner = ownerService.save(owner);

            return "redirect:/owners/" + savedOwner.getId();
        }
    }

}
