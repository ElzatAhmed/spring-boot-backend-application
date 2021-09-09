package team.peiYangCoders.PeiYangResourceManagement.studentCertification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentCertificate;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.*;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;
import team.peiYangCoders.PeiYangResourceManagement.service.implementation.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils.randomUser;

@ExtendWith(MockitoExtension.class)
public class StudentCertificationRelatedServiceTest {

    @Mock private UserRepository userRepo;
    @Mock private UserTokenRepository userTokenRepo;
    @Mock private BCryptPasswordEncoder encoder;
    @Mock private ConfirmationTokenRepository cTokenRepo;
    @Mock private AdminRegistrationCodeRepository adminCodeRepo;
    @Mock private StudentCertificateRepository certificateRepo;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepo, userTokenRepo, cTokenRepo,
                adminCodeRepo, certificateRepo, encoder);
    }


    @Test void canDoStudentCertification(){

        // given
        User u = randomUser();
        u.setPhone("17627669320");
        u.setStudentCertified(false);
        u.setStudentId(null);
        UserToken uToken = new UserToken(
                u.getPhone(), u.getUserName(), "123456"
        );
        StudentCertificate certificate = new StudentCertificate(
                null, "3019244086", "AiLi", "300059", false
        );
        given(userRepo.findByPhone(anyString())).willReturn(Optional.of(u));
        given(userTokenRepo.findByUserPhone(anyString())).willReturn(Optional.of(uToken));
        given(certificateRepo.findByStudentId(anyString())).willReturn(Optional.of(certificate));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<StudentCertificate> certificateCaptor = ArgumentCaptor
                .forClass(StudentCertificate.class);

        // when
        Response response = userService.studentCertification(certificate, u.getPhone(), uToken.getToken());

        // then
        verify(userRepo).save(userCaptor.capture());
        verify(certificateRepo).save(certificateCaptor.capture());
        User capturedUser = userCaptor.getValue();
        StudentCertificate capturedCertificate = certificateCaptor.getValue();
        assertThat(capturedUser.isStudentCertified()).isTrue();
        assertThat(capturedUser.getStudentId()).isEqualTo(certificate.getStudentId());
        assertThat(capturedUser.getPhone()).isEqualTo(u.getPhone());
        assertThat(capturedCertificate.isUsed()).isTrue();
        assertThat(capturedCertificate.getStudentId()).isEqualTo(certificate.getStudentId());
        assertThat(response.getCode()).isEqualTo(Response.SUCCESS_CODE);
        assertThat(response.getData()).isEqualTo(null);

    }
}
