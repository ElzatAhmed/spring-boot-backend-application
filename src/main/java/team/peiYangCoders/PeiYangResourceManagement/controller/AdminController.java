package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserInfo;
import team.peiYangCoders.PeiYangResourceManagement.service.*;

import java.util.UUID;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    private final UserService userService;
    private final ResourceService resourceService;
    private final OrderService orderService;
    private final AdminRegistrationCodeService adminCodeService;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public AdminController(UserService userService,
                           ResourceService resourceService,
                           OrderService orderService,
                           AdminRegistrationCodeService adminCodeService,
                           ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.resourceService = resourceService;
        this.orderService = orderService;
        this.adminCodeService = adminCodeService;
        this.confirmationTokenService = confirmationTokenService;
    }

    /**
     * 登录接口:
     * 管理员登录
     * @param info : 用户信息
     *                   前端需提供信息如下：(带有星号为必填)
     *                   {
     *                        "phone": <用户手机号>,       *
     *                        "password": <用户登录密码>    *
     *                   }
     * */
    @PostMapping("/login")
    public Response adminLogin(@RequestBody UserInfo info){
        return userService.isAdmin(info.getPhone());
    }

    /**
     * 注册接口:
     * 管理员注册
     * @param info : 用户信息
     *             前端可提供的信息如下:(带有星号为必填)
     *             {
     *                  "phone": "17627669320",             *
     *                  "name": "Elzat",                    *
     *                  "qqId": "1208915986",
     *                  "wechatId": "1208915986",
     *                  "password": "300059",               *
     *                  "avatarUrl": "https://baidu.com"
     *             }
     * @param code : 管理员验证码，必须是数据库里存储的有效的验证码
     * @param token : 短信验证码
     * */
    @PostMapping("/register")
    public Response adminRegister(
            @RequestBody UserInfo info,
            @RequestParam String code, String token){
        if(!adminCodeService.isValid(code))
            return Response.errorMessage(Response.codeIsUsed);
        Response response = confirmationTokenService.receive(info.getPhone(), token);
        if(response.getCode() == Response.OK) {
            info.setTag("admin");
            return Response.okMessage(userService.addNewUser(info));
        }
        return response;
    }


    /**
     * 管理员可添加新的管理员注册代码
     * @param phone : 老管理员的手机号
     * */
    @GetMapping("/new_code")
    public Response addNewAdminCode(@RequestParam String phone){
        return adminCodeService.addCode(UUID.randomUUID().toString(), phone);
    }


    /**
     * 获取所有用户接口：
     * 管理员使用该接口获取所有用户信息
     * @param phone : 用户手机号，用来判断该用户是否为管理员用户
     * */
    @GetMapping("/get/user/all")
    public Response getAll(@RequestParam String phone){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.OK)
            return Response.okMessage(userService.getAll());
        return response;
    }

    /**
     * 删除指定用户
     * @param adminPhone : 管理员手机号
     * @param userPhone : 要删除的用户的手机号
     * */
    @PostMapping("/delete/user/")
    public Response deleteUser(@RequestParam String adminPhone, String userPhone){
        Response response = userService.isAdmin(adminPhone);
        if(response.getCode() == Response.ERROR)
            return response;
        return userService.deleteUser(userPhone);
    }


    /**
     * 获取所有资源
     * @param phone : 管理员手机号
     * */
    @GetMapping("/get/resource/all")
    public Response getAllResources(@RequestParam String phone){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return Response.okMessage(resourceService.getAllOrderByTime());
    }

    /**
     * 获取所有未经审核的资源
     * @param phone : 管理员手机号
     * */
    @GetMapping("/get/resource/unverified")
    public Response getAllUnverifiedResources(@RequestParam String phone){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return Response.okMessage(resourceService.getAllOrderByTime(false));
    }

    /**
     * 获取所有已经审核的资源
     * @param phone : 管理员手机号
     * */
    @GetMapping("/get/resource/verified")
    public Response getAllVerifiedResources(@RequestParam String phone){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return Response.okMessage(resourceService.getAllOrderByTime(true));
    }

    /**
     * 通过审核
     * @param phone : 管理员手机号
     * @param code : 资源代码
     * */
    @PutMapping("/verify/accept")
    public Response acceptResource(@RequestParam String phone, UUID code){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return resourceService.verifyResource(code, true);
    }

    /**
     * 审核拒绝
     * @param phone : 管理员手机号
     * @param code : 资源代码
     * */
    @PutMapping("/verify/reject")
    public Response rejectResource(@RequestParam String phone, UUID code){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return resourceService.verifyResource(code, false);
    }
}
