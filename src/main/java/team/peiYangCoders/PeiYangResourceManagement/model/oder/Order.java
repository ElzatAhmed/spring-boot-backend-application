package team.peiYangCoders.PeiYangResourceManagement.model.oder;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    /*
    * order id:
    *   primary key of order;
    *   generated UUID;
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "id"
    )
    private UUID id;

    /*
    * order type:
    *   there are two different types of order:
    *   1) lease    2) sale
    *   every order has to be of one of the these types;
    *   default to lease.
    * */
    @Enumerated(EnumType.STRING)
    @NotBlank
    @Column(
            nullable = false,
            name = "type",
            columnDefinition = "VARCHAR(10)",
            length = 10
    )
    private OrderType type = OrderType.lease;

    /*
    * the initiator of the order(seller)
    * */
    @NotNull
    @NotBlank
    @ManyToOne
    @JoinColumn(
            name = "initiator_phone",
            foreignKey = @ForeignKey(name = "order_initiator_fk")
    )
    private User initiator;

    /*
    * the receiver of the order(buyer)
    * */
    @NotNull
    @NotBlank
    @ManyToOne
    @JoinColumn(
            name = "recipient_phone",
            foreignKey = @ForeignKey(name = "order_recipient_fk")
    )
    private User recipient;

    /*
    * the resource in this order
    * */
    @NotNull
    @NotBlank
    @ManyToOne
    @JoinColumn(
            name = "resource_id",
            foreignKey = @ForeignKey(name = "order_resource_fk")
    )
    private Resource resource;

    /*
    * initiated time of this order:
    *   if the order type is lease, stands for the start time of the lease.
    * */
    @NotBlank
    @Column(
            nullable = false,
            updatable = false,
            name = "opened_time",
            columnDefinition = "TIMESTAMP"
    )
    @DateTimeFormat
    private String openedTime;

    /*
    * determine if the order is closed;
    * default to false.
    * */
    @NotBlank
    @Column(
            nullable = false,
            updatable = false,
            name = "closed",
            columnDefinition = "BOOLEAN"
    )
    private boolean closed = false;

    /*
    * the closed time of the order:
    *   if the order type is lease, stands for the time when the lease is over;
    *   if the order type is sale, stands for the time when the exchange is over.
    * */
    @DateTimeFormat
    @Column(
            updatable = false,
            name = "closed_time",
            columnDefinition = "TIMESTAMP"
    )
    private String closedTime;
}
