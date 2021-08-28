package team.peiYangCoders.PeiYangResourceManagement.config;

import lombok.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SMSParam {

    private int appId = 1400565098;

    private String appKey = "5ee7d76e352b0de1cde8db9ac2fad584";

    private int templateId;

    private String signature = "山谷说";

    private Long latency = 5;

    private int tokenLen = 6;

}
