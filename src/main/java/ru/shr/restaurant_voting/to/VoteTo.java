package ru.shr.restaurant_voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.shr.restaurant_voting.HasId;
import ru.shr.restaurant_voting.util.validation.NoHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo implements HasId {

    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String userName;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate voteDate;

    public VoteTo(Integer id, String userName, LocalDate voteDate) {
        super(id);
        this.userName = userName;
        this.voteDate = voteDate;
    }
}
