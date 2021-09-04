package team.peiYangCoders.PeiYangResourceManagement.model.user;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;

import javax.persistence.*;

/**
* User class:
*   everyone is a user in this application,
*   every user can be both the seller and buyer,
*   phone number is the unique key of the user
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
                @UniqueConstraint(name = "user_phone_unique", columnNames = "phone"),
                @UniqueConstraint(name = "user_studentId_unique", columnNames = "student_id")
        }
)
public class User {


    /**
     * user id
     * only back end server can detect
     * primary key
     * generated sequence
     * */
    @Id
    @SequenceGenerator(
            name = "user_seq_generator",
            sequenceName = "user_seq",
            initialValue = 10000,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "user_seq_generator"
    )
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            columnDefinition = "INTEGER"
    )
    private Long id;


    /**
    * phone number:
    *   cannot be null obviously;
    *   one of the user identifications;
    *   user can change the password only using the right phone number.
    * */
    @Column(
            name = "phone",
            updatable = false,
            nullable = false,
            columnDefinition = "VARCHAR(11)",
            length = 11
    )
    private String phone;


    /**
     * student id
     * assigned after student certification
     * */
    @Column(
            name = "student_id"
    )
    private String studentId;


    /**
     * qq id
     * one of the important contact information of the user
     * */
    @Column(
            name = "qq_id",
            columnDefinition = "VARCHAR(50)"
    )
    private String qqId;


    /**
     * wechat id
     * one of the important contact information of the user
     * */
    @Column(
            name = "wechat_id",
            columnDefinition = "VARCHAR(50)"
    )
    private String wechatId;



    /**
     * user name
     * cannot be null
     * one of the important contact information of the user
     * user provide
     * */
    @Column(
            name = "user_name",
            nullable = false,
            columnDefinition = "VARCHAR(50)",
            length = 50
    )
    private String userName;


    /**
    * password
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


    /**
    * avatar url
    * each user can have one avatar image.
    * */
    @URL
    @Column(
            name = "avatar_url",
            columnDefinition = "TEXT"
    )
    private String avatarUrl;


    /**
     * student certified
     * set true if certified
     * */
    @Column(
            name = "student_certified",
            nullable = false
    )
    private boolean studentCertified = false;

    /**
     * user tag
     * ordinary and admin
    * */
    @Column(
            name = "user_tag",
            nullable = false,
            columnDefinition = "VARCHAR(20)"
    )
    private String userTag = "ordinary";



    public boolean isAdmin(){
        return userTag.equals("admin");
    }

    public User(Body.Register info){
        this.phone = info.getUserPhone();
        this.userName = info.getUserName();
        this.password = info.getPassword();
    }
}
