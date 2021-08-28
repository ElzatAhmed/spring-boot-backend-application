package team.peiYangCoders.PeiYangResourceManagement.model.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserInfo {

    private String phone;

    private String name;

    private String qqId;

    private String wechatId;

    private String password;

    private String avatarUrl;

    private String tag;

    private boolean enabled;

    public UserInfo(User user){
        this.phone = user.getPhone();
        this.name = user.getName();
        this.qqId = user.getQqId();
        this.password = user.getPassword();
        this.avatarUrl = user.getAvatarUrl();
        this.tag = user.getTag().toString();
        this.wechatId = user.getWechatId();
        this.enabled = user.isEnabled();
    }

}
