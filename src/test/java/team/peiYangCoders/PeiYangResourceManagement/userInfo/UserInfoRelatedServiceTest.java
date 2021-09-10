package team.peiYangCoders.PeiYangResourceManagement.userInfo;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.*;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;
import team.peiYangCoders.PeiYangResourceManagement.service.implementation.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils.randomUser;

@ExtendWith(MockitoExtension.class)
public class UserInfoRelatedServiceTest {

    @Mock
    private UserRepository userRepo;
    @Mock private UserTokenRepository userTokenRepo;
    @Mock private BCryptPasswordEncoder encoder;
    @Mock private ConfirmationTokenRepository cTokenRepo;
    @Mock private AdminRegistrationCodeRepository adminCodeRepo;
    @Mock private StudentCertificateRepository certificateRepo;

    private UserService userService;
    private BCryptPasswordEncoder myEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepo, userTokenRepo, cTokenRepo,
                adminCodeRepo, certificateRepo, encoder);
        myEncoder = new BCryptPasswordEncoder();
    }


    @Test void canUpdateUserInfo(){

        // given
        User user = randomUser();
        user.setUserName("Elzat");
        user.setWechatId("456789");
        user.setQqId("456789");
        user.setPhone("17627669320");
        user.setUserTag("ordinary");
        UserToken uToken = new UserToken(
                user.getPhone(), user.getUserName(), "123456"
        );
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(user));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken));
        user.setUserName("Ahmed");
        user.setWechatId("987654");
        user.setQqId("987654");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // when
        Response response = userService.update(user, user.getPhone(), uToken.getToken());

        // then
        verify(userRepo).save(userCaptor.capture());
        User newUser = userCaptor.getValue();
        assertThat(newUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(newUser.getWechatId()).isEqualTo(user.getWechatId());
        assertThat(newUser.getQqId()).isEqualTo(user.getQqId());
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
        assertThat(response.getData()).isEqualTo(null);
    }


    @Test void canUpdatePassword(){

        // given
        User user = randomUser();
        user.setUserName("Elzat");
        user.setPhone("17627669320");
        String original = "123456";
        String newPass = "456789";
        user.setPassword(myEncoder.encode(original));

        UserToken uToken = new UserToken(
                user.getPhone(), user.getUserName(), "123456"
        );
        ConfirmationToken cToken = new ConfirmationToken(
                null, "123456", LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5), null, false, user.getPhone()
        );
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(user));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken));
        given(cTokenRepo.findByToken(anyString())).willReturn(Optional.of(cToken));
        given(encoder.encode(newPass)).willReturn(myEncoder.encode(newPass));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // when
        Response response = userService.update(user.getPhone(), cToken.getToken(), newPass);

        // then
        verify(userRepo).save(userCaptor.capture());
        User newUser = userCaptor.getValue();
        assertThat(myEncoder.matches(newPass, newUser.getPassword())).isTrue();
        assertThat(newUser.getPhone()).isEqualTo(user.getPhone());
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
    }

}
