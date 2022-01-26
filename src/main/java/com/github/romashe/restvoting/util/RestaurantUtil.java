package com.github.romashe.restvoting.util;

import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.to.RestaurantTo;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class RestaurantUtil {
    public static List<RestaurantTo> convertListTo(List<Restaurant> restaurants, Map<Integer, Long> restaurantVoteCnt) {
        return restaurants.stream()
                .map(rest -> createTo(rest, restaurantVoteCnt.getOrDefault(rest.getId(), 0L)))
                .sorted(RestaurantTo::compareTo)
                .toList();
    }

    private static RestaurantTo createTo(Restaurant restaurant, long voteCount) {
        return new RestaurantTo(restaurant.id(), restaurant.getName(), restaurant.getMenuItems(), voteCount);
    }
}
