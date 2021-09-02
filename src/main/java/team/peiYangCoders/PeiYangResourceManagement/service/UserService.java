package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.UserTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Response ordinaryLogin(Body.Login loginInfo){
        Optional<User> user = userRepo.findByPhone(loginInfo.getUser_phone());
        if(!user.isPresent()) return Response.invalidPhone();
        if(!user.get().getPassword().equals(loginInfo.getPassword()))
            return Response.invalidPassword();
        return Response.success(User.toBody(user.get()));
    }

    public Response updateInfo(Body.UserDetail detail, String phone){
        Optional<User> user = getByPhone(phone);
        if(!user.isPresent())
            return Response.invalidPhone();
        User newUser = user.get();
        newUser.setName(detail.getUser_name());
        newUser.setQqId(detail.getQq_id());
        newUser.setWechatId(detail.getWechat_id());
        newUser.setAvatarUrl(detail.getAvatar_url());
        userRepo.save(newUser);
        return Response.success(null);
    }

    public Response adminLogin(Body.Login info){
        Response response = isAdmin(info.getUser_phone());
        if(response.failed())
            return response;
        Body.UserDetail userInfo = (Body.UserDetail) response.getData();
        if(!userInfo.getPassword().equals(info.getPassword()))
            return Response.invalidPassword();
        return Response.success(null);
    }


    public Response deleteUser(String adminPhone, String userPhone){
        Response response = isAdmin(adminPhone);
        if(response.failed())
            return response;
        Optional<User> user = getByPhone(userPhone);
        if(!user.isPresent()) return Response.invalidPhone();
        userRepo.delete(user.get());
        return Response.success(null);
    }

    public Body.UserDetail addNewUser(Body.Register info, boolean isAdmin){
        User newUser = new User(info);
        newUser.setTag(isAdmin ? UserTag.admin : UserTag.ordinary);
        newUser = userRepo.save(newUser);
        return User.toBody(newUser);
    }

    public Optional<User> getByPhone(String phone){
        return userRepo.findByPhone(phone);
    }

    public Response isAdmin(String phone){
        Optional<User> u = userRepo.findByPhone(phone);
        if(!u.isPresent()) return Response.invalidPhone();
        if(!u.get().isAdmin()) return Response.permissionDenied();
        return Response.success(User.toBody(u.get()));
    }

    public void updatePassword(User newUser, String newPassword){
        newUser.setPassword(newPassword);
        userRepo.save(newUser);
    }

    public List<Body.UserDetail> getByFilter(UserFilter filter){
        List<User> allUsers = userRepo.findAll();
        List<Body.UserDetail> filteredUsers = new ArrayList<>();
        for(User u : allUsers){
            if(filter.match(u))
                filteredUsers.add(User.toBody(u));
        }
        return filteredUsers;
    }
}
