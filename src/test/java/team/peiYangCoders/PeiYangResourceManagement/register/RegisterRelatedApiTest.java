package team.peiYangCoders.PeiYangResourceManagement.register;


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
import team.peiYangCoders.PeiYangResourceManagement.model.AdminRegistrationCode;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.AdminRegistrationCodeRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.ConfirmationTokenRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserTokenRepository;

import java.time.LocalDateTime;
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
public class RegisterRelatedApiTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper mapper;

    @Autowired private UserRepository userRepo;

    @Autowired private ConfirmationTokenRepository cTokenRepo;

    @Autowired private AdminRegistrationCodeRepository adminCodeRepo;

    @BeforeEach
    void setUp() {
        cTokenRepo.deleteAll();
        adminCodeRepo.deleteAll();
        userRepo.deleteAll();
        cTokenRepo.save(
                new ConfirmationToken(
                        null, "123456", LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(10L), null,
                        false, "13899095284"
                )
        );
        adminCodeRepo.save(
                new AdminRegistrationCode(null, "456789789456", false)
        );
    }

    @Test void canRegisterOrdinary() throws Exception {

        // given
        User user = randomUser();
        user.setPhone("13899095284");
        user.setUserTag("ordinary");

        // when
        ResultActions registerRequest = mockMvc
                .perform(post("/api/v1/user/?cToken=123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)));
        MvcResult mvcResult = registerRequest.andReturn();

        // then
        Response response = mapper.readValue(mvcResult.getResponse().getContentAsString(), Response.class);
        registerRequest.andExpect(status().isOk());
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
        assertThat(response.getData()).isEqualTo(null);
        Optional<User> u = userRepo.findByPhone(user.getPhone());
        assertThat(u.isPresent()).isTrue();
        assertThat(u.get().getUserTag()).isEqualTo("ordinary");
    }


    @Test void canRegisterAdmin() throws Exception {

        // given
        User user = randomUser();
        user.setPhone("13899095284");
        user.setUserTag("admin");

        // when
        ResultActions adminRegisterRequest = mockMvc
                .perform(post("/api/v1/admin/?regCode=456789789456&cToken=123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)));

        // then
        adminRegisterRequest.andExpect(status().isOk());
        MvcResult mvcResult = adminRegisterRequest.andReturn();
        Response response = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                Response.class);
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
        assertThat(response.getData()).isEqualTo(null);
        Optional<User> u = userRepo.findByPhone(user.getPhone());
        assertThat(u.isPresent()).isTrue();
        assertThat(u.get().getUserTag()).isEqualTo("admin");
    }

}
