package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.StudentId;
import team.peiYangCoders.PeiYangResourceManagement.repository.StudentIdRepository;

import java.util.Optional;

@Service
public class StudentIdService {

    private final StudentIdRepository studentIdRepo;

    @Autowired
    public StudentIdService(StudentIdRepository studentIdRepo) {
        this.studentIdRepo = studentIdRepo;
    }

    public Response studentCertification(String studentId, String name, String password){
        Optional<StudentId> sid = studentIdRepo.findByStudentId(studentId);
        if(!sid.isPresent())
            return Response.errorMessage(Response.noSuchStudentId);
        if(sid.get().isUsed())
            return Response.errorMessage(Response.studentIdIsUsed);
        if(!sid.get().getStudentName().equals(name))
            return Response.errorMessage(Response.invalidStudentName);
        if(!sid.get().getStudentPassword().equals(password))
            return Response.errorMessage(Response.invalidPassword);
        sid.get().setUsed(true);
        studentIdRepo.save(sid.get());
        return Response.okMessage();
    }
}
