package team.peiYangCoders.PeiYangResourceManagement.model.order;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
            name = "order_code",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR"
    )
    private String orderCode;


    @Column(
            name = "getter_phone",
            nullable = false,
            updatable = false
    )
    private String getterPhone;


    @Column(
            name = "comments",
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String comments;


    @Column(
            name = "owner_phone",
            nullable = false,
            updatable = false
    )
    private String ownerPhone;


    @Column(
            name = "item_code",
            nullable = false,
            updatable = false
    )
    private String itemCode;


    @Column(
            name = "count",
            nullable = false
    )
    private int count;



    @Column(
            nullable = false,
            updatable = false,
            name = "opened_time"
    )
    private LocalDateTime openedTime;


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
            nullable = false,
            name = "completion_expires_at"
    )
    private LocalDateTime completionExpiresAt;


    @Column(
            name = "accepted",
            nullable = false
    )
    private boolean accepted = false;

    @Column(
            name = "accepted_or_rejected",
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


    private LocalDateTime OwnerCompletedTime = null;


    @Column(
            name = "completed_by_getter",
            nullable = false
    )
    private boolean completedByGetter = false;


    private LocalDateTime GetterCompletedTime = null;


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
}
