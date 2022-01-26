package com.github.romashe.restvoting.repository;

import com.github.romashe.restvoting.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r inner join r.menuItems m" +
            " WHERE m.itemDate = CURRENT_DATE ORDER BY r.name asc, m.name asc")
    List<Restaurant> getAllWithMenu();
}
