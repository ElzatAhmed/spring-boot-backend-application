package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;

@Service
public interface AdminRegistrationCodeService {


    /**
     * verify and add new code
     * @param phone : admin phone
     * @param userToken : user token
     * */
    Response addCode(String phone, String userToken);

}
