package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.UserTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserInfo;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final ConfirmationTokenService confirmationTokenService;
    private final StudentIdService studentIdService;
    private final AdminRegistrationCodeService adminCodeService;

    @Autowired
    public UserService(UserRepository userRepo,
                       ConfirmationTokenService confirmationTokenService,
                       StudentIdService studentIdService,
                       AdminRegistrationCodeService adminCodeService) {
        this.userRepo = userRepo;
        this.confirmationTokenService = confirmationTokenService;
        this.studentIdService = studentIdService;
        this.adminCodeService = adminCodeService;
    }

    public UserInfo save(User user){
        User u = userRepo.save(user);
        return new UserInfo(u);
    }

    public Response ordinaryLogin(Body.Login loginInfo){
        Optional<User> user = userRepo.findByPhone(loginInfo.getPhone());
        if(!user.isPresent()) return Response.errorMessage(Response.noSuchUser);
        if(!user.get().getPassword().equals(loginInfo.getPassword()))
            return Response.errorMessage(Response.invalidPassword);
        return Response.okMessage(new UserInfo(user.get()));
    }

    public Response ordinaryRegister(Body.Register info, String token){
        if(userRepo.findByPhone(info.getPhone()).isPresent())
            return Response.errorMessage(Response.invalidPhone);
        Response response = confirmationTokenService.receive(info.getPhone(), token);
        if(response.getCode() == Response.OK)
            return Response.okMessage(addNewUser(info, false));
        return response;
    }

    public Response updatePassword(Body.NewPassword info, String phone){
        Optional<User> user = getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Response response = confirmationTokenService.receive(phone, info.getToken());
        if(response.getCode() == Response.OK)
            return Response.okMessage(updatePassword(user.get(), info.getNewPassword()));
        return response;
    }

    public Response updateInfo(Body.UserDetail detail, String phone){
        Optional<User> user = getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        User newUser = user.get();
        newUser.setName(detail.getName());
        newUser.setQqId(detail.getQqId());
        newUser.setWechatId(detail.getWechatId());
        newUser.setAvatarUrl(detail.getAvatarUrl());
        return Response.okMessage(save(newUser));
    }

    public Response studentCertification(Body.Certification certificate, String phone){
        Optional<User> user = getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        return studentIdService.studentCertification(
                certificate.getStudentId(),
                certificate.getStudentName(),
                certificate.getStudentPassword());
    }

    public Response adminLogin(Body.Login info){
        Response response = isAdmin(info.getPhone());
        if(response.getCode() == Response.ERROR)
            return response;
        UserInfo userInfo = (UserInfo) response.getData();
        if(!userInfo.getPassword().equals(info.getPassword()))
            return Response.errorMessage(Response.invalidPassword);
        return Response.okMessage(userInfo);
    }

    public Response adminRegister(Body.Register info, String code, String token){
        if(getByPhone(info.getPhone()).isPresent())
            return Response.errorMessage(Response.invalidPhone);
        if(!adminCodeService.isValid(code))
            return Response.errorMessage(Response.invalidCode);
        Response response = confirmationTokenService.receive(info.getPhone(), token);
        if(response.getCode() == Response.OK)
            return Response.okMessage(addNewUser(info, true));
        return response;
    }

    public Response deleteUser(String adminPhone, String userPhone){
        Response response = isAdmin(adminPhone);
        if(response.getCode() == Response.ERROR)
            return response;
        Optional<User> user = getByPhone(userPhone);
        if(!user.isPresent()) return Response.errorMessage(Response.noSuchUser);
        userRepo.delete(user.get());
        return Response.okMessage();
    }

    public UserInfo addNewUser(Body.Register info, boolean isAdmin){
        User newUser = new User(info);
        newUser.setTag(isAdmin ? UserTag.admin : UserTag.ordinary);
        newUser = userRepo.save(newUser);
        return new UserInfo(newUser);
    }

    public Optional<User> getByPhone(String phone){
        return userRepo.findByPhone(phone);
    }

    public Response isAdmin(String phone){
        Optional<User> u = userRepo.findByPhone(phone);
        if(!u.isPresent()) return Response.errorMessage(Response.noSuchUser);
        if(!u.get().isAdmin()) return Response.errorMessage(Response.permissionDenied);
        return Response.okMessage(new UserInfo(u.get()));
    }

    public UserInfo updatePassword(User newUser, String newPassword){
        newUser.setPassword(newPassword);
        return new UserInfo(userRepo.save(newUser));
    }

    public List<UserInfo> getByFilter(UserFilter filter){
        List<User> allUsers = userRepo.findAll();
        List<UserInfo> filteredUsers = new ArrayList<>();
        for(User u : allUsers){
            if(filter.match(u))
                filteredUsers.add(new UserInfo(u));
        }
        return filteredUsers;
    }
}
