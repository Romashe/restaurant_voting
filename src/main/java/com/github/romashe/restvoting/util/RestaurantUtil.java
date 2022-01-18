package com.github.romashe.restvoting.util;

import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.to.RestaurantTo;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class RestaurantUtil {

    public static List<RestaurantTo> convertListTo(List<Restaurant> restaurants, LocalDate requestedDate) {
        return restaurants.stream()
                .map(rest -> createTo(rest, rest.getVotes().stream().filter(v -> v.getVoteDate().isEqual(requestedDate)).count(), requestedDate))
                .sorted(RestaurantTo::compareTo)
                .toList();
    }

    private static RestaurantTo createTo(Restaurant restaurant, long voteCount, LocalDate requestedDate) {
        return new RestaurantTo(restaurant.id(), restaurant.getName(), restaurant.getMenuItems(), requestedDate, voteCount);
    }
}
