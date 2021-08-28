package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.AdminRegistrationCode;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserInfo;
import team.peiYangCoders.PeiYangResourceManagement.repository.AdminRegistrationCodeRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

import java.util.Optional;

@Service
public class AdminRegistrationCodeService {

    private final AdminRegistrationCodeRepository adminCodeRepo;
    private final UserRepository userRepo;

    @Autowired
    public AdminRegistrationCodeService(AdminRegistrationCodeRepository adminCodeRepo,
                                        UserRepository userRepo) {
        this.adminCodeRepo = adminCodeRepo;
        this.userRepo = userRepo;
    }

    public boolean isValid(String code){
        Optional<AdminRegistrationCode> adminCode = adminCodeRepo.findByCode(code);
        return adminCode.isPresent() && !adminCode.get().isUsed();
    }

    public Response addCode(String code, UserInfo adderInfo){
        Optional<User> adder = userRepo.findByPhone(adderInfo.getPhone());
        if(!adder.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        if(!adder.get().isAdmin())
            return Response.errorMessage(Response.permissionDenied);
        return Response.okMessage(adminCodeRepo.save(new AdminRegistrationCode(code)));
    }
}
