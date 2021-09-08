package team.peiYangCoders.PeiYangResourceManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;
import team.peiYangCoders.PeiYangResourceManagement.service.OrderService;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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
        return orderService.post(phone, userToken, itemCode, order);
    }


    @DeleteMapping("order")
    public Response deleteOrder(@RequestParam(name = "phone") String phone,
                                @RequestParam(name = "orderCode") String orderCode,
                                @RequestParam(name = "uToken") String userToken){
        return orderService.delete(phone, userToken, orderCode);
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
        return orderService.accept(phone, userToken, orderCode);
    }



    @PutMapping("order")
    public Response completeOrder(@RequestParam(name = "phone") String phone,
                                  @RequestParam(name = "orderCode") String orderCode,
                                  @RequestParam(name = "uToken") String userToken){
        return orderService.complete(phone, userToken, orderCode);
    }


    @GetMapping("order")
    public Response getOrderInfo(@RequestParam(name = "phone") String phone,
                                 @RequestParam(name = "uToken") String userToken){
        return orderService.getOrderInfo(phone, userToken);
    }

    @GetMapping("orders")
    public Response getAll(@RequestParam(name = "phone") String phone,
                           @RequestParam(name = "uToken") String userToken){
        return orderService.getAll(phone, userToken);
    }
}
