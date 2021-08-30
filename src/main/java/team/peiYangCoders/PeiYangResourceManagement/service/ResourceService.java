package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final ItemRepository releasedResourceRepo;

    @Autowired
    public ResourceService(ResourceRepository resourceRepo,
                           UserRepository userRepo,
                           ItemRepository releasedResourceRepo) {
        this.resourceRepo = resourceRepo;
        this.userRepo = userRepo;
        this.releasedResourceRepo = releasedResourceRepo;
    }

    public Response postNewResource(Body.ResourceInfos resourceInfos, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Resource resource = Resource.getFromBody(resourceInfos, maybeUser.get());
        return Response.okMessage(resourceRepo.save(resource).getCode());
    }

    public Response postMultipleNewResources(List<Body.ResourceInfos> resourceInfos, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        List<UUID> responses = new ArrayList<>();
        for(Body.ResourceInfos resourceInfo : resourceInfos){
            Resource resource = Resource.getFromBody(resourceInfo, maybeUser.get());
            responses.add(resource.getCode());
        }
        return Response.okMessage(responses);
    }

    public Response releaseResource(String resourceCode, String ownerPhone, Body.ItemInfos infos){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.errorMessage(Response.noSuchResource);
        Resource resource = maybeResource.get();
        if(!resource.getOwner().getPhone().equals(ownerPhone))
            return Response.errorMessage(Response.permissionDenied);
        if(!resource.isVerified())
            return Response.errorMessage(Response.notVerified);
        if(!resource.isAccepted())
            return Response.errorMessage(Response.notAccepted);
        Item item = Item.getFromBody(infos, maybeResource.get());
        item = releasedResourceRepo.save(item);
        return Response.okMessage(item.getItemCode());
    }

    public Response retractResource(String releaseCode, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Optional<Item> maybeRe = releasedResourceRepo.findByItemCode(UUID.fromString(releaseCode));
        if(!maybeRe.isPresent())
            return Response.errorMessage(Response.noSuchResource);
        Item resource = maybeRe.get();
        if(!resource.getResource().getOwner().getPhone().equals(ownerPhone))
            return Response.errorMessage(Response.permissionDenied);
        releasedResourceRepo.delete(resource);
        return Response.okMessage();
    }

    public Response deleteResource(String resourceCode, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.errorMessage(Response.noSuchResource);
        Resource resource = maybeResource.get();
        if(!resource.getOwner().getPhone().equals(ownerPhone))
            return Response.errorMessage(Response.permissionDenied);
        boolean exists = releasedResourceRepo.existsByResource(resource);
        if(exists)
            return Response.errorMessage(Response.existsReleasedResource);
        resourceRepo.delete(resource);
        return Response.okMessage(resource.getCode());
    }

    public Response deleteMultipleResources(List<String> resourceCodes, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        List<String> codes = new ArrayList<>();
        for(String resourceCode : resourceCodes){
            Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
            if(!maybeResource.isPresent()) continue;
            Resource resource = maybeResource.get();
            if(!resource.getOwner().getPhone().equals(ownerPhone)) continue;
            codes.add(resource.getCode().toString());
            resourceRepo.delete(resource);
        }
        return Response.okMessage(codes);
    }

    public Response updateResourceInfo(Body.ResourceInfos newInfo, String resourceCode, String ownerPhone){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.errorMessage(Response.noSuchResource);
        Resource resource = maybeResource.get();
        if(!resource.getOwner().getPhone().equals(ownerPhone))
            return Response.errorMessage(Response.permissionDenied);
        boolean exists = releasedResourceRepo.existsByResource(resource);
        if(exists)
            return Response.errorMessage(Response.existsReleasedResource);
        Resource newResource = Resource.getFromBody(newInfo, maybeUser.get());
        newResource.setCode(resource.getCode());
        resource = resourceRepo.save(newResource);
        return Response.okMessage(resource.getCode());
    }

    public Response acceptResource(String resourceCode, String adminPhone){
        Optional<User> maybeUser = userRepo.findByPhone(adminPhone);
        if(!maybeUser.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        User user = maybeUser.get();
        if(!user.isAdmin())
            return Response.errorMessage(Response.permissionDenied);
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.errorMessage(Response.noSuchResource);
        Resource resource = maybeResource.get();
        resource.setVerified(true);
        resource.setAccepted(true);
        resource = resourceRepo.save(resource);
        return Response.okMessage(resource.getCode());
    }

    public Response rejectResource(String resourceCode, String adminPhone){
        Optional<User> maybeUser = userRepo.findByPhone(adminPhone);
        if(!maybeUser.isPresent())
            return Response.errorMessage(Response.noSuchUser);
        User user = maybeUser.get();
        if(!user.isAdmin())
            return Response.errorMessage(Response.permissionDenied);
        Optional<Resource> maybeResource = resourceRepo.findByCode(UUID.fromString(resourceCode));
        if(!maybeResource.isPresent())
            return Response.errorMessage(Response.noSuchResource);
        Resource resource = maybeResource.get();
        resource.setVerified(true);
        resource.setAccepted(false);
        resource = resourceRepo.save(resource);
        return Response.okMessage(resource.getCode());
    }

    public Response getItem(ItemFilter filter){
        List<Item> items = releasedResourceRepo.findAll();
        List<Body.ItemInfos> resultInfos = new ArrayList<>();
        for(Item resource : items){
            if(filter.match(resource))
                resultInfos.add(Item.toBody(resource));
        }
        return Response.okMessage(resultInfos);
    }

    public Response getResource(ResourceFilter filter){
        List<Resource> resources = resourceRepo.findAll();
        List<Body.ResourceInfos> resultInfos = new ArrayList<>();
        for(Resource resource : resources){
            if(filter.match(resource))
                resultInfos.add(Resource.toBody(resource));
        }
        return Response.okMessage(resultInfos);
    }

    public Response getPage(ResourcePage page){
        Sort sort = Sort.by(page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNum(), page.getPageSize(), sort);
        List<Item> resources = releasedResourceRepo.findAll(sort, pageable);
        List<Body.ItemInfos> resultInfos = new ArrayList<>();
        for(Item resource : resources){
            resultInfos.add(Item.toBody(resource));
        }
        return Response.okMessage(resultInfos);
    }
}
