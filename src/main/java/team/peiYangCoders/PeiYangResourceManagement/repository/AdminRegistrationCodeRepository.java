package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.peiYangCoders.PeiYangResourceManagement.model.AdminRegistrationCode;

import java.util.Optional;

@Repository
public interface AdminRegistrationCodeRepository extends JpaRepository<AdminRegistrationCode, Long> {

    Optional<AdminRegistrationCode> findByCode(String code);

}
