package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserInfo;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public Response login(UserInfo info){
        Optional<User> user = userRepo.findByPhone(info.getPhone());
        if(!user.isPresent()) return Response.errorMessage(Response.noSuchUser);
        if(!user.get().getPassword().equals(info.getPassword()))
            return Response.errorMessage(Response.invalidPassword);
        return Response.okMessage(new UserInfo(user.get()));
    }

    public UserInfo addNewUser(UserInfo info){
        User newUser = new User(info);
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
}
