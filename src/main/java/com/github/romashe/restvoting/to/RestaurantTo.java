package com.github.romashe.restvoting.to;

import com.github.romashe.restvoting.model.MenuItem;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {
    long voteCount;
    List<MenuItem> menuItems;

    public RestaurantTo(Integer id, String name, List<MenuItem> menuItems, long voteCount) {
        super(id, name);
        this.voteCount = voteCount;
        this.menuItems = menuItems;
    }

    public int compareTo(RestaurantTo restaurantTo) {
        return Long.compare(restaurantTo.getVoteCount(), this.getVoteCount());
    }
}