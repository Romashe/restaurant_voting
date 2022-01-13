package ru.shr.restaurant_voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.shr.restaurant_voting.HasId;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo implements HasId {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String userName;

    @NotNull
    LocalDate voteDate;

    public VoteTo(Integer id, String userName, LocalDate voteDate) {
        super(id);
        this.userName = userName;
        this.voteDate = voteDate;
    }
}
