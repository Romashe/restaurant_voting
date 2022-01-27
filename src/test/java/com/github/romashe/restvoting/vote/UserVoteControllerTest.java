package com.github.romashe.restvoting.vote;

import com.github.romashe.restvoting.AbstractControllerTest;
import com.github.romashe.restvoting.to.VoteTo;
import com.github.romashe.restvoting.util.JsonUtil;
import com.github.romashe.restvoting.web.vote.UserVoteController;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.github.romashe.restvoting.user.UserTestData.ADMIN_MAIL;
import static com.github.romashe.restvoting.user.UserTestData.USER_MAIL;
import static com.github.romashe.restvoting.util.validation.ValidationUtil.DEADLINE_TIME;
import static com.github.romashe.restvoting.vote.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = UserVoteController.REST_URL + '/';

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllUserVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVotes));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getUserVoteByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date?voteDate=" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVoteToday));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFoundUserVoteByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date?voteDate=" + LocalDate.now().minusDays(100)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getUserVoteById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + userVoteToday.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVoteToday));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFoundUserVoteById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + notFoundVote.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createVote() throws Exception {
        VoteTo newVoteTo = new VoteTo(null, 1);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVoteTo)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createConflictVote() throws Exception {
        VoteTo newVoteTo = new VoteTo(null, userVoteToday.getRestaurant().getId());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVoteTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateVote() throws Exception {
        VoteTo updVoteTo = new VoteTo(null, userVoteYesterday.getRestaurant().getId());
        ResultActions action = perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updVoteTo)))
                .andDo(print());

        if (LocalDateTime.now().isAfter(LocalDateTime.of(LocalDate.now(), DEADLINE_TIME))) {
            action.andExpect(status().isUnprocessableEntity());
        } else {
            action.andExpect(status().isNoContent());
        }
    }
}