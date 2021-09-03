package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.ResourceTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Optional<Resource> findByResourceCode(String resourceCode);

    List<Resource> findAllByResourceNameContains(String name);

    List<Resource> findAllByVerified(boolean verified);

    List<Resource> findAllByReleased(boolean released);

    List<Resource> findAllByDescriptionContains(String description);

    List<Resource> findAllByResourceTag(String resourceTag);

    List<Resource> findAllByOwnerPhone(String ownerPhone);

    List<Resource> findAllByAccepted(boolean accepted);
}
