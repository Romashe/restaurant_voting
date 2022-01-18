package com.github.romashe.restvoting.restaurant;

import com.github.romashe.restvoting.AbstractControllerTest;
import com.github.romashe.restvoting.user.UserTestData;
import com.github.romashe.restvoting.web.restaurant.UserRestaurantController;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserRestaurantControllerTest extends AbstractControllerTest {

    /*@Test
    @WithUserDetails(value = USER_MAIL)
    void getAllRestaurantWithMenuByToday() throws Exception{
            perform(MockMvcRequestBuilders.get(REST_URL))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(RESTAURANTS_TO_MATCHER.contentJson(RestaurantTestData.getRestaurantToForToday()));
    }*/

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getRestaurantById() throws Exception {
        perform(MockMvcRequestBuilders.get(UserRestaurantController.REST_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANTS_MATCHER.contentJson(RestaurantTestData.restaurant1List));
    }

    /*@Test
    @WithUserDetails(value = USER_MAIL)
    void getRestaurantTodayVotes() throws Exception{
        perform(MockMvcRequestBuilders.get(REST_URL+"/1/votes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTETO_MATCHER.contentJson(todayVotesToForRestaurant1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getRestaurantYesterayVotes() throws Exception{
        perform(MockMvcRequestBuilders.get(REST_URL+"/1/votes?voteDate="+ LocalDate.now().minusDays(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTETO_MATCHER.contentJson(yesterdayVotesToForRestaurant1));
    }*/

}