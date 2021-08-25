package team.peiYangCoders.PeiYangResourceManagement.model.user;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import team.peiYangCoders.PeiYangResourceManagement.model.oder.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/*
* User class:
*   everyone is a user in this application,
*   every user can be both the seller and buyer,
*   phone number is the primary key of the user,
*   but we can identify a user with both the phone
*   number and the user name.
* */

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Data
@Table(name = "users")
public class User {

    /*
    * phone number:
    *   primary key;
    *   cannot be null obviously;
    *   one of the user identifications;
    *   user can change the password only using the right phone number.
    * */
    @Id
    @NotNull
    @NotBlank
    @Column(nullable = false, updatable = false)
    private String phone;

    /*
    * user name:
    *   unique attribute;
    *   cannot be null;
    *   every user has a user name;
    *   user name is another one of the user identifications;
    *   max length of a user name is 30 characters.
    * */
    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true, length = 30)
    private String name;

    /*
    * password:
    *   essential for user login;
    *   a user can identify self with both the user name and the
    * phone number but password is essential for either the situation;
    *   cannot be null;
    *   max length is 16 characters.
    * */
    @NotNull
    @NotBlank
    @Column(nullable = false, length = 16)
    private String password;

    /*
    * avatar url:
    *   each user can have one avatar image.
    * */
    @URL
    private String avatarUrl;

    /*
    * resources:
    *   as i said, user can be both the seller and the buyer,so
    * each user can have none or many resources;
    *   resources stored in the resources table mapped by user id(phone).
    * */
    @OneToMany(mappedBy = "user")
    private List<Resource> resources;

    /*
    * initiated_orders:
    *   user can initiate an order as a seller;
    *   orders stored in the orders table mapped by user ids and resource id;
    * */
    @OneToMany(mappedBy = "initiator")
    private List<Order> initiated_orders;

    /*
     * received_orders:
     *   user can receive an order as a buyer;
     *   orders stored in the orders table mapped by user ids and resource id;
     * */
    @OneToMany(mappedBy = "recipient")
    private List<Order> received_orders;
}
