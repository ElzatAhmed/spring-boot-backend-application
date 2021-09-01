package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceType;

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
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private UUID itemCode;

    @Column(
            name = "type",
            nullable = false,
            updatable = false
    )
    private ResourceType type;

    @Column(
            name = "count",
            nullable = false,
            updatable = false
    )
    private int count;

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



    @Column(
            name = "completed",
            nullable = false
    )
    private boolean completed = false;

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


    @ManyToOne
    @JoinColumn(
            name = "resource_id",
            nullable = false,
            updatable = false
    )
    private Resource resource;


    @OneToMany
    private List<Order> orders;


    public static Item getFromBody(Body.ItemInfos infos, Resource resource){
        Item item = new Item();
        item.type = ResourceType.valueOf(infos.getType());
        item.count = infos.getCount();
        item.needs2Pay = infos.isNeeds2Pay();
        item.fee = infos.getFee();
        item.feeUnit = infos.getFeeUnit();
        item.campus = infos.getCampus();
        item.startsAt = infos.getStartsAt();
        item.endsAt = infos.getEndsAt();
        item.resource = resource;
        return item;
    }

    public static Body.ItemInfos toBody(Item item){
        Body.ItemInfos infos = new Body.ItemInfos();
        infos.setCode(item.getItemCode().toString());
        infos.setType(item.getType().toString());
        infos.setCount(item.getCount());
        infos.setNeeds2Pay(item.isNeeds2Pay());
        infos.setFee(item.getFee());
        infos.setFeeUnit(item.getFeeUnit());
        infos.setCampus(item.getCampus());
        infos.setStartsAt(item.getStartsAt());
        infos.setEndsAt(item.getEndsAt());
        return infos;
    }

}
