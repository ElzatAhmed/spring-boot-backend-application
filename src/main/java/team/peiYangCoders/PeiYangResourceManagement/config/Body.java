package team.peiYangCoders.PeiYangResourceManagement.config;

import lombok.*;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

public class Body {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {

        private String user_phone;

        private String password;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Register {

        private String user_phone;

        private String user_name;

        private String password;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewPassword {

        private String new_password;

        private String confirm_token;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDetail{

        private String user_name;

        private String qq_id;

        private String wechat_id;

        private String avatar_url;

        private String user_phone;

        private String studentId;

        private String password;

        private String tag;

        private boolean studentCertified;

        private Long id;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Certification{

        private String student_id;

        private String student_name;

        private String student_password;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceInfos {

        private String resource_code;

        private String resource_name;

        private String description;

        private String tag;

        private String image_url;

        private String owner_phone;

        private boolean verified;

        private boolean released;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfos {

        private String item_code;

        private String type;

        private boolean needs2pay;

        private int fee;

        private int count;

        private String fee_unit;

        private LocalDateTime startsAt;

        private LocalDateTime endsAt;

        private int campus;

        private LocalDateTime onTime;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderInfos{

        private String getter_phone;

        private int count;

        private String comments;

        private String code;

        private String owner_phone;

        private LocalDateTime openedTime;

        private LocalDateTime closedTime;

        private LocalDateTime acceptedTime;

        private LocalDateTime canceledTime;

        private LocalDateTime acceptingExpiresAt;

        private LocalDateTime closingExpiresAt;

        private boolean accepted;

        private boolean canceled;

        private boolean closedByOwner;

        private boolean closedByGetter;

        private boolean expired;

        private boolean acceptedOrRejected;

    }

}
