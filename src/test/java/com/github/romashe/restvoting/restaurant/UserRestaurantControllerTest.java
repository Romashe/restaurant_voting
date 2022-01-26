package com.github.romashe.restvoting.restaurant;

import com.github.romashe.restvoting.AbstractControllerTest;
import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.repository.VoteRepository;
import com.github.romashe.restvoting.to.RestaurantTo;
import com.github.romashe.restvoting.util.JsonUtil;
import com.github.romashe.restvoting.util.RestaurantUtil;
import com.github.romashe.restvoting.web.restaurant.UserRestaurantController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.romashe.restvoting.restaurant.RestaurantTestData.*;
import static com.github.romashe.restvoting.user.UserTestData.USER_MAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserRestaurantController.REST_URL + '/';
    @Autowired
    private VoteRepository voteRepository;

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

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllRestaurantWithMenuByToday() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "/menu-items"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Restaurant> actual = JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), Restaurant.class);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("votes", "menuItems.restaurant")
                .isEqualTo(RestaurantTestData.getRestaurantsWithMenuToday());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllRestaurantWithMenuByTodayWithRating() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "/ratings"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<RestaurantTo> actual = JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), RestaurantTo.class);
        List<RestaurantTo> expected = RestaurantUtil.convertListTo(
                RestaurantTestData.getRestaurantsWithMenuToday(), voteRepository.findAllTodayVotes());
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("menuItems.restaurant")
                .isEqualTo(expected);
    }
}