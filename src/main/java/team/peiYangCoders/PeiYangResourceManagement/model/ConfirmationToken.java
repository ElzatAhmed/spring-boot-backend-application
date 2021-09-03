package team.peiYangCoders.PeiYangResourceManagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
            name = "createdAt",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "expiresAt",
            nullable = false,
            updatable = false
    )
    private LocalDateTime expiresAt;

    @Column(
            name = "confirmedAt"
    )
    private LocalDateTime confirmedAt;

    @Column(
            name = "confirmed",
            nullable = false
    )
    private boolean confirmed = false;

    @Column(
            name = "userPhone",
            nullable = false,
            updatable = false
    )
    private String userPhone;


    public ConfirmationToken(String token, LocalDateTime createdAt,
                             LocalDateTime expiresAt, String userPhone) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userPhone = userPhone;
    }

    private static String generateRandomToken(int length){
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < length; i++)
            token.append((int) (Math.random() * 10));
        return token.toString();
    }

    public static ConfirmationToken construct(int tokenLen, Long latency, String user_phone){
        String token = generateRandomToken(tokenLen);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(latency);
        return new ConfirmationToken(token, createdAt, expiresAt, user_phone);
    }
}
