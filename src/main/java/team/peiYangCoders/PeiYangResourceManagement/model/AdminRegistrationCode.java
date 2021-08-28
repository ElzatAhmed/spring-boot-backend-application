package team.peiYangCoders.PeiYangResourceManagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "AdminRegistrationCode")
@Table(
        name = "admin_registration_codes",
        uniqueConstraints = {@UniqueConstraint(name = "admin_code_unique", columnNames = "code")}
)
public class AdminRegistrationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(
            name = "code",
            nullable = false
    )
    private String code;

    @Column(
            name = "used",
            nullable = false
    )
    private boolean used = false;

    public AdminRegistrationCode(String code){
        this.code = code;
    }
}
