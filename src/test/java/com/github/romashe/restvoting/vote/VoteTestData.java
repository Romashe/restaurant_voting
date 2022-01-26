package com.github.romashe.restvoting.vote;

import com.github.romashe.restvoting.MatcherFactory;
import com.github.romashe.restvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static com.github.romashe.restvoting.restaurant.RestaurantTestData.restaurant1;
import static com.github.romashe.restvoting.restaurant.RestaurantTestData.restaurant2;
import static com.github.romashe.restvoting.user.UserTestData.user;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final Vote userVoteToday = new Vote(1, LocalDate.now(), restaurant2, user);
    public static final Vote userVoteYesterday = new Vote(3, LocalDate.now().minusDays(1), restaurant1, user);
    public static final Vote notFoundVote = new Vote(99, LocalDate.now().minusDays(1), restaurant1, user);

    public static final List<Vote> userVotes = List.of(userVoteToday, userVoteYesterday);
}
