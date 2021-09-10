package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.ItemPage;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.ItemFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.ResourceFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.*;
import team.peiYangCoders.PeiYangResourceManagement.service.ResourceService;
import team.peiYangCoders.PeiYangResourceManagement.service.implementation.ResourceServiceImpl;

@RestController
@RequestMapping("api/v1")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }


    /**
     * user post new resource api
     * @param resource : Resource information body
     *             {
     *                  "resource_name": "",
     *                  "description": "",
     *                  "tag": "",
     *                  "image_url": ""
     *             }
     * @param phone : user phone
     * @param userToken : the valid token system has distributed to the user
     * */
    @PostMapping("resource")
    public Response postNewResource(
            @RequestBody Resource resource,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "uToken") String userToken){
        return resourceService.post(resource, phone, userToken);
    }


    /**
     * user release resource api
     * @param phone : user phone number
     * @param resourceCode : the code of the resource to release
     * @param userToken : the valid token system has distributed to the user
     * @param item : Item information body
     *              {
     *                  "count": "",
     *                  "type": "",
     *                  "needs2pay": "",
     *                  "fee": "",
     *                  "fee_unit": "",
     *                  "campus": ""
     *              }
     * */
    @PostMapping("item")
    public Response releaseResource(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "resourceCode") String resourceCode,
            @RequestParam(name = "uToken") String userToken,
            @RequestBody Item item){
        return resourceService.release(resourceCode, phone, userToken, item);
    }


    /**
     * user retract resource api
     * @param phone : user phone number
     * @param itemCode : the code of the resource to retract
     * @param userToken : the valid token system has distributed to the user
     * */
    @DeleteMapping("item")
    public Response retractResource(@RequestParam(name = "phone") String phone,
                                    @RequestParam(name = "itemCode") String itemCode,
                                    @RequestParam(name = "uToken") String userToken){
        return resourceService.retract(itemCode, phone, userToken);
    }


    /**
     * user delete resource api
     * @param phone : user phone number
     * @param resourceCode: the code of the resource to delete
     * @param userToken : the valid token system has distributed to the user
     * */
    @DeleteMapping("resource")
    public Response deleteResource(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "resourceCode") String resourceCode,
            @RequestParam(name = "uToken") String userToken) {
        return resourceService.delete(resourceCode, phone, userToken);
    }


    /**
     * user update resource information api
     * @param resource : new information
     *             {
     *                  "resource_name": "",
     *                  "description": "",
     *                  "tag": "",
     *                  "image_url": ""
     *             }
     * @param resourceCode : the code of the resource to update
     * @param phone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * */
    @PutMapping("resource")
    public Response updateResourceInfo(
            @RequestBody Resource resource,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "resourceCode") String resourceCode,
            @RequestParam(name = "uToken") String userToken){
        return resourceService.update(resource, resourceCode, phone, userToken);
    }


    @PostMapping("resource/admin")
    public Response checkResource(@RequestParam(name = "phone") String userPhone,
                                  @RequestParam(name = "resourceCode") String resourceCode,
                                  @RequestParam(name = "uToken") String userToken,
                                  @RequestParam(name = "valid") Boolean valid){
        return resourceService.check(resourceCode, userPhone, userToken, valid);
    }

    /**
     * user get item infos by filter api
     * @param userPhone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * @param code : filter param, not required
     * @param type : filter param, not required
     * @param needs2pay : filter param, not required
     * @param campus : filter param, not required
     * @param resourceCode : filter param, not required
     * */
    @GetMapping("items")
    public Response getItem(@RequestParam(name = "uPhone") String userPhone,
                            @RequestParam(name = "uToken") String userToken,
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) String phone,
                            @RequestParam(required = false) String code,
                            @RequestParam(required = false) String type,
                            @RequestParam(required = false) Boolean needs2pay,
                            @RequestParam(required = false) Integer campus,
                            @RequestParam(required = false) String resourceCode,
                            @RequestParam(required = false) Integer requestCount){
        ItemFilter filter = new ItemFilter();
        filter.setCode(code);
        filter.setType(type);
        filter.setNeeds2Pay(needs2pay);
        filter.setCampus(campus);
        filter.setResourceCode(resourceCode);
        filter.setPhone(phone);
        filter.setName(name);
        return Response.success(resourceService.getItemByFilter(filter, userPhone, userToken, requestCount));
    }


    /**
     * user get resource infos by filter api
     * @param userPhone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * @param code : filter param, not required
     * @param name : filter param, not required
     * @param verified : filter param, not required
     * @param released : filter param, not required
     * @param description : filter param, not required
     * @param tag : filter param, not required
     * @param owner_phone : filter param, not required
     * */
    @GetMapping("resources")
    public Response getResource(@RequestParam(name = "uPhone") String userPhone,
                                @RequestParam(name = "uToken") String userToken,
                                @RequestParam(name = "code", required = false) String code,
                                @RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "verified", required = false) Boolean verified,
                                @RequestParam(name = "released", required = false) Boolean released,
                                @RequestParam(name = "accepted", required = false) Boolean accepted,
                                @RequestParam(name = "description", required = false) String description,
                                @RequestParam(name = "tag", required = false) String tag,
                                @RequestParam(name = "phone", required = false) String owner_phone,
                                @RequestParam(name = "requestCount", required = false) Integer requestCount){
        ResourceFilter filter = new ResourceFilter();
        if("*".equals(name)) name = null;
        filter.setCode(code);
        filter.setName(name);
        filter.setVerified(verified);
        filter.setReleased(released);
        filter.setAccepted(accepted);
        filter.setDescription(description);
        filter.setTag(tag);
        filter.setOwner_phone(owner_phone);
        return resourceService.getResourceByFilter(filter, userPhone, userToken, requestCount);
    }


    /**
     * front end get item page
     * @param pageSize : page size
     * @param pageNum : page number
     * @param sortBy : the attribute to sort item with
     * */
    @GetMapping("items/page")
    public Response getItemPage(@RequestParam(name = "pageNum") int pageNum,
                                @RequestParam(name = "pageSize", defaultValue = "30",
                                    required = false) int pageSize,
                                @RequestParam(name = "sortBy", defaultValue = "onTime",
                                    required = false) String sortBy){
        ItemPage page = new ItemPage(pageNum, pageSize, sortBy);
        return resourceService.getPage(page);
    }
}
