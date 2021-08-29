package team.peiYangCoders.PeiYangResourceManagement.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserFilter {

    private String phone = null;

    private String name = null;

    private String qqId = null;

    private String wechatId = null;

    public boolean match(User u){
        boolean p, n, q, w;
        p = phone == null || phone.equals(u.getPhone());
        n = name == null || name.equals(u.getName());
        q = qqId == null || qqId.equals(u.getName());
        w = wechatId == null || wechatId.equals(u.getWechatId());
        return p && n && q && w;
    }

}
