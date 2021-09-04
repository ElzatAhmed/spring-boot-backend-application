package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.Item.Item;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.service.OrderService;
import team.peiYangCoders.PeiYangResourceManagement.service.ResourceService;
import team.peiYangCoders.PeiYangResourceManagement.service.UserTokenService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;
    private final UserTokenService userTokenService;

    @Autowired
    public OrderController(OrderService orderService,
                           UserTokenService userTokenService) {
        this.orderService = orderService;
        this.userTokenService = userTokenService;
    }


    /**
     * user post new order api (as getter)
     * @param phone : user phone
     * @param itemCode : the code of the item to order
     * @param userToken : the valid token system has distributed to the user
     * @param order : Order information body
     *                   {
     *                      "count" : "",
     *                      "comment": ""
     *                   }
     * */
    @PostMapping ("order/new")
    public Response newOrder(@RequestParam(name = "phone") String phone,
                             @RequestParam(name = "itemCode") String itemCode,
                             @RequestParam(name = "uToken") String userToken,
                             @RequestBody(required = false) Order order){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        return orderService.newOrder(phone, itemCode, order);
    }


    @DeleteMapping("order")
    public Response deleteOrder(@RequestParam(name = "phone") String phone,
                                @RequestParam(name = "orderCode") String orderCode,
                                @RequestParam(name = "uToken") String userToken){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        Optional<Order> maybe = orderService.getByCode(orderCode);
        if(!maybe.isPresent())
            return Response.invalidOrderCode();
        Order order = maybe.get();
        if(order.getOwnerPhone().equals(phone))
            return orderService.acceptOrRejectOrder(phone, orderCode, false);
        else if(order.getGetterPhone().equals(phone))
            return orderService.cancelOrder(phone, orderCode);
        else
            return Response.orderNotOwned();
    }


    /**
     * user accept order api (as owner)
     * @param phone : user phone
     * @param orderCode : the code of the order to accept
     * @param userToken : the valid token system has distributed to the user
     * */
    @PostMapping("order")
    public Response acceptOrder(@RequestParam(name = "phone") String phone,
                                @RequestParam(name = "orderCode") String orderCode,
                                @RequestParam(name = "uToken") String userToken){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        return orderService.acceptOrRejectOrder(phone, orderCode, true);
    }



    @PutMapping("order")
    public Response completeOrder(@RequestParam(name = "phone") String phone,
                                  @RequestParam(name = "orderCode") String orderCode,
                                  @RequestParam(name = "uToken") String userToken){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        Optional<Order> maybe = orderService.getByCode(orderCode);
        if(!maybe.isPresent())
            return Response.invalidOrderCode();
        Order order = maybe.get();
        if(order.getOwnerPhone().equals(phone))
            return orderService.ownerCompleteOrder(phone, orderCode);
        else if(order.getGetterPhone().equals(phone))
            return orderService.getterCompleteOrder(phone, orderCode);
        else
            return Response.orderNotOwned();
    }
}
