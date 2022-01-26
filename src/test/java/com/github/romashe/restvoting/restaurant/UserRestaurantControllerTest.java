package com.github.romashe.restvoting.restaurant;

import com.github.romashe.restvoting.AbstractControllerTest;
import com.github.romashe.restvoting.web.restaurant.UserRestaurantController;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.romashe.restvoting.restaurant.RestaurantTestData.*;
import static com.github.romashe.restvoting.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserRestaurantController.REST_URL + '/';

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANTS_MATCHER.contentJson(restaurantsAllList));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + restaurant1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANTS_MATCHER.contentJson(restaurant1));
    }
}