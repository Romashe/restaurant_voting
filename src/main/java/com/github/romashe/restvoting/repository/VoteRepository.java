package com.github.romashe.restvoting.repository;

import com.github.romashe.restvoting.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.restaurant.id = :restId and v.voteDate = :voteDate")
    List<Vote> findVotesByDateAndRestId(int restId, LocalDate voteDate);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id = :id ORDER BY v.voteDate DESC")
    List<Vote> findAllByUserId(int id);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id = :id and v.voteDate = :voteDate")
    Vote findByUserIdAndVoteDate(int id, LocalDate voteDate);
}
