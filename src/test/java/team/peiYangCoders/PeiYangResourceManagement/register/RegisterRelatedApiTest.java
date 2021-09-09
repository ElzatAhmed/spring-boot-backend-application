package team.peiYangCoders.PeiYangResourceManagement.register;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

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


    @Test void canRegisterOrdinary() throws Exception {

        // given
        User user = randomUser();
        user.setUserTag("ordinary");

        // when

        MvcResult tokeRequest = mockMvc
                .perform(post("/api/v1/token/?phone="
                        + user.getPhone())).andReturn();
        Response r0 = mapper.readValue(tokeRequest.getResponse().getContentAsString(), Response.class);
        ConfirmationToken cToken = mapper.readValue(mapper.writeValueAsString(r0.getData()),
                ConfirmationToken.class);

        ResultActions registerRequest = mockMvc
                .perform(post("/api/v1/user/?cToken=" + cToken.getToken())
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
        user.setUserTag("admin");

        // when
        MvcResult adminLoginRequest = mockMvc
                .perform(get("/api/v1/admin/?phone="
                        + "18799081038&password=300059")).andReturn();
        Response r0 = mapper.readValue(adminLoginRequest.getResponse().getContentAsString(),
                Response.class);
        UserToken uToken = mapper.readValue(mapper.writeValueAsString(r0.getData()), UserToken.class);

        MvcResult addAdminCodeRequest = mockMvc
                .perform(get("/api/v1/admin-code/?phone=" +
                        "18799081038&uToken=" + uToken.getToken())).andReturn();
        Response r1 = mapper.readValue(addAdminCodeRequest.getResponse().getContentAsString(),
                Response.class);
        AdminRegistrationCode adminCode = mapper.readValue(mapper.writeValueAsString(r1.getData()),
                AdminRegistrationCode.class);

        MvcResult cTokenRequest = mockMvc
                .perform(post("/api/v1/token/?phone=" +
                        user.getPhone())).andReturn();
        Response r2 = mapper.readValue(cTokenRequest.getResponse().getContentAsString(),
                Response.class);
        ConfirmationToken cToken = mapper.readValue(mapper.writeValueAsString(r2.getData()),
                ConfirmationToken.class);

        ResultActions adminRegisterRequest = mockMvc
                .perform(post("/api/v1/admin/?regCode=" +
                        adminCode.getCode() + "&cToken=" + cToken.getToken())
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
