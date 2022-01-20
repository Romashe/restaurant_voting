package com.github.romashe.restvoting.repository;

import com.github.romashe.restvoting.model.MenuItem;
import com.github.romashe.restvoting.util.validation.ValidationUtil;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :id ORDER BY m.itemDate desc, m.name asc")
    List<MenuItem> findByRestaurantId(int id);

    @Query("SELECT m FROM MenuItem m WHERE m.id = :menuItemId and m.restaurant.id = :id")
    Optional<MenuItem> findByRestaurantIdAndItemId(int id, int menuItemId);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :id and m.itemDate = :itemDate ORDER BY m.name asc")
    List<MenuItem> findByRestaurantIdFilteredByItemDate(int id, LocalDate itemDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem m WHERE m.id = :menuItemId and m.restaurant.id = :id")
    int deleteExistedMenuItemByRestIdAndItemId(int id, int menuItemId);

    default void deleteExistedMenuItemByRestIdAndItemIdWithCheck(int id, int menuItemId) {
        ValidationUtil.checkModificationWithChild(deleteExistedMenuItemByRestIdAndItemId(id, menuItemId), id, menuItemId);
    }
}
