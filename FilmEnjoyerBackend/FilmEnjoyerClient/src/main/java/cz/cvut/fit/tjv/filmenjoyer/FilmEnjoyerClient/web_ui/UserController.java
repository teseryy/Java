package cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.web_ui;

import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.model.UserDTO;
import org.springframework.ui.Model;
import cz.cvut.fit.tjv.filmenjoyer.FilmEnjoyerClient.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public String list(Model model) {
        var all = userService.readAll();
        model.addAttribute("allUsers", all);
        return "users";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "addUser";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute UserDTO newUser, RedirectAttributes redirectAttributes) {
        try {
            userService.create(newUser);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, @RequestParam String id) {
        userService.setCurrentUser(id);
        var currUser = userService.read().get();
        model.addAttribute("user", currUser);
        return "editUser";
    }

    @PostMapping("/edit")
    public String editSubmit(@ModelAttribute UserDTO formData, RedirectAttributes redirectAttributes) {
        userService.setCurrentUser(formData.getUsername());
        try {
            userService.update(formData);
        } catch (HttpClientErrorException.NotFound e) {
            redirectAttributes.addFlashAttribute("error", true);
        }
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubmit(Model model, @PathVariable(value = "id") String id) {
        try {
            userService.setCurrentUser(id);
            userService.delete();
        } catch (HttpClientErrorException.NotFound e) {
            model.addAttribute("error", true);
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/reviews")
    public String showUserReviews(Model model, @PathVariable(value = "id") String id){
        userService.setCurrentUser(id);
        var all = userService.getUserReviews();

        model.addAttribute("userReviews", all);
        return "showUserReviews";
    }
}
