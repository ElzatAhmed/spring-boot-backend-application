package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    Optional<Item> findByItemCode(UUID itemCode);

    boolean existsByResource(Resource resource);
}
