package team.peiYangCoders.PeiYangResourceManagement.model.user;

import lombok.*;

@Getter
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

}
