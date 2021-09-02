package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.AdminRegistrationCode;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
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
        if(adminCode.isPresent() && !adminCode.get().isUsed()){
            use(adminCode.get());
            return true;
        }
        return false;
    }

    public void use(AdminRegistrationCode code){
        code.setUsed(true);
        adminCodeRepo.save(code);
    }

    public Response addCode(String code, String phone){
        Optional<User> adder = userRepo.findByPhone(phone);
        if(!adder.isPresent())
            return Response.invalidPhone();
        if(!adder.get().isAdmin())
            return Response.permissionDenied();
        return Response.success(adminCodeRepo.save(new AdminRegistrationCode(code)));
    }
}
