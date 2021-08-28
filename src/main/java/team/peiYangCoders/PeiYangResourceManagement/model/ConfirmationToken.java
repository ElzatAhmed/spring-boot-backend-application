package team.peiYangCoders.PeiYangResourceManagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ConfirmationToken")
@Table(
        name = "confirmation_tokens"
)
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(
            name = "token",
            nullable = false,
            updatable = false
    )
    private String token;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "expires_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime expiresAt;

    @Column(
            name = "confirmed_at"
    )
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id",
            updatable = false
    )
    private User user;


    public ConfirmationToken(String token, LocalDateTime createdAt,
                             LocalDateTime expiresAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    private static String generateRandomToken(int length){
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < length; i++)
            token.append((int) (Math.random() * 10));
        return token.toString();
    }

    public static ConfirmationToken construct(int tokenLen, Long latency, User user){
        String token = generateRandomToken(tokenLen);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(latency);
        return new ConfirmationToken(token, createdAt, expiresAt, user);
    }
}
