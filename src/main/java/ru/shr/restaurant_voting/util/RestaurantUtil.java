package ru.shr.restaurant_voting.util;

import lombok.experimental.UtilityClass;
import ru.shr.restaurant_voting.model.Restaurant;
import ru.shr.restaurant_voting.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class RestaurantUtil {

    public static List<RestaurantTo> convertListTo(List<Restaurant> restaurants, LocalDate requestedDate){
        return restaurants.stream()
                .map(rest -> createTo(rest, rest.getVotes().size(), requestedDate))
                .sorted(RestaurantTo::compareTo)
                .toList();
    }

    private static RestaurantTo createTo (Restaurant restaurant, long voteCount, LocalDate requestedDate){
        return new RestaurantTo(restaurant.id(), restaurant.getName(), restaurant.getMenuItems(), requestedDate, voteCount);
    }
}
