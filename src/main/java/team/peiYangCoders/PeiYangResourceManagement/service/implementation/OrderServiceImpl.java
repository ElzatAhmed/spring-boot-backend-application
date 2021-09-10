package team.peiYangCoders.PeiYangResourceManagement.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.OrderConfig;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.UserToken;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.ItemRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.OrderRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserRepository;
import team.peiYangCoders.PeiYangResourceManagement.repository.UserTokenRepository;
import team.peiYangCoders.PeiYangResourceManagement.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final UserTokenRepository userTokenRepo;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepo,
                            UserRepository userRepo,
                            ItemRepository itemRepo,
                            UserTokenRepository userTokenRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
        this.userTokenRepo = userTokenRepo;
    }

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    // post new order interface for upper layer
    @Override
    public Response post(String getterPhone, String userToken, String itemCode, Order order){
        Optional<User> maybeUser = userRepo.findByPhone(getterPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(getterPhone, userToken)) return Response.invalidUserToken();
        Optional<Item> maybeItem = itemRepo.findByItemCode(itemCode);
        if(!maybeItem.isPresent()) return Response.invalidItemCode();
        Item item = maybeItem.get();
        User getter = maybeUser.get();
        if(item.getCount() < order.getCount())
            return Response.itemNotSufficient();
        LocalDateTime now = LocalDateTime.now();
        OrderConfig config = new OrderConfig();
        order.setOpenedTime(now);
        order.setAcceptingOrRejectingExpiresAt(now.plusDays(config.getAcceptingValidTime()));
        order.setCompletionExpiresAt(now.plusDays(config.getCompletionValidTime()));
        order.setGetterPhone(getter.getPhone());
        order.setOwnerPhone(item.getOwnerPhone());
        order.setItemCode(itemCode);
        item.setCount(item.getCount() - order.getCount());
        item.setOrdered(true);
        itemRepo.save(item);
        logger.info("user " + getterPhone + " posted a new order on item " + itemCode);
        return Response.success(orderRepo.save(order).getOrderCode());
    }

    // cancel or reject order interface for upper layer
    @Override
    public Response delete(String userPhone, String userToken, String orderCode){
        logger.info("user " + userPhone + " is deleting order " + orderCode);
        Optional<User> maybeUser = userRepo.findByPhone(userPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, userToken)) return Response.invalidUserToken();
        Optional<Order> maybeOrder = orderRepo.findByOrderCode(orderCode);
        if(!maybeOrder.isPresent()) return Response.invalidOrderCode();
        Order order = maybeOrder.get();
        if(isGetterOfOrder(userPhone, order)) return cancel(order);
        if(isOwnerOfOrder(userPhone, order)) return acceptOrReject(order, false);
        return Response.orderNotOwned();
    }


    // accept order interface for upper layer
    @Override
    public Response accept(String ownerPhone, String userToken, String orderCode){
        Optional<User> maybeUser = userRepo.findByPhone(ownerPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(ownerPhone, userToken)) return Response.invalidUserToken();
        Optional<Order> maybeOrder = orderRepo.findByOrderCode(orderCode);
        if(!maybeOrder.isPresent()) return Response.invalidOrderCode();
        Order order = maybeOrder.get();
        if(!isOwnerOfOrder(ownerPhone, order)) return Response.orderNotOwned();
        return acceptOrReject(order, true);
    }


    // complete order interface for upper layer
    @Override
    public Response complete(String userPhone, String userToken, String orderCode){
        logger.info("user " + userPhone + " is completing order " + orderCode);
        Optional<User> maybeUser = userRepo.findByPhone(userPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, userToken)) return Response.invalidUserToken();
        Optional<Order> maybeOrder = orderRepo.findByOrderCode(orderCode);
        if(!maybeOrder.isPresent()) return Response.invalidOrderCode();
        Order order = maybeOrder.get();
        if(isOwnerOfOrder(userPhone, order)) return ownerCompleteOrder(order);
        if(isGetterOfOrder(userPhone, order)) return getterCompleteOrder(order);
        return Response.orderNotOwned();
    }

    @Override
    public Response unComplete(String userPhone, String userToken, String orderCode) {
        logger.info("user " + userPhone + " is unCompleting order " + orderCode);
        Optional<User> maybeUser = userRepo.findByPhone(userPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, userToken)) return Response.invalidUserToken();
        Optional<Order> maybeOrder = orderRepo.findByOrderCode(orderCode);
        if(!maybeOrder.isPresent()) return Response.invalidOrderCode();
        Order order = maybeOrder.get();
        if(order.isExpired())
            return Response.orderExpired();
        if(order.isCanceled())
            return Response.orderAlreadyCanceled();
        if(!order.isAccepted())
            return Response.orderNotAccepted();
        if(order.isCompleted() || order.isUncompleted())
            return Response.orderClosed();
        if(order.isCompletedByOwner())
            return Response.orderIsCompletedAtYourSide();
        order.setUncompleted(true);
        order.setUncompletedByOwner(true);
        order.setClosedTime(LocalDateTime.now());
        logger.info("order " + orderCode + " has been unCompleted by user " + userPhone);
        return Response.success(orderRepo.save(order));
    }

    @Override
    public Response getOrderInfo(String userPhone, String userToken) {
        logger.info("user " + userPhone + " is requesting the information of its orders");
        Optional<User> maybeUser = userRepo.findByPhone(userPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(userPhone, userToken)) return Response.invalidUserToken();
        logger.info("the information of orders related with user " + userPhone + " has been" +
                " sent to the user");
        return Response.success(orderRepo.findAllByGetterPhoneOrOwnerPhone(userPhone, userPhone));
    }

    @Override
    public Response getAll(String adminPhone, String userToken) {
        Optional<User> maybeUser = userRepo.findByPhone(adminPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(adminPhone, userToken)) return Response.invalidUserToken();
        if(!maybeUser.get().isAdmin()) return Response.permissionDenied();
        return Response.success(orderRepo.findAll());
    }

    @Override
    public Response getOrdersByPhone(String adminPhone, String userToken, String phone) {
        Optional<User> maybeUser = userRepo.findByPhone(adminPhone);
        if(!maybeUser.isPresent()) return Response.invalidPhone();
        if(!uTokenValid(adminPhone, userToken)) return Response.invalidUserToken();
        if(!maybeUser.get().isAdmin()) return Response.permissionDenied();
        return Response.success(orderRepo.findAllByGetterPhoneOrOwnerPhone(phone, phone));
    }


    /**----------------------private methods----------------------------------**/

    private boolean uTokenValid(String userPhone, String uToken){
        Optional<UserToken> maybe = userTokenRepo.findByUserPhone(userPhone);
        if(!maybe.isPresent()) return false;
        UserToken token = maybe.get();
        return token.getToken().equals(uToken);
    }

    private boolean isOwnerOfOrder(String userPhone, Order order){
        if(order.getOwnerPhone().equals(userPhone)){
            logger.info("user " + userPhone + " is the owner side of the order " + order.getOrderCode());
            return true;
        }
        else return false;
    }

    private boolean isGetterOfOrder(String userPhone, Order order){
        if(order.getGetterPhone().equals(userPhone)){
            logger.info("user " + userPhone + " is the getter side of the order " + order.getOrderCode());
            return true;
        }
        else return false;
    }

    private Response cancel(Order order){
        logger.info("user " + order.getGetterPhone() + " is canceling order " + order.getOrderCode());
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
        orderRepo.save(order);
        logger.info("order " + order.getOrderCode() + " has been canceled by user " + order.getOrderCode());
        return Response.success(null);
    }

    private Response acceptOrReject(Order order, boolean accept){
        logger.info("user " + order.getOwnerPhone() + " is "
                + (accept ? "accepting" : "rejecting") + " order " + order.getOrderCode());
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
        if(!accept){
            Optional<Item> maybe = itemRepo.findByItemCode(order.getItemCode());
            maybe.ifPresent(item -> item.setCount(item.getCount() + order.getCount()));
        }
        orderRepo.save(order);
        logger.info("order " + order.getOrderCode() + " has been "
                + (accept ? "accepted" : "rejected") + " by user " + order.getOwnerPhone());
        return Response.success(null);
    }

    private Response getterCompleteOrder(Order order){
        if(order.isExpired())
            return Response.orderExpired();
        if(order.isCanceled())
            return Response.orderAlreadyCanceled();
        if(!order.isAccepted())
            return Response.orderNotAccepted();
        if(order.isCompleted() || order.isUncompleted())
            return Response.orderClosed();
        if(order.isCompletedByGetter())
            return Response.orderIsCompletedAtYourSide();
        order.setCompletedByGetter(true);
        order.setGetterCompletedTime(LocalDateTime.now());
        if(order.isCompletedByOwner()) order.setCompleted(true);
        logger.info("order " + order.getOrderCode() + " has been " +
                "completed by getter side user " + order.getGetterPhone());
        return Response.success(orderRepo.save(order));
    }

    private Response ownerCompleteOrder(Order order){
        if(order.isExpired())
            return Response.orderExpired();
        if(order.isCanceled())
            return Response.orderAlreadyCanceled();
        if(!order.isAccepted())
            return Response.orderNotAccepted();
        if(order.isCompleted() || order.isUncompleted())
            return Response.orderClosed();
        if(order.isCompletedByOwner() || order.isUncompletedByOwner())
            return Response.orderIsCompletedAtYourSide();
        order.setCompletedByOwner(true);
        order.setOwnerCompletedTime(LocalDateTime.now());
        if(order.isCompletedByGetter()) order.setCompleted(true);
        logger.info("order " + order.getOrderCode() + " has been " +
                "completed by owner side user " + order.getOwnerPhone());
        return Response.success(orderRepo.save(order));
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

    private boolean expiredFromOwner(Order order){
        if(order.isExpired()) return false;
        if(order.isCanceled()) return false;
        LocalDateTime now = LocalDateTime.now();
        if(!order.isAcceptedOrRejected())
            return order.getAcceptingOrRejectingExpiresAt().isBefore(now);
        return false;
    }

    private boolean completionExpired(Order order){
        if(order.isExpired()) return false;
        if(order.isCanceled()) return false;
        if(order.isUncompleted()) return false;
        LocalDateTime now = LocalDateTime.now();
        if(!order.isCompleted())
            return order.getCompletionExpiresAt().isBefore(now);
        return false;
    }
}
