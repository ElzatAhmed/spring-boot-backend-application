package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;

@Service
public interface AdminRegistrationCodeService {

    Response addCode(String phone, String userToken);

}
