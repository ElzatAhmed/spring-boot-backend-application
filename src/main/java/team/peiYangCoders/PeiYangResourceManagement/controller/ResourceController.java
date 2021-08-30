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
            @RequestParam String ownerPhone){
        return resourceService.postNewResource(info, ownerPhone);
    }


    @PostMapping("resource/all")
    public Response postMultipleNewResources(
            @RequestParam String ownerPhone,
            @RequestBody List<Body.ResourceInfos> infos) {
        return resourceService.postMultipleNewResources(infos, ownerPhone);
    }


    @PostMapping("item")
    public Response releaseResource(
            @RequestParam String ownerPhone, String resourceCode,
            @RequestBody Body.ItemInfos infos){
        return resourceService.releaseResource(resourceCode, ownerPhone, infos);
    }


    @DeleteMapping("item")
    public Response retractResource(@RequestParam String ownerPhone, String releaseCode){
        return resourceService.retractResource(ownerPhone, releaseCode);
    }


    @DeleteMapping("resource")
    public Response deleteResource(
            @RequestParam String ownerPhone, String resourceCode) {
        return resourceService.deleteResource(resourceCode, ownerPhone);
    }


    @DeleteMapping("resource/all")
    public Response deleteMultipleResources(
            @RequestParam String ownerPhone,
            @RequestBody List<String> resourceCodes) {
        return resourceService.deleteMultipleResources(resourceCodes, ownerPhone);
    }


    @PutMapping("resource")
    public Response updateResourceInfo(
            @RequestBody Body.ResourceInfos info,
            @RequestParam String ownerPhone, String resourceCode){
        return resourceService.updateResourceInfo(info, ownerPhone, resourceCode);
    }

    @PostMapping("resource/valid")
    public Response acceptResource(@RequestParam String adminPhone,
                                   String resourceCode){
        return resourceService.acceptResource(resourceCode, adminPhone);
    }

    @PostMapping("resource/invalid")
    public Response rejectResource(@RequestParam String adminPhone,
                                   String resourceCode){
        return resourceService.rejectResource(resourceCode, adminPhone);
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
