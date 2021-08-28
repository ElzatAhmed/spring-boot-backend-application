package team.peiYangCoders.PeiYangResourceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.resource.Resource;
import team.peiYangCoders.PeiYangResourceManagement.model.tags.OrderTag;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByInitiator(User initiator);

    List<Order> findAllByRecipient(User recipient);

    List<Order> findAllByInitiatorAndRecipient(User initiator, User recipient);

    List<Order> findAllByResourceAndInitiatorAndRecipient(
            Resource resource,
            User initiator,
            User recipient
    );

    List<Order> findAllByClosed(boolean closed);

    List<Order> findAllByClosedAndInitiator(boolean closed, User initiator);

    List<Order> findAllByClosedAndRecipient(boolean closed, User recipient);

    List<Order> findAllByClosedAndInitiatorAndRecipient(
            boolean closed,
            User initiator,
            User recipient
    );

    List<Order> findAllByTag(OrderTag tag);

    List<Order> findAllByTagAndInitiator(OrderTag tag, User initiator);

    List<Order> findAllByTagAndRecipient(OrderTag tag, User recipient);

    List<Order> findAllByTagAndInitiatorAndRecipient(
            OrderTag tag,
            User initiator,
            User recipient
    );

}
