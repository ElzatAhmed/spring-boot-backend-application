package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.*;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ResourceInfo {

    private UUID code;

    private String name;

    private boolean needsToPay;

    private int fee;

    private String description;

    private String tag;

    private LocalDateTime onTime;

    private String imageUrl;

    private String owner_phone;

    private User owner;

    public ResourceInfo(Resource resource){
        this.code = resource.getCode();
        this.name = resource.getName();
        this.needsToPay = resource.isNeedsToPay();
        this.fee = resource.getFee();
        this.description = resource.getDescription();
        this.tag = resource.getTag().toString();
        this.onTime = resource.getOnTime();
        this.imageUrl = resource.getImageUrl();
        this.owner_phone = resource.getOwner().getPhone();
    }

}
