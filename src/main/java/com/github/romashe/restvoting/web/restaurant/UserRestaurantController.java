package com.github.romashe.restvoting.web.restaurant;

import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.github.romashe.restvoting.web.restaurant.UserRestaurantController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class UserRestaurantController {

    public static final String REST_URL = "/api/restaurants";
    final private RestaurantRepository restaurantRepository;

    @GetMapping
    @Operation(summary = "Get All restaurants")
    public List<Restaurant> getAllRestaurant() {
        log.info("getAllRestaurants");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable int id) {
        log.info("get Restaurant {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }
}
