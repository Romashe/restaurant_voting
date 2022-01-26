package com.github.romashe.restvoting.web.menuitem;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.romashe.restvoting.model.MenuItem;
import com.github.romashe.restvoting.repository.MenuItemRepository;
import com.github.romashe.restvoting.repository.RestaurantRepository;
import com.github.romashe.restvoting.util.JsonViews;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @JsonView(JsonViews.Public.class)
    @GetMapping("/{id}/menu-items")
    @Operation(summary = "Get Menu-items by RestaurantId for Today")
    public List<MenuItem> getRestaurantMenu(@PathVariable int id) {
        log.info("get Restaurant with id={}, Menu for date {}", id, LocalDate.now());
        return menuItemRepository.findByRestaurantIdFilteredByItemDate(id, LocalDate.now());
    }
}
