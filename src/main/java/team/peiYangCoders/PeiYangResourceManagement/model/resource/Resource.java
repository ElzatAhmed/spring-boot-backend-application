package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.URL;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/*
* Resource class:
*   resource can be anything but it has belong to a user;
*   so there is a many to one relationship between resources and users;
*   a resource can appear in different orders;
*   so there is a one to many relationship between resources and orders.
* */

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "Resource")
@Data
@Table(
        name = "resources"
)
public class Resource {

    /**
    *
    * */
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

    /**
    * resource name:
    *   any resource has to have a name;
    *   provided by user.
    * */
    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    /**
     * verified by administrator
     * */
    @Column(
            name = "verified",
            nullable = false
    )
    private boolean verified = false;

    /*
    * there are two different kinds of resource:
    *   1) needs to pay;
    *   2) does`nt;
    *   3) default to false.
    * */
    @Column(
            name = "needs2pay",
            columnDefinition = "BOOLEAN",
            nullable = false
    )
    private boolean needsToPay = false;


    /*
    * the price or fee of the resource:
    *   1) if does`nt need to pay equals to 0;
    *   2) else equals to the actual price.
    * */
    @Column(
            name = "fee",
            nullable = false,
            columnDefinition = "INTEGER"
    )
    private int fee = 0;

    /*
    * resource description:
    *   users can describe the resources;
    *   but don`t have to.
    * */
    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    /*
    * */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "tag",
            columnDefinition = "TEXT"
    )
    private ResourceTag tag;

    /*
    * */
    @Column(
            name = "on_time",
            nullable = false,
            updatable = false
    )
    private LocalDateTime onTime;

    /*
    * resource image url:
    *   users can upload one image for each resource;
    *   but don`t have to.
    * */
    @URL
    @Column(
            name = "image_url",
            columnDefinition = "TEXT"
    )
    private String imageUrl;

    /*
    * user id:
    *   every resource has to belong to a user;
    *   user stored in the users table;
    *   so there is foreign key user_id referenced user.id.
    * */
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "owner_id",
            foreignKey = @ForeignKey(name = "resource_user_fk")
    )
    private User owner;

    /*
    * orders:
    *   a resource can appear in different orders;
    *   orders stored in the orders table mapped by user ids and resource id.
    * */
    @OneToMany(
            mappedBy = "resource",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<Order> orders;

    public Resource(Body.NewResource info, User owner, LocalDateTime onTime){
        this.name = info.getName();
        this.description = info.getDescription();
        this.fee = info.getFee();
        this.imageUrl = info.getImageUrl();
        this.needsToPay = info.isNeedsToPay();
        this.onTime = onTime;
        this.tag = ResourceTag.valueOf(info.getTag());
        this.owner = owner;
    }
}
