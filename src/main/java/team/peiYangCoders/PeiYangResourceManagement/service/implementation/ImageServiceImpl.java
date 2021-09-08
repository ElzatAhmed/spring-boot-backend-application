package team.peiYangCoders.PeiYangResourceManagement.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.ResourceRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserTokenRepository;
import team.peiYangCoders.PeiYangResourceManagement.service.ImageService;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;


@Service
@Component
public class ImageServiceImpl implements ImageService{

    private final static String USER_DIR = "E:\\school\\junior\\java-web-project" +
            "\\codes\\PeiYangResourceManagement_Backend\\images\\user";

    private final static String RESOURCE_DIR = "E:\\school\\junior\\java-web-project" +
            "\\codes\\PeiYangResourceManagement_Backend\\images\\resource";

    private final UserRepository userRepo;
    private final UserTokenRepository userTokenRepo;
    private final ResourceRepository resourceRepo;

    @Autowired
    public ImageServiceImpl(UserRepository userRepo,
                            UserTokenRepository userTokenRepo,
                            ResourceRepository resourceRepo) {
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
        this.resourceRepo = resourceRepo;
    }

    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Override
    public Response upload(String phone, String userToken, MultipartFile image) throws IOException {
        Optional<User> maybe = userRepo.findByPhone(phone);
        if(!maybe.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(phone, userToken)) return Response.invalidUserToken();
        String fileName = phone + ".jpg";
        return upload_image(fileName, USER_DIR, image);
    }

    @Override
    public Response upload(String phone, String userToken, String resourceCode, MultipartFile image)
            throws IOException {
        Optional<User> maybe = userRepo.findByPhone(phone);
        if(!maybe.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(phone, userToken)) return Response.invalidUserToken();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent()) return Response.invalidResourceCode();
        User owner = maybe.get();
        Resource resource = maybeResource.get();
        if(!resource.getOwnerPhone().equals(owner.getPhone())) return Response.resourceNotOwned();
        String fileName = resourceCode + ".jpg";
        return upload_image(fileName, RESOURCE_DIR, image);
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource>
    download(String phone, String userToken) throws IOException{
        Optional<User> maybe = userRepo.findByPhone(phone);
        if(!maybe.isPresent()) return ResponseEntity.notFound().build();
        if(!uTokenValid(phone, userToken)) return ResponseEntity.notFound().build();
        String fileName = phone + ".jpg";
        return download_image(fileName, USER_DIR);
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource>
    download(String phone, String userToken, String resourceCode) throws IOException {
        Optional<User> maybe = userRepo.findByPhone(phone);
        if(!maybe.isPresent()) return ResponseEntity.notFound().build();
        if(!uTokenValid(phone, userToken)) return ResponseEntity.notFound().build();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent()) return ResponseEntity.notFound().build();
        User owner = maybe.get();
        Resource resource = maybeResource.get();
        if(!resource.getOwnerPhone().equals(owner.getPhone())) return ResponseEntity.badRequest().build();
        String fileName = resourceCode + ".jpg";
        return download_image(fileName, RESOURCE_DIR);
    }



    /**--------------------------private methods--------------------------------**/

    private boolean uTokenValid(String userPhone, String uToken){
        Optional<UserToken> maybe = userTokenRepo.findByUserPhone(userPhone);
        if(!maybe.isPresent()) return false;
        UserToken token = maybe.get();
        return token.getToken().equals(uToken);
    }

    private ResponseEntity<org.springframework.core.io.Resource> download_image(String fileName, String dir)
            throws IOException{
        Path imagePath = get(dir).toAbsolutePath().normalize().resolve(fileName);
        org.springframework.core.io.Resource transResource = new UrlResource(imagePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", fileName);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + transResource.getFilename());
        logger.info("image " + fileName + " has been sent to the user");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(imagePath)))
                .headers(httpHeaders).body(transResource);
    }

    private Response upload_image(String fileName, String dir, MultipartFile image) throws IOException {
        Path imagePath = get(dir, fileName).toAbsolutePath().normalize();
        copy(image.getInputStream(), imagePath, REPLACE_EXISTING);
        logger.info("image " + fileName + " has been uploaded to the server");
        return Response.success(null);
    }
}
