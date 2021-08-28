package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserInfo;
import team.peiYangCoders.PeiYangResourceManagement.service.AdminRegistrationCodeService;
import team.peiYangCoders.PeiYangResourceManagement.service.ConfirmationTokenService;
import team.peiYangCoders.PeiYangResourceManagement.service.userService.UserService;

import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;
    private final AdminRegistrationCodeService adminCodeService;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public UserController(UserService userService,
                          AdminRegistrationCodeService adminCodeService,
                          ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.adminCodeService = adminCodeService;
        this.confirmationTokenService = confirmationTokenService;
    }

    /**
     * 获取所有用户接口：
     * 管理员使用该接口获取所有用户信息
     * @param phone : 用户手机号，用来判断该用户是否为管理员用户
     * */
    @GetMapping("/get/all")
    public Response getAll(@RequestParam String phone){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.OK)
            return userService.getAll();
        return response;
    }

    /**
     * 登录接口_1：
     * 普通用户登录
     * @param info : 用户信息
     *             前端需提供信息如下：(带有星号为必填)
     *             {
     *                  "phone": <用户手机号>,       *
     *                  "password": <用户登录密码>    *
     *             }
     * */
    @PostMapping("/login/ordinary")
    public Response ordinaryLogin(@RequestBody UserInfo info){
        return userService.login(info);
    }


    /**
     * 注册接口_1：
     * 普通用户注册
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
     * */
    @PostMapping("/register/ordinary")
    public Response ordinaryRegister(@RequestBody UserInfo info){
        info.setTag("ordinary");
        info.setEnabled(false);
        return userService.register(info);
    }

    /**
     * 登录接口_2:
     * 管理员登录
     * @param info : 用户信息
     *                   前端需提供信息如下：(带有星号为必填)
     *                   {
     *                        "phone": <用户手机号>,       *
     *                        "password": <用户登录密码>    *
     *                   }
     * */
    @PostMapping("/login/admin")
    public Response adminLogin(@RequestBody UserInfo info){
        Response response = userService.isAdmin(info.getPhone());
        if(response.getCode() == Response.ERROR)
            return response;
        return userService.login(info);
    }

    /**
     * 注册接口_2:
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
     * */
    @PostMapping("/register/admin")
    public Response adminRegister(
            @RequestBody UserInfo info,
            @RequestParam String code){
        if(!adminCodeService.isValid(code))
            return Response.errorMessage(Response.codeIsUsed);
        info.setTag("admin");
        info.setEnabled(false);
        return userService.register(info);
    }

    /**
     * 更改密码接口:
     * @param info : 用户信息
     *             前端可提供的信息如下:(带有星号为必填)
     *             {
     *                  "phone": "17627669320",             *
     *                  "name": "Elzat",
     *                  "qqId": "1208915986",
     *                  "wechatId": "1208915986",
     *                  "password": "300059",               *
     *                  "avatarUrl": "https://baidu.com"
     *             }
     * @param newPassword : 新密码
     * */
    @PutMapping("/update/password")
    public Response updatePassword(
            @RequestBody UserInfo info,
            @RequestParam String newPassword){
        return userService.updatePassword(info, newPassword);
    }

    /**
     * 发送验证码接口
     * */
    @PostMapping("/token/send")
    public Response sendConfirmationToken(@RequestBody UserInfo info){
        Optional<User> user = userService.getByPhone(info.getPhone());
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        ConfirmationToken cToken = confirmationTokenService.send(user.get());
        return Response.okMessage(cToken);
    }

    /**
     * 接受验证码接口
     * */
    @GetMapping("/token/get")
    public Response receiveConfirmationToken(
            @RequestBody UserInfo info,
            @RequestParam String token){
        Optional<User> user = userService.getByPhone(info.getPhone());
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Response response = confirmationTokenService.receive(user.get(), token);
        if(response.getCode() == Response.OK) userService.setEnabled(user.get());
        return response;
    }
}
