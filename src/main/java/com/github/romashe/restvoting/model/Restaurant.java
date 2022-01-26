package com.github.romashe.restvoting.model;

import com.fasterxml.jackson.annotation.*;
import com.github.romashe.restvoting.util.JsonViews;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonView(JsonViews.Public.class)
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonView(JsonViews.Internal.class)
    @JsonManagedReference
    private List<MenuItem> menuItems;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<Vote> votes = new HashSet<>();

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}