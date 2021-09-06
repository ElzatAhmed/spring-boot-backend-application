package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("api/v1")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping("user/image")
    public Response uploadUserImage(@RequestParam(name = "phone") String phone,
                                    @RequestParam(name = "uToken") String userToken,
                                    @RequestParam(name = "image") MultipartFile file)
            throws IOException {
        return imageService.upload(phone, userToken, file);
    }

    @GetMapping("user/image")
    public ResponseEntity<Resource> downloadUserImage(@RequestParam(name = "phone") String phone,
                                                      @RequestParam(name = "uToken") String userToken)
            throws IOException {
        return imageService.download(phone, userToken);
    }

    @PostMapping("resource/image")
    public Response uploadResourceImage(@RequestParam(name = "phone") String userPhone,
                                        @RequestParam(name = "uToken") String userToken,
                                        @RequestParam(name = "resourceCode") String resourceCode,
                                        @RequestParam(name = "image") MultipartFile file)
            throws IOException {
        return imageService.upload(userPhone, userToken, resourceCode, file);
    }

    @GetMapping("resource/image")
    public ResponseEntity<Resource> downloadResourceImage(@RequestParam(name = "phone") String userPhone,
                                                          @RequestParam(name = "uToken") String userToken,
                                                          @RequestParam(name = "resourceCode") String resourceCode)
            throws IOException {
        return imageService.download(userPhone, userToken, resourceCode);
    }

}
