package team.peiYangCoders.PeiYangResourceManagement.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.AdminRegistrationCode;
import team.peiYangCoders.PeiYangResourceManagement.model.ConfirmationToken;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.UserTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentCertificate;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.*;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;
import team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserTokenRepository userTokenRepo;
    private final ConfirmationTokenRepository cTokenRepo;
    private final AdminRegistrationCodeRepository regCodeRepo;
    private final StudentCertificateRepository certificateRepo;

    @Autowired
    public UserServiceImpl(UserRepository userRepo,
                           UserTokenRepository userTokenRepo,
                           ConfirmationTokenRepository cTokenRepo,
                           AdminRegistrationCodeRepository regCodeRepo,
                           StudentCertificateRepository certificateRepo) {
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
        this.cTokenRepo = cTokenRepo;
        this.regCodeRepo = regCodeRepo;
        this.certificateRepo = certificateRepo;
    }


    // login interface for upper layer
    @Override
    public Response login(String userPhone, String password, boolean admin){
        if(admin) return adminLogin(userPhone, password);
        return ordinaryLogin(userPhone, password);
    }


    // ordinary register interface for upper layer
    @Override
    public Response register(User newUser, String cToken){
        Optional<User> maybe = userRepo.findByPhone(newUser.getPhone());
        if(maybe.isPresent()) return Response.invalidPhone();
        if(!cTokenValid(newUser.getPhone(), cToken)) return Response.invalidConfirmationToken();
        newUser.setUserTag(UserTag.ordinary.toString());
        userRepo.save(newUser);
        return Response.success(null);
    }


    // admin register interface for upper layer
    @Override
    public Response register(User newUser, String cToken, String regCode){
        Optional<User> maybe = userRepo.findByPhone(newUser.getPhone());
        if(maybe.isPresent()) return Response.invalidPhone();
        if(!regCodeValid(regCode)) return Response.invalidRegistrationCode();
        if(!cTokenValid(newUser.getPhone(), cToken)) return Response.invalidConfirmationToken();
        newUser.setUserTag(UserTag.admin.toString());
        userRepo.save(newUser);
        return Response.success(null);
    }


    // update user info interface for upper layer
    @Override
    public Response update(User newInfo, String userPhone, String uToken){
        Optional<User> maybe = userRepo.findByPhone(userPhone);
        if(!maybe.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, uToken)) return Response.invalidUserToken();
        User user = maybe.get();
        user.setUserName(newInfo.getUserName());
        user.setQqId(newInfo.getQqId());
        user.setWechatId(newInfo.getWechatId());
        user.setAvatarUrl(newInfo.getAvatarUrl());
        userRepo.save(user);
        return Response.success(null);
    }


    // update password interface for upper layer
    @Override
    public Response update(String userPhone, String uToken, String cToken, String newPassword){
        Optional<User> maybe = userRepo.findByPhone(userPhone);
        if(!maybe.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, uToken)) return Response.invalidUserToken();
        if(!cTokenValid(userPhone, cToken)) return Response.invalidConfirmationToken();
        User user = maybe.get();
        user.setPassword(newPassword);
        userRepo.save(user);
        return Response.success(null);
    }


    // student certification interface for upper layer
    @Override
    public Response studentCertification(StudentCertificate certificate, String userPhone, String uToken){
        Optional<User> maybe = userRepo.findByPhone(userPhone);
        if(!maybe.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, uToken)) return Response.invalidUserToken();
        if(!studentInfoValid(certificate)) return Response.invalidStudentInfo();
        User user = maybe.get();
        user.setStudentCertified(true);
        user.setStudentId(certificate.getStudentId());
        userRepo.save(user);
        return Response.success(null);
    }


    // get users by filter interface for upper layer
    @Override
    public Response getByFilter(UserFilter filter, String userPhone, String uToken, Integer requestCount){
        Optional<User> maybe = userRepo.findByPhone(userPhone);
        if(!maybe.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, uToken)) return Response.invalidUserToken();
        if(filter.allNull())
            return Response.success(userRepo.findAll());
        else if(filter.nullExceptPhone()){
            Optional<User> maybeFound = userRepo.findByPhone(filter.getPhone());
            return maybeFound.map(user -> Response.success(Collections.singletonList(user)))
                    .orElseGet(() -> Response.success(Collections.emptyList()));
        }
        else if(filter.getPhone() != null)
            return Response.success(Collections.emptyList());

        List<User> name = null;
        List<User> qqId = null;
        List<User> wechatId = null;
        List<User> studentCertified = null;

        if(filter.getName() != null)
            name = userRepo.findAllByUserNameContains(filter.getName());
        if(filter.getQqId() != null)
            qqId = userRepo.findAllByQqId(filter.getQqId());
        if(filter.getWechatId() != null)
            wechatId = userRepo.findAllByWechatId(filter.getWechatId());
        if(filter.getStudentCertified() != null)
            studentCertified = userRepo.findAllByStudentCertified(filter.getStudentCertified());
        MyUtils<User> util = new MyUtils<>();
        List<User> result = util.intersect(util.intersect(name, qqId),
                util.intersect(wechatId, studentCertified));
        return Response.success(util.contract(result, requestCount));
    }


    /**--------------------------private methods--------------------------------**/

    private Response ordinaryLogin(String userPhone, String password){
        Optional<User> maybe = userRepo.findByPhone(userPhone);
        if(!maybe.isPresent())
            return Response.invalidPhone();
        User user = maybe.get();
        if(user.getPassword().equals(password))
            return Response.success(userTokenRepo.save(generateUToken(userPhone, user.getUserName())));
        return Response.invalidPassword();
    }

    private Response adminLogin(String userPhone, String password){
        Optional<User> maybe = userRepo.findByPhone(userPhone);
        if(!maybe.isPresent())
            return Response.invalidPhone();
        User user = maybe.get();
        if(!user.isAdmin())
            return Response.permissionDenied();
        if(user.getPassword().equals(password))
            return Response.success(userTokenRepo.save(generateUToken(userPhone, user.getUserName())));
        return Response.invalidPassword();
    }

    private boolean cTokenValid(String phone, String cToken){
        Optional<ConfirmationToken> maybe = cTokenRepo.findByToken(cToken);
        if(!maybe.isPresent()) return false;
        ConfirmationToken token = maybe.get();
        if(!token.getUserPhone().equals(phone)) return false;
        if(token.isConfirmed()) return false;
        LocalDateTime now = LocalDateTime.now();
        if(token.getExpiresAt().isBefore(now)) return false;
        token.setConfirmed(true);
        token.setConfirmedAt(now);
        cTokenRepo.save(token);
        return true;
    }

    private boolean regCodeValid(String regCode){
        Optional<AdminRegistrationCode> maybe = regCodeRepo.findByCode(regCode);
        if(!maybe.isPresent()) return false;
        AdminRegistrationCode code = maybe.get();
        if(code.isUsed()) return false;
        code.setUsed(true);
        regCodeRepo.save(code);
        return true;
    }

    private UserToken generateUToken(String userPhone, String userName){
        return new UserToken(userPhone, userName, UUID.randomUUID().toString());
    }

    private boolean uTokenValid(String userPhone, String uToken){
        Optional<UserToken> maybe = userTokenRepo.findByUserPhone(userPhone);
        if(!maybe.isPresent()) return false;
        UserToken token = maybe.get();
        return token.getToken().equals(uToken);
    }

    private boolean studentInfoValid(StudentCertificate certificate){
        Optional<StudentCertificate> maybe = certificateRepo.findByStudentId(certificate.getStudentId());
        if(!maybe.isPresent()) return false;
        StudentCertificate studentCertificate = maybe.get();
        if(studentCertificate.isUsed()) return false;
        if(!studentCertificate.getStudentName().equals(certificate.getStudentName())
                || !studentCertificate.getStudentPassword().equals(certificate.getStudentPassword()))
            return false;
        studentCertificate.setUsed(true);
        certificateRepo.save(studentCertificate);
        return true;
    }
}
