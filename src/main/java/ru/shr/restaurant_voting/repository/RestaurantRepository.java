package ru.shr.restaurant_voting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import ru.shr.restaurant_voting.model.Restaurant;

@Transactional(readOnly = true)
@Tag(name = "Restaurant Controller")
public interface RestaurantRepository extends BaseRepository<Restaurant>{

}
