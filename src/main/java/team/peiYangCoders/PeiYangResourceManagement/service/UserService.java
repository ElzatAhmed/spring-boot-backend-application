package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.UserTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.UserFilter;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils;

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
        return Response.success(null);
    }

    public Response updateInfo(User user, String phone){
        Optional<User> maybe_user = getByPhone(phone);
        if(!maybe_user.isPresent())
            return Response.invalidPhone();
        User newUser = maybe_user.get();
        newUser.setUserName(user.getUserName());
        newUser.setQqId(user.getQqId());
        newUser.setWechatId(user.getWechatId());
        newUser.setAvatarUrl(user.getAvatarUrl());
        userRepo.save(newUser);
        return Response.success(null);
    }

    public Response adminLogin(Body.Login info){
        Response response = isAdmin(info.getUser_phone());
        if(response.failed())
            return response;
        User userInfo = (User) response.getData();
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

    public void addNewUser(User info, boolean isAdmin){
        info.setUserTag(isAdmin ? UserTag.admin.toString() : UserTag.ordinary.toString());
        userRepo.save(info);
    }

    public Optional<User> getByPhone(String phone){
        return userRepo.findByPhone(phone);
    }

    public Response isAdmin(String phone){
        Optional<User> u = userRepo.findByPhone(phone);
        if(!u.isPresent()) return Response.invalidPhone();
        if(!u.get().isAdmin()) return Response.permissionDenied();
        return Response.success(u.get());
    }

    public void updatePassword(User newUser, String newPassword){
        newUser.setPassword(newPassword);
        userRepo.save(newUser);
    }

    public List<User> getByFilter(UserFilter filter){

        if(filter.allNull())
            return userRepo.findAll();
        else if(filter.nullExceptPhone()){
            Optional<User> maybe = userRepo.findByPhone(filter.getPhone());
            return maybe.map(Collections::singletonList).orElse(Collections.emptyList());
        }
        else if(filter.getPhone() != null)
            return Collections.emptyList();

        List<User> name = null;
        List<User> qqId = null;
        List<User> wechatId = null;
        List<User> studentCertified = null;

        if(filter.getName() != null)
            name = userRepo.findByUserNameContains(filter.getName());
        if(filter.getQqId() != null)
            qqId = userRepo.findByQqId(filter.getQqId());
        if(filter.getWechatId() != null)
            wechatId = userRepo.findByWechatId(filter.getWechatId());
        if(filter.getStudentCertified() != null)
            studentCertified = userRepo.findByStudentCertified(filter.getStudentCertified());

        return MyUtils.userListIntersection(MyUtils.userListIntersection(name, qqId),
                MyUtils.userListIntersection(wechatId, studentCertified));
    }


}
