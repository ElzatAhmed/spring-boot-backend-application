package team.peiYangCoders.PeiYangResourceManagement.userInfo;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import team.peiYangCoders.PeiYangResourceManagement.repository.ConfirmationTokenRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserTokenRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
public class UserInfoRelatedApiTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper mapper;

    @Autowired private UserRepository userRepo;

    @Autowired private UserTokenRepository uTokenRepo;

    @Autowired private ConfirmationTokenRepository cTokenRepo;



}
