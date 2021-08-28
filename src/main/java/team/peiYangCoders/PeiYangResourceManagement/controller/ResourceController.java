package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourceInfo;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourcePage;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.service.resourceService.ResourceService;
import team.peiYangCoders.PeiYangResourceManagement.service.userService.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/resource")
public class ResourceController {

    private final ResourceService resourceService;
    private final UserService userService;

    @Autowired
    public ResourceController(ResourceService resourceService, UserService userService) {
        this.resourceService = resourceService;
        this.userService = userService;
    }

    @GetMapping("/get/all")
    public Response getAll(@RequestParam String phone){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.OK)
            return resourceService.getAll();
        return response;
    }

    /**
     * @param info
     * */
    @PutMapping("/add")
    public Response addResource(@RequestBody ResourceInfo info){
        Optional<User> owner = userService.getByPhone(info.getOwner_phone());
        if(!owner.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        info.setOnTime(LocalDateTime.now());
        return resourceService.add(info, owner.get());
    }

    @PutMapping("/add/all")
    public Response addResources(
            @RequestBody List<ResourceInfo> infos,
            @RequestParam String phone) {
        Optional<User> owner = userService.getByPhone(phone);
        if(!owner.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        for(ResourceInfo info : infos)
            info.setOnTime(LocalDateTime.now());
        return resourceService.addResources(infos, owner.get());
    }

    @GetMapping("/get/page")
    public Response getResourcePage(@RequestBody ResourcePage page){
        return resourceService.getResources(page);
    }

    @PutMapping("/delete")
    public Response deleteResource(
            @RequestBody ResourceInfo info,
            @RequestParam String phone) {
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Response response = resourceService.getByCode(info.getCode());
        if(response.getCode() == Response.ERROR)
            return response;
        return resourceService.deleteResource(info, user.get());
    }

    @PutMapping("/delete/all")
    public Response deleteResources(
            @RequestBody List<ResourceInfo> infos,
            @RequestParam String phone) {
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        return resourceService.deleteResources(infos, user.get());
    }
}
