package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentId;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.StudentIdRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

import java.util.Optional;

@Service
public class StudentIdService {

    private final StudentIdRepository studentIdRepo;
    private final UserRepository userRepo;

    @Autowired
    public StudentIdService(StudentIdRepository studentIdRepo, UserRepository userRepo) {
        this.studentIdRepo = studentIdRepo;
        this.userRepo = userRepo;
    }

    public Response studentCertification(User student, String studentId, String name, String password){
        Optional<StudentId> sid = studentIdRepo.findByStudentId(studentId);
        if(!sid.isPresent()
                || sid.get().isUsed()
                || !sid.get().getStudentName().equals(name) ||
                !sid.get().getStudentPassword().equals(password))
            return Response.invalidStudentInfo();
        sid.get().setUsed(true);
        student.setStudentCertified(true);
        student.setStudentId(studentId);
        studentIdRepo.save(sid.get());
        userRepo.save(student);
        return Response.success(null);
    }
}
