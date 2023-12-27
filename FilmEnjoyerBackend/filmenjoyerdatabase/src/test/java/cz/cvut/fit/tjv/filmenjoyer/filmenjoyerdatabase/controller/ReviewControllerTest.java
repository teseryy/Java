package cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.application.ReviewServiceImpl;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter.FilmDTOConverter;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter.ReviewDTOConverter;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.converter.UserDTOConverter;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.FilmDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.ReviewDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.controller.dto.UserDTO;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Film;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.Review;
import cz.cvut.fit.tjv.filmenjoyer.filmenjoyerdatabase.domain.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    ReviewServiceImpl reviewService;
    @MockBean
    private ReviewDTOConverter reviewDTOConverter;
    @MockBean
    private FilmDTOConverter filmDTOConverter;
    @MockBean
    private UserDTOConverter userDTOConverter;

    @Test
    void create() throws Exception {
        Long id = 21L;
        String text = "testText";
        Integer rating = 8;
        Date date = new Date(123, 10, 30);
        ReviewDTO reviewDTO = new ReviewDTO(id, text, rating, date, null, null);
        Review review = new Review(id, text, rating, date, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        String reviewJSON = objectMapper.writeValueAsString(reviewDTO);

        Mockito.when(reviewService.create(Mockito.any())).thenReturn(review);
        Mockito.when(reviewDTOConverter.toDTO(Mockito.any())).thenReturn(reviewDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(21)));
    }

    @Test
    void createWhenExceptionThrown() throws Exception {
        Long id = 21L;
        String text = "testText";
        Integer rating = 8;
        Date date = new Date(123, 10, 30);
        ReviewDTO reviewDTO = new ReviewDTO(id, text, rating, date, null, null);

        Mockito.when(reviewService.create(Mockito.any())).thenThrow(new IllegalArgumentException());

        ObjectMapper objectMapper = new ObjectMapper();
        String reviewJSON = objectMapper.writeValueAsString(reviewDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getReviewById() throws Exception {
        Long id = 21L;
        String text = "testText";
        Integer rating = 8;
        Date date = new Date(123, 10, 30);
        ReviewDTO reviewDTO = new ReviewDTO(id, text, rating, date, null, null);
        Review review = new Review(id, text, rating, date, null, null);

        Mockito.when(reviewService.readById(id)).thenReturn(Optional.of(review));
        Mockito.when(reviewDTOConverter.toDTO(Mockito.any())).thenReturn(reviewDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/api/review/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(21)));
    }

    @Test
    void getReviewByIdWhenIdNotFound() throws Exception {
        Long nonExistingId = 999L;

        Mockito.when(reviewService.readById(nonExistingId)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/api/review/{id}", nonExistingId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getAllReviews() throws Exception {
        Review review1 = new Review(1L, "Review 1", 8, new Date(), null, null);
        Review review2 = new Review(2L, "Review 2", 7, new Date(), null, null);

        ReviewDTO reviewDTO1 = new ReviewDTO(1L, "Review 1", 8, new Date(), null, null);
        ReviewDTO reviewDTO2 = new ReviewDTO(2L, "Review 2", 7, new Date(), null, null);

        Mockito.when(reviewService.readAll()).thenReturn(List.of(review1, review2));

        List<ReviewDTO> expectedDTOs = List.of(reviewDTO1, reviewDTO2);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/api/review"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedDTOs.size())));
    }

    @Test
    void getAllReviewsWhenEmptyListReturned() throws Exception {
        Mockito.when(reviewService.readAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/api/review"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void getReviewFilm() throws Exception {
        Long reviewId = 1L;
        String text = "testText";
        Integer rating = 8;
        Date date = new Date(123, 10, 30);
        Film film = new Film(2L, "testTitle", 160, "Nolan", new Date(123, 10, 30), 100, "USA", new ArrayList<>(), new ArrayList<>());
        FilmDTO filmDTO = new FilmDTO(2L, "testTitle", 160, "Nolan", new Date(123, 10, 30), 100, "USA", new ArrayList<>(), new ArrayList<>());
        Review review = new Review(reviewId, text, rating, date, film, null);

        Mockito.when(reviewService.readById(reviewId)).thenReturn(Optional.of(review));
        Mockito.when(filmDTOConverter.toDTO(Mockito.any())).thenReturn(filmDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/api/review/{id}/film", reviewId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(2)));
    }

    @Test
    void getReviewUser() throws Exception {
        Long reviewId = 1L;
        String text = "testText";
        Integer rating = 8;
        Date date = new Date(123, 10, 30);
        User author = new User("username", "John", "Grey", new ArrayList<>());
        UserDTO userDTO = new UserDTO("username", "John", "Grey", new ArrayList<>());
        Review review = new Review(reviewId, text, rating, date, null, author);

        Mockito.when(reviewService.readById(reviewId)).thenReturn(Optional.of(review));
        Mockito.when(userDTOConverter.toDTO(Mockito.any())).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/api/review/{id}/author", reviewId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("username")));
    }
}