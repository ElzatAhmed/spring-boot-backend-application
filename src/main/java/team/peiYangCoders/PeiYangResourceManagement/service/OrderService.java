package team.peiYangCoders.PeiYangResourceManagement.service;

import org.springframework.stereotype.Service;
import team.peiYangCoders.PeiYangResourceManagement.config.Response;
import team.peiYangCoders.PeiYangResourceManagement.model.user.User;
import team.peiYangCoders.PeiYangResourceManagement.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Response getAll(){
        return Response.okMessage(orderRepo.findAll());
    }

    public Response getAllByInitiator(User initiator){
        return Response.okMessage(orderRepo.findAllByInitiator(initiator));
    }

    public Response getAllByRecipient(User recipient){
        return Response.okMessage(orderRepo.findAllByRecipient(recipient));
    }

    public Response getAllByInitiatorAndRecipient(User initiator, User recipient){
        return Response.okMessage(orderRepo.findAllByInitiatorAndRecipient(initiator, recipient));
    }

    public Response getAllByClosed(boolean closed){
        return Response.okMessage(orderRepo.findAllByClosed(closed));
    }

    public Response getAllByClosedAndInitiator(boolean closed, User initiator){
        return Response.okMessage(orderRepo.findAllByClosedAndInitiator(closed, initiator));
    }

    public Response getAllByClosedAndRecipient(boolean closed, User recipient){
        return Response.okMessage(orderRepo.findAllByClosedAndRecipient(closed, recipient));
    }

    public Response getAllByClosedAndInitiatorAndRecipient(
            boolean closed, User initiator, User recipient){
        return Response.okMessage(orderRepo
                .findAllByClosedAndInitiatorAndRecipient(closed, initiator, recipient));
    }
}
