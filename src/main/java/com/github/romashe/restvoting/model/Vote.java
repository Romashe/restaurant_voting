package com.github.romashe.restvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vote",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "vote_date"}),
        indexes = {
                @Index(name = "voteDate_idx", columnList = "vote_date"),
                @Index(name = "RestIdVoteDate_idx", columnList = "restaurant_id, vote_date")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private LocalDate voteDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "restaurant_id", updatable = false, insertable = false)
    private int restaurantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @NotNull
    private User user;

    public Vote(Integer id, LocalDate voteDate, Restaurant restaurant) {
        super(id);
        this.voteDate = voteDate;
        this.restaurant = restaurant;
    }

    public Vote(LocalDate voteDate, Restaurant restaurant, User user) {
        this.voteDate = voteDate;
        this.restaurant = restaurant;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", voteDate=" + voteDate +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
