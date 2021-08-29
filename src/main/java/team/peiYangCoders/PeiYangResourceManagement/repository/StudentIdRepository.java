package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentId;

import java.util.Optional;

public interface StudentIdRepository extends JpaRepository<StudentId, Long> {

    Optional<StudentId> findByStudentId(String studentId);

}
