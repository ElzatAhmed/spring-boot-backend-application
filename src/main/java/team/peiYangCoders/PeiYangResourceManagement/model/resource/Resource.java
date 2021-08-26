package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import team.peiYangCoders.PeiYangResourceManagement.model.oder.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
@Entity
@Data
@Table(name = "resources")
public class Resource {

    /*
    * resource id:
    *   primary key for resource;
    *   just to identify the resource in database;
    *   resource id does`nt concern to the real users;
    *   so it`s generated.
    * */
    @Id
    @SequenceGenerator(
            name = "entity_seq_generator",
            sequenceName = "entity_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "entity_seq_generator",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    /*
    * resource name:
    *   any resource has to have a name;
    *   provided by user.
    * */
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    /*
    * there are two different kinds of resource:
    *   1) needs to pay;
    *   2) does`nt;
    *   3) default to false.
    * */
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private boolean needsToPay = false;


    /*
    * the price or fee of the resource:
    *   1) if does`nt need to pay equals to 0;
    *   2) else equals to the actual price.
    * */
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private int fee = 0;

    /*
    * resource description:
    *   users can describe the resources;
    *   but don`t have to.
    * */
    private String description;

    /*
    * resource image url:
    *   users can upload one image for each resource;
    *   but don`t have to.
    * */
    @URL
    private String imageUrl;

    /*
    * user id:
    *   every resource has to belong to a user;
    *   user stored in the users table;
    *   so there is foreign key user_id referenced user.id.
    * */
    @NotNull
    @NotBlank
    @ManyToOne
    @JoinColumn
    private User user;

    /*
    * orders:
    *   a resource can appear in different orders;
    *   orders stored in the orders table mapped by user ids and resource id.
    * */
    @OneToMany(mappedBy = "resource")
    private List<Order> orders;
}
