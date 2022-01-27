package com.github.romashe.restvoting.web.restaurant;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.repository.RestaurantRepository;
import com.github.romashe.restvoting.repository.VoteRepository;
import com.github.romashe.restvoting.to.RestaurantTo;
import com.github.romashe.restvoting.util.JsonViews;
import com.github.romashe.restvoting.util.RestaurantUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.github.romashe.restvoting.web.restaurant.UserRestaurantController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class UserRestaurantController {

    public static final String REST_URL = "/api/restaurants";
    final private RestaurantRepository restaurantRepository;
    final private VoteRepository voteRepository;

    @JsonView(JsonViews.Public.class)
    @GetMapping
    @Operation(summary = "Get All restaurants")
    public List<Restaurant> getAllRestaurant() {
        log.info("getAllRestaurants");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @JsonView(JsonViews.Public.class)
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable int id) {
        log.info("get Restaurant {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    @GetMapping("/menu-items")
    @Cacheable({"restMenuItems"})
    @Operation(summary = "Get Restaurants and it's Menu-items for Today")
    public List<Restaurant> getAllRestaurantWithMenuByToday() {
        log.info("getAllRestaurantWithMenuByToday for MenuDate {}", LocalDate.now());
        return restaurantRepository.getAllWithMenu();
    }

    @GetMapping("/ratings")
    @Transactional
    @Cacheable({"ratings"})
    @Operation(summary = "Get Restaurants, Vote count and it's Menu-items for Today")
    public List<RestaurantTo> getAllRestaurantWithMenuByTodayWithRating() {
        log.info("getAllRestaurantWithMenuByTodayWithRating for MenuDate {}", LocalDate.now());
        return RestaurantUtil.convertListTo(restaurantRepository.getAllWithMenu(), voteRepository.findAllTodayVotes());
    }
}
