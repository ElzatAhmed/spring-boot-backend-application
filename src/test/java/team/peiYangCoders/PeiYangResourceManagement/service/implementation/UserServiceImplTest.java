package team.peiYangCoders.PeiYangResourceManagement.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.AdminRegistrationCode;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentCertificate;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.*;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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

    private User u1;
    private final String rawPassword1 = "123456789";
    private ConfirmationToken cToken1;
    private UserToken uToken1;

    private User u2;
    private final String rawPassword2 = "987654321";
    private ConfirmationToken cToken2;
    private UserToken uToken2;

    private AdminRegistrationCode registrationCode;

    private BCryptPasswordEncoder encoder;


    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepo, userTokenRepo,
                cTokenRepo, regCodeRepo, certificateRepo, passwordEncoder);
        encoder = new BCryptPasswordEncoder();
        String encoded1 = encoder.encode(rawPassword1);
        String encoded2 = encoder.encode(rawPassword2);
        u1 = new User(
                null, "17627669320", "3019244086", "1208915986",
                "1208915986", "Elzat", encoded1, "https://github.com",
                true, "ordinary"
        );
        u2 = new User(
                null, "18799081038", "3019244234", "1209885216",
                "1209885216", "Jefferson", encoded2, "https://youtube.com",
                true, "admin"
        );
        cToken1 = new ConfirmationToken(null, "123456",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                null, false, "17627669320");
        cToken2 = new ConfirmationToken(
                null, "456789",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                null, false, "18799081038"
        );
        registrationCode = new AdminRegistrationCode(null, "abcdefg", false);
        uToken1 = new UserToken(u1.getPhone(), u1.getUserName(), "uToken1");
        uToken2 = new UserToken(u2.getPhone(), u2.getUserName(), "uToken2");
    }

    /**--------------------------login test--------------------------------------------------**/

    @Test   // passed
    void canDetectErrorsWhenLoggingInOrdinary(){

        /* can detect invalid phone */
        // when
        Response r1 = underTest.login(u1.getPhone(), rawPassword1, false);
        ArgumentCaptor<String> phoneCaptor = ArgumentCaptor.forClass(String.class);
        // then
        verify(userRepo).findByPhone(phoneCaptor.capture());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userRepo, never()).save(any());
        assertThat(phoneCaptor.getValue()).isEqualTo(u1.getPhone());
        assertThat(r1.getCode()).isEqualTo(Response.invalidPhone().getCode());

        /* can detect invalid password */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        // when
        Response r2 = underTest.login(u1.getPhone(), rawPassword1, false);
        ArgumentCaptor<String> rawPassword = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> encodedPassword = ArgumentCaptor.forClass(String.class);
        // then
        verify(passwordEncoder).matches(rawPassword.capture(), encodedPassword.capture());
        assertThat(rawPassword.getValue()).isEqualTo(rawPassword1);
        assertThat(encodedPassword.getValue()).isEqualTo(u1.getPassword());
        assertThat(r2.getCode()).isEqualTo(Response.invalidPassword().getCode());

    }

    @Test   // passed
    void canLoginOrdinary(){
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        // when
        Response r = underTest.login(u1.getPhone(), rawPassword1, false);
        // then
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);
        verify(userTokenRepo).save(any());
    }


    @Test   // passed
    void canDetectErrorsWhenAdminLoggingIn(){

        /* can detect invalid phone */
        // when
        Response r1 = underTest.login(u2.getPhone(), rawPassword2, true);
        ArgumentCaptor<String> phoneCaptor = ArgumentCaptor.forClass(String.class);
        // then
        verify(userRepo).findByPhone(phoneCaptor.capture());
        assertThat(phoneCaptor.getValue()).isEqualTo(u2.getPhone());
        assertThat(r1.getCode()).isEqualTo(Response.invalidPhone().getCode());

        /* can detect not an admin */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        // when
        Response r2 = underTest.login(u1.getPhone(), rawPassword1, true);
        // then
        assertThat(r2.getCode()).isEqualTo(Response.permissionDenied().getCode());
        verify(passwordEncoder, never()).matches(anyString(), anyString());

        /* can detect invalid password */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u2));
        // when
        Response r3 = underTest.login(u2.getPhone(), rawPassword2, true);
        ArgumentCaptor<String> rawPassword = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> encodedPassword = ArgumentCaptor.forClass(String.class);
        // then
        verify(passwordEncoder).matches(rawPassword.capture(), encodedPassword.capture());
        assertThat(rawPassword.getValue()).isEqualTo(rawPassword2);
        assertThat(encodedPassword.getValue()).isEqualTo(u2.getPassword());
        assertThat(r3.getCode()).isEqualTo(Response.invalidPassword().getCode());

    }

    @Test   // passed
    void canAdminLogin(){
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u2));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        // when
        Response r = underTest.login(u2.getPhone(), rawPassword2, true);
        // then
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);
        verify(userTokenRepo).save(any());
    }


    /**--------------------------register test--------------------------------------------------**/

    @Test   // passed
    void canDetectErrorsWhenRegisteringOrdinary(){

        /* can detect invalid phone */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        // when
        Response r1 = underTest.register(u1, cToken1.getToken());
        ArgumentCaptor<String> phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userRepo).findByPhone(phone.capture());
        verify(userRepo, never()).save(any());
        assertThat(phone.getValue()).isEqualTo(u1.getPhone());
        assertThat(r1.getCode()).isEqualTo(Response.invalidPhone().getCode());

        /* can detect invalid confirmation token */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.empty());
        // when
        Response r2 = underTest.register(u1, cToken1.getToken());
        // then
        assertThat(r2.getCode()).isEqualTo(Response.invalidConfirmationToken().getCode());
        verify(userRepo, never()).save(any());
    }


    @Test   // passed
    void canRegisterOrdinary(){
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.empty());
        given(cTokenRepo.findByToken(anyString())).willReturn(Optional.of(cToken1));
        // when
        Response r = underTest.register(u1, cToken1.getToken());
        // then
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);
        assertThat(u1.getUserTag()).isEqualTo("ordinary");
        verify(userRepo).save(any());
    }

    @Test   // passed
    void canDetectErrorsWhenRegisteringAdmin(){

        /* can detect invalid phone */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u2));
        // when
        Response r1 = underTest.register(u2, cToken2.getToken(), registrationCode.getCode());
        ArgumentCaptor<String> phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userRepo).findByPhone(phone.capture());
        verify(userRepo, never()).save(any());
        assertThat(phone.getValue()).isEqualTo(u2.getPhone());
        assertThat(r1.getCode()).isEqualTo(Response.invalidPhone().getCode());


        /* can detect invalid registration code */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.empty());
        // when
        Response r2 = underTest.register(u2, cToken2.getToken(), registrationCode.getCode());
        ArgumentCaptor<String> regCode = ArgumentCaptor.forClass(String.class);
        // then
        verify(regCodeRepo).findByCode(regCode.capture());
        assertThat(regCode.getValue()).isEqualTo(registrationCode.getCode());
        assertThat(r2.getCode()).isEqualTo(Response.invalidRegistrationCode().getCode());
        verify(userRepo, never()).save(any());


        /* can detect invalid registration token */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.empty());
        given(regCodeRepo.findByCode(anyString())).willReturn(Optional.of(registrationCode));
        // when
        Response r3 = underTest.register(u2, cToken2.getToken(), registrationCode.getCode());
        ArgumentCaptor<String> cToken = ArgumentCaptor.forClass(String.class);
        // then
        verify(cTokenRepo).findByToken(cToken.capture());
        assertThat(cToken.getValue()).isEqualTo(cToken2.getToken());
        assertThat(r3.getCode()).isEqualTo(Response.invalidConfirmationToken().getCode());
        verify(userRepo, never()).save(any());

    }


    @Test   // passed
    void canRegisterAdmin(){
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.empty());
        given(cTokenRepo.findByToken(anyString())).willReturn(Optional.of(cToken2));
        given(regCodeRepo.findByCode(anyString())).willReturn(Optional.of(registrationCode));
        // when
        Response r = underTest.register(u2, cToken2.getToken(), registrationCode.getCode());
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        // then
        verify(userRepo).save(user.capture());
        assertThat(user.getValue().getPhone()).isEqualTo(u2.getPhone());
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);
    }


    /**--------------------------user info related test--------------------------------------------------**/

    @Test   // passed
    void canDetectErrorsWhenGettingUserInfo(){

        /* can detect invalid user phone */
        // when
        Response r1 = underTest.getUserInfo(u1.getPhone(), uToken1.getToken());
        ArgumentCaptor<String> phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userRepo).findByPhone(phone.capture());
        assertThat(phone.getValue()).isEqualTo(u1.getPhone());
        assertThat(r1.getCode()).isEqualTo(Response.invalidPhone().getCode());


        /* can detect invalid user token */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        // when
        Response r2 = underTest.getUserInfo(u1.getPhone(), uToken1.getToken());
        phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userTokenRepo).findByUserPhone(phone.capture());
        assertThat(phone.getValue()).isEqualTo(u1.getPhone());
        assertThat(r2.getCode()).isEqualTo(Response.invalidUserToken().getCode());

    }


    @Test   // passed
    void canGetUserInfo(){
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken1));
        // when
        Response r = underTest.getUserInfo(u1.getPhone(), uToken1.getToken());
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);
        Object data = r.getData();
        assertThat(data).isExactlyInstanceOf(User.class);
        User user = (User) data;
        assertThat(user.getPhone()).isEqualTo(u1.getPhone());
        assertThat(user.getPassword()).isEqualTo(u1.getPassword());
        assertThat(user.getUserTag()).isEqualTo(u1.getUserTag());
    }


    @Test   // passed
    void canDetectErrorsWhenUpdatingUserInfo(){

        /* can detect invalid user phone */
        // when
        Response r1 = underTest.update(u1, u1.getPhone(), uToken1.getToken());
        ArgumentCaptor<String> phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userRepo).findByPhone(phone.capture());
        verify(userRepo, never()).save(any());
        assertThat(phone.getValue()).isEqualTo(u1.getPhone());
        assertThat(r1.getCode()).isEqualTo(Response.invalidPhone().getCode());

        /* can detect invalid user token */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        // when
        Response r2 = underTest.update(u1, u1.getPhone(), uToken1.getToken());
        phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userTokenRepo).findByUserPhone(phone.capture());
        verify(userRepo, never()).save(any());
        assertThat(phone.getValue()).isEqualTo(u1.getPhone());
        assertThat(r2.getCode()).isEqualTo(Response.invalidUserToken().getCode());

    }


    @Test   // passed
    void canUpdateUserInfo(){
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken1));
        // when
        u1.setUserName("Elzat Ahmed");
        Response r = underTest.update(u1, u1.getPhone(), uToken1.getToken());
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        // then
        verify(userRepo).save(user.capture());
        assertThat(user.getValue().getPhone()).isEqualTo(u1.getPhone());
        assertThat(user.getValue().getUserName()).isEqualTo(u1.getUserName());
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);
    }


    @Test   // passed
    void canDetectErrorsWhenUpdatingPassword(){

        /* can detect invalid user phone */
        // when
        String newPassword = "300059";
        Response r1 = underTest.update(u1.getPhone(), uToken1.getToken(), cToken1.getToken(), newPassword);
        ArgumentCaptor<String> phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userRepo).findByPhone(phone.capture());
        assertThat(phone.getValue()).isEqualTo(u1.getPhone());
        verify(userRepo, never()).save(any());
        assertThat(r1.getCode()).isEqualTo(Response.invalidPhone().getCode());

        /* can detect invalid user token */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        // when
        Response r2 = underTest.update(u1.getPhone(), uToken1.getToken(), cToken1.getToken(), newPassword);
        phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userTokenRepo).findByUserPhone(phone.capture());
        verify(userRepo, never()).save(any());
        assertThat(phone.getValue()).isEqualTo(u1.getPhone());
        assertThat(r2.getCode()).isEqualTo(Response.invalidUserToken().getCode());

        /* can detect invalid confirmation token */
        // given
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken1));
        // when
        Response r3 = underTest.update(u1.getPhone(), uToken1.getToken(), cToken1.getToken(), newPassword);
        ArgumentCaptor<String> token = ArgumentCaptor.forClass(String.class);
        // then
        verify(cTokenRepo).findByToken(token.capture());
        verify(userRepo, never()).save(any());
        assertThat(token.getValue()).isEqualTo(cToken1.getToken());
        assertThat(r3.getCode()).isEqualTo(Response.invalidConfirmationToken().getCode());
    }


    @Test   // passed
    void canUpdatePassword(){
        // given
        String newPassword = "300059";
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken1));
        given(cTokenRepo.findByToken(anyString())).willReturn(Optional.of(cToken1));
        given(passwordEncoder.encode(newPassword)).willReturn(encoder.encode(newPassword));
        // when
        Response r = underTest.update(u1.getPhone(), uToken1.getToken(), cToken1.getToken(), newPassword);
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        // then
        verify(userRepo).save(user.capture());
        User u = user.getValue();
        assertThat(encoder.matches(newPassword, u.getPassword())).isTrue();
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);
    }



    /**--------------------------student certification test------------------------------------**/

    @Test   // passed
    void canDetectErrorsWhileStudentCertification(){

        /* can detect invalid user phone */
        // given
        StudentCertificate certificate = new StudentCertificate(
                null, "3019244086", "Elzat", "300059", false
        );
        u1.setStudentCertified(false);
        u1.setStudentId(null);
        // when
        Response r1 = underTest.studentCertification(certificate, u1.getPhone(), uToken1.getToken());
        ArgumentCaptor<String> phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userRepo).findByPhone(phone.capture());
        verify(userRepo, never()).save(any());
        verify(certificateRepo, never()).save(any());
        assertThat(phone.getValue()).isEqualTo(u1.getPhone());
        assertThat(r1.getCode()).isEqualTo(Response.invalidPhone().getCode());


        /* can detect invalid user token */
        // given
        certificate = new StudentCertificate(
                null, "3019244086", "Elzat", "300059", false
        );
        u1.setStudentCertified(false);
        u1.setStudentId(null);
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        // when
        Response r2 = underTest.studentCertification(certificate, u1.getPhone(), uToken1.getToken());
        phone = ArgumentCaptor.forClass(String.class);
        // then
        verify(userTokenRepo).findByUserPhone(phone.capture());
        verify(userRepo, never()).save(any());
        verify(certificateRepo, never()).save(any());
        assertThat(phone.getValue()).isEqualTo(u1.getPhone());
        assertThat(r2.getCode()).isEqualTo(Response.invalidUserToken().getCode());


        /* can detect invalid student info */
        // can detect no such info
        // given
        certificate = new StudentCertificate(
                null, "3019244086", "Elzat", "300059", false
        );
        u1.setStudentCertified(false);
        u1.setStudentId(null);
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken1));
        // when
        Response r3 = underTest.studentCertification(certificate, u1.getPhone(), uToken1.getToken());
        ArgumentCaptor<String> studentId = ArgumentCaptor.forClass(String.class);
        // then
        verify(certificateRepo).findByStudentId(studentId.capture());
        verify(userRepo, never()).save(any());
        verify(certificateRepo, never()).save(any());
        assertThat(studentId.getValue()).isEqualTo(certificate.getStudentId());
        assertThat(r3.getCode()).isEqualTo(Response.invalidStudentInfo().getCode());

        // can detect if certificate has been used before
        // given
        certificate = new StudentCertificate(
                null, "3019244086", "Elzat", "300059", true
        );
        u1.setStudentCertified(false);
        u1.setStudentId(null);
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken1));
        given(certificateRepo.findByStudentId(anyString())).willReturn(Optional.of(certificate));
        // when
        Response r4 = underTest.studentCertification(certificate, u1.getPhone(), uToken1.getToken());
        // then
        verify(userRepo, never()).save(any());
        verify(certificateRepo, never()).save(any());
        assertThat(r4.getCode()).isEqualTo(Response.invalidStudentInfo().getCode());

        // can detect if certificate has wrong info
        certificate = new StudentCertificate(
                null, "3019244086", "Elzat", "300059", false
        );
        StudentCertificate wrongCertificate = new StudentCertificate(
                null, "3019244086", "elzat", "305987", false
        );
        u1.setStudentCertified(false);
        u1.setStudentId(null);
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken1));
        given(certificateRepo.findByStudentId(anyString())).willReturn(Optional.of(certificate));
        // when
        Response r5 = underTest.studentCertification(wrongCertificate, u1.getPhone(), uToken1.getToken());
        // then
        verify(userRepo, never()).save(any());
        verify(certificateRepo, never()).save(any());
        assertThat(r4.getCode()).isEqualTo(Response.invalidStudentInfo().getCode());
    }

    @Test
    void canDoStudentCertification(){
        // given
        StudentCertificate certificate = new StudentCertificate(
                null, "3019244086", "Elzat", "300059", false
        );
        u1.setStudentCertified(false);
        u1.setStudentId(null);
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u1));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken1));
        given(certificateRepo.findByStudentId(anyString())).willReturn(Optional.of(certificate));
        // when
        Response r = underTest.studentCertification(certificate, u1.getPhone(), uToken1.getToken());
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<StudentCertificate> cert = ArgumentCaptor.forClass(StudentCertificate.class);
        // then
        verify(userRepo).save(user.capture());
        verify(certificateRepo).save(cert.capture());
        assertThat(user.getValue().getStudentId()).isEqualTo(certificate.getStudentId());
        assertThat(user.getValue().isStudentCertified()).isTrue();
        assertThat(cert.getValue().getStudentId()).isEqualTo(certificate.getStudentId());
        assertThat(cert.getValue().getStudentName()).isEqualTo(certificate.getStudentName());
        assertThat(cert.getValue().getStudentPassword()).isEqualTo(certificate.getStudentPassword());
        assertThat(cert.getValue().isUsed()).isTrue();
        assertThat(r.getCode()).isEqualTo(Response.SUCCESS_CODE);

    }

}
