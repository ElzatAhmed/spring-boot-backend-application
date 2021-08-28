package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<Resource> findAllByOwner(User owner);

    List<Resource> findAllByName(String name);

    List<Resource> findAllByNameContains(String name);

    List<Resource> findAllByTag(ResourceTag tag);

    List<Resource> findAllByDescriptionContains(String description);

    Page<Resource> findAllByVerified(boolean verified, Pageable pageable);

    Optional<Resource> findByCode(UUID code);

}
