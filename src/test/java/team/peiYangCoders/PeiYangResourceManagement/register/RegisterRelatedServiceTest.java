package team.peiYangCoders.PeiYangResourceManagement.register;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.AdminRegistrationCode;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.*;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;
import team.peiYangCoders.PeiYangResourceManagement.service.implementation.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils.randomUser;

@ExtendWith(MockitoExtension.class)
public class RegisterRelatedServiceTest {

    @Mock private UserRepository userRepo;
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


    @Test void canDetectInvalidPhone(){

        // given
        User u = randomUser();
        u.setPhone("17627669320");
        u.setUserTag("ordinary");
        ConfirmationToken cToken = new ConfirmationToken(
                "123456", LocalDateTime.now(), LocalDateTime.now().plusMinutes(5L), u.getPhone()
        );
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u));
        ArgumentCaptor<String> phoneCaptor = ArgumentCaptor.forClass(String.class);

        // when
        Response r = userService.register(u, cToken.getToken());

        // then
        verify(userRepo).findByPhone(phoneCaptor.capture());
        verify(userRepo, never()).save(any());
        assertThat(r.getCode()).isEqualTo(Response.invalidPhone().getCode());
        assertThat(r.getData()).isEqualTo(null);

    }

    @Test void canDetectInvalidConfirmationToken(){
        // given
        User u = randomUser();
        u.setPhone("17627669320");
        u.setUserTag("ordinary");
        given(userRepo.findByPhone(anyString())).willReturn(Optional.empty());
        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);

        // when
        Response response = userService.register(u, "123456");

        // then
        verify(cTokenRepo).findByToken(tokenCaptor.capture());
        verify(userRepo, never()).save(any());
        assertThat(tokenCaptor.getValue()).isEqualTo("123456");
        assertThat(response.getCode()).isEqualTo(Response.invalidConfirmationToken().getCode());
        assertThat(response.getData()).isEqualTo(null);
    }


    @Test void canDetectInvalidRegistrationCode(){
        // given
        User u = randomUser();
        u.setPhone("17627669320");
        u.setUserTag("admin");
        given(userRepo.findByPhone(anyString())).willReturn(Optional.empty());
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);

        // when
        Response response = userService.register(u, "123456", "123456");

        // then
        verify(adminCodeRepo).findByCode(codeCaptor.capture());
        verify(userRepo, never()).save(any());
        assertThat(codeCaptor.getValue()).isEqualTo("123456");
        assertThat(response.getCode()).isEqualTo(Response.invalidRegistrationCode().getCode());
        assertThat(response.getData()).isEqualTo(null);
    }

    @Test void canRegisterOrdinary(){
        // given
        User u = randomUser();
        u.setPhone("17627669320");
        u.setPassword("300059");
        u.setUserTag("ordinary");
        String raw = u.getPassword();
        ConfirmationToken cToken = new ConfirmationToken(
                "123456", LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), u.getPhone()
        );
        given(userRepo.findByPhone(anyString())).willReturn(Optional.empty());
        given(cTokenRepo.findByToken(anyString())).willReturn(Optional.of(cToken));
        given(encoder.encode(u.getPassword())).willReturn(myEncoder.encode(u.getPassword()));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // when
        Response response = userService.register(u, cToken.getToken());

        // then
        verify(encoder).encode(anyString());
        verify(userRepo).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getPhone()).isEqualTo(u.getPhone());
        assertThat(capturedUser.getUserTag()).isEqualTo("ordinary");
        assertThat(myEncoder.matches(raw, capturedUser.getPassword())).isTrue();
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
        assertThat(response.getData()).isEqualTo(null);
    }


    @Test void canRegisterAdmin(){
        // given
        User u = randomUser();
        u.setPhone("17627669320");
        u.setUserTag("admin");
        u.setPassword("300059");
        String raw = u.getPassword();
        ConfirmationToken cToken = new ConfirmationToken(
                "123456", LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), u.getPhone()
        );
        AdminRegistrationCode regCode = new AdminRegistrationCode(
                null, "123456", false
        );
        given(userRepo.findByPhone(anyString())).willReturn(Optional.empty());
        given(cTokenRepo.findByToken(anyString())).willReturn(Optional.of(cToken));
        given(adminCodeRepo.findByCode(anyString())).willReturn(Optional.of(regCode));
        given(encoder.encode(u.getPassword())).willReturn(myEncoder.encode(u.getPassword()));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // when
        Response response = userService.register(u, cToken.getToken(), regCode.getCode());

        // then
        verify(encoder).encode(anyString());
        verify(userRepo).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getPhone()).isEqualTo(u.getPhone());
        assertThat(capturedUser.getUserTag()).isEqualTo("admin");
        assertThat(myEncoder.matches(raw, capturedUser.getPassword())).isTrue();
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
        assertThat(response.getData()).isEqualTo(null);
    }
}
