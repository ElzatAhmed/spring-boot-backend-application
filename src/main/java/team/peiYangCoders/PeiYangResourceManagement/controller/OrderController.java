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


    @PostMapping ("getter/order/new")
    public Response newOrder(@RequestParam(name = "phone") String phone,
                             @RequestParam(name = "resource_code") String resourceCode,
                             @RequestParam(name = "user_token") String userToken,
                             @RequestBody Body.OrderInfos orderInfos){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return orderService.newOrder(phone, resourceCode, orderInfos);
    }

    @DeleteMapping("getter/order")
    public Response cancelOrder(@RequestParam(name = "phone") String phone,
                                @RequestParam(name = "order_code") String orderCode,
                                @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return orderService.cancelOrder(phone, orderCode);
    }

    @PostMapping("owner/order")
    public Response acceptOrder(@RequestParam(name = "phone") String phone,
                                @RequestParam(name = "order_code") String orderCode,
                                @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return orderService.acceptOrRejectOrder(phone, orderCode, true);
    }

    @DeleteMapping("owner/order")
    public Response rejectOrder(@RequestParam(name = "phone") String phone,
                                @RequestParam(name = "order_code") String orderCode,
                                @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return orderService.acceptOrRejectOrder(phone, orderCode, false);
    }

    @PutMapping("getter/order")
    public Response getterCompleteOrder(@RequestParam(name = "phone") String phone,
                                        @RequestParam(name = "order_code") String orderCode,
                                        @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return orderService.getterCompleteOrder(phone, orderCode);
    }

    @PutMapping("owner/order")
    public Response ownerCompleteOrder(@RequestParam(name = "phone") String phone,
                                       @RequestParam(name = "order_code") String orderCode,
                                       @RequestParam(name = "user_token") String userToken){
        if(!userTokenService.codeIsValid(phone, userToken))
            return Response.invalidUserCode();
        return orderService.ownerCompleteOrder(phone, userToken);
    }
}
