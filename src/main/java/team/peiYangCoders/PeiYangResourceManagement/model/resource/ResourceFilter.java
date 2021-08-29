package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResourceFilter {

    private UUID code = null;

    private String name = null;

    private Boolean verified = null;

    private Boolean needsToPay = null;

    private Integer fee = null;

    private String description = null;

    private ResourcePage tag = null;

    private String owner_phone = null;

    public boolean match(Resource resource){
        boolean c, n, v, nt, f, d, t, o;
        c = code == null || code.equals(resource.getCode());
        n = name == null || name.equals(resource.getName());
        v = verified == null || verified.equals(resource.isVerified());
        nt = needsToPay == null || needsToPay.equals(resource.isNeedsToPay());
        f = fee == null || fee.equals(resource.getFee());
        d = description == null || description.equals(resource.getDescription());
        t = tag == null || tag.toString().equals(resource.getTag().toString());
        o = owner_phone == null || owner_phone.equals(resource.getOwner().getPhone());
        return c && n && v && nt && f && d && t && o;
    }

}
