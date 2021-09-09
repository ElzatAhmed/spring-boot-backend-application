package team.peiYangCoders.PeiYangResourceManagement.login;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.*;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;
import team.peiYangCoders.PeiYangResourceManagement.service.implementation.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.asMediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils.randomUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
public class LoginRelatedApiTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper mapper;

    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @Test
    void canLoginOrdinary() throws Exception{

        // given
        User u = randomUser();
        u.setUserName("Elzat");
        u.setUserTag("ordinary");
        u.setPhone("17627669320");
        u.setPassword("300059");
        String rawPassword = u.getPassword();
        u.setPassword(passwordEncoder.encode(rawPassword));
        Response response;

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/v1/user/?phone="
                        + u.getPhone() + "&password="
                        + rawPassword));
        MvcResult mvcResult = resultActions.andReturn();

        // then
        response = mapper.readValue(mvcResult.getResponse().getContentAsString(), Response.class);
        resultActions.andExpect(status().isOk());
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
        UserToken uToken = mapper.readValue(mapper.writeValueAsString(response.getData()), UserToken.class);
        assertThat(uToken.getUserPhone()).isEqualTo(u.getPhone());
        assertThat(uToken.getUserName()).isEqualTo(u.getUserName());

    }


    @Test
    void canLoginAdmin() throws Exception {

        // given
        User u = randomUser();
        u.setUserName("Elzat");
        u.setUserTag("admin");
        u.setPhone("18799081038");
        u.setPassword("300059");
        String rawPassword = u.getPassword();
        u.setPassword(passwordEncoder.encode(rawPassword));
        Response response;

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/v1/admin/?phone="
                        + u.getPhone() + "&password="
                        + rawPassword));
        MvcResult mvcResult = resultActions.andReturn();

        // then
        response = mapper.readValue(mvcResult.getResponse().getContentAsString(), Response.class);
        resultActions.andExpect(status().isOk());
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
        UserToken uToken = mapper.readValue(mapper.writeValueAsString(response.getData()), UserToken.class);
        assertThat(uToken.getUserPhone()).isEqualTo(u.getPhone());
        assertThat(uToken.getUserName()).isEqualTo(u.getUserName());
    }

    @Test
    void canDetectInvalidPassword() throws Exception {

        // given
        User ordinary = randomUser();
        ordinary.setUserName("Elzat");
        ordinary.setUserTag("ordinary");
        ordinary.setPhone("17627669320");
        ordinary.setPassword("300059");
        String wrong1 = "123456";
        Response r1;

        User admin = randomUser();
        admin.setUserName("Elzat");
        admin.setUserTag("admin");
        admin.setPhone("18799081038");
        admin.setPassword("300059");
        String wrong2 = "123456";
        Response r2;

        // when
        ResultActions resultActions1 = mockMvc
                .perform(get("/api/v1/user/?phone="
                        + ordinary.getPhone() + "&password="
                        + wrong1));
        MvcResult result1 = resultActions1.andReturn();
        ResultActions resultActions2 = mockMvc
                .perform(get("/api/v1/admin/?phone="
                        + admin.getPhone() + "&password="
                        + wrong2));
        MvcResult result2 = resultActions2.andReturn();

        // then
        resultActions1.andExpect(status().isOk());
        resultActions2.andExpect(status().isOk());
        r1 = mapper.readValue(result1.getResponse().getContentAsString(), Response.class);
        r2 = mapper.readValue(result2.getResponse().getContentAsString(), Response.class);
        assertThat(r1.getCode()).isEqualTo(Response.invalidPassword().getCode());
        assertThat(r2.getCode()).isEqualTo(Response.invalidPassword().getCode());
        assertThat(r1.getData()).isEqualTo(null);
        assertThat(r2.getData()).isEqualTo(null);
    }


    @Test
    void loginOrdinaryOnAdminLoginApi() throws Exception {

        // given
        User ordinary = randomUser();
        ordinary.setUserName("Elzat");
        ordinary.setUserTag("ordinary");
        ordinary.setPhone("17627669320");
        ordinary.setPassword("300059");
        String rawPassword = ordinary.getPassword();
        ordinary.setPassword(passwordEncoder.encode(rawPassword));
        Response r;

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/v1/admin/?phone="
                        + ordinary.getPhone() + "&password="
                        + ordinary.getPassword()));
        MvcResult mvcResult = resultActions.andReturn();

        // then
        resultActions.andExpect(status().isOk());
        r = mapper.readValue(mvcResult.getResponse().getContentAsString(), Response.class);
        assertThat(r.getCode()).isEqualTo(Response.permissionDenied().getCode());
        assertThat(r.getData()).isEqualTo(null);
    }


    @Test
    void loginAdminOnOrdinaryLoginApi() throws Exception {

        // given
        User u = randomUser();
        u.setUserName("Elzat");
        u.setUserTag("admin");
        u.setPhone("18799081038");
        u.setPassword("300059");
        String rawPassword = u.getPassword();
        u.setPassword(passwordEncoder.encode(rawPassword));
        Response response;

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/v1/user/?phone="
                        + u.getPhone() + "&password="
                        + rawPassword));
        MvcResult mvcResult = resultActions.andReturn();

        // then
        resultActions.andExpect(status().isOk());
        response = mapper.readValue(mvcResult.getResponse().getContentAsString(), Response.class);
        assertThat(response.getCode()).isEqualTo(Response.permissionDenied().getCode());
        assertThat(response.getData()).isEqualTo(null);

    }
}
