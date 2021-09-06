package team.peiYangCoders.PeiYangResourceManagement.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.ItemPage;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.ItemFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.ResourceFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.*;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.ItemRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.ResourceRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserTokenRepository;
import team.peiYangCoders.PeiYangResourceManagement.service.ResourceService;
import team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepo;
    private final UserTokenRepository userTokenRepo;
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepo,
                               UserTokenRepository userTokenRepo,
                               UserRepository userRepo,
                               ItemRepository itemRepo) {
        this.resourceRepo = resourceRepo;
        this.userTokenRepo = userTokenRepo;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
    }

    // post new resource interface for upper layer
    @Override
    public Response post(Resource resource, String ownerPhone, String uToken){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(ownerPhone, uToken)) return Response.invalidUserToken();
        resource.setOwnerPhone(ownerPhone);
        return Response.success(resourceRepo.save(resource).getResourceCode());
    }


    // release new item interface for upper layer
    @Override
    public Response release(String resourceCode, String ownerPhone, String uToken, Item item){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent()) return Response.invalidResourceCode();
        if(!uTokenValid(ownerPhone, uToken)) return Response.invalidUserToken();
        Resource resource = maybeResource.get();
        User owner = maybeUser.get();
        if(!resource.getOwnerPhone().equals(owner.getPhone())) return Response.resourceNotOwned();
        if(!resource.isVerified()) return Response.resourceUnavailableToRelease();
        if(!resource.isAccepted()) return Response.resourceUnavailableToRelease();
        item.setOnTime(LocalDateTime.now());
        item.setOwnerPhone(owner.getPhone());
        item.setResourceCode(resource.getResourceCode());
        return Response.success(itemRepo.save(item).getItemCode());
    }


    // retract interface for upper layer
    @Override
    public Response retract(String itemCode, String ownerPhone, String uToken){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(ownerPhone, uToken)) return Response.invalidUserToken();
        Optional<Item> maybeItem = itemRepo.findByItemCode(itemCode);
        if(!maybeItem.isPresent()) return Response.invalidItemCode();
        Item item = maybeItem.get();
        User owner = maybeUser.get();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(item.getResourceCode());
        if(!maybeResource.isPresent()) return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        if(!resource.getOwnerPhone().equals(owner.getPhone())) return Response.resourceNotOwned();
        if(!item.getOwnerPhone().equals(owner.getPhone())) return Response.itemNotOwned();
        if(item.isOrdered()) return Response.itemAlreadyOrdered();
        itemRepo.delete(item);
        return Response.success(null);
    }


    // delete resource interface for upper layer
    @Override
    public Response delete(String resourceCode, String ownerPhone, String uToken){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(ownerPhone, uToken)) return Response.invalidUserToken();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent()) return Response.invalidResourceCode();
        User owner = maybeUser.get();
        Resource resource = maybeResource.get();
        if(!resource.getOwnerPhone().equals(owner.getPhone())) return Response.resourceNotOwned();
        if(itemRepo.existsByResourceCode(resourceCode)) return Response.resourceAlreadyReleased();
        resourceRepo.delete(resource);
        return Response.success(null);
    }


    // update resource info interface for upper layer
    @Override
    public Response update(Resource newResource, String resourceCode, String ownerPhone, String uToken){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(ownerPhone, uToken)) return Response.invalidUserToken();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent()) return Response.invalidResourceCode();
        User owner = maybeUser.get();
        Resource resource = maybeResource.get();
        if(!resource.getOwnerPhone().equals(owner.getPhone())) return Response.resourceNotOwned();
        if(itemRepo.existsByResourceCode(resourceCode)) return Response.resourceAlreadyReleased();
        resource.setResourceName(newResource.getResourceName());
        resource.setResourceTag(newResource.getResourceTag());
        resource.setDescription(newResource.getDescription());
        resource.setImageUrl(newResource.getImageUrl());
        resource.setVerified(false);
        resource.setAccepted(false);
        return Response.success(resourceRepo.save(resource).getResourceCode());
    }


    // admin check resource interface for upper layer
    @Override
    public Response check(String resourceCode, String adminPhone, String uToken, boolean accept){
        Optional<User> maybeUser = userRepo.findByPhone(adminPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        User admin = maybeUser.get();
        if(!admin.isAdmin()) return Response.permissionDenied();
        if(!uTokenValid(adminPhone, uToken)) return Response.invalidUserToken();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent()) return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        resource.setVerified(true);
        resource.setAccepted(accept);
        resourceRepo.save(resource);
        return Response.success(null);
    }


    // get items by filter interface for upper layer
    @Override
    public Response getItemByFilter(ItemFilter filter, String userPhone,
                                    String userToken, Integer requestCount){
        Optional<User> maybeUser = userRepo.findByPhone(userPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, userToken)) return Response.invalidUserToken();
        if(filter.allNull())
            return Response.success(itemRepo.findAll());
        else if(filter.nullExceptCode()){
            Optional<Item> maybe = itemRepo.findByItemCode(filter.getCode());
            return maybe.map(item -> Response.success(Collections.singletonList(item)))
                    .orElseGet(() -> Response.success(Collections.emptyList()));
        }
        else if(filter.getCode() != null)
            return Response.success(Collections.emptyList());
        List<Item> type = null;
        List<Item> needs2Pay = null;
        List<Item> campus = null;
        List<Item> resourceCode = null;
        if(filter.getType() != null)
            type = itemRepo.findAllByItemType(filter.getType());
        if(filter.getNeeds2Pay() != null)
            needs2Pay = itemRepo.findAllByNeeds2Pay(filter.getNeeds2Pay());
        if(filter.getCampus() != null)
            campus = itemRepo.findAllByCampus(filter.getCampus());
        if(filter.getResourceCode() != null)
            resourceCode = itemRepo.findAllByResourceCode(filter.getResourceCode());
        MyUtils<Item> util = new MyUtils<>();
        List<Item> result = util.intersect(util.intersect(type, needs2Pay),
                util.intersect(campus, resourceCode));
        return Response.success(util.contract(result, requestCount));
    }


    // get resources by filter interface for upper layer
    @Override
    public Response getResourceByFilter(ResourceFilter filter, String userPhone,
                                        String userToken, Integer requestCount){
        Optional<User> maybeUser = userRepo.findByPhone(userPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, userToken)) return Response.invalidUserToken();
        if(filter.allNull())
            return Response.success(resourceRepo.findAll());
        else if(filter.nullExceptCode()){
            Optional<Resource> maybe = resourceRepo.findByResourceCode(filter.getCode());
            return maybe.map(resource -> Response.success(Collections.singletonList(resource)))
                    .orElseGet(() -> Response.success(Collections.emptyList()));
        }
        else if(filter.getCode() != null)
            return Response.success(Collections.emptyList());

        List<Resource> name = null;
        List<Resource> verified = null;
        List<Resource> released = null;
        List<Resource> description = null;
        List<Resource> tag = null;
        List<Resource> ownerPhone = null;
        List<Resource> accepted = null;
        if(filter.getName() != null)
            name = resourceRepo.findAllByResourceNameContains(filter.getName());
        if(filter.getVerified() != null)
            verified = resourceRepo.findAllByVerified(filter.getVerified());
        if(filter.getReleased() != null)
            released = resourceRepo.findAllByReleased(filter.getReleased());
        if(filter.getDescription() != null)
            description = resourceRepo.findAllByDescriptionContains(filter.getDescription());
        if(filter.getTag() != null)
            tag = resourceRepo.findAllByResourceTag(filter.getTag());
        if(filter.getOwner_phone() != null)
            ownerPhone = resourceRepo.findAllByOwnerPhone(filter.getOwner_phone());
        if(filter.getAccepted() != null)
            accepted = resourceRepo.findAllByAccepted(filter.getAccepted());
        MyUtils<Resource> util = new MyUtils<>();
        List<Resource> r1 = util.intersect(name, verified);
        List<Resource> r2 = util.intersect(released, description);
        List<Resource> r3 = util.intersect(tag, ownerPhone);
        List<Resource> result = util.intersect(util.intersect(r1, accepted), util.intersect(r2, r3));
        return Response.success(util.contract(result, requestCount));
    }


    // get item page interface for upper layer
    @Override
    public Response getPage(ItemPage page){
        Sort sort = Sort.by(page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNum(), page.getPageSize(), sort);
        return Response.success(itemRepo.findAll(pageable));
    }

    public Optional<Resource> getByCode(String code){
        return resourceRepo.findByResourceCode(code);
    }


    /**----------------------private methods----------------------------------**/

    private boolean uTokenValid(String userPhone, String uToken){
        Optional<UserToken> maybe = userTokenRepo.findByUserPhone(userPhone);
        if(!maybe.isPresent()) return false;
        UserToken token = maybe.get();
        return token.getToken().equals(uToken);
    }
}
