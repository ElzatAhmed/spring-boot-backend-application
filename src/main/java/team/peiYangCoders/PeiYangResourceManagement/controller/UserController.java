package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserInfo;
import team.peiYangCoders.PeiYangResourceManagement.service.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    private final UserService userService;
    private final AdminRegistrationCodeService adminCodeService;
    private final ConfirmationTokenService confirmationTokenService;
    private final StudentIdService studentIdService;

    @Autowired
    public UserController(UserService userService,
                          AdminRegistrationCodeService adminCodeService,
                          ConfirmationTokenService confirmationTokenService,
                          StudentIdService studentIdService) {
        this.userService = userService;
        this.adminCodeService = adminCodeService;
        this.confirmationTokenService = confirmationTokenService;
        this.studentIdService = studentIdService;
    }

    /**
     * 登录接口：
     * 普通用户登录
     * @param info : 用户信息
     *             前端需提供信息如下：
     *             {
     *                  "phone": <用户手机号>,
     *                  "password": <用户登录密码>
     *             }
     * */
    @PostMapping("/user/login")
    public Response ordinaryLogin(@RequestBody Body.Login info){
        return userService.ordinaryLogin(info);
    }


    /**
     * 注册接口：
     * 普通用户注册
     * @param info : 用户信息
     *             前端需提供的信息如下:
     *             {
     *                  "phone": "17627669320",             *
     *                  "name": "Elzat",                    *
     *                  "password": "300059",               *
     *             }
     * @param token : 短信验证码
     * */
    @PostMapping("/user/register")
    public Response ordinaryRegister(
            @RequestBody Body.Register info,
            @RequestParam String token){
        if(userService.getByPhone(info.getPhone()).isPresent())
            return Response.errorMessage(Response.invalidPhone);
        Response response = confirmationTokenService.receive(info.getPhone(), token);
        if(response.getCode() == Response.OK)
            return Response.okMessage(userService.addNewUser(info, false));
        return response;
    }

    /**
     * 更改密码接口:
     * @param info : 用户信息
     *             前端需提供的信息如下:
     *             {
     *                  "newPassword": "",  <新密码>
     *                  "token": ""         <短信验证码>
     *             }
     * @param phone : 手机号
     * */
    @PutMapping("user/update/password")
    public Response updatePassword(
            @RequestBody Body.NewPassword info,
            @RequestParam String phone){
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Response response = confirmationTokenService.receive(phone, info.getToken());
        if(response.getCode() == Response.OK)
            return Response.okMessage(userService.updatePassword(user.get(), info.getNewPassword()));
        return response;
    }

    /**
     * 用户更改信息接口:
     * @param detail : 新的用户信息,若无变化继续发送老信息
     *               前端需提供的信息如下:
     *               {
     *                      "name": "",
     *                      "qqId": "",
     *                      "wechatId": "",
     *                      "avatarUrl": ""
     *               }
     * */
    @PutMapping("user/update")
    public Response updateInfo(
            @RequestBody Body.UserDetail detail,
            @RequestParam String phone){
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        User newUser = user.get();
        newUser.setName(detail.getName());
        newUser.setQqId(detail.getQqId());
        newUser.setWechatId(detail.getWechatId());
        newUser.setAvatarUrl(detail.getAvatarUrl());
        return Response.okMessage(userService.save(newUser));
    }

    /**
     * 学生认证接口
     * */
    @PostMapping("user/certify")
    public Response studentCertification(
            @RequestBody Body.Certification certificate,
            @RequestParam String phone){
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        return studentIdService.studentCertification(
                certificate.getStudentId(),
                certificate.getStudentName(),
                certificate.getStudentPassword());
    }


    /**
     * 登录接口:
     * 管理员登录
     * @param info : 用户信息
     *                   前端需提供信息如下：
     *                   {
     *                        "phone": <用户手机号>,
     *                        "password": <用户登录密码>
     *                   }
     * */
    @PostMapping("admin/login")
    public Response adminLogin(@RequestBody Body.Login info){
        Response response = userService.isAdmin(info.getPhone());
        if(response.getCode() == Response.ERROR)
            return response;
        UserInfo userInfo = (UserInfo) response.getData();
        if(!userInfo.getPassword().equals(info.getPassword()))
            return Response.errorMessage(Response.invalidPassword);
        return Response.okMessage(userInfo);
    }

    /**
     * 注册接口:
     * 管理员注册
     * @param info : 用户信息
     *             前端可提供的信息如下:(带有星号为必填)
     *             {
     *                  "phone": "17627669320",             *
     *                  "name": "Elzat",                    *
     *                  "password": "300059",               *
     *             }
     * @param code : 管理员验证码，必须是数据库里存储的有效的验证码
     * @param token : 短信验证码
     * */
    @PostMapping("admin/register")
    public Response adminRegister(
            @RequestBody Body.Register info,
            @RequestParam String code, String token){
        if(userService.getByPhone(info.getPhone()).isPresent())
            return Response.errorMessage(Response.invalidPhone);
        if(!adminCodeService.isValid(code))
            return Response.errorMessage(Response.invalidCode);
        Response response = confirmationTokenService.receive(info.getPhone(), token);
        if(response.getCode() == Response.OK)
            return Response.okMessage(userService.addNewUser(info, true));
        return response;
    }


    /**
     * 获取所有用户接口：
     * 管理员使用该接口获取所有用户信息
     * @param phone : 用户手机号，用来判断该用户是否为管理员用户
     * */
    @GetMapping("admin/users")
    public Response getAll(@RequestParam String phone){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.OK)
            return Response.okMessage(userService.getAll());
        return response;
    }

    /**
     * 管理员利用过滤器获得用户:
     * @param filter : 过滤参数
     *               {
     *                      "phone": "",
     *                      "name": "",
     *                      "qqId": "",
     *                      "wechatId": ""
     *               }
     * @param phone : 管理员手机号
     * */
    @GetMapping("admin/users/filter")
    public Response getByFilter(
            @RequestBody UserFilter filter,
            @RequestParam String phone){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return Response.okMessage(userService.getByFilter(filter));
    }

    /**
     * 删除指定用户
     * @param adminPhone : 管理员手机号
     * @param userPhone : 要删除的用户的手机号
     * */
    @PostMapping("admin/users/delete/")
    public Response deleteUser(@RequestParam String adminPhone, String userPhone){
        Response response = userService.isAdmin(adminPhone);
        if(response.getCode() == Response.ERROR)
            return response;
        return userService.deleteUser(userPhone);
    }
}
