package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentCertificate;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;


@Service
public interface UserService {

    Response login(String userPhone, String password, boolean admin);

    Response register(User newUser, String cToken);

    Response register(User newUser, String cToken, String regCode);

    Response update(User newInfo, String userPhone, String uToken);

    Response update(String userPhone, String uToken, String cToken, String newPassword);

    Response studentCertification(StudentCertificate certificate, String userPhone, String uToken);

    Response getByFilter(UserFilter filter, String userPhone, String uToken, Integer requestCount);

}
