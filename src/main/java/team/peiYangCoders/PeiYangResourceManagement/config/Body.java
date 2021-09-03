package team.peiYangCoders.PeiYangResourceManagement.config;

import lombok.*;

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
    public static class Certification{

        private String student_id;

        private String student_name;

        private String student_password;

    }

}
