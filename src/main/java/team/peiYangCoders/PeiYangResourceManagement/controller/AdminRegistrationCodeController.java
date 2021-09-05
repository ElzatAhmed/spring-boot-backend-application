package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.service.AdminRegistrationCodeService;

@RestController
@RequestMapping("api/v1")
public class AdminRegistrationCodeController {

    private final AdminRegistrationCodeService adminCodeService;

    @Autowired
    public AdminRegistrationCodeController(AdminRegistrationCodeService adminCodeService) {
        this.adminCodeService = adminCodeService;
    }

    /**
     * 管理员可添加新的管理员注册代码
     * @param phone : 老管理员的手机号
     * @param userToken : the valid token system has distributed to the user
     * */
    @GetMapping("/admin-code")
    public Response addNewAdminCode(@RequestParam(name = "phone") String phone,
                                    @RequestParam(name = "uToken") String userToken){
        return adminCodeService.addCode(phone, userToken);
    }
}
