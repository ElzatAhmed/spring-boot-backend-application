package team.peiYangCoders.PeiYangResourceManagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "UserToken")
@Table(name = "user_tokens")
public class UserToken {

    @Id
    @Column(
            name = "userPhone",
            nullable = false,
            updatable = false
    )
    private String userPhone;

    @Column(
            name = "userName",
            nullable = false
    )
    private String userName;

    @Column(
            name = "token",
            nullable = false
    )
    private String token;

}
