package team.peiYangCoders.PeiYangResourceManagement.model.user;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.URL;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.UserTag;

import javax.persistence.*;
import java.util.ArrayList;
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
@Entity(name = "User")
@Data
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_phone_unique", columnNames = "phone_num"),
                @UniqueConstraint(name = "user_qq_unique", columnNames = "qq_id"),
                @UniqueConstraint(name = "user_wechat_unique", columnNames = "wechat_id")
        }
)
public class User {

    @Id
    @SequenceGenerator(
            name = "user_seq_generator",
            sequenceName = "user_seq",
            initialValue = 10000,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "INTEGER"
    )
    private Long id;

    /*
    * phone number:
    *   cannot be null obviously;
    *   one of the user identifications;
    *   user can change the password only using the right phone number.
    * */
    @Column(
            name = "phone_num",
            updatable = false,
            nullable = false,
            columnDefinition = "VARCHAR(11)",
            length = 11
    )
    private String phone;

    /*
    *
    * */
    @Column(
            name = "qq_id",
            columnDefinition = "VARCHAR(50)"
    )
    private String qqId;

    /*
    * */
    @Column(
            name = "wechat_id",
            columnDefinition = "VARCHAR(50)"
    )
    private String wechatId;

    /*
    * user name:
    *   unique attribute;
    *   cannot be null;
    *   every user has a user name;
    *   user name is another one of the user identifications;
    *   max length of a user name is 30 characters.
    * */
    @Column(
            name = "user_name",
            nullable = false,
            columnDefinition = "VARCHAR(50)",
            length = 50
    )
    private String name;

    /*
    * password:
    *   essential for user login;
    *   a user can identify self with both the user name and the
    * phone number but password is essential for either the situation;
    *   cannot be null;
    *   max length is 16 characters.
    * */
    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "VARCHAR(16)",
            length = 16
    )
    private String password;

    /*
    * avatar url:
    *   each user can have one avatar image.
    * */
    @URL
    @Column(
            name = "avatar_url",
            columnDefinition = "TEXT"
    )
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "user_tag",
            nullable = false,
            columnDefinition = "VARCHAR(20)"
    )
    private UserTag tag = UserTag.ordinary;

    /*
    * resources:
    *   as i said, user can be both the seller and the buyer,so
    * each user can have none or many resources;
    *   resources stored in the resources table mapped by user id(phone).
    * */
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(
            mappedBy = "owner",
            orphanRemoval = true
    )
    private List<Resource> resources;

    /*
    * initiated_orders:
    *   user can initiate an order as a seller;
    *   orders stored in the orders table mapped by user ids and resource id;
    * */
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(
            mappedBy = "initiator",
            orphanRemoval = true
    )
    private List<Order> initiated_orders = new ArrayList<>();

    /*
     * received_orders:
     *   user can receive an order as a buyer;
     *   orders stored in the orders table mapped by user ids and resource id;
     * */
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(
            mappedBy = "recipient",
            orphanRemoval = true
    )
    private List<Order> received_orders = new ArrayList<>();

    /*
    *
    * */
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true
    )
    private List<UserProfile> profiles = new ArrayList<>();


    public boolean isAdmin(){
        return tag.equals(UserTag.admin);
    }

    public User(UserInfo info){
        this.phone = info.getPhone();
        this.name = info.getName();
        this.avatarUrl = info.getAvatarUrl();
        this.password = info.getPassword();
        this.qqId = info.getQqId();
        this.wechatId = info.getWechatId();
        this.tag = UserTag.valueOf(info.getTag());
    }
}
