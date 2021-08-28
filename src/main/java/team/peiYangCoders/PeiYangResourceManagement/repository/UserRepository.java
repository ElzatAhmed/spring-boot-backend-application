package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByPhone(String phone);

    Optional<User> findByQqId(String qqId);

    Optional<User> findByWechatId(String wechatId);

    List<User> findAllByName(String name);

    List<User> findAllByNameContains(String name);

    boolean existsById(User user);

    boolean existsByPhone(User user);

}
