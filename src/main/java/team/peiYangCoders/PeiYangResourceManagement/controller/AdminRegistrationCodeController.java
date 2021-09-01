package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.service.AdminRegistrationCodeService;

import java.util.UUID;

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
     * */
    @GetMapping("/admin-code")
    public Response addNewAdminCode(@RequestParam String phone){
        return adminCodeService.addCode(UUID.randomUUID().toString(), phone);
    }
}
