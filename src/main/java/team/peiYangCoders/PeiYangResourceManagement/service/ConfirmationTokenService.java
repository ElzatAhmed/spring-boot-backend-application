package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;

@Service
public interface ConfirmationTokenService {

    /**
     * send and save confirmation token
     * @param phone : phone number
     * */
    ConfirmationToken send(String phone);

}
