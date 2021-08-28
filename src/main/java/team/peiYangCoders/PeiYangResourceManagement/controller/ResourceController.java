package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourceInfo;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourcePage;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.service.ResourceService;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


    /**
     * 添加新资源
     * @param info : 资源信息
     * */
    @PutMapping("/add")
    public Response addResource(@RequestBody ResourceInfo info){
        Optional<User> owner = userService.getByPhone(info.getOwner_phone());
        if(!owner.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        info.setOnTime(LocalDateTime.now());
        return resourceService.add(info, owner.get());
    }


    /**
     * 批量添加资源
     * @param phone : 用户手机号
     * @param infos : 资源信息列表
     * */
    @PutMapping("/add/all")
    public Response addResources(
            @RequestParam String phone,
            @RequestBody List<ResourceInfo> infos) {
        Optional<User> owner = userService.getByPhone(phone);
        if(!owner.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        for(ResourceInfo info : infos)
            info.setOnTime(LocalDateTime.now());
        return resourceService.addResources(infos, owner.get());
    }


    /**
     * 获取单页资源
     * @param page : 页面信息
     * */
    @GetMapping("/get/page")
    public Response getResourcePage(@RequestBody ResourcePage page){
        return resourceService.getResources(page);
    }


    /**
     * 删除指定资源
     * @param phone : 用户手机号码
     * @param code : 资源信息
     * */
    @PutMapping("/delete")
    public Response deleteResource(
            @RequestParam String phone, UUID code) {
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Optional<Resource> resource = resourceService.getByCode(code);
        if(!resource.isPresent())
            return Response.errorMessage(Response.noSuchResource);
        return resourceService.deleteResource(resource.get(), user.get());
    }

    /**
     * 批量删除资源
     * @param phone : 用户手机号
     * @param codes : 资源代码列表
     * */
    @PutMapping("/delete/all")
    public Response deleteResources(
            @RequestParam String phone,
            @RequestBody List<UUID> codes) {
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        return resourceService.deleteResources(codes, user.get());
    }
}
