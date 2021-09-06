package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentCertificate;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * user login api
     * @param userPhone : user phone number
     * @param password : user password
     * */
    @GetMapping("/user")
    public Response ordinaryLogin(@RequestParam(name = "phone") String userPhone,
                                  @RequestParam(name = "password") String password){
        return userService.login(userPhone, password, false);
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
            @RequestBody User info,
            @RequestParam(name = "cToken") String confirmationToken){
        return userService.register(info, confirmationToken);
    }


    /**
     * user update password api
     * @param userPhone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * */
    @PutMapping("user/password")
    public Response updatePassword(
            @RequestParam(name = "phone") String userPhone,
            @RequestParam(name = "uToken") String userToken,
            @RequestParam(name = "cToken") String cToken,
            @RequestParam(name = "newPassword") String newPassword){
        return userService.update(userPhone, userToken, cToken, newPassword);
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
            @RequestParam(name = "phone") String userPhone,
            @RequestParam(name = "uToken") String userToken){
        return userService.update(user, userPhone, userToken);
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
            @RequestBody StudentCertificate certificate,
            @RequestParam(name = "phone") String userPhone,
            @RequestParam(name = "uToken") String userToken){
        return userService.studentCertification(certificate, userPhone, userToken);
    }


    /**
     * administrator login api
     * @param userPhone : admin phone number
     * @param password : admin password
     * */
    @GetMapping("admin")
    public Response adminLogin(@RequestParam(name = "phone") String userPhone,
                               @RequestParam(name = "password") String password){
        return userService.login(userPhone, password, true);
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
            @RequestBody User info,
            @RequestParam(name = "regCode") String registrationCode,
            @RequestParam(name = "cToken") String confirmationToken){
        return userService.register(info, registrationCode, confirmationToken);
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
            @RequestParam(name = "uPhone") String userPhone,
            @RequestParam(name = "uToken") String userToken,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "qqId", required = false) String qqId,
            @RequestParam(name = "wechatId", required = false) String wechatId,
            @RequestParam(name = "studentCertified", required = false) Boolean student_certified,
            @RequestParam(name = "requestCount", required = false) Integer requestCount){
        UserFilter filter = new UserFilter();
        filter.setPhone(phone);
        filter.setName(name);
        filter.setQqId(qqId);
        filter.setWechatId(wechatId);
        filter.setStudentCertified(student_certified);
        return userService.getByFilter(filter, userPhone, userToken, requestCount);
    }
}
