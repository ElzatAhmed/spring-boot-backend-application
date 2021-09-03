package team.peiYangCoders.PeiYangResourceManagement.model.filter;

import lombok.*;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ResourceFilter implements Filter{

    private String code = null;

    private String name = null;

    private Boolean verified = null;

    private Boolean released = null;

    private Boolean accepted = null;

    private String description = null;

    private String tag = null;

    private String owner_phone = null;

    @Override
    public boolean allNull() {
        return code == null && name == null && verified == null && released == null
                && description == null && tag == null && owner_phone == null && accepted == null;
    }

    public boolean nullExceptCode(){
        return code != null && name == null && verified == null && released == null
                && description == null && tag == null && owner_phone == null && accepted == null;
    }
}
