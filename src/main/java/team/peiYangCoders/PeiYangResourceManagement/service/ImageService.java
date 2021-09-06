package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;

import java.io.IOException;


@Service
@Component
public interface ImageService {

    Response upload(String phone, String userToken, MultipartFile image)
            throws IOException;

    Response upload(String phone, String userToken, String resourceCode, MultipartFile image)
            throws IOException;

    ResponseEntity<Resource> download(String phone, String userToken)
            throws IOException;

    ResponseEntity<Resource> download(String phone, String userToken, String resourceCode)
            throws IOException;

}
