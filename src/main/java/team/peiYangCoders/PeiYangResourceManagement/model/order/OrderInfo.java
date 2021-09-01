package team.peiYangCoders.PeiYangResourceManagement.model.order;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderInfo {

    private String tag;

    private String initiator_phone;

    private String recipient_phone;

    private String resource_id;

    private String opened_time;

    private boolean closed;

    private String closedTime;

}
