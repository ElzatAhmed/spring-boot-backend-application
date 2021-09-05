package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentCertificate;

import java.util.Optional;

public interface StudentCertificateRepository extends JpaRepository<StudentCertificate, Long> {

    Optional<StudentCertificate> findByStudentId(String studentId);

}
