package com.github.romashe.restvoting.web.menuitem;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.romashe.restvoting.error.IllegalRequestDataException;
import com.github.romashe.restvoting.model.MenuItem;
import com.github.romashe.restvoting.repository.MenuItemRepository;
import com.github.romashe.restvoting.repository.RestaurantRepository;
import com.github.romashe.restvoting.util.JsonViews;
import com.github.romashe.restvoting.util.validation.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.github.romashe.restvoting.web.restaurant.AdminRestaurantController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class AdminMenuItemController {
    final private RestaurantRepository restaurantRepository;
    final private MenuItemRepository menuItemRepository;

    @JsonView(JsonViews.Public.class)
    @GetMapping("/{restaurantId}/menu-items")
    public List<MenuItem> getRestaurantMenu(@PathVariable int restaurantId) {
        log.info("get Restaurant with id={} All Menu ", restaurantId);
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    @JsonView(JsonViews.Public.class)
    @GetMapping("/{restaurantId}/menu-items/by-date")
    public List<MenuItem> getRestaurantMenuByDate(@PathVariable int restaurantId,
                                                  @RequestParam
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("get Restaurant with id={} All Menu By Date {}", restaurantId, menuDate);
        return menuItemRepository.findByRestaurantIdFilteredByItemDate(restaurantId, menuDate);
    }

    @JsonView(JsonViews.Public.class)
    @GetMapping("/{restaurantId}/menu-items/{id}")
    public ResponseEntity<MenuItem> getRestaurantMenuById(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get Restaurant with id={} Menu with id={}", restaurantId, id);
        return ResponseEntity.of(menuItemRepository.findByRestaurantIdAndItemId(restaurantId, id));
    }

    @DeleteMapping("/{restaurantId}/menu-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"ratings", "restMenuItems"}, allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete Menu with id={} for Restaurant with id={} ", restaurantId, id);
        menuItemRepository.deleteExistedMenuItemByRestIdAndItemIdWithCheck(restaurantId, id);
    }

    @JsonView(JsonViews.Public.class)
    @PostMapping(value = "/{restaurantId}/menu-items/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(value = {"ratings", "restMenuItems"}, allEntries = true)
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId) {
        log.info("create {}", menuItem);
        ValidationUtil.checkNew(menuItem);
        menuItem.setRestaurant(restaurantRepository.getById(restaurantId));
        MenuItem created = menuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}/menu-items/{menuItemId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @JsonView(JsonViews.Public.class)
    @PutMapping(value = "/{restaurantId}/menu-items/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"ratings", "restMenuItems"}, allEntries = true)
    public void updateMenuItem(@Valid @RequestBody MenuItem menuItem,
                               @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update MenuItem with id={} for Restaurant with id={}", menuItem, restaurantId);
        Optional<MenuItem> ItemForUpdate = menuItemRepository.findByRestaurantIdAndItemId(restaurantId, id);
        if (ItemForUpdate.isEmpty()) {
            throw new IllegalRequestDataException("Entity with restaurant id=" + restaurantId + "" +
                    " and menu-item id=" + id + " not found");
        } else {
            menuItem.setRestaurant(ItemForUpdate.get().getRestaurant());
            ValidationUtil.assureIdConsistent(menuItem, id);
            menuItemRepository.save(menuItem);
        }
    }
}