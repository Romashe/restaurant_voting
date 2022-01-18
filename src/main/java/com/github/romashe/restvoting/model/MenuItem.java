package com.github.romashe.restvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"item_date", "name", "restaurant_id"}),
        indexes = {
                @Index(name = "itemDate_idx", columnList = "item_date"),
                @Index(name = "RestIdItemDate_idx", columnList = "restaurant_id, item_date")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    private BigDecimal price;

    @Column(name = "item_date", nullable = false)
    @NotNull
    private LocalDate itemDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    public MenuItem(Integer id, String name, BigDecimal price, LocalDate itemDate) {
        super(id, name);
        this.price = price;
        this.itemDate = itemDate;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", price=" + price +
                ", itemDate=" + itemDate +
                ", name='" + name + '\'' +
                '}';
    }
}
