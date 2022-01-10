package ru.shr.restaurant_voting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.shr.restaurant_voting.model.MenuItem;
import ru.shr.restaurant_voting.model.Restaurant;
import ru.shr.restaurant_voting.repository.MenuItemRepository;
import ru.shr.restaurant_voting.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static ru.shr.restaurant_voting.util.validation.ValidationUtil.assureIdConsistent;
import static ru.shr.restaurant_voting.util.validation.ValidationUtil.checkNew;
import static ru.shr.restaurant_voting.web.AdminRestaurantController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController {
    public static final String REST_URL = "/api/admin/restaurants";

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected MenuItemRepository menuItemRepository;

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAllRestaurant");
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get Restaurant {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update Restaurant {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @GetMapping("/{id}/menu")
    public List<MenuItem> getRestaurantMenu(@PathVariable int id) {
        log.info("get Restaurant with id={} All Menu ", id);
        return menuItemRepository.findByRestaurantId(id);
    }

    @GetMapping("/{id}/menu/{menuItemId}")
    public Optional<MenuItem> getRestaurantMenuById(@PathVariable int id, @PathVariable int menuItemId) {
        log.info("get Restaurant with id={} Menu with id={}", id, menuItemId);
        return menuItemRepository.findById(menuItemId);
    }

    @DeleteMapping("/{id}/menu/{menuItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int menuItemId) {
        log.info("delete Menu with id={} for Restaurant with id={} ", id, menuItemId);
        menuItemRepository.deleteExisted(menuItemId);
    }

    @PostMapping(value = "/{id}/menu/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem, @PathVariable int id) {
        log.info("create {}", menuItem);
        checkNew(menuItem);
        menuItem.setRestaurant(restaurantRepository.getById(id));
        MenuItem created = menuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}/menu/{menuItemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMenuItem(@Valid @RequestBody MenuItem menuItem, @PathVariable int id, @PathVariable int menuItemId) {
        log.info("update MenuItem with id={} for Restaurant with id={}", menuItem, id);
        menuItem.setRestaurant(restaurantRepository.getById(id));
        assureIdConsistent(menuItem, menuItemId);
        menuItemRepository.save(menuItem);
    }
}