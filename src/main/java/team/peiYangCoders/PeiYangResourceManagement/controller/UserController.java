package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserInfo;
import team.peiYangCoders.PeiYangResourceManagement.service.AdminRegistrationCodeService;
import team.peiYangCoders.PeiYangResourceManagement.service.ConfirmationTokenService;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;

import java.util.Optional;
import java.util.UUID;

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
     * 登录接口：
     * 普通用户登录
     * @param info : 用户信息
     *             前端需提供信息如下：(带有星号为必填)
     *             {
     *                  "phone": <用户手机号>,       *
     *                  "password": <用户登录密码>    *
     *             }
     * */
    @PostMapping("/login")
    public Response ordinaryLogin(@RequestBody UserInfo info){
        return userService.login(info);
    }


    /**
     * 注册接口：
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
     * @param token : 短信验证码
     * */
    @PostMapping("/register")
    public Response ordinaryRegister(
            @RequestBody UserInfo info,
            @RequestParam String token){
        Optional<User> user = userService.getByPhone(info.getPhone());
        if(user.isPresent())
            return Response.errorMessage(Response.invalidPhone);
        Response response = confirmationTokenService.receive(info.getPhone(), token);
        if(response.getCode() == Response.OK)
            return Response.okMessage(userService.addNewUser(info));
        return response;
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
     * @param token : 短信验证码
     * */
    @PutMapping("/update/password")
    public Response updatePassword(
            @RequestBody UserInfo info,
            @RequestParam String newPassword, String token){
        Optional<User> user = userService.getByPhone(info.getPhone());
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Response response = confirmationTokenService.receive(info.getPhone(), token);
        if(response.getCode() == Response.OK)
            return Response.okMessage(userService.updatePassword(user.get(), newPassword));
        return response;
    }
}
