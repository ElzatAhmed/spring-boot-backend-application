package team.peiYangCoders.PeiYangResourceManagement.studentCertification;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentCertificate;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.StudentCertificateRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserTokenRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils.randomUser;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
public class StudentCertificationRelatedApiTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper mapper;

    @Autowired private UserRepository userRepo;

    @Autowired private UserTokenRepository uTokenRepo;

    @Autowired private StudentCertificateRepository certificateRepo;

    @BeforeEach
    void setUp() {
        certificateRepo.deleteAll();
        uTokenRepo.deleteAll();
        certificateRepo.save(
                new StudentCertificate(
                        null, "3019244086", "AiLi",
                        "300059", false
                )
        );
        uTokenRepo.save(
                new UserToken(
                        "17627669320", "Elzat", "123456"
                )
        );
    }

    @Test void canDoStudentCertification() throws Exception {

        // given
        User user = randomUser();
        user.setPhone("17627669320");
        user.setStudentCertified(false);
        user.setStudentId(null);

        StudentCertificate certificate = new StudentCertificate(
                null, "3019244086", "AiLi",
                "300059", false
        );

        // when
        ResultActions certificateRequest = mockMvc
                .perform(post("/api/v1/user/student/?phone="
                        + user.getPhone() + "&uToken=123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(certificate)));
        MvcResult mvcResult = certificateRequest.andReturn();

        // then
        certificateRequest.andExpect(status().isOk());
        Response response = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                Response.class);
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
        assertThat(response.getData()).isEqualTo(null);

        Optional<User> u = userRepo.findByPhone(user.getPhone());
        assertThat(u.isPresent()).isTrue();
        assertThat(u.get().isStudentCertified()).isTrue();
        assertThat(u.get().getStudentId()).isEqualTo(certificate.getStudentId());
        Optional<StudentCertificate> c = certificateRepo.findByStudentId(certificate.getStudentId());
        assertThat(c.isPresent()).isTrue();
        assertThat(c.get().getStudentId()).isEqualTo(certificate.getStudentId());
        assertThat(c.get().isUsed()).isTrue();
    }

}
