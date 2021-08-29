package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    public List<UserInfo> getAll(){
        List<User> users = userRepo.findAll();
        List<UserInfo> infos = new ArrayList<>();
        for(User u : users)
            infos.add(new UserInfo(u));
        return infos;
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

    public Response deleteUser(String phone){
        Optional<User> user = getByPhone(phone);
        if(!user.isPresent()) return Response.errorMessage(Response.noSuchUser);
        userRepo.delete(user.get());
        return Response.okMessage();
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
