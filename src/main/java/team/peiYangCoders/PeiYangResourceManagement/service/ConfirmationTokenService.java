package team.peiYangCoders.PeiYangResourceManagement.service;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.SMSConfig;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.repository.ConfirmationTokenRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepo;
    private final SMSConfig smsConfig;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepo,
                                    SMSConfig smsConfig) {
        this.confirmationTokenRepo = confirmationTokenRepo;
        this.smsConfig = smsConfig;
    }


    // send confirmation token interface for upper layer
    public ConfirmationToken send(String phone){
        Optional<ConfirmationToken> maybe = confirmationTokenRepo.findByUserPhone(phone);
        if(maybe.isPresent() && !maybe.get().isConfirmed() && maybe.get().getExpiresAt().isAfter(LocalDateTime.now()))
            return maybe.get();
        ConfirmationToken cToken = ConfirmationToken.construct(smsConfig.getTokenLen(),
                smsConfig.getLatency(), phone);
        confirmationTokenRepo.save(cToken);
        return cToken;
//        sendConfirmationToken(phone, cToken.getToken());
    }


    /**----------------------private methods----------------------------------**/

    private void sendConfirmationToken(String phone, String token){
        String[] params = {token, smsConfig.getLatency().toString()};
        SmsSingleSender sender = new SmsSingleSender(smsConfig.getAppId(), smsConfig.getAppKey());
        try {
            SmsSingleSenderResult result = sender.sendWithParam(
                    "86", phone, smsConfig.getTemplateId(),
                    params, smsConfig.getSignature(), "", ""
            );
            System.out.println(result);
        } catch (HTTPException | IOException e) {
            e.printStackTrace();
        }
    }
}
