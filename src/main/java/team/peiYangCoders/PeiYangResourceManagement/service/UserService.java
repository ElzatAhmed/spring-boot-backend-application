package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.model.user.UserInfo;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    public Response getAll(){
        List<User> users = userRepo.findAll();
        List<UserInfo> infos = new ArrayList<>();
        for(User u : users)
            infos.add(new UserInfo(u));
        return Response.okMessage(infos);
    }

    public Response login(UserInfo info){
        Optional<User> user = userRepo.findByPhone(info.getPhone());
        if(!user.isPresent()) return Response.errorMessage(Response.noSuchUser);
        if(!user.get().getPassword().equals(info.getPassword()))
            return Response.errorMessage(Response.invalidPassword);
        return Response.okMessage(new UserInfo(user.get()));
    }

    public Response register(UserInfo info){
        Optional<User> user = userRepo.findByPhone(info.getPhone());
        if(user.isPresent()) return Response.errorMessage(Response.invalidPhone);

        User u = userRepo.save(new User(info));
        return Response.okMessage(new UserInfo(u));
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

    public Response updatePassword(UserInfo info, String newPassword){
        Optional<User> user = userRepo.findByPhone(info.getPhone());
        if(!user.isPresent()) return Response.errorMessage(Response.noSuchUser);
        if(!user.get().getPassword().equals(info.getPassword()))
            return Response.errorMessage(Response.invalidPassword);
        User u = user.get();
        u.setPassword(newPassword);
        u = userRepo.save(u);
        return Response.okMessage(new UserInfo(u));
    }

    public void setEnabled(User user){
        user.setEnabled(true);
        userRepo.save(user);
    }
}
