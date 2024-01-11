package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.web_ui;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ActorDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service.ActorService;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service.FilmService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/actors")
@AllArgsConstructor
public class ActorController {
    private ActorService actorService;
    private FilmService filmService;

    @GetMapping
    public String list(Model model) {
        var all = actorService.readAll();
        model.addAttribute("allActors", all);
        return "actors";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("actor", new ActorDTO());
        return "addActor";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute ActorDTO newActor, RedirectAttributes redirectAttributes) {
        try {
            actorService.create(newActor);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/actors";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, @RequestParam Long id) {
        actorService.setCurrentActor(id);
        var currActor = actorService.read().get();
        model.addAttribute("actor", currActor);
        return "editActor";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute ActorDTO formData, RedirectAttributes redirectAttributes) {
        actorService.setCurrentActor(formData.getId());
        try {
            actorService.update(formData);
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/actors";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubmit(RedirectAttributes redirectAttributes, @PathVariable(value = "id") Long id) {
        try {
            actorService.setCurrentActor(id);
            actorService.delete();
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/actors";
    }

    @GetMapping("/{id}/films")
    public String showActorFilms(Model model, @PathVariable(value = "id") Long id) {
        actorService.setCurrentActor(id);
        var all = actorService.getActorFilms();

        model.addAttribute("actorFilms", all);
        return "showActorFilms";
    }

    @GetMapping("/{id}/allFilms")
    public String showAllFilms(Model model, @PathVariable(value = "id") Long id){
        var all = filmService.readAll();

        model.addAttribute("allFilms", all);
        model.addAttribute("actorId", id);
        return "allFilms";
    }

    @PostMapping("/{id}/allFilms/add/{filmId}")
    public String addFilmToActor(RedirectAttributes redirectAttributes, @PathVariable(value = "id") Long id,
                                 @PathVariable(value = "filmId") Long filmId) {
        try {
            actorService.setCurrentActor(id);
            actorService.addFilmToActor(filmId);
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/actors/{id}/allFilms";
    }

    @PostMapping("/{id}/films/delete/{filmId}")
    public String deleteFilmFromActor(RedirectAttributes redirectAttributes, @PathVariable(value = "id") Long id,
                                 @PathVariable(value = "filmId") Long filmId) {
        try {
            actorService.setCurrentActor(id);
            actorService.deleteFilmFromActor(filmId);
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/actors/{id}/films";
    }
}
