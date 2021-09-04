package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.ItemPage;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.ItemFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.filter.ResourceFilter;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.*;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.ItemRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.ResourceRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.utils.MyUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepo;
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;

    @Autowired
    public ResourceService(ResourceRepository resourceRepo,
                           UserRepository userRepo,
                           ItemRepository itemRepo) {
        this.resourceRepo = resourceRepo;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
    }

    public Response postNewResource(Resource resource, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        resource.setOwnerPhone(ownerPhone);
        return Response.success(resourceRepo.save(resource).getResourceCode());
    }

    public Response postMultipleNewResources(List<Resource> resources, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        List<String> responses = new ArrayList<>();
        resources = resourceRepo.saveAll(resources);
        for(Resource resource : resources)
            responses.add(resource.getResourceCode());
        return Response.success(responses);
    }

    public Response releaseResource(String resourceCode, String ownerPhone, Item item){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        if(!resource.getOwnerPhone().equals(ownerPhone))
            return Response.resourceNotOwned();
        if(!resource.isVerified())
            return Response.resourceUnavailableToRelease();
        if(!resource.isAccepted())
            return Response.resourceUnavailableToRelease();
        item.setOnTime(LocalDateTime.now());
        item.setOwnerPhone(ownerPhone);
        item.setResourceCode(resourceCode);
        item = itemRepo.save(item);
        return Response.success(item.getItemCode());
    }

    public Response retractItem(String itemCode, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Item> maybeRe = itemRepo.findByItemCode(itemCode);
        if(!maybeRe.isPresent())
            return Response.invalidItemCode();
        Item item = maybeRe.get();
        Optional<Resource> resource = resourceRepo.findByResourceCode(item.getResourceCode());
        if(!resource.isPresent())
            return Response.invalidResourceCode();
        if(!resource.get().getOwnerPhone().equals(ownerPhone))
            return Response.itemNotOwned();
        itemRepo.delete(item);
        return Response.success(null);
    }

    public Response deleteResource(String resourceCode, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        if(!resource.getOwnerPhone().equals(ownerPhone))
            return Response.resourceNotOwned();
        boolean exists = itemRepo.existsByResourceCode(resourceCode);
        if(exists)
            return Response.resourceAlreadyReleased();
        resourceRepo.delete(resource);
        return Response.success(null);
    }

    public Response deleteMultipleResources(List<String> resourceCodes, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        List<String> codes = new ArrayList<>();
        for(String resourceCode : resourceCodes){
            Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
            if(!maybeResource.isPresent()) continue;
            Resource resource = maybeResource.get();
            if(!resource.getOwnerPhone().equals(ownerPhone)) continue;
            codes.add(resource.getResourceCode());
            resourceRepo.delete(resource);
        }
        return Response.success(codes);
    }

    public Response updateResourceInfo(Resource newResource, String resourceCode, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        if(!resource.getOwnerPhone().equals(ownerPhone))
            return Response.resourceNotOwned();
        boolean exists = itemRepo.existsByResourceCode(resourceCode);
        if(exists)
            return Response.resourceAlreadyReleased();
        newResource.setResourceCode(resource.getResourceCode());
        resource = resourceRepo.save(newResource);
        return Response.success(resource.getResourceCode());
    }

    public Response acceptResource(String resourceCode, String adminPhone){
        Optional<User> maybeUser = userRepo.findByPhone(adminPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        User user = maybeUser.get();
        if(!user.isAdmin())
            return Response.permissionDenied();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        resource.setVerified(true);
        resource.setAccepted(true);
        resourceRepo.save(resource);
        return Response.success(null);
    }

    public Response rejectResource(String resourceCode, String adminPhone){
        Optional<User> maybeUser = userRepo.findByPhone(adminPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        User user = maybeUser.get();
        if(!user.isAdmin())
            return Response.permissionDenied();
        Optional<Resource> maybeResource = resourceRepo.findByResourceCode(resourceCode);
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        resource.setVerified(true);
        resource.setAccepted(false);
        resource = resourceRepo.save(resource);
        return Response.success(resource.getResourceCode());
    }

    public List<Item> getItem(ItemFilter filter){
        if(filter.allNull())
            return itemRepo.findAll();
        else if(filter.nullExceptCode()){
            Optional<Item> maybe = itemRepo.findByItemCode(filter.getCode());
            return maybe.map(Collections::singletonList).orElse(Collections.emptyList());
        }
        else if(filter.getCode() != null)
            return Collections.emptyList();

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
        return MyUtils.itemListIntersection(MyUtils.itemListIntersection(type, needs2Pay),
                MyUtils.itemListIntersection(campus, resourceCode));
    }

    public List<Resource> getResource(ResourceFilter filter, Integer requestCount){
        if(filter.allNull())
            return resourceRepo.findAll();
        else if(filter.nullExceptCode()){
            Optional<Resource> maybe = resourceRepo.findByResourceCode(filter.getCode());
            return maybe.map(Collections::singletonList).orElse(Collections.emptyList());
        }
        else if(filter.getCode() != null)
            return Collections.emptyList();

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
        List<Resource> r1 = MyUtils.resourceListIntersection(name, verified);
        List<Resource> r2 = MyUtils.resourceListIntersection(released, description);
        List<Resource> r3 = MyUtils.resourceListIntersection(tag, ownerPhone);
        List<Resource> intersected = MyUtils.resourceListIntersection(
                MyUtils.resourceListIntersection(r1, accepted),
                MyUtils.resourceListIntersection(r2, r3));
        if(intersected == null) return null;
        if(requestCount != null){
            List<Resource> result = new ArrayList<>();
            for(int i = 0; i < requestCount; i++){
                if(i >= intersected.size())
                    break;
                result.add(intersected.get(i));
            }
            return result;
        }
        return intersected;
    }

    public Response getPage(ItemPage page){
        Sort sort = Sort.by(page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNum(), page.getPageSize(), sort);
        return Response.success(itemRepo.findAll(pageable));
    }
}
