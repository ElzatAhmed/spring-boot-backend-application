package team.peiYangCoders.PeiYangResourceManagement.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import static org.mockito.Mockito.*;
import static team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils.randomUser;

@ExtendWith(MockitoExtension.class)
public class LoginRelatedServiceTest {

    @Mock private UserRepository userRepo;
    @Mock private UserTokenRepository userTokenRepo;
    @Mock private BCryptPasswordEncoder encoder;
    @Mock private ConfirmationTokenRepository cTokenRepo;
    @Mock private AdminRegistrationCodeRepository adminCodeRepo;
    @Mock private StudentCertificateRepository certificateRepo;

    private UserService userService;
    private BCryptPasswordEncoder myEncoder;

    @BeforeEach
    void setUp(){
        userService = new UserServiceImpl(userRepo, userTokenRepo, cTokenRepo,
                adminCodeRepo, certificateRepo, encoder);
        myEncoder = new BCryptPasswordEncoder();
    }


    @Test void canDetectInvalidPhone(){

        /* phone = null, admin = false */
        // given
        User u = randomUser();
        u.setPhone(null);
        ArgumentCaptor<String> phoneCaptor = ArgumentCaptor.forClass(String.class);

        // when
        Response r = userService.login(u.getPhone(), u.getPassword(), false);

        // then
        verify(userRepo).findByPhone(phoneCaptor.capture());
        verify(userRepo, never()).save(any());
        assertThat(phoneCaptor.getValue()).isEqualTo(u.getPhone());
        assertThat(r.getCode()).isEqualTo(Response.invalidPhone().getCode());


        /* phone = null, admin = true */
        // given
        u = randomUser();
        u.setPhone(null);

        // when
        r = userService.login(u.getPhone(), u.getPassword(), true);

        // then
        verify(userRepo, times(2)).findByPhone(phoneCaptor.capture());
        assertThat(phoneCaptor.getValue()).isEqualTo(u.getPhone());
        assertThat(r.getCode()).isEqualTo(Response.invalidPhone().getCode());
    }


    @Test void canDetectInvalidPassword(){

        /* admin = false, password = null */
        // given
        User u = randomUser();
        u.setUserTag("ordinary");
        u.setPassword(myEncoder.encode(u.getPassword()));
        given(userRepo.findByPhone(u.getPhone())).willReturn(Optional.of(u));
        ArgumentCaptor<String> rawCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> encodedCaptor = ArgumentCaptor.forClass(String.class);

        // when
        Response r = userService.login(u.getPhone(), null, false);

        // then
        verify(encoder).matches(rawCaptor.capture(), encodedCaptor.capture());
        assertThat(rawCaptor.getValue()).isEqualTo(null);
        assertThat(encodedCaptor.getValue()).isEqualTo(u.getPassword());
        assertThat(r.getCode()).isEqualTo(Response.invalidPassword().getCode());


        /* admin = true, password = null */
        // given
        u = randomUser();
        u.setUserTag("admin");
        u.setPassword(myEncoder.encode(u.getPassword()));
        given(userRepo.findByPhone(u.getPhone())).willReturn(Optional.of(u));

        // when
        r = userService.login(u.getPhone(), null, true);

        // then
        verify(encoder, times(2))
                .matches(rawCaptor.capture(), encodedCaptor.capture());
        assertThat(rawCaptor.getValue()).isEqualTo(null);
        assertThat(encodedCaptor.getValue()).isEqualTo(u.getPassword());
        assertThat(r.getCode()).isEqualTo(Response.invalidPassword().getCode());
    }

    @Test void passOrdinaryIntoAdmin(){

        // given
        User u = randomUser();
        u.setUserTag("ordinary");
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u));

        // when
        Response r = userService.login(u.getPhone(), u.getPassword(), true);

        // then
        assertThat(r.getCode()).isEqualTo(Response.permissionDenied().getCode());
        verify(encoder, never()).matches(anyString(), anyString());
        verify(userTokenRepo, never()).save(any());
    }

    @Test void passAdminIntoOrdinary(){
        // given
        User u = randomUser();
        u.setUserTag("admin");
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u));

        // when
        Response r = userService.login(u.getPhone(), u.getPassword(), false);

        // then
        assertThat(r.getCode()).isEqualTo(Response.permissionDenied().getCode());
        verify(encoder, never()).matches(anyString(), anyString());
        verify(userTokenRepo, never()).save(any());
    }


    @Test void canOrdinaryLogin(){

        // given
        User u = randomUser();
        u.setUserTag("ordinary");
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u));
        given(encoder.matches(anyString(), anyString())).willReturn(true);
        ArgumentCaptor<UserToken> tokenCaptor = ArgumentCaptor.forClass(UserToken.class);

        // when
        Response r = userService.login(u.getPhone(), u.getPassword(), false);

        // then
        verify(userTokenRepo).save(tokenCaptor.capture());
        assertThat(tokenCaptor.getValue().getUserPhone()).isEqualTo(u.getPhone());
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);
    }

    @Test void canAdminLogin(){

        // given
        User u = randomUser();
        u.setUserTag("admin");
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u));
        given(encoder.matches(anyString(), anyString())).willReturn(true);
        ArgumentCaptor<UserToken> tokenCaptor = ArgumentCaptor.forClass(UserToken.class);

        // when
        Response r = userService.login(u.getPhone(), u.getPassword(), true);

        // then
        verify(userTokenRepo).save(tokenCaptor.capture());
        assertThat(tokenCaptor.getValue().getUserPhone()).isEqualTo(u.getPhone());
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);

    }



}
