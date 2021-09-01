package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Body;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.service.OrderService;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping ("getter/order/new")
    public Response newOrder(@RequestParam String phone, String code,
                             @RequestBody Body.OrderInfos orderInfos){
        return orderService.newOrder(phone, code, orderInfos);
    }

    @DeleteMapping("getter/order")
    public Response cancelOrder(@RequestParam String phone, String code){
        return orderService.cancelOrder(phone, code);
    }

    @PostMapping("owner/order")
    public Response acceptOrder(@RequestParam String phone, String code){
        return orderService.acceptOrRejectOrder(phone, code, true);
    }

    @DeleteMapping("owner/order")
    public Response rejectOrder(@RequestParam String phone, String code){
        return orderService.acceptOrRejectOrder(phone, code, false);
    }

    @PutMapping("getter/order")
    public Response getterCompleteOrder(@RequestParam String phone, String code){
        return orderService.getterCompleteOrder(phone, code);
    }

    @PutMapping("owner/order")
    public Response ownerCompleteOrder(@RequestParam String phone, String code){
        return orderService.ownerCompleteOrder(phone, code);
    }
}
