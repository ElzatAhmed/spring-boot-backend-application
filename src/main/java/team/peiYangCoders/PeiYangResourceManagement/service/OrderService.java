package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.order.Order;


@Service
public interface OrderService {

    /**
     * post new order
     * @param getterPhone : getter phone
     * @param userToken : user token
     * @param itemCode : item code
     * @param order : order information
     * */
    Response post(String getterPhone, String userToken, String itemCode, Order order);


    /**
     * delete or reject an order
     * @param userPhone : user phone
     * @param userToken : user token
     * @param orderCode : order code
     * */
    Response delete(String userPhone, String userToken, String orderCode);


    /**
     * accept an order
     * @param ownerPhone : owner phone
     * @param userToken : user token
     * @param orderCode : order code
     * */
    Response accept(String ownerPhone, String userToken, String orderCode);


    /**
     * complete an order
     * @param userPhone : user phone
     * @param userToken : user token
     * @param orderCode : order code
     * */
    Response complete(String userPhone, String userToken, String orderCode);

}
