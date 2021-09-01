package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.*;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ResourceFilter {

    private UUID code = null;

    private String name = null;

    private Boolean verified = null;

    private Boolean accepted = null;

    private Boolean needsToPay = null;

    private Boolean released = null;

    private String description = null;

    private ResourceTag tag = null;

    private String owner_phone = null;

    public boolean match(Resource resource){
        boolean c, n, a, v, r, d, t, o;
        c = code == null || code.equals(resource.getCode());
        n = name == null || name.equals(resource.getName());
        a = accepted == null || accepted.equals(resource.isAccepted());
        v = verified == null || verified.equals(resource.isVerified());
        r = released == null || released.equals(resource.isReleased());
        d = description == null || description.equals(resource.getDescription());
        t = tag == null || tag.toString().equals(resource.getTag().toString());
        o = owner_phone == null || owner_phone.equals(resource.getOwner().getPhone());
        return c && n && a && v && r && d && t && o;
    }

}
