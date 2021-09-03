package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.service.ConfirmationTokenService;

@RestController
@RequestMapping("api/v1")
public class ConfirmationTokenController {

    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public ConfirmationTokenController(ConfirmationTokenService confirmationTokenService) {
        this.confirmationTokenService = confirmationTokenService;
    }

    /**
     * 发送验证码接口
     * 所有的验证码都使用该接口去发送
     * @param phone : 提供手机号
     * */
    @PostMapping("token")
    public Response sendConfirmationToken(@RequestParam String phone){
        ConfirmationToken cToken = confirmationTokenService.send(phone);
        return Response.success(cToken);
    }

}
