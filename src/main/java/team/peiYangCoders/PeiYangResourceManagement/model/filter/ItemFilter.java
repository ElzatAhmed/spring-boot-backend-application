package team.peiYangCoders.PeiYangResourceManagement.model.filter;

import lombok.*;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ItemType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ItemFilter implements Filter{

    private String code = null;

    private String type = null;

    private Boolean needs2Pay = null;

    private Integer campus = null;

    private String resourceCode = null;

    private String phone = null;

    @Override
    public boolean allNull() {
        return code == null && type == null && needs2Pay == null
                 && campus == null && resourceCode == null && phone == null;
    }

    public boolean nullExceptCode(){
        return code != null && type == null && needs2Pay == null
                 && campus == null && resourceCode == null && phone == null;
    }
}
