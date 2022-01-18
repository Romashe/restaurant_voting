package com.github.romashe.restvoting.restaurant;

import com.github.romashe.restvoting.MatcherFactory;
import com.github.romashe.restvoting.model.MenuItem;
import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.model.Vote;
import com.github.romashe.restvoting.to.RestaurantTo;
import com.github.romashe.restvoting.to.VoteTo;
import com.github.romashe.restvoting.util.RestaurantUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANTS_TO_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantTo.class);
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANTS_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);
    public static final MatcherFactory.Matcher<VoteTo> VOTETO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final Restaurant restaurant1 = new Restaurant(1, "Sahalin");
    public static final Restaurant restaurant2 = new Restaurant(2, "Metropol");
    public static final Restaurant restaurant3 = new Restaurant(3, "u Ashota");

    public static final MenuItem rest1MenuItem1 = new MenuItem(1, "Sibas", BigDecimal.valueOf(100.25), LocalDate.now());
    public static final MenuItem rest1MenuItem2 = new MenuItem(3, "Midii", BigDecimal.valueOf(45.25), LocalDate.now());
    public static final MenuItem rest2MenuItem1 = new MenuItem(5, "Olivie", BigDecimal.valueOf(1000.05), LocalDate.now());
    public static final MenuItem rest2MenuItem2 = new MenuItem(7, "Sup", BigDecimal.valueOf(67.05), LocalDate.now());
    public static final MenuItem rest3MenuItem1 = new MenuItem(9, "Lulya Kebab", BigDecimal.valueOf(38.00), LocalDate.now());
    public static final MenuItem rest3MenuItem2 = new MenuItem(11, "Shaverma", BigDecimal.valueOf(10.05), LocalDate.now());

    public static final Vote rest1Vote1 = new Vote(2, LocalDate.now(), restaurant1);
    public static final Set<Vote> votesRest1 = Set.of(rest1Vote1);

    public static final Set<MenuItem> menuRest1 = Set.of(rest1MenuItem1, rest1MenuItem2);
    public static final Set<MenuItem> menuRest2 = Set.of(rest2MenuItem1, rest2MenuItem2);
    public static final Set<MenuItem> menuRest3 = Set.of(rest3MenuItem1, rest3MenuItem2);

    public static final List<Restaurant> restaurant1List = List.of(restaurant1);
    public static final List<VoteTo> todayVotesToForRestaurant1 = List.of(new VoteTo(2, "Admin", LocalDate.now()));
    public static final List<VoteTo> yesterdayVotesToForRestaurant1 = List.of(new VoteTo(1, "User", LocalDate.now().minusDays(1)));

    public static List<RestaurantTo> getRestaurantToForToday() {
        restaurant1.setMenuItems(menuRest1);
        restaurant1.setVotes(votesRest1);
        restaurant2.setMenuItems(menuRest2);
        restaurant3.setMenuItems(menuRest3);
        return RestaurantUtil.convertListTo(List.of(restaurant1, restaurant2, restaurant3), LocalDate.now());
    }
}
