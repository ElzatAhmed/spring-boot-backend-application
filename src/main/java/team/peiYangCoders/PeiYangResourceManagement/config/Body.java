package team.peiYangCoders.PeiYangResourceManagement.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

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

        private String phone;

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

        private String studentId;

        private String studentName;

        private String studentPassword;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceInfos {

        private String code;

        private String name;

        private String description;

        private String tag;

        private String imageUrl;

        private String ownerPhone;

        private boolean verified;

        private boolean released;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfos {

        private String code;

        private String type;

        private boolean needs2Pay;

        private int fee;

        private int count;

        private String feeUnit;

        private LocalDateTime startsAt;

        private LocalDateTime endsAt;

        private int campus;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderInfos{

        private String getterPhone;

        private int count;

        private String comments;

        private String code;

        private String ownerPhone;

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
