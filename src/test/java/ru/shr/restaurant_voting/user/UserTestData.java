package ru.shr.restaurant_voting.user;



import ru.shr.restaurant_voting.MatcherFactory;
import ru.shr.restaurant_voting.model.Restaurant;
import ru.shr.restaurant_voting.model.Role;
import ru.shr.restaurant_voting.model.User;
import ru.shr.restaurant_voting.model.Vote;
import ru.shr.restaurant_voting.util.JsonUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password", "votes");
    public static final MatcherFactory.Matcher<Vote> ADMIN_VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int NOT_FOUND = 100;
    public static final String USER_MAIL = "user@mail.com";
    public static final String ADMIN_MAIL = "admin@mail.com";

    public static final User user = new User(USER_ID, "User", USER_MAIL, "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);
    public static final Restaurant restaurant1 = new Restaurant(1, "Sahalin");
    public static final Restaurant restaurant2 = new Restaurant(2, "Metropol");
    public static final List<Vote> adminTodayVotes = List.of(new Vote(3, LocalDate.now(), restaurant1), new Vote(4, LocalDate.now(), restaurant2));
    public static final List<Vote> adminYesterdayVotes = List.of(new Vote(5, LocalDate.now().minusDays(1), restaurant2));


    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "UpdatedName", USER_MAIL, "newPass", false, new Date(), Collections.singleton(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
