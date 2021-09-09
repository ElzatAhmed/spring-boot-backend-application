package team.peiYangCoders.PeiYangResourceManagement.studentCertification;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import team.peiYangCoders.PeiYangResourceManagement.repository.StudentCertificateRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
public class StudentCertificationRelatedApiTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper mapper;

    @Autowired private UserRepository userRepo;

    @Autowired private StudentCertificateRepository certificateRepo;


    @Test void canDoStudentCertification(){

    }

}
