package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;


@Service
public interface OrderService {

    Response post(String getterPhone, String userToken, String itemCode, Order order);

    Response delete(String userPhone, String userToken, String orderCode);

    Response accept(String ownerPhone, String userToken, String orderCode);

    Response complete(String userPhone, String userToken, String orderCode);

}
