package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.OrderTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderCode(String code);

    List<Order> findByGetterPhone(String getterPhone);

    List<Order> findByOwnerPhone(String ownerPhone);

    List<Order> findByAccepted(boolean accepted);

}
