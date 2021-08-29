package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.service.AdminRegistrationCodeService;

import java.util.UUID;

@RestController
@RequestMapping("api/admin/new-code")
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
    @PostMapping("")
    public Response addNewAdminCode(@RequestParam String phone){
        return adminCodeService.addCode(UUID.randomUUID().toString(), phone);
    }
}
