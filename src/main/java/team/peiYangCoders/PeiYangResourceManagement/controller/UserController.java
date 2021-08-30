package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.service.*;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/user")
    public Response ordinaryLogin(@RequestBody Body.Login info){
        return userService.ordinaryLogin(info);
    }



    @PostMapping("/user")
    public Response ordinaryRegister(
            @RequestBody Body.Register info,
            @RequestParam String token){
        return userService.ordinaryRegister(info, token);
    }


    @PutMapping("user/password")
    public Response updatePassword(
            @RequestBody Body.NewPassword info,
            @RequestParam String phone){
        return userService.updatePassword(info, phone);
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
        return userService.studentCertification(certificate, phone);
    }



    @PostMapping("admin")
    public Response adminLogin(@RequestBody Body.Login info){
        return userService.adminLogin(info);
    }


    @PostMapping("admin")
    public Response adminRegister(
            @RequestBody Body.Register info,
            @RequestParam String code, String token){
        return userService.adminRegister(info, code, token);
    }


    @GetMapping("users")
    public Response getByFilter(@RequestBody UserFilter filter){
        return Response.okMessage(userService.getByFilter(filter));
    }


    @DeleteMapping("users")
    public Response deleteUser(@RequestParam String adminPhone, String userPhone){
        return userService.deleteUser(adminPhone, userPhone);
    }
}
