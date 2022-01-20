package com.github.romashe.restvoting.restaurant;

import com.github.romashe.restvoting.MatcherFactory;
import com.github.romashe.restvoting.model.MenuItem;
import com.github.romashe.restvoting.model.Restaurant;
import com.github.romashe.restvoting.model.Vote;
import com.github.romashe.restvoting.to.RestaurantTo;
import com.github.romashe.restvoting.to.VoteTo;

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

    public static final MenuItem rest1MenuItem1 = new MenuItem(1, "Sibas", 10025, LocalDate.now());
    public static final MenuItem rest1MenuItem2 = new MenuItem(3, "Midii", 4525, LocalDate.now());
    public static final MenuItem rest2MenuItem1 = new MenuItem(5, "Olivie", 100005, LocalDate.now());
    public static final MenuItem rest2MenuItem2 = new MenuItem(7, "Sup", 6705, LocalDate.now());
    public static final MenuItem rest3MenuItem1 = new MenuItem(9, "Lulya Kebab", 3800, LocalDate.now());
    public static final MenuItem rest3MenuItem2 = new MenuItem(11, "Shaverma", 1005, LocalDate.now());

    public static final Vote rest1Vote1 = new Vote(2, LocalDate.now(), restaurant1);
    public static final Set<Vote> votesRest1 = Set.of(rest1Vote1);

    public static final Set<MenuItem> menuRest1 = Set.of(rest1MenuItem1, rest1MenuItem2);
    public static final Set<MenuItem> menuRest2 = Set.of(rest2MenuItem1, rest2MenuItem2);
    public static final Set<MenuItem> menuRest3 = Set.of(rest3MenuItem1, rest3MenuItem2);

    public static final List<Restaurant> restaurant1List = List.of(restaurant1);
    public static final List<VoteTo> todayVotesToForRestaurant1 = List.of(new VoteTo(2, restaurant1.id()));
    public static final List<VoteTo> yesterdayVotesToForRestaurant1 = List.of(new VoteTo(1, restaurant1.id()));

   /* public static List<RestaurantTo> getRestaurantToForToday() {
        restaurant1.setMenuItems(menuRest1);
        restaurant1.setVotes(votesRest1);
        restaurant2.setMenuItems(menuRest2);
        restaurant3.setMenuItems(menuRest3);
        return RestaurantUtil.convertListTo(List.of(restaurant1, restaurant2, restaurant3));
    }*/
}
