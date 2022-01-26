package com.github.romashe.restvoting.restaurant;

import com.github.romashe.restvoting.MatcherFactory;
import com.github.romashe.restvoting.model.Restaurant;

import java.util.List;

import static com.github.romashe.restvoting.menuitem.MenuItemTestData.*;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANTS_MATCHER =
            MatcherFactory.usingEqualsComparator(Restaurant.class);
    public static final Restaurant restaurant1 = new Restaurant(2, "Metropol");
    public static final Restaurant restaurant2 = new Restaurant(1, "Sahalin");
    public static final Restaurant restaurant3 = new Restaurant(3, "u Ashota");
    public static final Restaurant notFoundRestaurant = new Restaurant(5, "NotFound");
    public static final List<Restaurant> restaurantsAllList = List.of(restaurant1, restaurant2, restaurant3);

    public static List<Restaurant> getRestaurantsWithMenuToday() {
        restaurant1.setMenuItems(List.of(rest1MenuItemToday1, rest1MenuItemToday2));
        restaurant2.setMenuItems(List.of(rest2MenuItemToday1, rest2MenuItemToday2));
        restaurant3.setMenuItems(List.of(rest3MenuItemToday1, rest3MenuItemToday2));
        return List.of(restaurant1, restaurant2, restaurant3);
    }
}
