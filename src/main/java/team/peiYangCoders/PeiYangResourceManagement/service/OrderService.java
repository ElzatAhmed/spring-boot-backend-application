package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.OrderConfig;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.ItemRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.OrderRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;

    @Autowired
    public OrderService(OrderRepository orderRepo, UserRepository userRepo, ItemRepository itemRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
    }

    public Response newOrder(String getterPhone, String itemCode, Order order){
        Optional<User> maybeUser = userRepo.findByPhone(getterPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Item> maybeItem = itemRepo.findByItemCode(itemCode);
        if(!maybeItem.isPresent())
            return Response.invalidItemCode();
        Item item = maybeItem.get();
        if(item.getCount() < order.getCount())
            return Response.itemNotSufficient();
        item.setCount(item.getCount() - order.getCount());
        LocalDateTime now = LocalDateTime.now();
        OrderConfig config = new OrderConfig();
        order.setOpenedTime(now);
        order.setAcceptingOrRejectingExpiresAt(now.plusDays(config.getAcceptingValidTime()));
        order.setCompletionExpiresAt(now.plusDays(config.getCompletionValidTime()));
        order.setGetterPhone(getterPhone);
        order.setOwnerPhone(item.getOwnerPhone());
        order.setItemCode(itemCode);
        System.out.println(order);
        return Response.success(orderRepo.save(order));
    }

    public Response cancelOrder(String getterPhone, String orderCode){
        Optional<User> maybeUser = userRepo.findByPhone(getterPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Order> maybeOrder = orderRepo.findByOrderCode(orderCode);
        if(!maybeOrder.isPresent())
            return Response.invalidOrderCode();
        Order order = maybeOrder.get();
        if(!order.getGetterPhone().equals(getterPhone))
            return Response.orderNotOwned();
        if(order.isExpired())
            return Response.orderExpired();
        if(order.isCanceled())
            return Response.orderAlreadyCanceled();
        if(order.isCompleted() || order.isUncompleted())
            return Response.orderClosed();
        if(order.isCompletedByGetter() || order.isCompletedByOwner())
            return Response.orderIsClosedByAtLeastOneSide();
        if(order.isAccepted())
            return Response.orderAlreadyAccepted();
        order.setCanceled(true);
        order.setCanceledTime(LocalDateTime.now());
        Optional<Item> maybe = itemRepo.findByItemCode(order.getItemCode());
        maybe.ifPresent(item -> item.setCount(item.getCount() + order.getCount()));
        return Response.success(orderRepo.save(order));
    }

    public Response acceptOrRejectOrder(String ownerPhone, String orderCode, boolean accept){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Order> maybeOrder = orderRepo.findByOrderCode(orderCode);
        if(!maybeOrder.isPresent())
            return Response.invalidOrderCode();
        Order order = maybeOrder.get();
        if(!order.getOwnerPhone().equals(ownerPhone))
            return Response.orderNotOwned();
        if(order.isExpired())
            return Response.orderExpired();
        if(order.isCanceled())
            return Response.orderAlreadyCanceled();
        if(order.isCompleted() || order.isUncompleted())
            return Response.orderClosed();
        if(order.isCompletedByGetter() || order.isCompletedByOwner())
            return Response.orderIsClosedByAtLeastOneSide();
        if(order.isAccepted())
            return Response.orderAlreadyAccepted();
        order.setAcceptedOrRejectedTime(LocalDateTime.now());
        order.setAccepted(accept);
        order.setAcceptedOrRejected(true);
        return Response.success(orderRepo.save(order));
    }

    public Response getterCompleteOrder(String getterPhone, String orderCode){
        Optional<User> maybeUser = userRepo.findByPhone(getterPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Order> maybeOrder = orderRepo.findByOrderCode(orderCode);
        if(!maybeOrder.isPresent())
            return Response.invalidOrderCode();
        Order order = maybeOrder.get();
        if(!order.getGetterPhone().equals(getterPhone))
            return Response.orderNotOwned();
        if(order.isExpired())
            return Response.orderExpired();
        if(order.isCanceled())
            return Response.orderAlreadyCanceled();
        if(!order.isAccepted())
            return Response.orderNotAccepted();
        if(order.isCompleted() || order.isUncompleted())
            return Response.orderClosed();
        if(order.isCompletedByGetter())
            return Response.orderIsClosedAtYourSide();
        order.setCompletedByGetter(true);
        return Response.success(orderRepo.save(order));
    }

    public Response ownerCompleteOrder(String ownerPhone, String orderCode){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent())
            return Response.invalidPhone();
        Optional<Order> maybeOrder = orderRepo.findByOrderCode(orderCode);
        if(!maybeOrder.isPresent())
            return Response.invalidOrderCode();
        Order order = maybeOrder.get();
        if(!order.getOwnerPhone().equals(ownerPhone))
            return Response.orderNotOwned();
        if(order.isExpired())
            return Response.orderExpired();
        if(order.isCanceled())
            return Response.orderAlreadyCanceled();
        if(!order.isAccepted())
            return Response.orderNotAccepted();
        if(order.isCompleted() || order.isUncompleted())
            return Response.orderClosed();
        if(order.isCompletedByOwner())
            return Response.orderIsClosedAtYourSide();
        order.setCompletedByOwner(true);
        return Response.success(orderRepo.save(order));
    }

    public Optional<Order> getByCode(String code){
        return orderRepo.findByOrderCode(code);
    }


    @Scheduled(fixedRate = 600000L)
    void checkOrders(){
        List<Order> orders = orderRepo.findAll();
        for(Order order : orders){
            order.setExpired(expiredFromOwner(order));
            if(completionExpired(order)){
                order.setCompletedByOwner(true);
                order.setCompletedByGetter(true);
                order.setCompleted(true);
            }
        }
        orderRepo.saveAll(orders);
    }

    public boolean expiredFromOwner(Order order){
        if(order.isExpired()) return false;
        if(order.isCanceled()) return false;
        LocalDateTime now = LocalDateTime.now();
        if(!order.isAcceptedOrRejected())
            return order.getAcceptingOrRejectingExpiresAt().isBefore(now);
        return false;
    }

    public boolean completionExpired(Order order){
        if(order.isExpired()) return false;
        if(order.isCanceled()) return false;
        if(order.isUncompleted()) return false;
        LocalDateTime now = LocalDateTime.now();
        if(!order.isCompleted())
            return order.getCompletionExpiresAt().isAfter(now);
        return false;
    }
}
