package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourceInfo;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.ResourcePage;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.ResourceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepo;

    @Autowired
    public ResourceService(ResourceRepository resourceRepo) {
        this.resourceRepo = resourceRepo;
    }

    public Response getResources(ResourcePage page){
        Sort sort = Sort.by(page.getDirection(), page.getSortBy());
        Pageable pageable = PageRequest.of(page.getPageNum(), page.getPageSize(), sort);
        Page<Resource> resources = resourceRepo.findAll(pageable);
        for(Resource resource : resources){
            resource.setOwner(null);
            resource.setOrders(null);
        }
        return Response.okMessage(resources);
    }

    public Response addResources(List<ResourceInfo> infos, User owner){
        List<Resource> resources = new ArrayList<>();
        for(ResourceInfo info : infos){
            resources.add(new Resource(info, owner));
        }
        resourceRepo.saveAll(resources);
        return Response.okMessage(infos);
    }

    public Response deleteResource(ResourceInfo info, User owner){
        if(!info.getOwner_phone().equals(owner.getPhone()))
            return Response.errorMessage(Response.permissionDenied);
        resourceRepo.delete(new Resource(info, owner));
        return Response.okMessage(info);
    }

    public Response deleteResources(List<ResourceInfo> infos, User owner){
        List<Resource> resources = new ArrayList<>();
        for(ResourceInfo info : infos){
            resources.add(new Resource(info, owner));
        }
        resourceRepo.deleteAll(resources);
        return Response.okMessage(infos);
    }

    public Response getByCode(UUID code){
        Optional<Resource> resource = resourceRepo.findByCode(code);
        if(resource.isPresent()) return Response.okMessage(resource);
        else return Response.errorMessage(Response.noSuchResource);
    }

    public Response getAll(){
        List<Resource> allResource = resourceRepo.findAll();
        List<ResourceInfo> infos = new ArrayList<>();
        for(Resource resource : allResource){
            infos.add(new ResourceInfo(resource));
        }
        return Response.okMessage(infos);
    }

    public Response getAllByOwner(User owner){
        return Response.okMessage(resourceRepo.findAllByOwner(owner));
    }

    public Response getAllByName(String name){
        return Response.okMessage(resourceRepo.findAllByName(name));
    }

    public Response getAllByNameContains(String name){
        return Response.okMessage(resourceRepo.findAllByNameContains(name));
    }

    public Response getAllByTag(ResourceTag tag){
        return Response.okMessage(resourceRepo.findAllByTag(tag));
    }

    public Response getAllByDescriptionContains(String description){
        return Response.okMessage(resourceRepo.findAllByDescriptionContains(description));
    }

    public Response add(ResourceInfo info, User owner){
        resourceRepo.save(new Resource(info, owner));
        return Response.okMessage(info);
    }
}
