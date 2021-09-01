package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ItemFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourceFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourcePage;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceType;
import team.peiYangCoders.PeiYangResourceManagement.service.ResourceService;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
    public Response getItem(@RequestParam(required = false) String code,
                            @RequestParam(required = false) String type,
                            @RequestParam(required = false) Boolean needsToPay,
                            @RequestParam(required = false) Boolean ordered,
                            @RequestParam(required = false) Boolean completed,
                            @RequestParam(required = false) String ownerPhone){
        ItemFilter filter = new ItemFilter();
        filter.setCode(code == null ? null : UUID.fromString(code));
        filter.setType(type == null ? null : ResourceType.valueOf(type));
        filter.setNeeds2Pay(needsToPay);
        filter.setOrdered(ordered);
        filter.setCompleted(completed);
        filter.setOwnerPhone(ownerPhone);
        System.out.println(filter);
        return resourceService.getItem(filter);
    }

    @GetMapping("resources")
    public Response getResource(@RequestParam(required = false) String code,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) Boolean verified,
                                @RequestParam(required = false) Boolean accepted,
                                @RequestParam(required = false) Boolean needToPay,
                                @RequestParam(required = false) Boolean released,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) String tag,
                                @RequestParam(required = false) String ownerPhone){
        ResourceFilter filter = new ResourceFilter();
        filter.setCode(code == null ? null : UUID.fromString(code));
        filter.setName(name);
        filter.setVerified(verified);
        filter.setAccepted(accepted);
        filter.setNeedsToPay(needToPay);
        filter.setReleased(released);
        filter.setDescription(description);
        filter.setTag(tag == null ? null : ResourceTag.valueOf(tag));
        filter.setOwner_phone(ownerPhone);
        System.out.println(filter);
        return resourceService.getResource(filter);
    }

    @GetMapping("resources/page")
    public Response getPage(@RequestParam int pageNum,
                            @RequestParam(defaultValue = "30", required = false) int pageSize,
                            @RequestParam(defaultValue = "fee", required = false) String sortBy){
        ResourcePage page = new ResourcePage(pageNum, pageSize, sortBy);
        return resourceService.getPage(page);
    }
}
