package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, String> {

    Optional<UserToken> findByUserPhone(String userPhone);

}
