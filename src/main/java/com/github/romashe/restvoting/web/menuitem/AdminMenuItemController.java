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
    @GetMapping("/{id}/menu-items")
    public List<MenuItem> getRestaurantMenu(@PathVariable int id) {
        log.info("get Restaurant with id={} All Menu ", id);
        return menuItemRepository.findByRestaurantId(id);
    }

    @JsonView(JsonViews.Public.class)
    @GetMapping("/{id}/menu-items/by-date")
    public List<MenuItem> getRestaurantMenuByDate(@PathVariable int id,
                                                  @RequestParam
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("get Restaurant with id={} All Menu By Date {}", id, menuDate);
        return menuItemRepository.findByRestaurantIdFilteredByItemDate(id, menuDate);
    }

    @JsonView(JsonViews.Public.class)
    @GetMapping("/{id}/menu-items/{menuItemId}")
    public ResponseEntity<MenuItem> getRestaurantMenuById(@PathVariable int id, @PathVariable int menuItemId) {
        log.info("get Restaurant with id={} Menu with id={}", id, menuItemId);
        return ResponseEntity.of(menuItemRepository.findByRestaurantIdAndItemId(id, menuItemId));
    }

    @DeleteMapping("/{id}/menu-items/{menuItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int menuItemId) {
        log.info("delete Menu with id={} for Restaurant with id={} ", id, menuItemId);
        menuItemRepository.deleteExistedMenuItemByRestIdAndItemIdWithCheck(id, menuItemId);
    }

    @JsonView(JsonViews.Public.class)
    @PostMapping(value = "/{id}/menu-items/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem, @PathVariable int id) {
        log.info("create {}", menuItem);
        ValidationUtil.checkNew(menuItem);
        menuItem.setRestaurant(restaurantRepository.getById(id));
        MenuItem created = menuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}/menu-items/{menuItemId}")
                .buildAndExpand(id, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @JsonView(JsonViews.Public.class)
    @PutMapping(value = "/{id}/menu-items/{menuItemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMenuItem(@Valid @RequestBody MenuItem menuItem,
                               @PathVariable int id, @PathVariable int menuItemId) {
        log.info("update MenuItem with id={} for Restaurant with id={}", menuItem, id);
        Optional<MenuItem> ItemForUpdate = menuItemRepository.findByRestaurantIdAndItemId(id, menuItemId);
        if (ItemForUpdate.isEmpty()) {
            throw new IllegalRequestDataException("Entity with restaurant id=" + id + "" +
                    " and menu-item id=" + menuItemId + " not found");
        } else {
            menuItem.setRestaurant(ItemForUpdate.get().getRestaurant());
            ValidationUtil.assureIdConsistent(menuItem, menuItemId);
            menuItemRepository.save(menuItem);
        }
    }
}