package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    Optional<Item> findByItemCode(String itemCode);

    boolean existsByResourceCode(String resourceCode);

    List<Item> findAllByCampus(int campus);

    List<Item> findAllByItemType(String type);

    List<Item> findAllByNeeds2Pay(boolean needs2Pay);

    List<Item> findAllByResourceCode(String resourceCode);
}
