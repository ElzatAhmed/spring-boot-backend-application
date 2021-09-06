package team.peiYangCoders.PeiYangResourceManagement.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.AdminRegistrationCode;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.AdminRegistrationCodeRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserTokenRepository;
import team.peiYangCoders.PeiYangResourceManagement.service.AdminRegistrationCodeService;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdminRegistrationCodeServiceImpl implements AdminRegistrationCodeService {

    private final AdminRegistrationCodeRepository adminCodeRepo;
    private final UserRepository userRepo;
    private final UserTokenRepository userTokenRepo;

    @Autowired
    public AdminRegistrationCodeServiceImpl(AdminRegistrationCodeRepository adminCodeRepo,
                                            UserRepository userRepo,
                                            UserTokenRepository userTokenRepo) {
        this.adminCodeRepo = adminCodeRepo;
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
    }


    @Override
    public Response addCode(String phone, String userToken){
        Optional<User> adder = userRepo.findByPhone(phone);
        if(!adder.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(phone, userToken)) return Response.invalidUserToken();
        if(!adder.get().isAdmin()) return Response.permissionDenied();
        return Response.success(adminCodeRepo.save(
                new AdminRegistrationCode(UUID.randomUUID().toString())));
    }

    /**--------------------------private methods--------------------------------**/

    private boolean uTokenValid(String userPhone, String uToken){
        Optional<UserToken> maybe = userTokenRepo.findByUserPhone(userPhone);
        if(!maybe.isPresent()) return false;
        UserToken token = maybe.get();
        return token.getToken().equals(uToken);
    }
}
