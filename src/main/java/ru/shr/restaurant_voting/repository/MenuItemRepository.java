package ru.shr.restaurant_voting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.shr.restaurant_voting.model.MenuItem;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@Tag(name = "Restaurant Controller")
public interface MenuItemRepository extends BaseRepository<MenuItem>{

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :id")
    List<MenuItem> findByRestaurantId(int id);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :id and m.itemDate = :itemDate")
    List<MenuItem> findByRestaurantIdFilteredByItemDate(int id, LocalDate itemDate);
}
