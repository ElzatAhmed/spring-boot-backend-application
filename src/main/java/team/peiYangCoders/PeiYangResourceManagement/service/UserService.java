package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentCertificate;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;


@Service
public interface UserService {

    /**
     * login
     * @param userPhone : user phone
     * @param password : password
     * @param admin : true if is admin login, false otherwise
     * */
    Response login(String userPhone, String password, boolean admin);

    /**
     * register as a ordinary user
     * @param newUser : user info
     * @param cToken : confirmation token
     * */
    Response register(User newUser, String cToken);


    /**
     * register as a admin user
     * @param newUser : user info
     * @param cToken : confirmation token
     * @param regCode : registration code
     * */
    Response register(User newUser, String cToken, String regCode);


    /**
     * update user info
     * @param userPhone : user phone
     * @param newInfo : new information
     * @param uToken : user token
     * */
    Response update(User newInfo, String userPhone, String uToken);


    /**
     * update password
     * @param userPhone : user phone
     * @param uToken : user token
     * @param cToken : confirmation token
     * @param newPassword : new password
     * */
    Response update(String userPhone, String uToken, String cToken, String newPassword);


    /**
     * student certification
     * @param certificate : certificate information
     * @param userPhone : user phone
     * @param uToken : user token
     * */
    Response studentCertification(StudentCertificate certificate, String userPhone, String uToken);


    /**
     * get users by filter
     * @param filter : user filter
     * @param userPhone : user phone
     * @param uToken : user token
     * @param requestCount : request count
     * */
    Response getByFilter(UserFilter filter, String userPhone, String uToken, Integer requestCount);

}
