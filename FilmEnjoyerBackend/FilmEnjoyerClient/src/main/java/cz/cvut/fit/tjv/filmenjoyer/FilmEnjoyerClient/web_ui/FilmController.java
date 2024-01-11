package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.web_ui;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.FilmDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ReviewDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service.FilmService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private FilmService filmService;

    @GetMapping
    public String list(Model model) {
        var all = filmService.readAll();

        model.addAttribute("allFilms", all);
        return "films";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("film", new FilmDTO());
        return "addFilm";
    }

    @PostMapping("/create")
    public String createFilm(@ModelAttribute FilmDTO newFilm, RedirectAttributes redirectAttributes) {
        try {
            filmService.create(newFilm);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/films";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, @RequestParam Long id) {
        filmService.setCurrentFilm(id);
        var currFilm = filmService.read().get();
        model.addAttribute("film", currFilm);
        return "editFilm";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute FilmDTO formData, RedirectAttributes redirectAttributes) {
        filmService.setCurrentFilm(formData.getId());
        try {
            filmService.update(formData);
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/films";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubmit(RedirectAttributes redirectAttributes, @PathVariable(value = "id") Long id) {
        try {
            filmService.setCurrentFilm(id);
            filmService.delete();
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/films";
    }

    @GetMapping("/{id}/reviews")
    public String showFilmReviews(Model model, @PathVariable(value = "id") Long id) {
        filmService.setCurrentFilm(id);
        var all = filmService.getReviews();

        model.addAttribute("filmReviews", all);
        model.addAttribute("filmId", id);
        return "showFilmReviews";
    }

    @PostMapping("/{id}/reviews/delete/{reviewId}")
    public String deleteReview(@PathVariable(value = "id") Long id, @PathVariable(value = "reviewId") Long reviewId,
                               RedirectAttributes redirectAttributes) {
        try {
            filmService.setCurrentFilm(id);
            filmService.deleteReview(reviewId);
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("error", true);
        }

        redirectAttributes.addAttribute("id", id);
        return "redirect:/films/{id}/reviews";
    }

    @GetMapping("/{id}/reviews/create")
    public String showCreateReviewForm(Model model, @PathVariable(value = "id") Long filmId) {
        model.addAttribute("review", new ReviewDTO());
        model.addAttribute("id", filmId);
        return "addReview";
    }

    @PostMapping("/{id}/reviews/create")
    public String createReview(@ModelAttribute ReviewDTO newReview,
                               RedirectAttributes redirectAttributes, @PathVariable(value = "id") Long filmId) {
        try {
            filmService.setCurrentFilm(filmId);
            filmService.createReviewForCurrentFilm(newReview);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
        }

        redirectAttributes.addAttribute("id", filmId);
        return "redirect:/films/{id}/reviews";
    }

    @GetMapping("/{id}/actors")
    public String showFilmActors(Model model, @PathVariable(value = "id") Long id){
        filmService.setCurrentFilm(id);
        var filmActors = filmService.showFilmActor();

        model.addAttribute("filmActors", filmActors);
        return "showFilmActors";
    }

    @GetMapping("/rating")
    public String filmsWithHighRating(Model model){
        var films = filmService.getFilmsWithReviewRating8AndMore();

        model.addAttribute("films", films);
        return "filmsWithHighRating";
    }
}
