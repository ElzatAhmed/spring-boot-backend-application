package team.peiYangCoders.PeiYangResourceManagement.model.order;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.OrderConfig;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

/*
* Order class:
*   order class includes the User class and the Resource class;
*   every order has to be initiated by a initiator(User), received
*   by a recipient(User), it has to include one resource(Resource);
*   order records the time of initiation, the time of closing.
* */

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Entity(name = "Order")
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            name = "code",
            nullable = false,
            updatable = false,
            columnDefinition = "UUID"
    )
    private UUID code;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "getter_id",
            foreignKey = @ForeignKey(name = "order_getter_fk"),
            nullable = false
    )
    private User getter;


    @Column(
            name = "comments",
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String comments;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "owner_id",
            foreignKey = @ForeignKey(name = "order_owner_fk"),
            nullable = false
    )
    private User owner;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "item_id",
            foreignKey = @ForeignKey(name = "order_item_fk"),
            nullable = false,
            updatable = false
    )
    private Item item;


    @Column(
            name = "count",
            nullable = false
    )
    private int count;



    @NotBlank
    @Column(
            nullable = false,
            updatable = false,
            name = "opened_time"
    )
    @DateTimeFormat
    private LocalDateTime openedTime;


    @DateTimeFormat
    @Column(
            updatable = false,
            name = "closed_time"
    )
    private LocalDateTime closedTime;


    @Column(
            name = "accepted_rejected_time"
    )
    private LocalDateTime acceptedOrRejectedTime;


    @Column(
            name = "canceled_time"
    )
    private LocalDateTime canceledTime;


    @Column(
            name = "accept_expires_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime acceptingOrRejectingExpiresAt;


    @Column(
            name = "completion_expires_at"
    )
    private LocalDateTime completionExpiresAt;


    @Column(
            name = "accepted",
            nullable = false
    )
    private boolean accepted = false;

    @Column(
            name = "acceptedOrRejected",
            nullable = false
    )
    private boolean acceptedOrRejected = false;

    @Column(
            name = "canceled",
            nullable = false
    )
    private boolean canceled = false;


    @Column(
            name = "expired",
            nullable = false
    )
    private boolean expired = false;

    @Column(
            name = "completed",
            nullable = false
    )
    private boolean completed = false;

    @Column(
            name = "uncompleted",
            nullable = false
    )
    private boolean uncompleted = false;


    @Column(
            nullable = false,
            name = "completed_by_owner"
    )
    private boolean completedByOwner = false;


    @Column(
            name = "completed_by_getter",
            nullable = false
    )
    private boolean completedByGetter = false;

    @Column(
            name = "uncompleted_by_owner",
            nullable = false
    )
    private boolean uncompletedByOwner = false;

    @Column(
            name = "uncompleted_by_getter",
            nullable = false
    )
    private boolean unCompletedByGetter = false;



    public static Order newOrderFromBody(Body.OrderInfos infos, User getter, Item item) {
        OrderConfig config = new OrderConfig();
        User owner = item.getResource().getOwner();
        Order order = new Order();
        order.setCount(infos.getCount());
        order.setComments(infos.getComments());
        order.setGetter(getter);
        order.setOwner(owner);
        order.setItem(item);
        order.setOpenedTime(LocalDateTime.now());
        order.setClosedTime(null);
        order.setAcceptedOrRejectedTime(null);
        order.setCanceledTime(null);
        order.setAcceptingOrRejectingExpiresAt(order.getOpenedTime().plusHours(config.getAcceptingValidTime()));
        order.setCompletionExpiresAt(null);
        order.setAccepted(false);
        order.setCompletedByOwner(false);
        order.setCompletedByGetter(false);
        order.setCanceled(false);
        order.setExpired(false);
        order.setAcceptedOrRejected(infos.isAcceptedOrRejected());
        return order;
    }

    public static Body.OrderInfos toBody(Order order){
        Body.OrderInfos infos = new Body.OrderInfos();
        infos.setCount(order.getCount());
        infos.setComments(order.getComments());
        infos.setGetterPhone(order.getGetter().getPhone());
        infos.setCode(order.getCode().toString());
        infos.setOpenedTime(order.getOpenedTime());
        infos.setClosedTime(order.getClosedTime());
        infos.setAcceptedTime(order.getAcceptedOrRejectedTime());
        infos.setCanceledTime(order.getCanceledTime());
        infos.setAcceptingExpiresAt(order.getAcceptingOrRejectingExpiresAt());
        infos.setClosingExpiresAt(order.getCompletionExpiresAt());
        infos.setAccepted(order.isAccepted());
        infos.setClosedByOwner(order.isCompletedByOwner());
        infos.setClosedByGetter(order.isCompletedByGetter());
        infos.setCanceled(order.isCanceled());
        infos.setExpired(order.isExpired());
        infos.setAcceptedOrRejected(order.isAcceptedOrRejected());
        return infos;
    }
}
