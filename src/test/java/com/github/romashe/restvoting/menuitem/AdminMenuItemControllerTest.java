package com.github.romashe.restvoting.menuitem;

import com.github.romashe.restvoting.AbstractControllerTest;
import com.github.romashe.restvoting.model.MenuItem;
import com.github.romashe.restvoting.repository.MenuItemRepository;
import com.github.romashe.restvoting.util.JsonUtil;
import com.github.romashe.restvoting.web.restaurant.AdminRestaurantController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.github.romashe.restvoting.menuitem.MenuItemTestData.*;
import static com.github.romashe.restvoting.restaurant.RestaurantTestData.notFoundRestaurant;
import static com.github.romashe.restvoting.restaurant.RestaurantTestData.restaurant2;
import static com.github.romashe.restvoting.user.UserTestData.ADMIN_MAIL;
import static com.github.romashe.restvoting.user.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuItemControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getRestaurantMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + restaurant2.getId() + "/menu-items"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENUITEMS_MATCHER.contentJson(rest1AllMenuItemsList));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getRestaurantMenuByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + restaurant2.getId() +
                "/menu-items/by-date?menuDate=" + LocalDate.now().minusDays(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENUITEMS_MATCHER.contentJson(rest1YesterdayMenuItemsList));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getRestaurantMenuById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + restaurant2.getId() +
                "/menu-items/" + rest2MenuItemToday1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENUITEMS_MATCHER.contentJson(rest2MenuItemToday1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getRestaurantMenuNotFoundByMenuItemId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + restaurant2.getId() +
                "/menu-items/" + notFoundMenuItem.getId()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getRestaurantMenuNotFoundByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + notFoundRestaurant.getId() +
                "/menu-items/" + rest2MenuItemToday1.getId()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + restaurant2.getId() +
                "/menu-items/" + rest2MenuItemToday1.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuItemRepository.findById(rest2MenuItemToday1.getId()).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFoundByMenuItemId() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + restaurant2.getId() +
                "/menu-items/" + notFoundMenuItem.getId()))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFoundByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + notFoundRestaurant.getId() +
                "/menu-items/" + rest2MenuItemToday1.getId()))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createMenuItem() throws Exception {
        MenuItem newMenuItem = new MenuItem(null, "newDish", 7777, LocalDate.now());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL
                        + restaurant2.getId() + "/menu-items/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItem)))
                .andDo(print())
                .andExpect(status().isCreated());

        MenuItem created = MENUITEMS_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MENUITEMS_MATCHER.assertMatch(created, newMenuItem);
        MENUITEMS_MATCHER.assertMatch(menuItemRepository.getById(newId), newMenuItem);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createMenuItemDuplicate() throws Exception {
        MenuItem newMenuItem = new MenuItem(null, rest2MenuItemToday1.getName(), 7777, LocalDate.now());
        perform(MockMvcRequestBuilders.post(REST_URL
                        + restaurant2.getId() + "/menu-items/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItem)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createMenuItemInvalid() throws Exception {
        MenuItem newMenuItem = new MenuItem(null, null, 7777, LocalDate.now());
        perform(MockMvcRequestBuilders.post(REST_URL
                        + restaurant2.getId() + "/menu-items/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItem)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateMenuItem() throws Exception {
        MenuItem updatedMenuItem = new MenuItem(null, "UpdatedName", 7777, LocalDate.now());
        perform(MockMvcRequestBuilders.put(REST_URL
                        + restaurant2.getId() + "/menu-items/" + rest2MenuItemToday1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenuItem)))
                .andDo(print())
                .andExpect(status().isNoContent());

        updatedMenuItem.setId(restaurant2.getId());
        MENUITEMS_MATCHER.assertMatch(menuItemRepository.getById(restaurant2.getId()), updatedMenuItem);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateMenuItemDuplicate() throws Exception {
        MenuItem updatedMenuItem = new MenuItem(null, rest2MenuItemToday2.getName(), 7777, LocalDate.now());
        perform(MockMvcRequestBuilders.put(REST_URL
                        + restaurant2.getId() + "/menu-items/" + rest2MenuItemToday1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenuItem)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}