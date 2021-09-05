package team.peiYangCoders.PeiYangResourceManagement.config;

import lombok.*;

public class Body {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Certification{

        private String studentId;

        private String studentName;

        private String studentPassword;

    }

}
