package ru.shr.restaurant_voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.shr.restaurant_voting.HasId;
import ru.shr.restaurant_voting.model.MenuItem;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo implements HasId {

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate requestedDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long voteCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Set<MenuItem> menuItems;

    public RestaurantTo(Integer id, String name, Set<MenuItem> menuItems, LocalDate requestedDate, long voteCount) {
        super(id, name);
        this.requestedDate = requestedDate;
        this.voteCount = voteCount;
        this.menuItems = menuItems;
    }

    public int compareTo(RestaurantTo restaurantTo){
        return Long.compare(restaurantTo.getVoteCount(), this.getVoteCount());
    }
}
