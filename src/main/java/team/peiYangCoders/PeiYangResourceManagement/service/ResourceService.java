package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.*;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.ItemRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.ResourceRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Response postNewResource(Body.ResourceInfos resourceInfos, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Resource resource = Resource.getFromBody(resourceInfos, maybeUser.get());
        return Response.success(resourceRepo.save(resource).getCode());
    }

    public Response postMultipleNewResources(List<Body.ResourceInfos> resourceInfos, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        List<UUID> responses = new ArrayList<>();
        for(Body.ResourceInfos resourceInfo : resourceInfos){
            Resource resource = Resource.getFromBody(resourceInfo, maybeUser.get());
            resource = resourceRepo.save(resource);
            responses.add(resource.getCode());
        }
        return Response.success(responses);
    }

    public Response releaseResource(String resourceCode, String ownerPhone, Body.ItemInfos infos){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        if(!resource.getOwner().getPhone().equals(ownerPhone))
            return Response.resourceNotOwned();
        if(!resource.isVerified())
            return Response.resourceUnavailableToRelease();
        if(!resource.isAccepted())
            return Response.resourceUnavailableToRelease();
        Item item = Item.getFromBody(infos, maybeResource.get());
        item = itemRepo.save(item);
        return Response.success(item.getItemCode());
    }

    public Response retractItem(String itemCode, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Item> maybeRe = itemRepo.findByItemCode(UUID.fromString(itemCode));
        if(!maybeRe.isPresent())
            return Response.invalidItemCode();
        Item item = maybeRe.get();
        if(!item.getResource().getOwner().getPhone().equals(ownerPhone))
            return Response.itemNotOwned();
        itemRepo.delete(item);
        return Response.success(null);
    }

    public Response deleteResource(String resourceCode, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        if(!resource.getOwner().getPhone().equals(ownerPhone))
            return Response.resourceNotOwned();
        boolean exists = itemRepo.existsByResource(resource);
        if(exists)
            return Response.resourceAlreadyReleased();
        resourceRepo.delete(resource);
        return Response.success(resource.getCode());
    }

    public Response deleteMultipleResources(List<String> resourceCodes, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        List<String> codes = new ArrayList<>();
        for(String resourceCode : resourceCodes){
            Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
            if(!maybeResource.isPresent()) continue;
            Resource resource = maybeResource.get();
            if(!resource.getOwner().getPhone().equals(ownerPhone)) continue;
            codes.add(resource.getCode().toString());
            resourceRepo.delete(resource);
        }
        return Response.success(codes);
    }

    public Response updateResourceInfo(Body.ResourceInfos newInfo, String resourceCode, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        if(!resource.getOwner().getPhone().equals(ownerPhone))
            return Response.resourceNotOwned();
        boolean exists = itemRepo.existsByResource(resource);
        if(exists)
            return Response.resourceAlreadyReleased();
        Resource newResource = Resource.getFromBody(newInfo, maybeUser.get());
        newResource.setCode(resource.getCode());
        resource = resourceRepo.save(newResource);
        return Response.success(resource.getCode());
    }

    public Response acceptResource(String resourceCode, String adminPhone){
        Optional<User> maybeUser = userRepo.findByPhone(adminPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        User user = maybeUser.get();
        if(!user.isAdmin())
            return Response.permissionDenied();
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        resource.setVerified(true);
        resource.setAccepted(true);
        resource = resourceRepo.save(resource);
        return Response.success(resource.getCode());
    }

    public Response rejectResource(String resourceCode, String adminPhone){
        Optional<User> maybeUser = userRepo.findByPhone(adminPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        User user = maybeUser.get();
        if(!user.isAdmin())
            return Response.permissionDenied();
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.invalidResourceCode();
        Resource resource = maybeResource.get();
        resource.setVerified(true);
        resource.setAccepted(false);
        resource = resourceRepo.save(resource);
        return Response.success(resource.getCode());
    }

    public Response getItem(ItemFilter filter){
        List<Item> items = itemRepo.findAll();
        List<Body.ItemInfos> resultInfos = new ArrayList<>();
        for(Item resource : items){
            if(filter.match(resource))
                resultInfos.add(Item.toBody(resource));
        }
        return Response.success(resultInfos);
    }

    public Response getResource(ResourceFilter filter){
        List<Resource> resources = resourceRepo.findAll();
        List<Body.ResourceInfos> resultInfos = new ArrayList<>();
        for(Resource resource : resources){
            if(filter.match(resource))
                resultInfos.add(Resource.toBody(resource));
        }
        return Response.success(resultInfos);
    }

    public Response getPage(ResourcePage page){
        Sort sort = Sort.by(page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNum(), page.getPageSize(), sort);
        Page<Item> resources = itemRepo.findAll(pageable);
        List<Body.ItemInfos> resultInfos = new ArrayList<>();
        for(Item resource : resources){
            resultInfos.add(Item.toBody(resource));
        }
        return Response.success(resultInfos);
    }
}
