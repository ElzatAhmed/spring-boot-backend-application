package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDataService {

    private final UserRepository userRepo;

    @Autowired
    public UserDataService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Response getAll(){
        return Response.okMessage(userRepo.findAll());
    }

    public Response getById(Long id){
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()) return Response.okMessage(user);
        return Response.errorMessage(Response.noSuchUser);
    }

    public Response getByPhone(String phone){
        Optional<User> user = userRepo.findByPhone(phone);
        if(user.isPresent()) return Response.okMessage(user);
        return Response.errorMessage(Response.noSuchUser);
    }

    public Response getByQqId(String qqId){
        Optional<User> user = userRepo.findByQqId(qqId);
        if(user.isPresent()) return Response.okMessage(user);
        return Response.errorMessage(Response.noSuchUser);
    }

    public Response getByWechatId(String wechatId){
        Optional<User> user = userRepo.findByWechatId(wechatId);
        if(user.isPresent()) return Response.okMessage(user);
        return Response.errorMessage(Response.noSuchUser);
    }

    public Response getAllByName(String name){
        return Response.okMessage(userRepo.findAllByName(name));
    }

    public Response getAllByNameContains(String name){
        return Response.okMessage(userRepo.findAllByNameContains(name));
    }
}
