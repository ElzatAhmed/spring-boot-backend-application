package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemFilter {

    private UUID code = null;

    private ResourceType type = null;

    private Boolean needs2Pay = null;

    private Boolean ordered = null;

    private Boolean completed = null;

    private String ownerPhone = null;

    public boolean match(Item resource){
        boolean c, t, n, o, cp, op;
        c = code == null || code.equals(resource.getItemCode());
        t = type == null || type.toString().equals(resource.getType().toString());
        n = needs2Pay == null || needs2Pay.equals(resource.isNeeds2Pay());
        o = ordered == null ||  ordered.equals(resource.isOrdered());
        cp = completed == null || completed.equals(resource.isCompleted());
        op = ownerPhone == null || ownerPhone.equals(resource.getResource().getOwner().getPhone());
        return c && t && n && o && cp && op;
    }

}
