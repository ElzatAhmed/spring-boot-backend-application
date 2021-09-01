package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.service.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final StudentIdService studentIdService;
    private final AdminRegistrationCodeService adminCodeService;

    @Autowired
    public UserController(UserService userService,
                          ConfirmationTokenService confirmationTokenService,
                          StudentIdService studentIdService,
                          AdminRegistrationCodeService adminCodeService) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.studentIdService = studentIdService;
        this.adminCodeService = adminCodeService;
    }


    @GetMapping("/user")
    public Response ordinaryLogin(@RequestParam String phone, String password){
        Body.Login info = new Body.Login(phone, password);
        return userService.ordinaryLogin(info);
    }



    @PostMapping("/user")
    public Response ordinaryRegister(
            @RequestBody Body.Register info,
            @RequestParam String token){
        if(userService.getByPhone(info.getPhone()).isPresent())
            return Response.invalidPhone();
        Response response = confirmationTokenService.receive(info.getPhone(), token);
        if(response.succeeded())
            return Response.success(userService.addNewUser(info, false));
        return response;
    }


    @PutMapping("user/password")
    public Response updatePassword(
            @RequestBody Body.NewPassword info,
            @RequestParam String phone){
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.invalidPhone();
        Response response = confirmationTokenService.receive(phone, info.getToken());
        if(response.succeeded())
            return Response.success(userService.updatePassword(user.get(), info.getNewPassword()));
        return response;
    }


    @PutMapping("user")
    public Response updateInfo(
            @RequestBody Body.UserDetail detail,
            @RequestParam String phone){
        return userService.updateInfo(detail, phone);
    }


    @PostMapping("user/student")
    public Response studentCertification(
            @RequestBody Body.Certification certificate,
            @RequestParam String phone){
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.invalidPhone();
        return studentIdService.studentCertification(
                user.get(),
                certificate.getStudentId(),
                certificate.getStudentName(),
                certificate.getStudentPassword());
    }



    @GetMapping("admin")
    public Response adminLogin(@RequestParam String phone, String password){
        Body.Login info = new Body.Login(phone, password);
        return userService.adminLogin(info);
    }


    @PostMapping("admin")
    public Response adminRegister(
            @RequestBody Body.Register info,
            @RequestParam String code, String token){
        if(userService.getByPhone(info.getPhone()).isPresent())
            return Response.invalidPhone();
        if(!adminCodeService.isValid(code))
            return Response.invalidRegistrationCode();
        Response response = confirmationTokenService.receive(info.getPhone(), token);
        if(response.succeeded())
            return Response.success(userService.addNewUser(info, true));
        return response;
    }


    @GetMapping("users")
    public Response getByFilter(@RequestParam(required = false) String phone,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String qqId,
                                @RequestParam(required = false) String wechatId){
        UserFilter filter = new UserFilter();
        filter.setPhone(phone);
        filter.setName(name);
        filter.setQqId(qqId);
        filter.setWechatId(wechatId);
        System.out.println(filter);
        return Response.success(userService.getByFilter(filter));
    }


    @DeleteMapping("users")
    public Response deleteUser(@RequestParam String adminPhone, String userPhone){
        return userService.deleteUser(adminPhone, userPhone);
    }
}
