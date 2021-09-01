package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ItemFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourceFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourcePage;
import team.peiYangCoders.PeiYangResourceManagement.service.ResourceService;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }


    @PostMapping("resource")
    public Response postNewResource(
            @RequestBody Body.ResourceInfos info,
            @RequestParam String phone){
        return resourceService.postNewResource(info, phone);
    }


    @PostMapping("resource/all")
    public Response postMultipleNewResources(
            @RequestParam String phone,
            @RequestBody List<Body.ResourceInfos> infos) {
        return resourceService.postMultipleNewResources(infos, phone);
    }


    @PostMapping("item")
    public Response releaseResource(
            @RequestParam String phone, String code,
            @RequestBody Body.ItemInfos infos){
        return resourceService.releaseResource(code, phone, infos);
    }


    @DeleteMapping("item")
    public Response retractResource(@RequestParam String phone, String code){
        return resourceService.retractItem(code, phone);
    }


    @DeleteMapping("resource")
    public Response deleteResource(
            @RequestParam String phone, String code) {
        return resourceService.deleteResource(code, phone);
    }


    @DeleteMapping("resource/all")
    public Response deleteMultipleResources(
            @RequestParam String phone,
            @RequestBody List<String> codes) {
        return resourceService.deleteMultipleResources(codes, phone);
    }


    @PutMapping("resource")
    public Response updateResourceInfo(
            @RequestBody Body.ResourceInfos info,
            @RequestParam String phone, String code){
        return resourceService.updateResourceInfo(info, phone, code);
    }

    @PostMapping("resource/valid")
    public Response acceptResource(@RequestParam String phone,
                                   String code){
        return resourceService.acceptResource(code, phone);
    }

    @PostMapping("resource/invalid")
    public Response rejectResource(@RequestParam String phone,
                                   String code){
        return resourceService.rejectResource(code, phone);
    }

    @GetMapping("items")
    public Response getItem(@RequestBody ItemFilter filter){
        return resourceService.getItem(filter);
    }

    @GetMapping("resources")
    public Response getResource(@RequestBody ResourceFilter filter){
        return resourceService.getResource(filter);
    }

    @GetMapping("resources/page")
    public Response getPage(@RequestBody ResourcePage page){
        return resourceService.getPage(page);
    }
}
