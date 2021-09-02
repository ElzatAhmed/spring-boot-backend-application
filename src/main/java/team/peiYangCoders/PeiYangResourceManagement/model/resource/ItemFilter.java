package team.peiYangCoders.PeiYangResourceManagement.model.resource;

import lombok.*;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ItemType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemFilter {

    private UUID code = null;

    private ItemType type = null;

    private Boolean needs2Pay = null;

    private String ownerPhone = null;

    public boolean match(Item resource){
        boolean c, t, n, op;
        c = code == null || code.equals(resource.getItemCode());
        t = type == null || type.toString().equals(resource.getType().toString());
        n = needs2Pay == null || needs2Pay.equals(resource.isNeeds2Pay());
        op = ownerPhone == null || ownerPhone.equals(resource.getResource().getOwner().getPhone());
        return c && t && n && op;
    }

}
