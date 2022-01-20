package com.github.romashe.restvoting.repository;

import com.github.romashe.restvoting.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId ORDER BY v.voteDate DESC")
    List<Vote> findAllByUserId(int userId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId and v.voteDate = :voteDate")
    Optional<Vote> findByUserIdAndVoteDate(int userId, LocalDate voteDate);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId and v.id = :voteId")
    Optional<Vote> findByUserIdAndVoteId(int userId, int voteId);

    @Query("SELECT v.restaurantId as restaurantId, count(v.id) as voteCount FROM Vote v WHERE v.voteDate = CURRENT_DATE GROUP BY v.restaurantId")
    List<RestaurantVoteCnt> findAllTodayVotesAsList();

    default Map<Integer, Long> findAllTodayVotes() {
        return findAllTodayVotesAsList().stream().collect(Collectors.toMap(RestaurantVoteCnt::getRestaurantId, RestaurantVoteCnt::getVoteCount));
    }

    interface RestaurantVoteCnt {
        Integer getRestaurantId();

        Long getVoteCount();
    }

}
