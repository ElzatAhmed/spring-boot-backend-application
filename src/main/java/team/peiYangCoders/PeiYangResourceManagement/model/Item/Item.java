package team.peiYangCoders.PeiYangResourceManagement.model.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ItemType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Item")
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "item_code",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private String itemCode;

    @Column(
            name = "item_type",
            nullable = false,
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String itemType;

    @Column(
            name = "count",
            nullable = false,
            updatable = false
    )
    private int count;

    @Column(
            name = "on_time",
            nullable = false,
            updatable = false
    )
    private LocalDateTime onTime;

    @Column(
            name = "needs2Pay",
            nullable = false,
            updatable = false
    )
    private boolean needs2Pay;

    @Column(
            name = "fee",
            updatable = false
    )
    private int fee;

    @Column(
            name = "fee_unit",
            updatable = false
    )
    private String feeUnit;

    @Column(
            name = "starts_at",
            updatable = false
    )
    private LocalDateTime startsAt;

    @Column(
            name = "ends_at",
            updatable = false
    )
    private LocalDateTime endsAt;

    @Column(
            name = "ordered",
            nullable = false
    )
    private boolean ordered = false;


    /**
     * 0: old campus
     * 1: new campus
     * */
    @Column(
            name = "campus",
            updatable = false,
            nullable = false
    )
    private int campus;


    @Column(
            name = "resource_code"
    )
    private String resourceCode;


}
