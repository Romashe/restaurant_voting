package ru.shr.restaurant_voting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.shr.restaurant_voting.HasId;

import java.time.LocalDate;
@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo implements HasId {
    String userName;
    LocalDate voteDate;

    public VoteTo(Integer id, String userName, LocalDate voteDate) {
        super(id);
        this.userName = userName;
        this.voteDate = voteDate;
    }
}
