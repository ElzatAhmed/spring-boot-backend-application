package team.peiYangCoders.PeiYangResourceManagement.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Body {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {

        private String phone;

        private String password;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {

        private String phone;

        private String name;

        private String password;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewPassword {

        private String newPassword;

        private String token;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDetail{

        private String name;

        private String qqId;

        private String wechatId;

        private String avatarUrl;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Certification{

        private String studentId;

        private String studentName;

        private String studentPassword;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewResource{

        private String name;

        private boolean needsToPay;

        private int fee;

        private String description;

        private String tag;

        private String imageUrl;

    }

}
