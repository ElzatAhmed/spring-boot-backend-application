package team.peiYangCoders.PeiYangResourceManagement.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderConfig {

    private int acceptingValidTime = 24;        // hours

    private int closingValidTime = 24 * 7;      // hours

}
