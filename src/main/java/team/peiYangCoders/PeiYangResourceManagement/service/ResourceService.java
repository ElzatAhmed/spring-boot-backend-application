package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.ItemPage;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.ItemFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.ResourceFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;

@Service
public interface ResourceService {

    /**
     * post new resource
     * @param resource : Resource information body
     * @param ownerPhone : owner phone
     * @param uToken : user token
     * */
    Response post(Resource resource, String ownerPhone, String uToken);


    /**
     * release resource
     * @param resourceCode : resource code
     * @param ownerPhone : owner phone
     * @param uToken : user token
     * @param item : item info
     * */
    Response release(String resourceCode, String ownerPhone, String uToken, Item item);


    /**
     * retract item
     * @param itemCode : item code
     * @param ownerPhone : owner phone
     * @param uToken : user token
     * */
    Response retract(String itemCode, String ownerPhone, String uToken);


    /**
     * delete a resource
     * @param resourceCode : resource code
     * @param ownerPhone : owner phone
     * @param uToken : user token
     * */
    Response delete(String resourceCode, String ownerPhone, String uToken);


    /**
     * update resource information
     * @param newResource : new resource info
     * @param resourceCode : resource code
     * @param ownerPhone : owner phone
     * @param uToken : user token
     * */
    Response update(Resource newResource, String resourceCode, String ownerPhone, String uToken);


    /**
     * admin validate resource
     * @param resourceCode : resource code
     * @param adminPhone : admin phone
     * @param uToken : user token
     * @param accept : accept
     * */
    Response check(String resourceCode, String adminPhone, String uToken, boolean accept);


    /**
     * get items by filter
     * @param filter : item filter
     * @param userPhone : user phone
     * @param userToken : user token
     * @param requestCount : request count
     * */
    Response getItemByFilter(ItemFilter filter, String userPhone,
                             String userToken, Integer requestCount);


    /**
     * get resources by filter
     * @param filter : item filter
     * @param userPhone : user phone
     * @param userToken : user token
     * @param requestCount : request count
     * */
    Response getResourceByFilter(ResourceFilter filter, String userPhone,
                                 String userToken, Integer requestCount);


    /**
     * get resource page
     * @param page : page information
     * */
    Response getPage(ItemPage page);

}
