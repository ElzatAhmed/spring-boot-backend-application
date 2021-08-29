package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourceFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourcePage;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.service.ResourceService;
import team.peiYangCoders.PeiYangResourceManagement.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
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
     *             {
     *                  "name": "",
     *                  "needsToPay": "",
     *                  "fee": ,
     *                  "description": "",
     *                  "tag": "",
     *                  "imageUrl": ""
     *             }
     * @param phone : 用户手机号
     * */
    @PostMapping("resources/add")
    public Response addResource(
            @RequestBody Body.NewResource info,
            @RequestParam String phone){
        Optional<User> owner = userService.getByPhone(phone);
        if(!owner.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        return resourceService.add(info, owner.get(), LocalDateTime.now());
    }


    /**
     * 批量添加资源
     * @param phone : 用户手机号
     * @param infos : 资源信息列表
     * */
    @PostMapping("resources/add/all")
    public Response addResources(
            @RequestParam String phone,
            @RequestBody List<Body.NewResource> infos) {
        Optional<User> owner = userService.getByPhone(phone);
        if(!owner.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        return resourceService.addResources(infos, owner.get(), LocalDateTime.now());
    }

    /**
     * 删除指定资源
     * @param phone : 用户手机号码
     * @param code : 资源信息
     * */
    @DeleteMapping("resources/delete")
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
    @DeleteMapping("resources/delete/all")
    public Response deleteResources(
            @RequestParam String phone,
            @RequestBody List<String> codes) {
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        return resourceService.deleteResources(codes, user.get());
    }

    /**
     * 用户更新资源信息
     * @param info : 资源的更新信息，若有未更新的属性，则继续发送老属性
     * @param phone : 用户手机号
     * @param code : 资源代码
     * */
    @PutMapping("resources/update")
    public Response updateResource(
            @RequestBody Body.NewResource info,
            @RequestParam String phone, String code){
        Optional<User> user = userService.getByPhone(phone);
        if(!user.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Optional<Resource> resource = resourceService.getByCode(UUID.fromString(code));
        if(!resource.isPresent())
            return Response.errorMessage(Response.noSuchResource);
        return resourceService.updateResource(info, resource.get());
    }

    /**
     * 获取单页资源
     * @param page : 页面信息
     * */
    @GetMapping("resources/page")
    public Response getResourcePage(@RequestBody ResourcePage page){
        return resourceService.getResources(page);
    }


    /**
     * 获取所有资源(order by onTime)
     * @param phone : 管理员手机号
     * */
    @GetMapping("/resources")
    public Response getAllResources(@RequestParam String phone){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return Response.okMessage(resourceService.getAllSortByTime());
    }

    /**
     * 管理员过滤选择资源
     * */
    @GetMapping("/resources/filter")
    public Response getResources(
            @RequestParam String phone,
            @RequestBody ResourceFilter filter){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return Response.okMessage(resourceService.getByFilter(filter));
    }

    /**
     * 通过审核
     * @param phone : 管理员手机号
     * @param code : 资源代码
     * */
    @PutMapping("/resources/accept")
    public Response acceptResource(@RequestParam String phone, String code){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return resourceService.verifyResource(UUID.fromString(code), true);
    }

    /**
     * 审核拒绝
     * @param phone : 管理员手机号
     * @param code : 资源代码
     * */
    @PutMapping("/resources/reject")
    public Response rejectResource(@RequestParam String phone, String code){
        Response response = userService.isAdmin(phone);
        if(response.getCode() == Response.ERROR)
            return response;
        return resourceService.verifyResource(UUID.fromString(code), false);
    }
}
