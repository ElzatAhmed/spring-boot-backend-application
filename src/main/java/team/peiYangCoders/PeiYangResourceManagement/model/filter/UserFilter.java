package team.peiYangCoders.PeiYangResourceManagement.model.filter;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserFilter implements Filter{

    private String phone = null;

    private String name = null;

    private String qqId = null;

    private String wechatId = null;

    private Boolean studentCertified = null;

    @Override
    public boolean allNull(){
        return phone == null && name == null && qqId == null
                && wechatId == null && studentCertified == null;
    }

    public boolean nullExceptPhone(){
        return phone != null && name == null && qqId == null
                && wechatId == null && studentCertified == null;
    }

}
