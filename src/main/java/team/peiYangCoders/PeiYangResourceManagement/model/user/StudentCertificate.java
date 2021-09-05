package team.peiYangCoders.PeiYangResourceManagement.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "StudentId")
@Table(
        name = "student_id",
        uniqueConstraints = {
                @UniqueConstraint(name = "student_id_unique", columnNames = "student_id")
        }
)
public class StudentCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(
            name = "student_id",
            nullable = false,
            updatable = false
    )
    private String studentId;

    @Column(
            name = "student_name",
            nullable = false,
            updatable = false
    )
    private String studentName;

    @Column(
            name = "student_password",
            nullable = false,
            updatable = false
    )
    private String studentPassword;

    @Column(
            name = "used",
            nullable = false
    )
    private boolean used = false;

}
