package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.apache.http.HttpResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;
import team.peiYangCoders.PeiYangResourceManagement.service.UserTokenService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping("api/v1")
public class ImageController {

    private final UserService userService;
    private final UserTokenService userTokenService;

    public static final String USER_DIRECTORY = "E:\\school\\junior\\java-web-project\\codes\\PeiYangResourceManagement_Backend\\images\\user";
    public static final String RESOURCE_DIRECTORY = "E:\\school\\junior\\java-web-project\\codes\\PeiYangResourceManagement_Backend\\images\\resource";

    public ImageController(UserService userService, UserTokenService userTokenService) {
        this.userService = userService;
        this.userTokenService = userTokenService;
    }

    @PostMapping("user/image")
    public Response uploadUserImage(@RequestParam(name = "phone") String phone,
                                    @RequestParam(name = "uToken") String userToken,
                                    @RequestParam(name = "image") MultipartFile file) throws IOException {
        if(!userService.getByPhone(phone).isPresent())
            return Response.invalidPhone();
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        String fileName = phone + ".jpg";
        Path fileStorage = get(USER_DIRECTORY, fileName).toAbsolutePath().normalize();
        copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
        return Response.success(fileName);
    }

    @GetMapping("user/image")
    public ResponseEntity<Resource> downloadUserImage(@RequestParam(name = "phone") String phone,
                                      @RequestParam(name = "uToken") String userToken) throws IOException {
        if(!userService.getByPhone(phone).isPresent())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!userTokenService.tokenIsValid(phone, userToken))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Path filePath = get(USER_DIRECTORY).toAbsolutePath().normalize().resolve(phone + ".jpg");
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name", String.valueOf(filePath.getFileName()));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name="
                + filePath.getFileName());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(headers).body(resource);
    }

}
