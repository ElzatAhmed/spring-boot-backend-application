package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.service.OrderService;
import team.peiYangCoders.PeiYangResourceManagement.service.UserTokenService;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;
    private final UserTokenService userTokenService;

    @Autowired
    public OrderController(OrderService orderService, UserTokenService userTokenService) {
        this.orderService = orderService;
        this.userTokenService = userTokenService;
    }


    /**
     * user post new order api (as getter)
     * @param phone : user phone
     * @param itemCode : the code of the item to order
     * @param userToken : the valid token system has distributed to the user
     * @param orderInfos : Order information body
     *                   {
     *                      "count" : "",
     *                      "comment": ""
     *                   }
     * */
    @PostMapping ("getter/order/new")
    public Response newOrder(@RequestParam(name = "user_phone") String phone,
                             @RequestParam(name = "item_code") String itemCode,
                             @RequestParam(name = "user_token") String userToken,
                             @RequestBody Body.OrderInfos orderInfos){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        return orderService.newOrder(phone, itemCode, orderInfos);
    }


    /**
     * user cancel order api (as getter)
     * @param phone : user phone number
     * @param orderCode : the code of the order to cancel
     * @param userToken : the valid token system has distributed to the user
     * */
    @DeleteMapping("getter/order")
    public Response cancelOrder(@RequestParam(name = "user_phone") String phone,
                                @RequestParam(name = "order_code") String orderCode,
                                @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        return orderService.cancelOrder(phone, orderCode);
    }


    /**
     * user accept order api (as owner)
     * @param phone : user phone
     * @param orderCode : the code of the order to accept
     * @param userToken : the valid token system has distributed to the user
     * */
    @PostMapping("owner/order")
    public Response acceptOrder(@RequestParam(name = "user_phone") String phone,
                                @RequestParam(name = "order_code") String orderCode,
                                @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        return orderService.acceptOrRejectOrder(phone, orderCode, true);
    }



    /**
     * user reject order api (as owner)
     * @param phone : user phone
     * @param orderCode : the code of the order to accept
     * @param userToken : the valid token system has distributed to the user
     * */
    @DeleteMapping("owner/order")
    public Response rejectOrder(@RequestParam(name = "user_phone") String phone,
                                @RequestParam(name = "order_code") String orderCode,
                                @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        return orderService.acceptOrRejectOrder(phone, orderCode, false);
    }


    /**
     * user complete order api (as getter)
     * @param phone : user phone
     * @param orderCode : the code of the order to accept
     * @param userToken : the valid token system has distributed to the user
     * */
    @PutMapping("getter/order")
    public Response getterCompleteOrder(@RequestParam(name = "user_phone") String phone,
                                        @RequestParam(name = "order_code") String orderCode,
                                        @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        return orderService.getterCompleteOrder(phone, orderCode);
    }


    /**
     * user complete order api (as owner)
     * @param phone : user phone
     * @param orderCode : the code of the order to accept
     * @param userToken : the valid token system has distributed to the user
     * */
    @PutMapping("owner/order")
    public Response ownerCompleteOrder(@RequestParam(name = "user_phone") String phone,
                                       @RequestParam(name = "order_code") String orderCode,
                                       @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.tokenIsValid(phone, userToken))
            return Response.invalidUserToken();
        return orderService.ownerCompleteOrder(phone, orderCode);
    }
}
