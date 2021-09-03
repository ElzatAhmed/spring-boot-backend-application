package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByPhone(String phone);

    List<User> findByUserNameContains(String userName);

    List<User> findByQqId(String qqId);

    List<User> findByWechatId(String wechatId);

    List<User> findByStudentCertified(boolean studentCertified);
}
