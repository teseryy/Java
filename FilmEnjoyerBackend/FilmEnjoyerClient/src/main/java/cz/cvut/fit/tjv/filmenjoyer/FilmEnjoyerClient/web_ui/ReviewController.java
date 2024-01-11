package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.web_ui;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.ReviewDTO;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service.FilmService;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {

    private ReviewService reviewService;

    @GetMapping("/edit")
    public String showEditForm(Model model, @RequestParam Long id) {
        reviewService.setCurrentReview(id);
        var currReview = reviewService.read().get();
        model.addAttribute("review", currReview);
        model.addAttribute("author", currReview.getAuthor());
        model.addAttribute("filmId", currReview.getFilmId());
        return "editReview";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute ReviewDTO formData, RedirectAttributes redirectAttributes) {
        reviewService.setCurrentReview(formData.getId());
        try {
            reviewService.update(formData);
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("error", true);
        }

        redirectAttributes.addAttribute("id", formData.getFilmId());
        return "redirect:/films/{id}/reviews";
    }
}
