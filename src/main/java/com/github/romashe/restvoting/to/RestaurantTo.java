package com.github.romashe.restvoting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.romashe.restvoting.model.MenuItem;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long voteCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Set<MenuItem> menuItems;

    public RestaurantTo(Integer id, String name, Set<MenuItem> menuItems, long voteCount) {
        super(id, name);
        this.voteCount = voteCount;
        this.menuItems = menuItems;
    }

    public int compareTo(RestaurantTo restaurantTo) {
        return Long.compare(restaurantTo.getVoteCount(), this.getVoteCount());
    }
}
