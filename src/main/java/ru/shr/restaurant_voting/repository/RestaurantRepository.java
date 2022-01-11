package ru.shr.restaurant_voting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.shr.restaurant_voting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Tag(name = "Restaurant Controller")
public interface RestaurantRepository extends BaseRepository<Restaurant>{

    @EntityGraph(attributePaths = {"menuItems","votes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r left outer join r.menuItems m left outer join r.votes v WHERE m.itemDate = :menuDate")
    List<Restaurant> getAllWithMenu(LocalDate menuDate);
}
