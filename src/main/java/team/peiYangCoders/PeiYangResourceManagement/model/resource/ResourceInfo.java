package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ResourceInfo {

    private String name;

    private boolean needsToPay;

    private int fee;

    private String description;

    private String tag;

    private String onTime;

    private String imageUrl;

    private String owner_phone;

}
