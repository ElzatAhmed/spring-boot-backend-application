package team.peiYangCoders.PeiYangResourceManagement.model.user;

import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;

import javax.persistence.*;

@Entity
@Table(
        name = "user_profiles"
)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "profile_user_fk")
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "tag",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private ResourceTag tag;

}
