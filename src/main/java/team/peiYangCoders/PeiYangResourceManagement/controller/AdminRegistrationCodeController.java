package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.service.AdminRegistrationCodeService;
import team.peiYangCoders.PeiYangResourceManagement.service.UserTokenService;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class AdminRegistrationCodeController {

    private final AdminRegistrationCodeService adminCodeService;
    private final UserTokenService userTokenService;

    @Autowired
    public AdminRegistrationCodeController(AdminRegistrationCodeService adminCodeService,
                                           UserTokenService userTokenService) {
        this.adminCodeService = adminCodeService;
        this.userTokenService = userTokenService;
    }

    /**
     * 管理员可添加新的管理员注册代码
     * @param phone : 老管理员的手机号
     * @param userToken : the valid token system has distributed to the user
     * */
    @GetMapping("/admin-code")
    public Response addNewAdminCode(@RequestParam(name = "admin_phone") String phone,
                                    @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        return adminCodeService.addCode(UUID.randomUUID().toString(), phone);
    }
}
