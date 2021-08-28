package team.peiYangCoders.PeiYangResourceManagement.service;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.config.SMSParam;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.ConfirmationTokenRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepo;
    private final SMSParam smsParam;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepo,
                                    SMSParam smsParam) {
        this.confirmationTokenRepo = confirmationTokenRepo;
        this.smsParam = smsParam;
    }

    public void save(ConfirmationToken confirmationToken){
        confirmationTokenRepo.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getByToken(String token){
        return confirmationTokenRepo.findByToken(token);
    }

    public ConfirmationToken send(User user){
        ConfirmationToken cToken = ConfirmationToken.construct(smsParam.getTokenLen(),
                smsParam.getLatency(), user);
        confirmationTokenRepo.save(cToken);
        sendConfirmationToken(user.getPhone(), cToken.getToken());
        return cToken;
    }

    public Response receive(User user, String token){
        Optional<ConfirmationToken> cToken = confirmationTokenRepo.findByToken(token);
        if(!cToken.isPresent()
                || !cToken.get().getUser().getPhone().equals(user.getPhone()))
            return Response.errorMessage(Response.invalidToken);
        LocalDateTime now = LocalDateTime.now();
        boolean valid = now.isBefore(cToken.get().getExpiresAt());
        if(valid) {
            cToken.get().setConfirmedAt(now);
            confirmationTokenRepo.save(cToken.get());
            return Response.okMessage();
        }
        confirmationTokenRepo.delete(cToken.get());
        return Response.errorMessage(Response.tokenExpired);
    }

    private void sendConfirmationToken(String phone, String token){
        String[] params = {token, smsParam.getLatency().toString()};
        SmsSingleSender sender = new SmsSingleSender(smsParam.getAppId(), smsParam.getAppKey());
        try {
            SmsSingleSenderResult result = sender.sendWithParam(
                    "86", phone, smsParam.getTemplateId(),
                    params, smsParam.getSignature(), "", ""
            );
        } catch (HTTPException | IOException e) {
            e.printStackTrace();
        }
    }
}
