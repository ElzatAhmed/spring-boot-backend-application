package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;

@Service
public interface ConfirmationTokenService {

    ConfirmationToken send(String phone);

}
