package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserTokenService {

    private final UserTokenRepository userTokenRepo;
    private final UserRepository userRepo;

    @Autowired
    public UserTokenService(UserTokenRepository userTokenRepo, UserRepository userRepo) {
        this.userTokenRepo = userTokenRepo;
        this.userRepo = userRepo;
    }

    public Response provideNewCode(String phone){
        Optional<User> maybeUser = userRepo.findByPhone(phone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        UserToken userToken = new UserToken();
        userToken.setToken(UUID.randomUUID().toString());
        userToken.setUserPhone(phone);
        userToken.setUserName(maybeUser.get().getUserName());
        return Response.success(userTokenRepo.save(userToken));
    }

    public boolean tokenIsValid(String phone, String code){
        Optional<User> maybeUser = userRepo.findByPhone(phone);
        if(!maybeUser.isPresent())
            return false;
        Optional<UserToken> maybe = userTokenRepo.findById(phone);
        if(!maybe.isPresent())
            return false;
        return maybe.get().getToken().equals(code);
    }
}
