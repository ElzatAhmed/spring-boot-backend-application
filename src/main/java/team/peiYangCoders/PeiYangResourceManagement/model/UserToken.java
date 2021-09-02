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
            name = "user_phone",
            nullable = false,
            updatable = false
    )
    private String phone;

    @Column(
            name = "code",
            nullable = false
    )
    private UUID code;

}
