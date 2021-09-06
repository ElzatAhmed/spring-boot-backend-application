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

    Response post(Resource resource, String ownerPhone, String uToken);

    Response release(String resourceCode, String ownerPhone, String uToken, Item item);

    Response retract(String itemCode, String ownerPhone, String uToken);

    Response delete(String resourceCode, String ownerPhone, String uToken);

    Response update(Resource newResource, String resourceCode, String ownerPhone, String uToken);

    Response check(String resourceCode, String adminPhone, String uToken, boolean accept);

    Response getItemByFilter(ItemFilter filter, String userPhone,
                             String userToken, Integer requestCount);

    Response getResourceByFilter(ResourceFilter filter, String userPhone,
                                 String userToken, Integer requestCount);

    Response getPage(ItemPage page);

}
