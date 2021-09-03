package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.service.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final StudentIdService studentIdService;
    private final AdminRegistrationCodeService adminCodeService;
    private final UserTokenService userTokenService;

    @Autowired
    public UserController(UserService userService,
                          ConfirmationTokenService confirmationTokenService,
                          StudentIdService studentIdService,
                          AdminRegistrationCodeService adminCodeService,
                          UserTokenService userTokenService) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.studentIdService = studentIdService;
        this.adminCodeService = adminCodeService;
        this.userTokenService = userTokenService;
    }


    /**
     * user login api
     * @param userPhone : user phone number
     * @param password : user password
     * */
    @GetMapping("/user")
    public Response ordinaryLogin(@RequestParam(name = "user_phone") String userPhone,
                                  @RequestParam(name = "password") String password){
        Body.Login info = new Body.Login(userPhone, password);
        Response response = userService.ordinaryLogin(info);
        return response.succeeded() ? userTokenService.provideNewCode(userPhone) : response;
    }



    /**
     * user register api
     * @param info : register information body
     *             {
     *                  "phone": "",
     *                  "name": "",
     *                  "password": ""
     *             }
     * @param confirmationToken : the confirmation token user received
     * */
    @PostMapping("/user")
    public Response ordinaryRegister(
            @RequestBody Body.Register info,
            @RequestParam(name = "confirm_token") String confirmationToken){
        if(userService.getByPhone(info.getUser_phone()).isPresent())
            return Response.invalidPhone();
        Response response = confirmationTokenService.receive(info.getUser_phone(), confirmationToken);
        if(response.succeeded()) {
            userService.addNewUser(info, false);
            return Response.success(null);
        }
        return response;
    }


    /**
     * user update password api
     * @param info : NewPassword information body
     *             {
     *                  "newPassword": "",
     *                  "token": ""
     *             }
     * @param userPhone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * */
    @PutMapping("user/password")
    public Response updatePassword(
            @RequestBody Body.NewPassword info,
            @RequestParam(name = "user_phone") String userPhone,
            @RequestParam(name = "user_token") String userToken){
        Optional<User> user = userService.getByPhone(userPhone);
        if(!user.isPresent())
            return Response.invalidPhone();
        Response response = confirmationTokenService.receive(userPhone, info.getConfirm_token());
        if(response.failed())
            return response;
        if(!userTokenService.tokenIsValid(userPhone, userToken))
            return Response.invalidUserToken();
        userService.updatePassword(user.get(), info.getNew_password());
        return Response.success(null);
    }


    /**
     * user update information api
     * @param user : UserDetail information body
     *               {
     *                      "user_name": "",
     *                      "qq_id": "",
     *                      "wechat_id": "",
     *                      "avatar_url": ""
     *               }
     * @param userPhone : user phone number
     * */
    @PutMapping("user")
    public Response updateInfo(
            @RequestBody User user,
            @RequestParam(name = "user_phone") String userPhone,
            @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.tokenIsValid(userPhone, userToken))
            return Response.invalidUserToken();
        return userService.updateInfo(user, userPhone);
    }


    /**
     * user student certification api
     * @param certificate : Certification information body
     *                    {
     *                          "studentId": "",
     *                          "studentName": "",
     *                          "studentPassword": ""
     *                    }
     * @param userPhone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * */
    @PostMapping("user/student")
    public Response studentCertification(
            @RequestBody Body.Certification certificate,
            @RequestParam(name = "user_phone") String userPhone,
            @RequestParam(name = "user_token") String userToken){
        Optional<User> user = userService.getByPhone(userPhone);
        if(!user.isPresent())
            return Response.invalidPhone();
        if(!userTokenService.tokenIsValid(userPhone, userToken))
            return Response.invalidUserToken();
        return studentIdService.studentCertification(
                user.get(),
                certificate.getStudent_id(),
                certificate.getStudent_name(),
                certificate.getStudent_password());
    }


    /**
     * administrator login api
     * @param userPhone : admin phone number
     * @param password : admin password
     * */
    @GetMapping("admin")
    public Response adminLogin(@RequestParam(name = "user_phone") String userPhone,
                               @RequestParam(name = "password") String password){
        Body.Login info = new Body.Login(userPhone, password);
        Response response = userService.adminLogin(info);
        return response.succeeded() ? userTokenService.provideNewCode(userPhone) : response;
    }


    /**
     * administrator register api
     * @param info : register information body
     *             {
     *                  "phone": "",
     *                  "name": "",
     *                  "password": ""
     *             }
     * @param registrationCode : admin registration code
     * @param confirmationToken : the confirmation token admin received
     * */
    @PostMapping("admin")
    public Response adminRegister(
            @RequestBody Body.Register info,
            @RequestParam(name = "reg_code") String registrationCode,
            @RequestParam(name = "confirm_token") String confirmationToken){
        System.out.println(info);
        if(userService.getByPhone(info.getUser_phone()).isPresent())
            return Response.invalidPhone();
        if(!adminCodeService.isValid(registrationCode))
            return Response.invalidRegistrationCode();
        Response response = confirmationTokenService.receive(info.getUser_phone(), confirmationToken);
        if(response.succeeded()) {
            userService.addNewUser(info, true);
            return Response.success(null);
        }
        return response;
    }


    /**
     * get user by filter api
     * @param userPhone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * @param phone : filter param, not required
     * @param name : filter param, not required
     * @param qqId : filter param, not required
     * @param wechatId : filter param, not required
     * */
    @GetMapping("users")
    public Response getByFilter(
            @RequestParam(name = "user_phone") String userPhone,
            @RequestParam(name = "user_token") String userToken,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "qq_id", required = false) String qqId,
            @RequestParam(name = "wechat_id", required = false) String wechatId,
            @RequestParam(name = "student_certified", required = false) Boolean student_certified){
        if(!userService.getByPhone(userPhone).isPresent())
            return Response.invalidPhone();
        if(!userTokenService.tokenIsValid(userPhone, userToken))
            return Response.invalidUserToken();
        UserFilter filter = new UserFilter();
        filter.setPhone(phone);
        filter.setName(name);
        filter.setQqId(qqId);
        filter.setWechatId(wechatId);
        filter.setStudentCertified(student_certified);
        return Response.success(userService.getByFilter(filter));
    }


    /**
     * admin delete user api
     * @param adminPhone : admin phone number
     * @param userPhone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * */
    @DeleteMapping("users")
    public Response deleteUser(@RequestParam(name = "admin_phone") String adminPhone,
                               @RequestParam(name = "user_phone") String userPhone,
                               @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.tokenIsValid(adminPhone, userToken))
            return Response.invalidUserToken();
        return userService.deleteUser(adminPhone, userPhone);
    }
}
