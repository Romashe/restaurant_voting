package com.github.romashe.restvoting.menuitem;

import com.github.romashe.restvoting.MatcherFactory;
import com.github.romashe.restvoting.model.MenuItem;

import java.time.LocalDate;
import java.util.List;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENUITEMS_MATCHER = MatcherFactory.usingEqualsComparator(MenuItem.class);

    public static final MenuItem rest2MenuItemToday1 = new MenuItem(3, "Midii", 4525, LocalDate.now());
    public static final MenuItem rest2MenuItemToday2 = new MenuItem(1, "Sibas", 10025, LocalDate.now());
    public static final MenuItem rest2MenuItemYesterday1 = new MenuItem(4, "MidiiOld", 4525, LocalDate.now().minusDays(1));
    public static final MenuItem rest2MenuItemYesterday2 = new MenuItem(2, "SibasOld", 10025, LocalDate.now().minusDays(1));
    public static final MenuItem notFoundMenuItem = new MenuItem(99, "NotFound", 404, LocalDate.now());

    public static final MenuItem rest1MenuItemToday1 = new MenuItem(5, "Olivie", 100005, LocalDate.now());
    public static final MenuItem rest1MenuItemToday2 = new MenuItem(7, "Sup", 6705, LocalDate.now());
    public static final MenuItem rest3MenuItemToday1 = new MenuItem(9, "Lulya Kebab", 3800, LocalDate.now());
    public static final MenuItem rest3MenuItemToday2 = new MenuItem(11, "Shaverma", 1005, LocalDate.now());

    public static final List<MenuItem> rest1AllMenuItemsList = List.of(
            rest2MenuItemToday1, rest2MenuItemToday2, rest2MenuItemYesterday1, rest2MenuItemYesterday2);
    public static final List<MenuItem> rest1YesterdayMenuItemsList = List.of(
            rest2MenuItemYesterday1, rest2MenuItemYesterday2);
    public static final List<MenuItem> rest2TodayMenuItemsList = List.of(rest2MenuItemToday1, rest2MenuItemToday2);
}
