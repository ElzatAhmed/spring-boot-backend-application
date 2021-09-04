package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByPhone(String phone);

    List<User> findAllByUserNameContains(String userName);

    List<User> findAllByQqId(String qqId);

    List<User> findAllByWechatId(String wechatId);

    List<User> findAllByStudentCertified(boolean studentCertified);
}
