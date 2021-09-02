package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ItemFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourceFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ItemPage;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceType;
import team.peiYangCoders.PeiYangResourceManagement.service.ResourceService;
import team.peiYangCoders.PeiYangResourceManagement.service.UserTokenService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class ResourceController {

    private final ResourceService resourceService;
    private final UserTokenService userTokenService;

    @Autowired
    public ResourceController(ResourceService resourceService,
                              UserTokenService userTokenService) {
        this.resourceService = resourceService;
        this.userTokenService = userTokenService;
    }


    /**
     * user post new resource api
     * @param info : Resource information body
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
            @RequestBody Body.ResourceInfos info,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return resourceService.postNewResource(info, phone);
    }


    /**
     * user post multiple resources api
     * @param infos : a list of Resource information body
     * @param phone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * */
    @PostMapping("resource/all")
    public Response postMultipleNewResources(
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "user_token") String userToken,
            @RequestBody List<Body.ResourceInfos> infos) {
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return resourceService.postMultipleNewResources(infos, phone);
    }


    /**
     * user release resource api
     * @param phone : user phone number
     * @param resourceCode : the code of the resource to release
     * @param userToken : the valid token system has distributed to the user
     * @param infos : Item information body
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
            @RequestParam(name = "user_token") String userToken,
            @RequestBody Body.ItemInfos infos){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return resourceService.releaseResource(resourceCode, phone, infos);
    }


    /**
     * user retract resource api
     * @param phone : user phone number
     * @param resourceCode : the code of the resource to retract
     * @param userToken : the valid token system has distributed to the user
     * */
    @DeleteMapping("item")
    public Response retractResource(@RequestParam(name = "phone") String phone,
                                    @RequestParam(name = "resource_code") String resourceCode,
                                    @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return resourceService.retractItem(resourceCode, phone);
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
            @RequestParam(name = "resource_code") String resourceCode,
            @RequestParam(name = "user_token") String userToken) {
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return resourceService.deleteResource(resourceCode, phone);
    }


    /**
     * user delete multiple resource api
     * @param phone : user phone number
     * @param codes: list of the codes of the resources to delete
     * @param userToken : the valid token system has distributed to the user
     * */
    @DeleteMapping("resource/all")
    public Response deleteMultipleResources(
            @RequestBody List<String> codes,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "user_token") String userToken) {
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return resourceService.deleteMultipleResources(codes, phone);
    }


    /**
     * user update resource information api
     * @param info : new information
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
            @RequestBody Body.ResourceInfos info,
            @RequestParam(name = "phone") String phone,
            @RequestParam(name = "resource_code") String resourceCode,
            @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return resourceService.updateResourceInfo(info, phone, resourceCode);
    }


    /**
     * admin accept resource api
     * @param phone : admin phone number
     * @param resourceCode : the code of the resource to accept
     * @param userToken : the valid token system has distributed to the user
     * */
    @PostMapping("resource/valid")
    public Response acceptResource(@RequestParam(name = "phone") String phone,
                                   @RequestParam(name = "resource_code") String resourceCode,
                                   @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return resourceService.acceptResource(resourceCode, phone);
    }


    /**
     * admin reject resource api
     * @param phone : admin phone number
     * @param resourceCode : the code of the resource to reject
     * @param userToken : the valid token system has distributed to the user
     * */
    @PostMapping("resource/invalid")
    public Response rejectResource(@RequestParam(name = "phone") String phone,
                                   @RequestParam(name = "resource_code") String resourceCode,
                                   @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return resourceService.rejectResource(resourceCode, phone);
    }


    /**
     * user get item infos by filter api
     * @param userPhone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * @param code : filter param, not required
     * @param type : filter param, not required
     * @param needs2pay : filter param, not required
     * @param ordered : filter param, not required
     * @param completed : filter param, not required
     * @param owner_phone : filter param, not required
     * */
    @GetMapping("items")
    public Response getItem(@RequestParam(name = "user_phone") String userPhone,
                            @RequestParam(name = "user_token") String userToken,
                            @RequestParam(required = false) String code,
                            @RequestParam(required = false) String type,
                            @RequestParam(required = false) Boolean needs2pay,
                            @RequestParam(required = false) Boolean ordered,
                            @RequestParam(required = false) Boolean completed,
                            @RequestParam(required = false) String owner_phone){
        if(!userTokenService.codeIsValid(userPhone, userToken))
            return Response.invalidUserCode();
        ItemFilter filter = new ItemFilter();
        filter.setCode(code == null ? null : UUID.fromString(code));
        filter.setType(type == null ? null : ResourceType.valueOf(type));
        filter.setNeeds2Pay(needs2pay);
        filter.setOrdered(ordered);
        filter.setCompleted(completed);
        filter.setOwnerPhone(owner_phone);
        System.out.println(filter);
        return resourceService.getItem(filter);
    }


    /**
     * user get resource infos by filter api
     * @param userPhone : user phone number
     * @param userToken : the valid token system has distributed to the user
     * @param code : filter param, not required
     * @param name : filter param, not required
     * @param verified : filter param, not required
     * @param accepted : filter param, not required
     * @param needs2pay : filter param, not required
     * @param released : filter param, not required
     * @param description : filter param, not required
     * @param tag : filter param, not required
     * @param owner_phone : filter param, not required
     * */
    @GetMapping("resources")
    public Response getResource(@RequestParam(name = "user_phone") String userPhone,
                                @RequestParam(name = "user_token") String userToken,
                                @RequestParam(required = false) String code,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) Boolean verified,
                                @RequestParam(required = false) Boolean accepted,
                                @RequestParam(required = false) Boolean needs2pay,
                                @RequestParam(required = false) Boolean released,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) String tag,
                                @RequestParam(required = false) String owner_phone){
        if(!userTokenService.codeIsValid(userPhone, userToken))
            return Response.invalidUserCode();
        ResourceFilter filter = new ResourceFilter();
        filter.setCode(code == null ? null : UUID.fromString(code));
        filter.setName(name);
        filter.setVerified(verified);
        filter.setAccepted(accepted);
        filter.setNeedsToPay(needs2pay);
        filter.setReleased(released);
        filter.setDescription(description);
        filter.setTag(tag == null ? null : ResourceTag.valueOf(tag));
        filter.setOwner_phone(owner_phone);
        System.out.println(filter);
        return resourceService.getResource(filter);
    }


    /**
     * front end get item page
     * @param pageSize : page size
     * @param pageNum : page number
     * @param sortBy : the attribute to sort item with
     * */
    @GetMapping("resources/page")
    public Response getPage(@RequestParam int pageNum,
                            @RequestParam(defaultValue = "30", required = false) int pageSize,
                            @RequestParam(defaultValue = "onTime", required = false) String sortBy){
        ItemPage page = new ItemPage(pageNum, pageSize, sortBy);
        return resourceService.getPage(page);
    }
}
