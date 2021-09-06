package team.peiYangCoders.PeiYangResourceManagement.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;
    @Mock
    private UserTokenRepository userTokenRepo;
    @Mock
    private ConfirmationTokenRepository cTokenRepo;
    @Mock
    private AdminRegistrationCodeRepository regCodeRepo;
    @Mock
    private StudentCertificateRepository certificateRepo;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepo, userTokenRepo,
                cTokenRepo, regCodeRepo, certificateRepo, passwordEncoder);
    }

    @Test
    void canInspectInvalidPhoneWhenLoggingInOrdinary() {
        Response response = underTest.login("17627669320", "300059", false);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepo).findByPhone(argumentCaptor.capture());
        String phone = argumentCaptor.getValue();
        assertThat(phone).isEqualTo("17627669320");
        assertThat(response.getCode()).isEqualTo(Response.invalidPhone().getCode());
    }

    @Test
    void canInspectInvalidPasswordWhenLoggingInOrdinary(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("300059");
        User ordinaryUser = new User(
                null, "17627669320", "3019244086", "1208915986",
                "1208915986", "Elzat", encodedPassword, "https://github.com",
                true, "ordinary"
        );
        given(userRepo.findByPhone(anyString())).willReturn(java.util.Optional.of(ordinaryUser));
        Response response = underTest.login("17627669320", "300058", false);

        ArgumentCaptor<String> encodedCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(passwordEncoder).matches(passwordCaptor.capture(), encodedCaptor.capture());

        assertThat(encodedCaptor.getValue()).isEqualTo(encodedPassword);
        assertThat(passwordCaptor.getValue()).isEqualTo("300058");
        assertThat(passwordEncoder.matches(passwordCaptor.capture(), encodedCaptor.capture()))
                .isEqualTo(false);

        assertThat(response.getCode()).isEqualTo(Response.invalidPassword().getCode());
    }

    @Test
    void canOrdinaryLogin(){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("300059");

        User ordinaryUser = new User(
                null, "17627669320", "3019244086", "1208915986",
                "1208915986", "Elzat", encodedPassword, "https://github.com",
                true, "ordinary"
        );
        given(userRepo.findByPhone(anyString())).willReturn(java.util.Optional.of(ordinaryUser));
        Response response = underTest.login("17627669320", "300059", false);

        assertThat(passwordEncoder.matches(anyString(), anyString())).isTrue();
//        verify(userTokenRepo).save(any());
//        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
    }

    @Test
    @Disabled
    void canAdminLogin(){
        User adminUser = new User(
                null, "18799081038", "3019244087", "1208915987",
                "1208915987", "Mardan", "300059", "https://github.com",
                true, "admin"
        );
    }

    @Test
    @Disabled
    void register() {
    }

    @Test
    @Disabled
    void testRegister() {
    }

    @Test
    @Disabled
    void getUserInfo() {
    }

    @Test
    @Disabled
    void update() {
    }

    @Test
    @Disabled
    void testUpdate() {
    }

    @Test
    @Disabled
    void studentCertification() {
    }

    @Test
    @Disabled
    void getByFilter() {
    }
}