package com.github.romashe.restvoting.web.menuitem;

import com.github.romashe.restvoting.model.MenuItem;
import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.repository.MenuItemRepository;
import com.github.romashe.restvoting.repository.RestaurantRepository;
import com.github.romashe.restvoting.repository.VoteRepository;
import com.github.romashe.restvoting.to.RestaurantTo;
import com.github.romashe.restvoting.util.RestaurantUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
public class UserMenuItemController {
    final private MenuItemRepository menuItemRepository;
    final private RestaurantRepository restaurantRepository;
    final private VoteRepository voteRepository;

    @GetMapping("/menu-items")
    @Operation(summary = "Get Restaurants and it's Menu-items for Today")
    public List<Restaurant> getAllRestaurantWithMenuByToday() {
        log.info("getAllRestaurantWithMenuByToday for MenuDate {}", LocalDate.now());
        return restaurantRepository.getAllWithMenu();
    }

    @GetMapping("/ratings")
    @Transactional
    @Operation(summary = "Get Restaurants, Vote count and it's Menu-items for Today")
    public List<RestaurantTo> getAllRestaurantWithMenuByTodayWithRaiting() {
        log.info("getAllRestaurantWithMenuByTodayWithRaiting for MenuDate {}", LocalDate.now());
        return RestaurantUtil.convertListTo(restaurantRepository.getAllWithMenu(), voteRepository.findAllTodayVotes());
    }

    @GetMapping("/{id}/menu-items")
    @Operation(summary = "Get Menu-items by RestaurantId for Today")
    public List<MenuItem> getRestaurantMenu(@PathVariable int id) {
        log.info("get Restaurant with id={}, Menu for date {}", id, LocalDate.now());
        return menuItemRepository.findByRestaurantIdFilteredByItemDate(id, LocalDate.now());
    }
}
