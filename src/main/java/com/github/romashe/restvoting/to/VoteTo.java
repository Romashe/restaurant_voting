package com.github.romashe.restvoting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    @NotNull
    int restaurantId;

    public VoteTo(Integer id, int restaurantId) {
        super(id);
        this.restaurantId = restaurantId;
    }
}
